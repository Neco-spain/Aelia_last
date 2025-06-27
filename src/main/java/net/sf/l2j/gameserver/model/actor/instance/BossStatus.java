package net.sf.l2j.gameserver.model.actor.instance;

import java.util.logging.Logger;

import net.sf.l2j.commons.util.StatsSet;

import net.sf.l2j.gameserver.data.manager.GrandBossManager;
import net.sf.l2j.gameserver.data.manager.RaidBossManager;
import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.spawn.BossSpawn;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;

import Custom.CustomConfig;


public class BossStatus extends Folk
{
  private static Logger _log = Logger.getLogger(BossStatus.class.getName());
  public BossStatus(int objectId, NpcTemplate template)
  {
    super(objectId, template);
  }

  @Override
public void onAction(Player activeChar)
  {
   /* if (!canTarget(activeChar))
    {
      return;
    }
*/
    if (this != activeChar.getTarget())
    {
    	activeChar.setTarget(this);

    	activeChar.sendPacket(new MyTargetSelected(getObjectId(), 0));

    	activeChar.sendPacket(new ValidateLocation(this));
    }
    else if (!canInteract(activeChar))
    {
    	activeChar.getAI().setIntention(IntentionType.INTERACT, this);
    }
    else
    {
      showHtmlWindow(activeChar);
    }

    activeChar.sendPacket(ActionFailed.STATIC_PACKET);
  }
  
	private static void showHtmlWindow(Player activeChar)
	{
		StringBuilder sb = new StringBuilder();


		
		for(int boss : CustomConfig.RAID_INFO_IDS_LIST)
		{
			String name = "";
			NpcTemplate template = null;
			if((template = NpcData.getInstance().getTemplate(boss)) != null){
				name = template.getName();
			}else{
				_log.warning("[RaidInfoHandler][sendInfo] Raid Boss with ID "+boss+" is not defined into NpcTable");
				continue;
			}
			 
			BossSpawn actual_boss_stat = null;
			StatsSet actual_boss_stat1 ;
			
			long delay = 0;
			
			if(NpcData.getInstance().getTemplate(boss).getType().equals("RaidBoss"))
			{
				actual_boss_stat=RaidBossManager.getInstance().getBossSpawn(boss);
				if(actual_boss_stat!=null)
					delay =  actual_boss_stat.getRespawnTime();
			}
			else if(NpcData.getInstance().getTemplate(boss).getType().equals("GrandBoss"))
			{
				actual_boss_stat1=GrandBossManager.getInstance().getStatsSet(boss);
				if(actual_boss_stat1!=null)
					delay = actual_boss_stat1.getLong("respawn_time");
			}else
				continue;
		
			if (delay <= System.currentTimeMillis())
			{
				sb.append("" + name + "&nbsp;<font color=\"00FF00\">IS ALIVE!</font><br1>");
			}
			else
			{
				int hours = (int) ((delay - System.currentTimeMillis()) / 1000 / 60 / 60);
				int mins = (int) (((delay - (hours * 60 * 60 * 1000)) - System.currentTimeMillis()) / 1000 / 60);
				int seconts = (int) (((delay - ((hours * 60 * 60 * 1000) + (mins * 60 * 1000))) - System.currentTimeMillis()) / 1000);
				sb.append("" + name + "&nbsp;<font color=\"b09979\">:&nbsp;" + hours + " : " + mins + " : " + seconts +  "</font><br1>");
			}
		}


	/*	NpcHtmlMessage msg = new NpcHtmlMessage(getObjectId());
		msg.setHtml(tb.toString());
		activeChar.sendPacket(msg);
	 */
		
		NpcHtmlMessage html = new NpcHtmlMessage(1);
		html.setFile("data/html/mods/raidboss/npcManager.htm");
		html.replace("%bosslist%", sb.toString());
		activeChar.sendPacket(html);
	}
	
}