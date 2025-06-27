package net.sf.l2j.gameserver.handler.voicedcommandhandlers;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import Customs.data.RankingClanManager;
import Customs.data.RankingOnlineManager;
import Customs.data.RankingPKManager;
import Customs.data.RankingPvPManager;

/**
 * @author Arcade
 *
 */
public class Rankings implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands = {
		"Rankings","topPvp","topPK","topClan","topOnline"
	};
	
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String params)
	{
		if (command.equalsIgnoreCase("Rankings")) {
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			   //String filename = "data/html/mods/Ranking-no.htm";
			   
			   String filename = "data/html/mods/Ranking/Ranking-menu.htm";
			   
			   NpcHtmlMessage html = new NpcHtmlMessage(5);
			   html.setFile(filename);
			   activeChar.sendPacket(html);
		}
		   else if (command.equalsIgnoreCase("topPvp"))
		   {
			   RankingPvPManager pl = new RankingPvPManager();
			   
			   String filename = "data/html/mods/Ranking/RankingPvP-menu.htm";
			   
			   NpcHtmlMessage html = new NpcHtmlMessage(5);
			   html.setFile(filename);
			   html.replace("%PlayerPvP%", pl.loadPlayerPvP());
			   html.replace("%playerName%", activeChar.getName());
			   activeChar.sendPacket(html);
		   }
		   
		   else if (command.equalsIgnoreCase("topPK"))
		   {
			   RankingPKManager pl1 = new RankingPKManager();
			   
			   String filename = "data/html/mods/Ranking/RankingPK-menu.htm";
			   
			   NpcHtmlMessage html = new NpcHtmlMessage(5);
			   html.setFile(filename);
			   html.replace("%PlayerPK%", pl1.loadPlayerPK());
			   html.replace("%playerName%", activeChar.getName());
			   activeChar.sendPacket(html);
		   }
		   else if (command.equalsIgnoreCase("topOnline"))
		   {
			   RankingOnlineManager pl2 = new RankingOnlineManager();
			   String  filename = "data/html/mods/Ranking/RankingOnline-menu.htm";
			   
			   NpcHtmlMessage html = new NpcHtmlMessage(5);
			   html.setFile(filename);
			   html.replace("%PlayerOnline%", pl2.loadPlayerOnline());
			   html.replace("%PlayerOnline%", pl2.loadPlayerOnline());
			   html.replace("%playerName%", activeChar.getName());
			   activeChar.sendPacket(html);
		   }
		   else if (command.equalsIgnoreCase("topClan"))
		   {
			   RankingClanManager pl2 = new RankingClanManager();
			   
			   String filename = "data/html/mods/Ranking/RankingClan-menu.htm";
			   
			   NpcHtmlMessage html = new NpcHtmlMessage(5);
			   html.setFile(filename);
			   html.replace("%PlayerClan%", pl2.loadClans());
			   html.replace("%playerName%", activeChar.getName());
			   activeChar.sendPacket(html);
		   }
		return false;
	}


	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
	
}
