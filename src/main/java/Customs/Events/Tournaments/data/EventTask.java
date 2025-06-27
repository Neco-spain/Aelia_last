package Customs.Events.Tournaments.data;

import Customs.Events.Tournaments.AbstractTournament;
import Customs.Events.Tournaments.TournamentConfig;
import Customs.Events.Tournaments.TournamentManager;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.data.sql.SpawnTable;
import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.spawn.L2Spawn;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

import java.util.concurrent.ScheduledFuture;

public class EventTask {
    public static L2Spawn _npcSpawn1;
    public static boolean _started = false;
    public static boolean _aborted = false;

    public static void SpawnEvent()
    {
        TournamentManager.init();

        spawnNpc1();

        World.announceToOnlinePlayers("Tournament: PvP Event",true);
        World.announceToOnlinePlayers("Tournament: Find Npc In the center of Giran.",true);
//        World.announceToOnlinePlayers("Tournament: Reward: " + ItemTable.getInstance().getTemplate(TournamentConfig.).getName(),true);
        World.announceToOnlinePlayers("Tournament: Duration: " + TournamentConfig.EVENT_TIME + " minute(s)!",true);

        _aborted = false;
        _started = true;

      /*  waiter(TournamentConfig.RUNNING_TIME * 60 * 1000);
        if (!_aborted) {
            finishEvent();
        }*/

        int eventTime = TournamentConfig.EVENT_TIME * 60;
//        int mltp = 60 * 1000;
        warnEventEnd(eventTime);
        schedule(() -> warnEventEnd(30 * 60), (eventTime) - (30 * 60));
        schedule(() -> warnEventEnd(15 * 60 ),(eventTime) - (15 * 60));
        schedule(() -> warnEventEnd(5  * 60), (eventTime) - (5 * 60));
        schedule(() -> warnEventEnd(60), (eventTime) - 60);
        schedule(() -> warnEventEnd(30), (eventTime) - 30);
        schedule(() -> warnEventEnd(10), (eventTime) - 10);
        schedule(() -> warnEventEnd(5), (eventTime) - 5);
        schedule(() -> warnEventEnd(1), (eventTime) - 1);
        schedule(() -> finishEvent(), eventTime);


    }

    public static int loc1x()
    {
        int loc1x = TournamentConfig.NPC_locx;
        return loc1x;
    }

    public static int loc1y()
    {
        int loc1y = TournamentConfig.NPC_locy;
        return loc1y;
    }

    public static int loc1z()
    {
        int loc1z = TournamentConfig.NPC_locz;
        return loc1z;
    }

    public static void spawnNpc1()
    {
        NpcTemplate tmpl = NpcData.getInstance().getTemplate(TournamentConfig.ARENA_NPC);
        try
        {
            _npcSpawn1 = new L2Spawn(tmpl);
            _npcSpawn1.setLoc(loc1x(), loc1y(), loc1z(), TournamentConfig.NPC_Heading);
            _npcSpawn1.setRespawnDelay(1);
            _npcSpawn1.setRespawnState(true);
            SpawnTable.getInstance().addSpawn(_npcSpawn1, false);

            _npcSpawn1.doSpawn(false);
            _npcSpawn1.getNpc().getStatus().setCurrentHp(9.99999999E8D);
            _npcSpawn1.getNpc().isAggressive();
            _npcSpawn1.getNpc().decayMe();
            _npcSpawn1.getNpc().spawnMe(_npcSpawn1.getNpc().getX(), _npcSpawn1.getNpc().getY(), _npcSpawn1.getNpc().getZ());
            _npcSpawn1.getNpc().broadcastPacket(new MagicSkillUse(_npcSpawn1.getNpc(), _npcSpawn1.getNpc(), 1034, 1, 1, 1));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void unspawnNpc1()
    {
        if (_npcSpawn1 == null) {
            return;
        }
        _npcSpawn1.getNpc().deleteMe();
        SpawnTable.getInstance().deleteSpawn(_npcSpawn1, true);
        _npcSpawn1.setRespawnState(false);
        _npcSpawn1 = null;
    }

    public static void finishEvent()
    {
        announce("Event Finished!");

        unspawnNpc1();

//        TournamentManager.getTournaments().clear();
        for(AbstractTournament tour : TournamentManager.getTournaments()) {
            for (Player p : tour.getAllPlayer())
                p.setTournament(null);

            tour.getAllPlayer().clear();
        }
        TournamentManager.getTournaments().clear();

        _started = false;
        EventTime.getInstance().StartCalculationOfNextEventTime();

        for (Player player : World.getInstance().getPlayers()) {
            if ((player != null) && (player.isOnline()))
            {
                CreatureSay cs = new CreatureSay(player.getObjectId(), Say2.PARTY, "[Tournament]", "Next Event: " + EventTime.getInstance().getNextTime() + " (GMT+2).");
                player.sendPacket(cs);
            }
        }
    }

    protected static ScheduledFuture<?> schedule(Runnable task, int seconds)
    {
        return ThreadPool.schedule(task, seconds * 1000);
    }

    private static void warnEventEnd(int seconds)
    {
        int mins = seconds / 60;
        int secs = seconds % 60;

        announce((mins == 0 ? "" : mins+" minute(s)")+(mins > 0 && secs > 0 ? " and " : "")+(secs == 0 ? "" : secs+" second(s)")+" remaining until the event ends.");
    }

    protected static void announce(String msg)
    {
        World.announceToOnlinePlayers("Tournament: " + msg,true);
    }

}
