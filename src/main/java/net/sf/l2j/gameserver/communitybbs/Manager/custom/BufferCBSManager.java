package net.sf.l2j.gameserver.communitybbs.Manager.custom;

import Custom.CustomConfig;
import net.sf.l2j.Config;
import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.util.StatsSet;
import net.sf.l2j.gameserver.communitybbs.Manager.BaseBBSManager;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.data.manager.BufferManager;
import net.sf.l2j.gameserver.data.manager.GrandBossManager;
import net.sf.l2j.gameserver.data.manager.RaidBossManager;
import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.spawn.BossSpawn;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class BufferCBSManager extends BaseBBSManager {

    List<Integer> fighter = CustomConfig.LIST_FIGHTER_SET;
    List<Integer> fighterbers = CustomConfig.LIST_FIGHTER_SET_BERS;

    List<Integer> mage = CustomConfig.LIST_MAGE_SET;
    List<Integer> magebers = CustomConfig.LIST_MAGE_SET_BERS;

    public void parseCmd(String command, Player player) {
        if(command.startsWith("_cbsbuffer;")){

            StringTokenizer st = new StringTokenizer(command, ";");
            st.nextToken();

            String actualCommand = String.valueOf(st.nextToken());

            if(!checkConditions(player)) {
                showMainWindow(player);
                return;
            }

            if (actualCommand.startsWith("restore"))
            {
                String noble = st.nextToken();

                //pet implement
                if (player.getBuff() == 0) {
                    player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
                    player.setCurrentCp(player.getMaxCp());

                    if (noble.equals("true"))
                    {
                        SkillTable.getInstance().getInfo(1323, 1).getEffects(player, player);
                        player.broadcastPacket(new MagicSkillUse(player, player, 1323, 1, 850, 0));
                    }
                }
                else if (player.getSummon() != null){
                    final Summon summon = player.getSummon();
                    summon.setCurrentHpMp(summon.getMaxHp(), summon.getMaxMp());
                }
                showMainWindow(player);
            }
            else if (actualCommand.equalsIgnoreCase("cancellation"))
            {
                L2Skill buff;
                buff = SkillTable.getInstance().getInfo(1056, 1);

                //pet implement
                if (player.getBuff() == 0) {
                    buff.getEffects(player, player);
                    player.stopAllEffectsExceptThoseThatLastThroughDeath();
                    player.broadcastPacket(new MagicSkillUse(player, player, 1056, 1, 850, 0));
                    player.stopAllEffects();
                }
                else if (player.getSummon() != null){
                    final Summon summon = player.getSummon();
                    summon.stopAllEffects();
                }
                showMainWindow(player);
            }
            else if (actualCommand.equals("changebuff"))
            {
                player.setBuff(player.getBuff() == 0 ? 1 : 0);
                showMainWindow(player);
            }
            else if (actualCommand.startsWith("openlist"))
            {
                String category = st.nextToken();
                String htmfile = st.nextToken();

                String content = null;

                if (category.startsWith("null"))
                {
                    content = HtmCache.getInstance().getHtm("data/html/CommunityBoard/custom/buffer/" + htmfile + ".htm");

                    // First Page
                    if (htmfile.equals("index"))
                    {
                        content = content.replace("%name%", player.getName());
                        content = content.replace("%buffing%", player.getBuff() == 0 ? "Yourself" : "Your pet");
                    }
                }
                else
                    content = HtmCache.getInstance().getHtm("data/html/CommunityBoard/custom/buffer/" + category + "/" + htmfile + ".htm");

                if(content == null)
                {
                    content = "<html><body><br><br><center>404 :File Not found: 'data/html/CommunityBoard/custom/buffer/" + category + "/" + htmfile + ".htm' </center></body></html>";
                }
                separateAndSend(content, player);
            }

            else if (actualCommand.startsWith("dobuff"))
            {
                int buffid = Integer.valueOf(st.nextToken());
                int bufflevel = Integer.valueOf(st.nextToken());
                String category = st.nextToken();
                String windowhtml = st.nextToken();

                //pet implement
                if (player.getBuff() == 0) {
                    MagicSkillUse mgc = new MagicSkillUse(player, player, buffid, bufflevel, 1150, 0);
                    player.sendPacket(mgc);
                    player.broadcastPacket(mgc);
                }
                else if (player.getSummon() != null){
                    MagicSkillUse mgc = new MagicSkillUse(player, player.getSummon(), buffid, bufflevel, 1150, 0);
                    player.sendPacket(mgc);
                    player.broadcastPacket(mgc);
                }

                //pet implement
                if (player.getBuff() == 0)
                    SkillTable.getInstance().getInfo(buffid, bufflevel).getEffects(player, player);
                else
                {
                    if (player.getSummon() != null)
                        SkillTable.getInstance().getInfo(buffid, bufflevel).getEffects(player.getSummon(), player.getSummon());
                }

                String content = HtmCache.getInstance().getHtm("data/html/CommunityBoard/custom/buffer/" + category + "/" + windowhtml + ".htm");

                if(content == null)
                {
                    content = "<html><body><br><br><center>404 :File Not foud: 'data/html/CommunityBoard/custom/buffer/" + category + "/" + windowhtml + ".htm' </center></body></html>";
                }
                separateAndSend(content, player);
            }
            else if (actualCommand.startsWith("getbuff"))
            {
                int buffid = Integer.valueOf(st.nextToken());
                int bufflevel = Integer.valueOf(st.nextToken());
                if (buffid != 0)
                {
                    //pet implement
                    if (player.getBuff() == 0) {
                        MagicSkillUse mgc = new MagicSkillUse(player, player, buffid, bufflevel, 450, 0);
                        player.sendPacket(mgc);
                        player.broadcastPacket(mgc);
                    }
                    else if (player.getSummon() != null){
                        MagicSkillUse mgc = new MagicSkillUse(player, player.getSummon(), buffid, bufflevel, 450, 0);
                        player.sendPacket(mgc);
                        player.broadcastPacket(mgc);
                    }

                    //pet implement
                    if (player.getBuff() == 0)
                        SkillTable.getInstance().getInfo(buffid, bufflevel).getEffects(player, player);
                    else
                    {
                        if (player.getSummon() != null)
                            SkillTable.getInstance().getInfo(buffid, bufflevel).getEffects(player.getSummon(), player.getSummon());
                    }
                    showMainWindow(player);
                }
            }
            //with bers
            else if (actualCommand.equalsIgnoreCase("fightersetbers"))
            {
                //pet implement
                if (player.getBuff() == 0) {
                    fighterbers.forEach(id -> SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(player, player));
                }
                else if (player.getSummon() != null){
                    final Summon summon = player.getSummon();
                    fighterbers.forEach(id -> SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(summon, summon));
                }
                showMainWindow(player);
            }
            else if (actualCommand.equalsIgnoreCase("magesetbers"))
            {
                //pet implement
                if (player.getBuff() == 0) {
                    magebers.forEach(id -> SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(player, player));
                }
                else if (player.getSummon() != null){
                    final Summon summon = player.getSummon();
                    magebers.forEach(id -> SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(summon, summon));
                }
                showMainWindow(player);
            }
            //no bers
            else if (actualCommand.equalsIgnoreCase("fighterset"))
            {
                //pet implement
                if (player.getBuff() == 0) {
                    fighter.forEach(id -> SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(player, player));
                }
                else if (player.getSummon() != null){
                    final Summon summon = player.getSummon();
                    fighter.forEach(id -> SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(summon, summon));
                }
                showMainWindow(player);
            }
            else if (actualCommand.equalsIgnoreCase("mageset"))
            {
                //pet implement
                if (player.getBuff() == 0) {
                    mage.forEach(id -> SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(player, player));
                }
                else if (player.getSummon() != null){
                    final Summon summon = player.getSummon();
                    mage.forEach(id -> SkillTable.getInstance().getInfo(id, SkillTable.getInstance().getMaxLevel(id)).getEffects(summon, summon));
                }
                showMainWindow(player);
            }
        }
    }

    public void showMainWindow(Player player){
        String content = HtmCache.getInstance().getHtm("data/html/CommunityBoard/custom/buffer/index.htm");
        content = content.replaceAll("%name%", String.valueOf(player.getName()));
        content = content.replace("%buffing%", player.getBuff() == 0 ? "Yourself" : "Your pet");
        separateAndSend(content, player);
    }

    protected boolean checkConditions(Player player) {
        if(!player.isInsideZone(ZoneId.TOWN)){
            player.sendMessage("You can use Buffer only in Town.");
            return false;
        }
        if(player.isSitting()){
            player.sendMessage("You cannot use Buffer while sitting.");
            return false;
        }
        if(player.isInCombat() || player.getPvpFlag() > 0){
            player.sendMessage("You cannot use Buffer while in combat/flag.");
            return false;
        }
        if(player.isDead()){
            player.sendMessage("You cannot use Buffer while your are dead.");
            return false;
        }
        if(player.isInOlympiadMode() || player.isOlympiadStart()){
            player.sendMessage("You cannot use Buffer while in olympiad.");
            return false;
        }
        return true;
    }

    public static BufferCBSManager getInstance() {
        return BufferCBSManager.SingletonHolder.INSTANCE;
    }
    private static class SingletonHolder {
        protected static final BufferCBSManager INSTANCE = new BufferCBSManager();
    }
}
