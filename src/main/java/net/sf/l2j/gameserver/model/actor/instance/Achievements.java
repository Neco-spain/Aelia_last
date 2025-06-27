package net.sf.l2j.gameserver.model.actor.instance;


import Customs.mods.Achievements.Achievements.base.Achievement;
import Customs.mods.Achievements.Achievements.base.Condition;
import Customs.mods.Achievements.AchievementsManager;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.serverpackets.*;

import java.util.StringTokenizer;

public class Achievements extends Folk
{
	private boolean first = true;

	public Achievements(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken();

		if (actualCommand.startsWith("showMyAchievements"))
		{
			player.getAchievemntData();
			showMyAchievements(player);
		}
		else if (actualCommand.startsWith("achievementInfo"))
		{
			int id = Integer.parseInt(st.nextToken());

			showAchievementInfo(id, player);
		}
		else if (actualCommand.startsWith("getReward"))
		{
			int id = Integer.parseInt(st.nextToken());

		 	if (id == 3 || id == 4)
			{
				ItemInstance weapon = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);

				if (weapon != null)
				{
					int objid = weapon.getObjectId();
					if (AchievementsManager.getInstance().getAchievementList().get(id).meetAchievementRequirements(player))
					{
						if (!AchievementsManager.getInstance().isBinded(objid, id))
						{
							AchievementsManager.getInstance().getBinded().add(objid + "@" + id);
							player.saveAchievementData(id, objid);
							AchievementsManager.getInstance().rewardForAchievement(id, player);
						}
						else
							player.sendMessage("This item was already used to earn this achievement");
					}
					else
					{
						player.sendMessage("Seems you don't meet the achievements requirements now.");
					}
				}
				else
					player.sendMessage("You must equip your weapon in order to get rewarded.");
			}
			else
			{
				player.saveAchievementData(id, 0);
				AchievementsManager.getInstance().rewardForAchievement(id, player);
			}
			showMyAchievements(player);
		}
	}


	@Override
	public void onAction(Player player)
	{
		if (this != player.getTarget()) {
			player.setTarget(this);
			player.sendPacket(new MyTargetSelected(getObjectId(), 0));
			player.sendPacket(new ValidateLocation(this));
		}
		else {
			if (!canInteract(player)) {
				player.getAI().setIntention(IntentionType.INTERACT, this);
			} else {
				// Rotate the player to face the instance
				player.sendPacket(new MoveToPawn(player, this, Npc.INTERACTION_DISTANCE));

				player.getAchievemntData();
				showMyAchievements(player);
			}
		}
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	private void showMyAchievements(Player player)
	{
		if (first)
		{
			AchievementsManager.getInstance().loadUsed();
			first = false;
		}

		NpcHtmlMessage msg = new NpcHtmlMessage(0);
		StringBuilder tb = new StringBuilder();

		tb.append("<html><title>Achievements Manager</title><body><br><center>");
		tb.append("<img src=\"l2ui.squaregray\" width=\"230\" height=\"1\"><br1>");

		tb.append("<table width=\"230\" cellpadding=\"5\" bgcolor=\"000000\"><tr>");
		tb.append("<td valign=\"top\"><font color=\"FF6600\">Character Achievements</font><br1>Beat your goals and get rewarded.</td></tr></table><br1>");

		tb.append("<img src=\"l2ui.squaregray\" width=\"230\" height=\"1\"><br>");
		tb.append("<img src=\"l2ui.squaregray\" width=\"230\" height=\"1\"><br1>");

		if (AchievementsManager.getInstance().getAchievementList().isEmpty())
		{
			tb.append("There are no Achievements created yet!");
		}
		else
		{
			int i = 0;

			for (Achievement a : AchievementsManager.getInstance().getAchievementList().values())
			{
				tb.append(getTableColor(i));
				tb.append("<tr><td width=270 align=\"left\">" + a.getName() + "</td><td width=50 align=\"right\"><a action=\"bypass -h npc_%objectId%_achievementInfo " + a.getID() + "\">info</a></td><td width=200 align=\"center\">" + getStatusString(a.getID(), player) + "</td></tr></table>");
				i++;
			}

			tb.append("<br><img src=\"l2ui.squaregray\" width=\"230\" height=\"1\">");
		}

		tb.append("</body></html>");
		msg.setHtml(tb.toString());
		msg.replace("%objectId%", getObjectId());
		player.sendPacket(msg);
	}

	private void showAchievementInfo(int achievementID, Player player)
	{
		Achievement a = AchievementsManager.getInstance().getAchievementList().get(achievementID);

		NpcHtmlMessage msg = new NpcHtmlMessage(0);
		StringBuilder tb = new StringBuilder();

		tb.append("<html><title>Achievements Manager</title><body><br><center>");

		tb.append("<center><table width=270 border=0>");
		tb.append("<tr><td width=270 align=\"center\">" + a.getName() + "</td></tr></table><br>");
		tb.append("<center>Status: " + getStatusString(achievementID, player));

		if (a.meetAchievementRequirements(player) && !player.getCompletedAchievements().contains(achievementID))
			tb.append("<button value=\"Get Reward\" action=\"bypass -h npc_%objectId%_getReward " + a.getID() + "\" width=93 height=18 back=\"BotoesNpc.botaomensagem_over\" fore=\"BotoesNpc.botaomensagem\">");

		tb.append("<br><img src=\"l2ui.squaregray\" width=\"270\" height=\"1s\"><br>");

		tb.append("<table width=270 border=0 >");
		tb.append("<tr><td width=270 align=\"center\"><font color=\"FF6600\">Description</font></td></tr></table><br>");
		tb.append(a.getDescription());
		tb.append("<br><img src=\"l2ui.squaregray\" width=\"270\" height=\"1s\"><br>");

		if(achievementID >= 7 && achievementID <= 15){
			tb.append("<table width=270 border=0>");
			tb.append("<tr><td width=270 align=\"center\"><font color=\"FF6600\">Current Value</font></td></tr></table><br>");
			tb.append(getConditionsStatus(achievementID, player));
			tb.append("<br><img src=\"l2ui.squaregray\" width=\"270\" height=\"1s\"><br>");
		}

		tb.append("<br><table width=270 border=0 >");
		tb.append("<tr><td width=270 align=\"center\"><font color=\"FF6600\">Reward List</font></td></tr></table><br>");

		for (int id: a.getRewardList().keySet())
		{
			int count = a.getRewardList().get(id).intValue();
			String name = ItemTable.getInstance().getTemplate(id).getName();

			tb.append(name + " " + count + "<br>");
		}

		tb.append("<img src=\"l2ui.squaregray\" width=\"270\" height=\"1s\"><br>");

		tb.append("<br><center><button value=\"Back\" action=\"bypass -h npc_%objectId%_showMyAchievements\" width=93 height=18 back=\"BotoesNpc.botaomensagem_over\" fore=\"BotoesNpc.botaomensagem\"></center>");
		tb.append("</body></html>");

		msg.setHtml(tb.toString());
		msg.replace("%objectId%",getObjectId());
		player.sendPacket(msg);
	}

	private static String getStatusString(int achievementID, Player player)
	{
		if (player.getCompletedAchievements().contains(achievementID))
		{
			return "<font color=\"5EA82E\">Completed</font>";
		}

		if (AchievementsManager.getInstance().getAchievementList().get(achievementID).meetAchievementRequirements(player))
		{
			return "<font color=\"LEVEL\">Get Reward</font>";
		}

		return "<font color=\"FF0000\">Not Completed</font>";
	}

	private static String getTableColor(int i)
	{
		if (i % 2 == 0)
			return "<center><table width=270 border=0 bgcolor=\"000000\">";

		return "<center><table width=270 border=0>";
	}

	private static String getConditionsStatus(int achievementID, Player player)
	{
		String s = "";
		Achievement a = AchievementsManager.getInstance().getAchievementList().get(achievementID);
		String completed = "<font color=\"5EA82E\">Completed</font>";
		String notCompleted = "<font color=\"FF0000\">Not Completed</font>";

		for (Condition c : a.getConditions())
		{
			if (c.meetConditionRequirements(player))
				s += completed;
			else if (!c.getStatus(player).equals("null"))
				s += c.getStatus(player);
			else
				s += notCompleted;
		}
		return s;
	}



}