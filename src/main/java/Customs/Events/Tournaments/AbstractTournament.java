package Customs.Events.Tournaments;

import Custom.loadCustomMods;
import Customs.Events.Tournaments.data.TournamentState;
import Customs.Instance.Instance;
import Customs.Instance.InstanceManager;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.enums.LootRule;
import net.sf.l2j.gameserver.enums.TeamType;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

public abstract class AbstractTournament
{
protected List<Player> _players = new CopyOnWriteArrayList<>();
protected int _size;
protected int _tick;
protected TournamentState _state = TournamentState.INACTIVE;

protected int _blueLives;
protected int _redLives;

protected Instance _instance;

private ScheduledFuture<?> _schedular;

private ScheduledFuture<?> _task = null;

public AbstractTournament(int size)
{
    _size = size;
    _tick = TournamentConfig.RUNNING_TIME;
    _blueLives = size;
    _redLives = size;
}

public TournamentState getState()
{
    return _state;
}

public int getTeamSize()
{
    return _size;
}

public synchronized void handleRegUnReg(Player player)
{
    if (getAllPlayer().contains(player))
    {
        if (_state != TournamentState.INACTIVE)
        {
            player.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "You may no unregister now."));
            return;
        }

        getAllPlayer().remove(player);
        player.setTournament(null);
        player.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "You successfuly unregistered."));
        return;
    }

    if (_state != TournamentState.INACTIVE)
    {
        player.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "A battle is starting. Please try again in"  +TournamentConfig.TELEPORT_DELAY + " second(s)."));
        return;
    }

    if (loadCustomMods.isPlayerInEvent(player))//(player.isInTVTEvent() || player.isInDMEvent() || player.isInCTFEvent())
    {
        player.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "You are already registed in another event."));
        return;
    }

    if(TournamentConfig.NEED_PVPS){
        if(player.getPvpKills() < TournamentConfig.PVPS_TO_JOIN){
            player.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "You need at least " + TournamentConfig.PVPS_TO_JOIN + " PVP's so you can join."));
            return;
        }
    }

    if (OlympiadManager.getInstance().isRegisteredInComp(player))
    {
        player.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "Olympiad participants can't register."));
        return;
    }

    if (player.isCursedWeaponEquipped())
    {
        player.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "Cursed weapon owners are not allowed to participate."));
        return;
    }

    if (player.getKarma() > 0)
    {
        player.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "Chaotic players are not allowed to participate."));
        return;
    }

    if (player.getLevel() < TournamentConfig.MIN_LEVEL)
    {
        player.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "Min level to register is " + TournamentConfig.MIN_LEVEL + "."));
        return;
    }

    if (player.getLevel() > TournamentConfig.MAX_LEVEL)
    {
        player.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "Max level to register is " + TournamentConfig.MAX_LEVEL + "."));
        return;
    }

    if (TournamentConfig.RESTRICTED_CLASSES.contains(player.getClassId().getId()))
    {
        player.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "Your class cannot participate in tournament."));
        return;
    }

    if(!TournamentConfig.Allow_Same_HWID_On_Tournament){
       /* for(Player p : getAllPlayer()){
            if(p.gethwid().equals(player.gethwid())){
                player.sendMessage("Tournament: Register only 1 player per Computer.");
                return;
            }
        }*/
        for(AbstractTournament t : TournamentManager.getTournaments()){
            for(Player p : t.getAllPlayer()){
                if(p.gethwid().equals(player.gethwid())){
                    player.sendMessage("Tournament: Register only 1 player per Computer.");
                    return;
                }
            }
        }
    }
    for (IntIntHolder item : TournamentConfig.REQUIRES)
    {
        if (!player.destroyItemByItemId("", item.getId(), item.getValue(), player, true))
        {
            return;
        }
    }

    getAllPlayer().add(player);
    player.setTournament(this);
    player.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "You have been registered in " + _size + " vs " + _size + "."));

    if (isValidSize())
    {
        checkToStart();
    }
}

public List<Player> getAllPlayer()
{
    return _players;
}

protected List<Player> getPlayers(TeamType type)
{
    return getAllPlayer().stream().filter(s -> s.getTeam() == type).collect(Collectors.toList());
}

protected boolean isValidSize()
{
    return getAllPlayer().size() == _size * 2;
}

private synchronized void checkToStart()
{
    // Set state to REGISTRATION
    _state = TournamentState.REGISTRATION;

    getAllPlayer().forEach(s -> s.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "Teleporting participants to an arena in " + TournamentConfig.TELEPORT_DELAY + " second(s).")));

    if (_task == null)
    {
        _task = ThreadPool.schedule(() ->
        {
            if (isValidSize())
            {
                startFight();
            }
            else
            {
                // Set state INACTIVE
                _state = TournamentState.INACTIVE;

                getAllPlayer().forEach(p ->
                {
                    p.setTournament(null);
                    p.sendPacket(new CreatureSay(0, Say2.CLAN, "[Tournament]", "Has been aborted due to participation."));
                });
                getAllPlayer().clear();
            }
        }, TournamentConfig.TELEPORT_DELAY * 1000);
    }
}

protected void startFight()
{
    TournamentManager.replaceTournament(_size);

    _instance = InstanceManager.getInstance().createInstance();

    // Set state to STARTING
    _state = TournamentState.STARTING;

    Collections.shuffle(getAllPlayer());

    List<Player> temp = new ArrayList<>(getAllPlayer());

    int i = 0;
    while (temp.size() != 0)
    {
        i++;
        Player player = temp.get(Rnd.nextInt(temp.size()));
        player.setTeam(i == 1 ? TeamType.BLUE : TeamType.RED);
        player.broadcastUserInfo();
        temp.remove(player);

        if (i == 2)
        {
            i = 0;
        }
    }


    getAllPlayer().forEach(p ->
    {
        if (p.isDead())
        {
            p.doRevive();
        }

        p.setCurrentCp(p.getMaxCp());
        p.setCurrentHp(p.getMaxHp());
        p.setCurrentMp(p.getMaxMp());

        final Summon summon = p.getSummon();
        if (summon != null)
        {
            summon.unSummon(p);
        }

        if (p.isInParty())
        {
            p.getParty().disband();
        }

        if(_size > 1)
            createPartyOfTeam(p, p.getTeam().getId());


        if (TournamentConfig.REMOVE_BUFFS)
        {
            for (L2Effect effect : p.getAllEffects())
                effect.exit();
        }

        p.setInstance(_instance, true);

        p.setIsParalyzed(true);
        p.setIsInvul(true);
        p.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "[Tournament]", "Match starts in few seconds."));
        p.teleToLocation(p.getTeam() == TeamType.RED ? TournamentConfig.TEAM_RED_LOCATION : TournamentConfig.TEAM_BLUE_LOCATION);
    });


    ThreadPool.schedule(() ->
    {
        // Set state STARTED
        _state = TournamentState.STARTED;

        getAllPlayer().forEach(p ->
        {
            p.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "[Tournament]", "Started, go fight!"));
            p.setIsParalyzed(false);
            p.setIsInvul(false);
        });

        _tick = TournamentConfig.RUNNING_TIME * 60;
    }, 10000);
}

    protected void createPartyOfTeam(Player players, int teamId)
    {

        for (Player p : getAllPlayer()) {
            if(p == players) continue;
            if (p.getTeam().getId() == teamId) {
                if(players.getParty() == null){
                    new Party(players, p, LootRule.ITEM_LOOTER);
                }
                else{
                    if(p.getParty() == null)
                      players.getParty().addPartyMember(p);
                }
            }
        }
    }

protected void stopFight(TeamType winner)
{
    // Set state INACTIVATING
    _state = TournamentState.INACTIVATING;

    getAllPlayer().forEach(p ->
    {
        if (p.isDead())
        {
            p.doRevive();
        }
        p.setCurrentCp(p.getMaxCp());
        p.setCurrentHp(p.getMaxHp());
        p.setCurrentMp(p.getMaxMp());

        p.setIsParalyzed(true);

        if (p.isInParty())
        {
            p.getParty().disband();
        }

        if (winner != null)
        {
            if (p.getTeam() == winner)
            {
                if(p.getTournament().getTeamSize() == 1) {
                    for (IntIntHolder rh : TournamentConfig.REWARDS) {
                        p.addItem("1x1", rh.getId(), rh.getValue(), p, true);
                    }
                }
                else if(p.getTournament().getTeamSize() == 2) {
                    for (IntIntHolder rh : TournamentConfig.REWARDS2) {
                        p.addItem("2x2", rh.getId(), rh.getValue() , p, true);
                    }
                }
                else if(p.getTournament().getTeamSize() == 3) {
                    for (IntIntHolder rh : TournamentConfig.REWARDS3) {
                        p.addItem("3x3", rh.getId(), rh.getValue() , p, true);
                    }
                }
                else if(p.getTournament().getTeamSize() == 4) {
                    for (IntIntHolder rh : TournamentConfig.REWARDS4) {
                        p.addItem("4x4", rh.getId(), rh.getValue() , p, true);
                    }
                }
                else if(p.getTournament().getTeamSize() == 5) {
                    for (IntIntHolder rh : TournamentConfig.REWARDS5) {
                        p.addItem("5x5", rh.getId(), rh.getValue() , p, true);
                    }
                }
                else if(p.getTournament().getTeamSize() == 6) {
                    for (IntIntHolder rh : TournamentConfig.REWARDS6) {
                        p.addItem("6x6", rh.getId(), rh.getValue() , p, true);
                    }
                }
                else if(p.getTournament().getTeamSize() == 7) {
                    for (IntIntHolder rh : TournamentConfig.REWARDS7) {
                        p.addItem("7x7", rh.getId(), rh.getValue() , p, true);
                    }
                }
                else if(p.getTournament().getTeamSize() == 8) {
                    for (IntIntHolder rh : TournamentConfig.REWARDS8) {
                        p.addItem("8x8", rh.getId(), rh.getValue() , p, true);
                    }
                }
                else if(p.getTournament().getTeamSize() == 9) {
                    for (IntIntHolder rh : TournamentConfig.REWARDS9) {
                        p.addItem("9x9", rh.getId(), rh.getValue() , p, true);
                    }
                }
            }
            else
                p.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "[Tournament]", "You will be teleported back after " + TournamentConfig.TELEPORT_DELAY +" second(s)."));
        }

        p.setTournament(null);
    });

    ThreadPool.schedule(() ->
    {
        getAllPlayer().forEach(p ->
        {
            p.setInstance(InstanceManager.getInstance().getInstance(0), true);
            p.setTeam(TeamType.NONE);
            p.broadcastUserInfo();
            p.setIsParalyzed(false);
            p.teleportTo(TournamentConfig.BACK_LOCATION, 20);
        });

        getAllPlayer().clear();
    }, TournamentConfig.TELEPORT_DELAY * 1000);

    // Set state INACTIVE
    _state = TournamentState.INACTIVE;
}

    protected void stopFightAll()
    {
        getAllPlayer().forEach(p ->
        {
            p.setIsParalyzed(true);
        });

        ThreadPool.schedule(() ->
        {
            getAllPlayer().forEach(p ->
            {
                p.setIsParalyzed(false);
            });

        }, TournamentConfig.TELEPORT_DELAY * 1000);

    }

public final void onStart()
{
    _schedular = ThreadPool.scheduleAtFixedRate(this::onEnd, 1000, 1000);
}

public final void cancel()
{
    if (_schedular != null)
    {
        _schedular.cancel(false);
        _schedular = null;
    }
}

public static boolean onScrollUse(Player player)
{
    if (player.isInTournament() && !TournamentConfig.SCROLL_ALLOWED)
    {
        return true;
    }

    return false;
}

public static boolean canDoAttack(Creature attacker, Creature target)
{
    if (attacker.getActingPlayer().isInTournament() && target.getActingPlayer().isInTournament()
            && attacker.getActingPlayer().getTeam() == target.getActingPlayer().getTeam() && !TournamentConfig.TARGET_TEAM_MEMBERS_ALLOWED)
    {
        attacker.sendPacket(ActionFailed.STATIC_PACKET);
        return true;
    }

    return false;
}

public void onDisconnect(Player player)
{
    _players.remove(player);
}

public abstract void onDie(Player player);

public abstract void onEnd();
}