package net.sf.l2j.gameserver.model.actor.instance;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import Custom.CustomConfig;
import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.manager.BufferManager;
import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.MoveToPawn;
import net.sf.l2j.gameserver.network.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;



public final class BufferNew extends Npc
{
	private static final int PAGE_LIMIT = 10;

	List<Integer> fighter = CustomConfig.LIST_FIGHTER_SET;
	List<Integer> fighterbers = CustomConfig.LIST_FIGHTER_SET_BERS;

	List<Integer> mage = CustomConfig.LIST_MAGE_SET;
	List<Integer> magebers = CustomConfig.LIST_MAGE_SET_BERS;

	public BufferNew(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	public void onAction(Player player)
	{
		if (this != player.getTarget())
		{
			player.setTarget(this);
			player.sendPacket(new MyTargetSelected(getObjectId(), 0));
			player.sendPacket(new ValidateLocation(this));
		}
		else
		{
			if (!canInteract(player))
				player.getAI().setIntention(IntentionType.INTERACT, this);
			else
			{
				// Rotate the player to face the instance
				player.sendPacket(new MoveToPawn(player, this, Npc.INTERACTION_DISTANCE));

				if (hasRandomAnimation())
					onRandomAnimation(Rnd.get(8));

				showMainWindow(player);

				// Send ActionFailed to the player in order to avoid he stucks
				player.sendPacket(ActionFailed.STATIC_PACKET);
			}
		}
	}

	private void showMainWindow(Player activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/mods/buffer/index.htm");
		html.replace("%objectId%", String.valueOf(getObjectId()));
		html.replace("%name%", activeChar.getName());
		html.replace("%buffcount%", "You have " + activeChar.getBuffCount() + "/" + activeChar.getMaxBuffCount() + " buffs.");

		html.replace("%buffing%", activeChar.getBuff() == 0 ? "Yourself" : "Your pet");

		activeChar.sendPacket(html);
	}

	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (player.getPvpFlag() > 0  )
		{
			player.sendMessage("You can't use buffer when you are pvp flagged.");
			return;
		}

		if (player.isInCombat() )
		{
			player.sendMessage("You can't use buffer when you are in combat.");
			return;
		}

		if (player.isDead())
			return;

		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken();

		if (actualCommand.equalsIgnoreCase("restore"))
		{
			String noble = st.nextToken();

			//pet implement
			if (player.getBuff() == 0) {
				player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
				player.setCurrentCp(player.getMaxCp());

				if (noble.equals("true"))
				{
					SkillTable.getInstance().getInfo(1323, 1).getEffects(player, player);
					player.broadcastPacket(new MagicSkillUse(this, player, 1323, 1, 850, 0));
				}
			}
			else if (player.getSummon() != null){
				final Summon summon = player.getSummon();
				summon.setCurrentHpMp(summon.getMaxHp(), summon.getMaxMp());
			}
			showMainWindow(player);
		}
		else if (actualCommand.equalsIgnoreCase("noble"))
		{
			SkillTable.getInstance().getInfo(1323, 1).getEffects(player, player);
			player.broadcastPacket(new MagicSkillUse(this, player, 1323, 1, 850, 0));
			showMainWindow(player);
		}
		else if (actualCommand.equalsIgnoreCase("cancellation"))
		{
			L2Skill buff;
			buff = SkillTable.getInstance().getInfo(1056, 1);

			//pet implement
			if (player.getBuff() == 0) {
				buff.getEffects(this, player);
				player.stopAllEffectsExceptThoseThatLastThroughDeath();
				player.broadcastPacket(new MagicSkillUse(this, player, 1056, 1, 850, 0));
				player.stopAllEffects();
			}
			else if (player.getSummon() != null){
				final Summon summon = player.getSummon();
				summon.stopAllEffects();
			}
			showMainWindow(player);
		}
		else if (command.equals("changebuff"))
		{
			player.setBuff(player.getBuff() == 0 ? 1 : 0);
			showMainWindow(player);
		}
		else if (actualCommand.equalsIgnoreCase("openlist"))
		{
			String category = st.nextToken();
			String htmfile = st.nextToken();

			NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());

			if (category.equalsIgnoreCase("null"))
			{
				html.setFile("data/html/mods/buffer/" + htmfile + ".htm");

				// First Page
				if (htmfile.equals("index"))
				{
					html.replace("%name%", player.getName());
					html.replace("%buffcount%", "You have " + player.getBuffCount() + "/" + player.getMaxBuffCount() + " buffs.");
				}
			}
			else
				html.setFile("data/html/mods/buffer/" + category + "/" + htmfile + ".htm");

			html.replace("%objectId%", String.valueOf(getObjectId()));
			player.sendPacket(html);
		}

		else if (actualCommand.equalsIgnoreCase("dobuff"))
		{
			int buffid = Integer.valueOf(st.nextToken());
			int bufflevel = Integer.valueOf(st.nextToken());
			String category = st.nextToken();
			String windowhtml = st.nextToken();

			//pet implement
			if (player.getBuff() == 0) {
				MagicSkillUse mgc = new MagicSkillUse(this, player, buffid, bufflevel, 1150, 0);
				player.sendPacket(mgc);
				player.broadcastPacket(mgc);
			}
			else if (player.getSummon() != null){
				MagicSkillUse mgc = new MagicSkillUse(this, player.getSummon(), buffid, bufflevel, 1150, 0);
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

			NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/mods/buffer/" + category + "/" + windowhtml + ".htm");
			html.replace("%objectId%", String.valueOf(getObjectId()));
			html.replace("%name%", player.getName());
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("getbuff"))
		{
			int buffid = Integer.valueOf(st.nextToken());
			int bufflevel = Integer.valueOf(st.nextToken());
			if (buffid != 0)
			{
				//pet implement
				if (player.getBuff() == 0) {
					MagicSkillUse mgc = new MagicSkillUse(this, player, buffid, bufflevel, 450, 0);
					player.sendPacket(mgc);
					player.broadcastPacket(mgc);
				}
				else if (player.getSummon() != null){
					MagicSkillUse mgc = new MagicSkillUse(this, player.getSummon(), buffid, bufflevel, 450, 0);
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
		else if (actualCommand.startsWith("support"))
		{
			showGiveBuffsWindow(player, st.nextToken());
		}
		else if (actualCommand.startsWith("givebuffs"))
		{
			final String targetType = st.nextToken();
			final String schemeName = st.nextToken();
			final int cost = Integer.parseInt(st.nextToken());

			//pet implement
			if (player.getBuff() == 0) {
				if (cost == 0 || player.reduceAdena("NPC Buffer", cost, this, true))
				{
					for (int skillId : BufferManager.getInstance().getScheme(player.getObjectId(), schemeName))
						SkillTable.getInstance().getInfo(skillId, SkillTable.getInstance().getMaxLevel(skillId)).getEffects(player, player);
				}
			}
			else
			{
				if(player.getSummon() != null) {
					final Summon summon = player.getSummon();
					if (cost == 0 || player.reduceAdena("NPC Buffer", cost, this, true))
					{
						for (int skillId : BufferManager.getInstance().getScheme(player.getObjectId(), schemeName))
							SkillTable.getInstance().getInfo(skillId, SkillTable.getInstance().getMaxLevel(skillId)).getEffects(summon, summon);
					}
				}

			}
			showGiveBuffsWindow(player, targetType);
		}
		else if (actualCommand.startsWith("editschemes"))
		{
			if (st.countTokens() == 3)
				showEditSchemeWindow(player, st.nextToken(), st.nextToken(), Integer.parseInt(st.nextToken()));
			else
				player.sendMessage("Something wrong with your scheme. Please contact with Admin");
		}
		else if (actualCommand.startsWith("skill"))
		{
			final String groupType = st.nextToken();
			final String schemeName = st.nextToken();

			final int skillId = Integer.parseInt(st.nextToken());
			final int page = Integer.parseInt(st.nextToken());

			final List<Integer> skills = BufferManager.getInstance().getScheme(player.getObjectId(), schemeName);

			if (actualCommand.startsWith("skillselect") && !schemeName.equalsIgnoreCase("none"))
			{
				if (skills.size() < player.getMaxBuffCount())//Config.MAX_BUFFS_AMOUNT
					skills.add(skillId);
				else
					player.sendMessage("This scheme has reached the maximum amount of buffs.");
			}
			else if (actualCommand.startsWith("skillunselect"))
				skills.remove(Integer.valueOf(skillId));

			showEditSchemeWindow(player, groupType, schemeName, page);
		}
		else if (actualCommand.startsWith("manageschemes"))
		{
			showManageSchemeWindow(player);
		}
		else if (actualCommand.startsWith("createscheme"))
		{
			try
			{
				final String schemeName = st.nextToken();
				if (schemeName.length() > 14)
				{
					player.sendMessage("Scheme's name must contain up to 14 chars. Spaces are trimmed.");
					showManageSchemeWindow(player);
					return;
				}

				final Map<String, ArrayList<Integer>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());
				if (schemes != null)
				{
					if (schemes.size() == Config.BUFFER_MAX_SCHEMES)
					{
						player.sendMessage("Maximum schemes amount is already reached.");
						showManageSchemeWindow(player);
						return;
					}

					if (schemes.containsKey(schemeName))
					{
						player.sendMessage("The scheme name already exists.");
						showManageSchemeWindow(player);
						return;
					}
				}

				BufferManager.getInstance().setScheme(player.getObjectId(), schemeName.trim(), new ArrayList<Integer>());
				showManageSchemeWindow(player);
			}
			catch (Exception e)
			{
				player.sendMessage("Scheme's name must contain up to 14 chars. Spaces are trimmed.");
				showManageSchemeWindow(player);
			}
		}
		else if (actualCommand.startsWith("deletescheme"))
		{
			try
			{
				final String schemeName = st.nextToken();
				final Map<String, ArrayList<Integer>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());

				if (schemes != null && schemes.containsKey(schemeName))
					schemes.remove(schemeName);
			}
			catch (Exception e)
			{
				player.sendMessage("This scheme name is invalid.");
			}
			showManageSchemeWindow(player);
		}
		else if (actualCommand.startsWith("clearscheme"))
		{
			try
			{
				final String schemeName = st.nextToken();
				final Map<String, ArrayList<Integer>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());

				if (schemes != null && schemes.containsKey(schemeName))
					schemes.get(schemeName).clear();
			}
			catch (Exception e)
			{
				player.sendMessage("This scheme name is invalid.");
			}
			showManageSchemeWindow(player);
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
		else
			super.onBypassFeedback(player, command);
	}

	/**
	 * Sends an html packet to player with Give Buffs menu info for player and pet, depending on targetType parameter {player, pet}
	 * @param player : The player to make checks on.
	 * @param targetType : a String used to define if the player or his pet must be used as target.
	 */
	private void showGiveBuffsWindow(Player player, String targetType)
	{
		final StringBuilder sb = new StringBuilder(200);

		final Map<String, ArrayList<Integer>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());
		if (schemes == null || schemes.isEmpty())
			sb.append("<font color=\"LEVEL\">You haven't defined any scheme, please go to 'Manage my schemes' and create at least one valid scheme.</font>");
		else
		{
			for (Map.Entry<String, ArrayList<Integer>> scheme : schemes.entrySet())
			{
				final int cost = getFee(scheme.getValue());
				StringUtil.append(sb, "<font color=\"LEVEL\"><a action=\"bypass -h npc_%objectId%_givebuffs ", targetType, " ", scheme.getKey(), " ", cost, "\">", scheme.getKey(), " (", scheme.getValue().size(), " skill(s))</a>", ((cost > 0) ? " - Adena cost: " + cost : ""), "</font><br1>");
			}
		}

		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/mods/buffer/schememanager/index-1.htm");
		html.replace("%schemes%", sb.toString());
		html.replace("%targettype%", (targetType.equalsIgnoreCase("pet") ? "&nbsp;<a action=\"bypass -h npc_%objectId%_support player\">yourself</a>&nbsp;|&nbsp;your pet" : "yourself&nbsp;|&nbsp;<a action=\"bypass -h npc_%objectId%_support pet\">your pet</a>"));
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}

	/**
	 * Sends an html packet to player with Manage scheme menu info. This allows player to create/delete/clear schemes
	 * @param player : The player to make checks on.
	 */
	private void showManageSchemeWindow(Player player)
	{
		final StringBuilder sb = new StringBuilder(200);

		final Map<String, ArrayList<Integer>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());
		if (schemes == null || schemes.isEmpty())
			sb.append("<font color=\"LEVEL\">You haven't created any scheme.</font>");
		else
		{
			sb.append("<table>");
			for (Map.Entry<String, ArrayList<Integer>> scheme : schemes.entrySet())
				StringUtil.append(sb, "<tr><td width=140>", scheme.getKey(), " (", scheme.getValue().size(), " skill(s))</td><td width=60><button value=\"Clear\" action=\"bypass -h npc_%objectId%_clearscheme ", scheme.getKey(), "\" width=55 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td><td width=60><button value=\"Drop\" action=\"bypass -h npc_%objectId%_deletescheme ", scheme.getKey(), "\" width=55 height=15 back=\"sek.cbui94\" fore=\"sek.cbui92\"></td></tr>");

			sb.append("</table>");
		}

		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/mods/buffer/schememanager/index-2.htm");
		html.replace("%schemes%", sb.toString());
		html.replace("%max_schemes%", Config.BUFFER_MAX_SCHEMES);
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}

	/**
	 * This sends an html packet to player with Edit Scheme Menu info. This allows player to edit each created scheme (add/delete skills)
	 * @param player : The player to make checks on.
	 * @param groupType : The group of skills to select.
	 * @param schemeName : The scheme to make check.
	 */
	private void showEditSchemeWindow(Player player, String groupType, String schemeName, int page)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);

		if (schemeName.equalsIgnoreCase("none"))
			html.setFile("data/html/mods/buffer/schememanager/index-3.htm");
		else
		{
			if (groupType.equalsIgnoreCase("none"))
				html.setFile("data/html/mods/buffer/schememanager/index-4.htm");
			else
			{
				html.setFile("data/html/mods/buffer/schememanager/index-5.htm");
				html.replace("%skilllistframe%", getGroupSkillList(player, groupType, schemeName));
			}
			html.replace("%schemename%", schemeName);
			html.replace("%myschemeframe%", getPlayerSchemeSkillList(player, groupType, schemeName, page));
			html.replace("%typesframe%", getTypesFrame(groupType, schemeName));
		}
		html.replace("%schemes%", getPlayerSchemes(player, schemeName));
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}

	/**
	 * @param player : The player to make checks on.
	 * @param schemeName : The name to don't link (previously clicked).
	 * @return a String listing player's schemes. The scheme currently on selection isn't linkable.
	 */
	private static String getPlayerSchemes(Player player, String schemeName)
	{
		final Map<String, ArrayList<Integer>> schemes = BufferManager.getInstance().getPlayerSchemes(player.getObjectId());
		if (schemes == null || schemes.isEmpty())
			return "Please create at least one scheme.";

		final StringBuilder sb = new StringBuilder(200);
		sb.append("<table>");

		for (Map.Entry<String, ArrayList<Integer>> scheme : schemes.entrySet())
		{
			if (schemeName.equalsIgnoreCase(scheme.getKey()))
				StringUtil.append(sb, "<tr><td width=200>", scheme.getKey(), " (<font color=\"LEVEL\">", scheme.getValue().size(), "</font> / ", player.getMaxBuffCount(), " skill(s))</td></tr>");
			else
				StringUtil.append(sb, "<tr><td width=200><a action=\"bypass -h npc_%objectId%_editschemes none ", scheme.getKey(), " 1\">", scheme.getKey(), " (", scheme.getValue().size(), " / ", player.getMaxBuffCount(), " skill(s))</a></td></tr>");
		}

		sb.append("</table>");

		return sb.toString();
	}

	/**
	 * @param player : The player to make checks on.
	 * @param groupType : The group of skills to select.
	 * @param schemeName : The scheme to make check.
	 * @return a String representing skills available to selection for a given groupType.
	 */
	private static String getGroupSkillList(Player player, String groupType, String schemeName)
	{
		final List<Integer> skills = new ArrayList<>();
		for (int skillId : BufferManager.getInstance().getSkillsIdsByType(groupType))
		{
			if (BufferManager.getInstance().getSchemeContainsSkill(player.getObjectId(), schemeName, skillId))
				continue;

			skills.add(skillId);
		}

		if (skills.isEmpty())
			return "That group doesn't contain any skills.";

		final StringBuilder sb = new StringBuilder(500);

		sb.append("<table>");
		int count = 0;
		for (int skillId : skills)
		{
			if (BufferManager.getInstance().getSchemeContainsSkill(player.getObjectId(), schemeName, skillId))
				continue;

			if (count == 0)
				sb.append("<tr>");

			if (skillId < 100)
				sb.append("<td width=180><font color=\"949490\"><a action=\"bypass -h npc_%objectId%_skillselect " + groupType + " " + schemeName + " " + skillId + " 1\">" + SkillTable.getInstance().getInfo(skillId, 1).getName() + "</a></font></td>");
			else if (skillId < 1000)
				sb.append("<td width=180><font color=\"949490\"><a action=\"bypass -h npc_%objectId%_skillselect " + groupType + " " + schemeName + " " + skillId + " 1\">" + SkillTable.getInstance().getInfo(skillId, 1).getName() + "</a></font></td>");
			else
				sb.append("<td width=180><font color=\"949490\"><a action=\"bypass -h npc_%objectId%_skillselect " + groupType + " " + schemeName + " " + skillId + " 1\">" + SkillTable.getInstance().getInfo(skillId, 1).getName() + "</a></font></td>");

			count++;
			if (count == 2)
			{
				sb.append("</tr><tr><td></td></tr>");
				count = 0;
			}
		}

		if (!sb.toString().endsWith("</tr>"))
			sb.append("</tr>");

		sb.append("</table>");

		return sb.toString();
	}

	/**
	 * @param player : The player to make checks on.
	 * @param groupType : The group of skills to select.
	 * @param schemeName : The scheme to make check.
	 * @return a String representing a given scheme's content.
	 */
	private  String getPlayerSchemeSkillList(Player player, String groupType, String schemeName, int page)
	{
		List<Integer> skills = BufferManager.getInstance().getScheme(player.getObjectId(), schemeName);
		if (skills.isEmpty())
			return "That scheme is empty.";

		// Calculate page number.
		final int max = MathUtil.countPagesNumber(skills.size(), PAGE_LIMIT);
		if (page > max)
			page = max;

		skills = skills.subList((page - 1) * PAGE_LIMIT, Math.min(page * PAGE_LIMIT, skills.size()));
		final StringBuilder sb = new StringBuilder(skills.size() * 150);

		int count = 0;
		for (int sk : skills)
		{
			count++;
			if (count == 1)
				sb.append("<table width=\"280\"><tr>");


			if (sk < 100)
				sb.append("<td width=180><font color=\"6e6e6a\"><a action=\"bypass -h npc_%objectId%_skillunselect " + groupType + " " + schemeName + " " + sk + " " + page +  "\">" + SkillTable.getInstance().getInfo(sk, 1).getName() + "</a></font></td>");
			else if (sk < 1000)
				sb.append("<td width=180><font color=\"6e6e6a\"><a action=\"bypass -h npc_%objectId%_skillunselect " + groupType + " " + schemeName + " " + sk + " " + page +  "\">" + SkillTable.getInstance().getInfo(sk, 1).getName() + "</a></font></td>");
			else
				sb.append("<td width=180><font color=\"6e6e6a\"><a action=\"bypass -h npc_%objectId%_skillunselect " + groupType + " " + schemeName + " " + sk + " " + page +  "\">" + SkillTable.getInstance().getInfo(sk, 1).getName() + "</a></font></td>");


			if (count == 2)
			{
				sb.append("</tr></table>");
				count = 0;
			}
		}

		// Build page footer.
		sb.append("<br><img src=\"L2UI.SquareGray\" width=277 height=1><table width=\"90%\" bgcolor=000000><tr>");

		if (page > 1)
			StringUtil.append(sb, "<td align=left width=70><a action=\"bypass npc_" + getObjectId() + "_editschemes ", groupType, " ", schemeName, " ", page - 1, "\">Previous</a></td>");
		else
			StringUtil.append(sb, "<td align=left width=70>Previous</td>");

		StringUtil.append(sb, "<td align=center width=100>Page ", page, "</td>");

		if (page < max)
			StringUtil.append(sb, "<td align=right width=70><a action=\"bypass npc_" + getObjectId() + "_editschemes ", groupType, " ", schemeName, " ", page + 1, "\">Next</a></td>");
		else
			StringUtil.append(sb, "<td align=right width=70>Next</td>");

		sb.append("</tr></table>");


		return sb.toString();
	}

	/**
	 * @param groupType : The group of skills to select.
	 * @param schemeName : The scheme to make check.
	 * @return a string representing all groupTypes availables. The group currently on selection isn't linkable.
	 */
	private static String getTypesFrame(String groupType, String schemeName)
	{
		final StringBuilder sb = new StringBuilder(500);
		sb.append("<table>");

		int count = 0;
		for (String s : BufferManager.getInstance().getSkillTypes())
		{
			if (count == 0)
				sb.append("<tr>");

			if (groupType.equalsIgnoreCase(s))
				StringUtil.append(sb, "<td width=65>", s, "</td>");
			else
				StringUtil.append(sb, "<td width=65><a action=\"bypass -h npc_%objectId%_editschemes ", s, " ", schemeName, " 1\">", s, "</a></td>");

			count++;
			if (count == 4)
			{
				sb.append("</tr>");
				count = 0;
			}
		}

		if (!sb.toString().endsWith("</tr>"))
			sb.append("</tr>");

		sb.append("</table>");

		return sb.toString();
	}

	/**
	 * @param list : A list of skill ids.
	 * @return a global fee for all skills contained in list.
	 */
	private static int getFee(ArrayList<Integer> list)
	{
		if (Config.BUFFER_STATIC_BUFF_COST > 0)
			return list.size() * Config.BUFFER_STATIC_BUFF_COST;

		int fee = 0;
		for (int sk : list)
			fee += BufferManager.getInstance().getAvailableBuff(sk).getValue();

		return fee;
	}

}