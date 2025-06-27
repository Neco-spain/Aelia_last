package net.sf.l2j.gameserver.network.clientpackets;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import dev.l2j.tesla.autobots.AutobotsManager;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.communitybbs.CommunityBoard;
import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.data.manager.HeroManager;
import net.sf.l2j.gameserver.data.xml.AdminData;
import net.sf.l2j.gameserver.handler.AdminCommandHandler;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
import net.sf.l2j.gameserver.handler.admincommandhandlers.Custom.AdminBalanceStat;
import net.sf.l2j.gameserver.handler.voicedcommandhandlers.Repair;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.DonateShop;
import net.sf.l2j.gameserver.model.actor.instance.OlympiadManagerNpc;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.FloodProtectors;
import net.sf.l2j.gameserver.network.FloodProtectors.Action;
import net.sf.l2j.gameserver.network.GameClient;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.scripting.QuestState;

import Customs.BalanceStatus.BalancerEdit;

public final class RequestBypassToServer extends L2GameClientPacket
{
	private static final Logger GMAUDIT_LOG = Logger.getLogger("gmaudit");
	
	private String _command;
	
	@Override
	protected void readImpl()
	{
		_command = readS();
	}
	
	@Override
	protected void runImpl()
	{
		if (_command.isEmpty())
			return;
		final Player player = getClient().getPlayer();
		if (player == null)
			return;

		if (!FloodProtectors.performAction(getClient(), Action.SERVER_BYPASS) && !player.isGM())
			return;
		


		
		if (_command.startsWith("admin_"))
		{
			String command = _command.split(" ")[0];
			
			final IAdminCommandHandler ach = AdminCommandHandler.getInstance().getHandler(command);
			if (ach == null)
			{
				if (player.isGM())
					player.sendMessage("The command " + command.substring(6) + " doesn't exist.");
				
				LOGGER.warn("No handler registered for admin command '{}'.", command);
				return;
			}
			
			if (!AdminData.getInstance().hasAccess(command, player.getAccessLevel()))
			{
				player.sendMessage("You don't have the access rights to use this command.");
				LOGGER.warn("{} tried to use admin command '{}' without proper Access Level.", player.getName(), command);
				return;
			}
			
			if (Config.GMAUDIT)
				GMAUDIT_LOG.info(player.getName() + " [" + player.getObjectId() + "] used '" + _command + "' command on: " + ((player.getTarget() != null) ? player.getTarget().getName() : "none"));
			
			ach.useAdminCommand(_command, player);
		}
		else if (_command.startsWith("player_help "))
		{
			final String path = _command.substring(12);
			if (path.indexOf("..") != -1)
				return;
			
			final StringTokenizer st = new StringTokenizer(path);
			final String[] cmd = st.nextToken().split("#");
			
			final NpcHtmlMessage html = new NpcHtmlMessage(0);
			html.setFile("data/html/help/" + cmd[0]);
			if (cmd.length > 1)
			{
				final int itemId = Integer.parseInt(cmd[1]);
				html.setItemId(itemId);
				
				if (itemId == 7064 && cmd[0].equalsIgnoreCase("lidias_diary/7064-16.htm"))
				{
					final QuestState qs = player.getQuestState("Q023_LidiasHeart");
					if (qs != null && qs.getInt("cond") == 5 && qs.getInt("diary") == 0)
						qs.set("diary", "1");
				}
			}
			html.disableValidation();
			player.sendPacket(html);
		}
		else if (_command.startsWith("npc_"))
		{
			if (!player.validateBypass(_command))
				return;
			
			int endOfId = _command.indexOf('_', 5);
			String id;
			if (endOfId > 0)
				id = _command.substring(4, endOfId);
			else
				id = _command.substring(4);
			
			try
			{
				final WorldObject object = World.getInstance().getObject(Integer.parseInt(id));
				
				if (object != null && object instanceof Npc && endOfId > 0 && ((Npc) object).canInteract(player))
					((Npc) object).onBypassFeedback(player, _command.substring(endOfId + 1));
				
				player.sendPacket(ActionFailed.STATIC_PACKET);
			}
			catch (NumberFormatException nfe)
			{
			}
		}
		// Navigate throught Manor windows
		else if (_command.startsWith("manor_menu_select?"))
		{
			WorldObject object = player.getTarget();
			if (object instanceof Npc)
				((Npc) object).onBypassFeedback(player, _command);
		}
		else if (_command.startsWith("_cbs") || _command.startsWith("bbs_") || _command.startsWith("_bbs") || _command.startsWith("_friend") || _command.startsWith("_mail") || _command.startsWith("_block"))
		{
			CommunityBoard.getInstance().handleCommands(getClient(), _command);
		}
		else if (_command.startsWith("Quest "))
		{
			if (!player.validateBypass(_command))
				return;
			
			String[] str = _command.substring(6).trim().split(" ", 2);
			if (str.length == 1)
				player.processQuestEvent(str[0], "");
			else
				player.processQuestEvent(str[0], str[1]);
		}
		else if (_command.startsWith("_match"))
		{
			String params = _command.substring(_command.indexOf("?") + 1);
			StringTokenizer st = new StringTokenizer(params, "&");
			int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
			int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
			int heroid = HeroManager.getInstance().getHeroByClass(heroclass);
			if (heroid > 0)
				HeroManager.getInstance().showHeroFights(player, heroclass, heroid, heropage);
		}
		else if (_command.startsWith("_diary"))
		{
			String params = _command.substring(_command.indexOf("?") + 1);
			StringTokenizer st = new StringTokenizer(params, "&");
			int heroclass = Integer.parseInt(st.nextToken().split("=")[1]);
			int heropage = Integer.parseInt(st.nextToken().split("=")[1]);
			int heroid = HeroManager.getInstance().getHeroByClass(heroclass);
			if (heroid > 0)
				HeroManager.getInstance().showHeroDiary(player, heroclass, heroid, heropage);
		}
		else if (_command.startsWith("arenachange")) // change
		{
			final boolean isManager = player.getCurrentFolk() instanceof OlympiadManagerNpc;
			if (!isManager)
			{
				// Without npc, command can be used only in observer mode on arena
				if (!player.isInObserverMode() || player.isInOlympiadMode() || player.getOlympiadGameId() < 0)
					return;
			}
			
			if (OlympiadManager.getInstance().isRegisteredInComp(player))
			{
				player.sendPacket(SystemMessageId.WHILE_YOU_ARE_ON_THE_WAITING_LIST_YOU_ARE_NOT_ALLOWED_TO_WATCH_THE_GAME);
				return;
			}
			
			final int arenaId = Integer.parseInt(_command.substring(12).trim());
			player.enterOlympiadObserverMode(arenaId);

			AutobotsManager.INSTANCE.onBypass(player, _command);

		}



		
		/* CUSTOM MODIFICATIONS*/
		
		/*balancer*/
		else if (_command.startsWith("balance "))
		{
//			String bp = _command.substring(12);
//			StringTokenizer st = new StringTokenizer(bp);
			final StringTokenizer st = new StringTokenizer(_command);
             st.nextToken();
             
             
			if (st.countTokens() != 1)
			{
				return;
			}
			
			int classId = Integer.parseInt(st.nextToken());
			
			AdminBalanceStat.sendBalanceWindow(classId, player);
		}
		else if (_command.startsWith("bal_add "))
		{
//			String bp = _command.substring(9);
//			StringTokenizer st = new StringTokenizer(bp);
			final StringTokenizer st = new StringTokenizer(_command);
            st.nextToken();
             
			if (st.countTokens() != 3)
			{
				return;
			}
			
			String stat = st.nextToken();
			int classId = Integer.parseInt(st.nextToken()),
				value = Integer.parseInt(st.nextToken());
			
			BalancerEdit.editStat(stat, classId, value, true);
			
			AdminBalanceStat.sendBalanceWindow(classId, player);
		}
		
		else if (_command.startsWith("bal_rem "))
		{
//			String bp = _command.substring(9);
//			StringTokenizer st = new StringTokenizer(bp);
			final StringTokenizer st = new StringTokenizer(_command);
			 st.nextToken();
			 
			if (st.countTokens() != 3)
			{
				return;
			}
			
			String stat = st.nextToken();
			int classId = Integer.parseInt(st.nextToken()),
				value = Integer.parseInt(st.nextToken());
			
			BalancerEdit.editStat(stat, classId, value, false);
			
			AdminBalanceStat.sendBalanceWindow(classId, player);
		}
		
		
		/* voiced command handlers */
		else if (_command.startsWith("voice_"))
		{
			String params = "";
			String command;
			if (_command.indexOf(" ") != -1)
			{
				command = _command.substring(6, _command.indexOf(" "));
				params = _command.substring(_command.indexOf(" ") + 1);
			}
			else
			{
				command = _command.substring(6);
			}

			IVoicedCommandHandler vc = VoicedCommandHandler.getInstance().getHandler(command);

			if (vc == null)
			{
				return;
			}
			vc.useVoicedCommand(command, player, params);
		}
		
		else if (_command.startsWith("voiced_"))
		{
			String command = _command.split(" ")[0];

			IVoicedCommandHandler ach = VoicedCommandHandler.getInstance().getHandler(_command.substring(7));

			if (ach == null)
			{
				player.sendMessage("The command " + command.substring(7) + " does not exist!");
				//_log.warning("No handler registered for command '" + _command + "'");
				return;
			}

			ach.useVoicedCommand(_command.substring(7), player, null);
		}
		else if (_command.startsWith("_bbsvoiced_"))
		{
			String command = _command.split(" ")[0];

			IVoicedCommandHandler ach = VoicedCommandHandler.getInstance().getHandler(_command.substring(7));

			if (ach == null)
			{
				player.sendMessage("The command " + command.substring(7) + " does not exist!");
				return;
			}

			ach.useVoicedCommand(_command.substring(7), player, null);
		}


		
		//custom donate shop
		else if (_command.startsWith("base"))
			DonateShop.Classes(_command, player);
		
		
		else if (_command.startsWith("repairchar "))
		{
			String value = _command.substring(11);
			StringTokenizer st = new StringTokenizer(value);
			String repairChar = null;

			try
			{
				if (st.hasMoreTokens())
					repairChar = st.nextToken();
			}
			catch (Exception e)
			{
				player.sendMessage("You can't put empty box.");
				return;
			}

			if (repairChar == null || repairChar.equals(""))
				return;

			if (Repair.checkAcc(player, repairChar))
			{
				if (Repair.checkChar(player, repairChar))
				{
					String htmContent = HtmCache.getInstance().getHtm("data/html/mods/repair/repair-self.htm");
					NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
					npcHtmlMessage.setHtml(htmContent);
					player.sendPacket(npcHtmlMessage);
					return;
				}
				else if (Repair.checkPunish(player, repairChar))
				{
					String htmContent = HtmCache.getInstance().getHtm("data/html/mods/repair/repair-jail.htm");
					NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
					npcHtmlMessage.setHtml(htmContent);
					player.sendPacket(npcHtmlMessage);
					return;
				}
				else if (Repair.checkKarma(player, repairChar))
				{
					player.sendMessage("Selected Char has Karma,Cannot be repaired!");
					return;
				}
				else
				{
					Repair.repairBadCharacter(repairChar);
					String htmContent = HtmCache.getInstance().getHtm("data/html/mods/repair/repair-done.htm");
					NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
					npcHtmlMessage.setHtml(htmContent);
					player.sendPacket(npcHtmlMessage);
					return;
				}
			}

			String htmContent = HtmCache.getInstance().getHtm("data/html/mods/repair/repair-error.htm");
			NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
			npcHtmlMessage.setHtml(htmContent);
			npcHtmlMessage.replace("%acc_chars%", Repair.getCharList(player));
			player.sendPacket(npcHtmlMessage);
		}
	
		
		
	}
}