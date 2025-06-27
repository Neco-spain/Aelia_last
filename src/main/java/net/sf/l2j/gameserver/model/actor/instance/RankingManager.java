package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import Customs.data.RankingClanManager;
import Customs.data.RankingOnlineManager;
import Customs.data.RankingPKManager;
import Customs.data.RankingPvPManager;



public class RankingManager extends Folk
{
  public RankingManager(int objectId, NpcTemplate template)
  {
   super(objectId, template);
  }
  
  @Override
  public void onBypassFeedback(Player player, String command)
  {
   if (player.isProcessingTransaction())
   {
    player.sendPacket(SystemMessageId.ALREADY_TRADING);
    return;
   }


   else if (command.startsWith("topPvp"))
   {
	   RankingPvPManager pl = new RankingPvPManager();
	   player.sendPacket(ActionFailed.STATIC_PACKET);
	   String filename = "data/html/mods/Ranking-no.htm";
	   
	   filename = "data/html/mods/Ranking/RankingPvP.htm";
	   
	   NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
	   html.setFile(filename);
	   html.replace("%PlayerPvP%", pl.loadPlayerPvP());
	   html.replace("%objectId%", String.valueOf(getObjectId()));
	   html.replace("%playerName%", player.getName());
	   player.sendPacket(html);
   }
   
   else if (command.startsWith("topPK"))
   {
	   RankingPKManager pl1 = new RankingPKManager();
	   player.sendPacket(ActionFailed.STATIC_PACKET);
	   String filename = "data/html/mods/Ranking-no.htm";
	   
	   filename = "data/html/mods/Ranking/RankingPK.htm";
	   
	   NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
	   html.setFile(filename);
	   html.replace("%PlayerPK%", pl1.loadPlayerPK());
	   html.replace("%objectId%", String.valueOf(getObjectId()));
	   html.replace("%playerName%", player.getName());
	   player.sendPacket(html);
   }
   
   else if (command.startsWith("Online"))
   {
	   RankingOnlineManager pl2 = new RankingOnlineManager();
	   player.sendPacket(ActionFailed.STATIC_PACKET);
	 //  String filename = "data/html/mods/Ranking-no.htm";
	   
	   String  filename = "data/html/mods/Ranking/RankingOnline.htm";
	   
	   NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
	   html.setFile(filename);
	   html.replace("%PlayerOnline%", pl2.loadPlayerOnline());
	   html.replace("%objectId%", String.valueOf(getObjectId()));
	   html.replace("%playerName%", player.getName());
	   player.sendPacket(html);
   }
   else if (command.startsWith("Clan"))
   {
	   RankingClanManager pl2 = new RankingClanManager();
	   player.sendPacket(ActionFailed.STATIC_PACKET);
	  // String filename = "data/html/mods/Ranking-no.htm";
	   
	   String filename = "data/html/mods/Ranking/RankingClan.htm";
	   
	   NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
	   html.setFile(filename);
	   html.replace("%PlayerClan%", pl2.loadClans());
	   html.replace("%objectId%", String.valueOf(getObjectId()));
	   html.replace("%playerName%", player.getName());
	   player.sendPacket(html);
   }
   else if(command.startsWith("Home"))
   {
	   player.sendPacket(ActionFailed.STATIC_PACKET);
	   //String filename = "data/html/mods/Ranking-no.htm";
	   
	   String filename = "data/html/mods/Ranking/Ranking.htm";
	   
	   NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
	   html.setFile(filename);
	   html.replace("%objectId%", String.valueOf(getObjectId()));
	   html.replace("%playerName%", player.getName());
	   player.sendPacket(html);
   }
   
  }
  
  @Override
  public void showChatWindow(Player player, int val)
  {

   player.sendPacket(ActionFailed.STATIC_PACKET);
   //String filename = "data/html/mods/Ranking-no.htm";
   
   String filename = "data/html/mods/Ranking/Ranking.htm";
   
   NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
   html.setFile(filename);
   html.replace("%objectId%", String.valueOf(getObjectId()));
   html.replace("%playerName%", player.getName());
   player.sendPacket(html);
  }
}