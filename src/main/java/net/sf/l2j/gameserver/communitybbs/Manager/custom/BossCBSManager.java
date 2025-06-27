package net.sf.l2j.gameserver.communitybbs.Manager.custom;

import Custom.CustomConfig;
import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.commons.util.StatsSet;
import net.sf.l2j.gameserver.communitybbs.Manager.BaseBBSManager;
import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.data.manager.GrandBossManager;
import net.sf.l2j.gameserver.data.manager.RaidBossManager;
import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.spawn.BossSpawn;

public class BossCBSManager extends BaseBBSManager {

    public void parseCmd(String command, Player player) {
            String content = HtmCache.getInstance().getHtm("data/html/CommunityBoard/custom/boss.htm");
            StringBuilder sb = new StringBuilder();

            for(int boss : CustomConfig.RAID_INFO_IDS_LIST)
            {
                String name = "";
                NpcTemplate template = null;
                if((template = NpcData.getInstance().getTemplate(boss)) != null){
                    name = template.getName();
                }else{
                    System.out.println("[RaidInfoHandler][sendInfo] Raid Boss with ID "+boss+" is not defined into NpcTable");
                    continue;
                }

                BossSpawn actual_boss_stat = null;
                StatsSet actual_boss_stat1 ;

                long delay = 0;

                if(NpcData.getInstance().getTemplate(boss).getType().equals("RaidBoss"))
                {
                    actual_boss_stat= RaidBossManager.getInstance().getBossSpawn(boss);
                    if(actual_boss_stat!=null)
                        delay =  actual_boss_stat.getRespawnTime();
                }
                else if(NpcData.getInstance().getTemplate(boss).getType().equals("GrandBoss"))
                {
                    actual_boss_stat1= GrandBossManager.getInstance().getStatsSet(boss);
                    if(actual_boss_stat1!=null)
                        delay = actual_boss_stat1.getLong("respawn_time");
                }else
                    continue;

                if (delay <= System.currentTimeMillis())
                {
                    StringUtil.append(sb,"" + name + "&nbsp;<font color=\"00FF00\">IS ALIVE!</font><br1>");
                }
                else
                {
                    int hours = (int) ((delay - System.currentTimeMillis()) / 1000 / 60 / 60);
                    int mins = (int) (((delay - (hours * 60 * 60 * 1000)) - System.currentTimeMillis()) / 1000 / 60);
                    int seconts = (int) (((delay - ((hours * 60 * 60 * 1000) + (mins * 60 * 1000))) - System.currentTimeMillis()) / 1000);
                    StringUtil.append(sb,"" + name + "&nbsp;<font color=\"b09979\">:&nbsp;" + hours + " : " + mins + " : " + seconts +  "</font><br1>");
                }
            }

            content = content.replaceAll("%BossList%", sb.toString());

            if(content == null)
            {
                content = "<html><body><br><br><center>404 :File Not found: 'data/html/CommunityBoard/custom/boss.htm' </center></body></html>";
            }
            separateAndSend(content, player);
    }

    protected String getFolder() {
        return "custom/";
    }

    public static BossCBSManager getInstance() {
        return BossCBSManager.SingletonHolder.INSTANCE;
    }
    private static class SingletonHolder {
        protected static final BossCBSManager INSTANCE = new BossCBSManager();
    }
}
