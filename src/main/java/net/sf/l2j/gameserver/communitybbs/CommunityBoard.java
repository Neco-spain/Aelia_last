package net.sf.l2j.gameserver.communitybbs;

import Custom.CustomConfig;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.communitybbs.Manager.*;
import net.sf.l2j.gameserver.communitybbs.Manager.custom.BossCBSManager;
import net.sf.l2j.gameserver.communitybbs.Manager.custom.BufferCBSManager;
import net.sf.l2j.gameserver.communitybbs.Manager.custom.IndexCBSManager;
import net.sf.l2j.gameserver.communitybbs.Manager.custom.TeleportCBManager;
import net.sf.l2j.gameserver.data.xml.MultisellData;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.GameClient;
import net.sf.l2j.gameserver.network.SystemMessageId;

import Customs.Balance.ClassBalanceGui;
import Customs.Balance.SkillBalanceGui;
import net.sf.l2j.gameserver.network.serverpackets.*;

import java.awt.*;
import java.util.StringTokenizer;

public class CommunityBoard
{
	protected CommunityBoard()
	{
	}
	
	public void handleCommands(GameClient client, String command)
	{
		final Player player = client.getPlayer();
		if (player == null)
			return;
		
		if (!Config.ENABLE_COMMUNITY_BOARD)
		{
			player.sendPacket(SystemMessageId.CB_OFFLINE);
			return;
		}

		if (!player.isGM() && !CustomConfig.ENABLE_CUSTOM_CB)
		{
			player.sendPacket(SystemMessageId.CB_OFFLINE);
			return;
		}

		if (player.isGM() && command.contains("balance")){
			if (command.contains("skillbalance"))
				SkillBalanceGui.getInstance().parseCmd(command, player);
			else if (command.contains("classbalance") || command.equals("_bbs_balancer"))
				ClassBalanceGui.getInstance().parseCmd(command, player);
			return;
		}

		if(CustomConfig.ENABLE_CUSTOM_CB){
			if (command.startsWith("_cbshome") || command.startsWith("_bbsShop") || command.startsWith("bbs_buffer") || command.startsWith("_bbsLink;") || command.startsWith("bbs_customs"))
				IndexCBSManager.getInstance().parseCmd(command, player);
			else if(command.startsWith("_cbsbuffer;"))
				BufferCBSManager.getInstance().parseCmd(command, player);
			else if(command.startsWith("bbs_raids"))
				BossCBSManager.getInstance().parseCmd(command, player);
			else if (command.startsWith("_cbsGoto") || command.startsWith("_cbsTelePage") || command.startsWith("_cbspvpzone"))
				TeleportCBManager.getInstance().parseCmd(command, player);

			else if(command.startsWith("_bbsDraw;")){
				StringTokenizer st = new StringTokenizer(command, ";");
				st.nextToken();
				IndexCBSManager.getInstance().parseCmd("_bbsShop;" + st.nextToken(), player);
				player.sendPacket(new HennaEquipList(player));
			}
			else if(command.startsWith("_bbsRemoveList;")){
				StringTokenizer st = new StringTokenizer(command, ";");
				st.nextToken();
				IndexCBSManager.getInstance().parseCmd("_bbsShop;" + st.nextToken(), player);

				if (player.getHennaList().isEmpty())
				{
					player.sendPacket(SystemMessageId.SYMBOL_NOT_FOUND);
					return;
				}
				player.sendPacket(new HennaRemoveList(player));
			}
			else if (command.startsWith("_bbsvoiced_"))
			{
				String _command = command.split(" ")[0];
				IVoicedCommandHandler ach = VoicedCommandHandler.getInstance().getHandler(_command.substring(11));
				if (ach == null)
				{
					player.sendMessage("The command " + command.substring(11) + " does not exist!");
					return;
				}
				ach.useVoicedCommand(_command.substring(11), player, null);
				IndexCBSManager.getInstance().SendHome(player);
			}
			else if(command.startsWith("_bbsmultisell;")) {
				StringTokenizer st = new StringTokenizer(command, ";");
				st.nextToken();
				IndexCBSManager.getInstance().parseCmd("_bbsLink;shop", player);
				MultisellData.getInstance().separateAndSend(st.nextToken(), player, null, false);
			}
		}
		else if (command.startsWith("_bbshome"))
			TopBBSManager.getInstance().parseCmd(command, player);
		else if (command.startsWith("_bbsloc"))
			RegionBBSManager.getInstance().parseCmd(command, player);
		else if (command.startsWith("_bbsclan"))
			ClanBBSManager.getInstance().parseCmd(command, player);
		else if (command.startsWith("_bbsmemo"))
			TopicBBSManager.getInstance().parseCmd(command, player);
		else if (command.startsWith("_bbsmail") || command.equals("_maillist_0_1_0_"))
			MailBBSManager.getInstance().parseCmd(command, player);
		else if (command.startsWith("_friend") || command.startsWith("_block"))
			FriendsBBSManager.getInstance().parseCmd(command, player);
		else if (command.startsWith("_bbstopics"))
			TopicBBSManager.getInstance().parseCmd(command, player);
		else if (command.startsWith("_bbsposts"))
			PostBBSManager.getInstance().parseCmd(command, player);
		else
			BaseBBSManager.separateAndSend("<html><body><br><br><center>The command: " + command + " isn't implemented.</center></body></html>", player);
	}
	
	public void handleWriteCommands(GameClient client, String url, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
		final Player player = client.getPlayer();
		if (player == null)
			return;
		
		if (!Config.ENABLE_COMMUNITY_BOARD)
		{
			player.sendPacket(SystemMessageId.CB_OFFLINE);
			return;
		}
		
		if (url.equals("Topic"))
			TopicBBSManager.getInstance().parseWrite(arg1, arg2, arg3, arg4, arg5, player);
		else if (url.equals("Post"))
			PostBBSManager.getInstance().parseWrite(arg1, arg2, arg3, arg4, arg5, player);
		else if (url.equals("_bbsloc"))
			RegionBBSManager.getInstance().parseWrite(arg1, arg2, arg3, arg4, arg5, player);
		else if (url.equals("_bbsclan"))
			ClanBBSManager.getInstance().parseWrite(arg1, arg2, arg3, arg4, arg5, player);
		else if (url.equals("Mail"))
			MailBBSManager.getInstance().parseWrite(arg1, arg2, arg3, arg4, arg5, player);
		else if (url.equals("_friend"))
			FriendsBBSManager.getInstance().parseWrite(arg1, arg2, arg3, arg4, arg5, player);
		else
			BaseBBSManager.separateAndSend("<html><body><br><br><center>The command: " + url + " isn't implemented.</center></body></html>", player);
	}
	
	public static CommunityBoard getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final CommunityBoard INSTANCE = new CommunityBoard();
	}
}