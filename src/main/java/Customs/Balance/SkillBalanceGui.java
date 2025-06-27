package Customs.Balance;


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.enums.skills.SkillChangeType;
import net.sf.l2j.gameserver.communitybbs.Manager.BaseBBSManager;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.Balance.holder.SkillBalanceHolder;
import Customs.Managers.SkillBalanceManager;


public class SkillBalanceGui extends BaseBBSManager
{
public static SkillBalanceGui getInstance()
{
	return SingletonHolder._instance;
}

private static class SingletonHolder
{
	protected static final SkillBalanceGui _instance = new SkillBalanceGui();
}

@Override
public void parseCmd(String command, Player activeChar)
{
	if (!activeChar.isGM())
		return;
	
	if (command.startsWith("_bbs_skillbalance"))
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken(); // Skip command
		
		final int pageId = st.countTokens() == 2 ? Integer.parseInt(st.nextToken()) : 1;
		final boolean isOly = Boolean.parseBoolean(st.nextToken());
		
		showMainHtml(activeChar, pageId, isOly);
	}
	else if (command.startsWith("_bbs_save_skillbalance"))
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken(); // Skip command
		
		final int pageId = Integer.parseInt(st.nextToken());
		final boolean isOly = Boolean.parseBoolean(st.nextToken());
		
		SkillBalanceManager.getInstance().store(activeChar);
		showMainHtml(activeChar, pageId, isOly);
	}
	else if (command.startsWith("_bbs_remove_skillbalance"))
	{
		final String[] info = command.substring(25).split(" ");
		final String key = info[0];
		final int pageId = info.length > 1 ? Integer.parseInt(info[1]) : 1;
		final int type = Integer.valueOf(info[2]).intValue();
		final boolean isOly = Boolean.parseBoolean(info[3]);
		
		SkillBalanceManager.getInstance().removeSkillBalance(key, SkillChangeType.VALUES[type], isOly);
		showMainHtml(activeChar, pageId, isOly);
	}
	else if (command.startsWith("_bbs_modify_skillbalance"))
	{
		final String[] st = command.split(";");
		final int skillId = Integer.valueOf(st[0].substring(25)).intValue();
		final int target = Integer.valueOf(st[1]).intValue();
		final int changeType = Integer.valueOf(st[2]).intValue();
		final double value = Double.parseDouble(st[3]);
		final int pageId = Integer.parseInt(st[4]);
		final boolean isSearch = Boolean.parseBoolean(st[5]);
		final boolean isOly = Boolean.parseBoolean(st[6]);
		final String key = skillId + ";" + target;
		final SkillBalanceHolder cbh = SkillBalanceManager.getInstance().getSkillHolder(key);
		
		if (isOly)
			cbh.addOlySkillBalance(SkillChangeType.VALUES[changeType], value);
		else
			cbh.addSkillBalance(SkillChangeType.VALUES[changeType], value);
		
		SkillBalanceManager.getInstance().addSkillBalance(key, cbh, true);
		
		if (isSearch)
			showSearchHtml(activeChar, pageId, skillId, isOly);
		else
			showMainHtml(activeChar, pageId, isOly);
	}
	else if (command.startsWith("_bbs_add_menu_skillbalance"))
	{
		final StringTokenizer st = new StringTokenizer(command.substring(27), " ");
		final int pageId = Integer.parseInt(st.nextToken());
		final int tRace = Integer.parseInt(st.nextToken());
		final boolean isOly = Boolean.parseBoolean(st.nextToken());
		
		showAddHtml(activeChar, pageId, tRace, isOly);
	}
	else if (command.startsWith("_bbs_add_skillbalance"))
	{
		final String[] st = command.substring(22).split(";");
		final StringTokenizer st2 = new StringTokenizer(command.substring(22), ";");
		
		if (st2.countTokens() != 5 || st[0].isEmpty() || st[1].isEmpty() || st[2].isEmpty() || st[3].isEmpty() || st[4].isEmpty())
		{
			activeChar.sendMessage("Incorrect input count.");
			return;
		}
		
		final int skillId = Integer.valueOf(st[0].trim()).intValue();
		final String attackTypeSt = st[1].trim();
		final String val = st[2].trim();
		final String targetClassName = st[3].trim();
		final boolean isoly = Boolean.parseBoolean(st[4].trim());
		final double value = Double.parseDouble(val);
		
		if (SkillTable.getInstance().getInfo(skillId, SkillTable.getInstance().getMaxLevel(skillId)) == null)
		{
			activeChar.sendMessage("Skill with id: " + skillId + " not found!");
			return;
		}
		
		int targetClassId = -1;
		if (!targetClassName.equals(""))
		{
			//aply to all classes
			if(targetClassName.equals("All_Classes"))
				targetClassId = -3;
			else{
				for (ClassId cId : ClassId.values())
				{
					if (cId.name().equalsIgnoreCase(targetClassName))
						targetClassId = cId.ordinal();
				}
			}

		}
		
		targetClassId = SkillChangeType.valueOf(attackTypeSt).isOnlyVsAll() ? -2 : targetClassId;
		
		final String key = skillId + ";" + targetClassId;
		final SkillBalanceHolder cbh = SkillBalanceManager.getInstance().getSkillHolder(key) != null ? SkillBalanceManager.getInstance().getSkillHolder(key) : new SkillBalanceHolder(skillId, targetClassId);
		
		if (isoly)
			cbh.addOlySkillBalance(SkillChangeType.valueOf(attackTypeSt), value);
		else
			cbh.addSkillBalance(SkillChangeType.valueOf(attackTypeSt), value);
		
		SkillBalanceManager.getInstance().addSkillBalance(key, cbh, isoly);
		
		showMainHtml(activeChar, 1, isoly);
	}
	else if (command.startsWith("_bbs_search_skillbalance"))
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken(); // Skip command
		
		if (st.countTokens() == 2)
		{
			final int skillId = Integer.valueOf(st.nextToken()).intValue();
			final boolean isOly = Boolean.parseBoolean(st.nextToken());
			
			showSearchHtml(activeChar, 1, skillId, isOly);
		}
	}
	else if (command.startsWith("_bbs_search_nav_skillbalance"))
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken(); // Skip command
		
		if (st.countTokens() == 3)
		{
			final int skillId = Integer.valueOf(st.nextToken()).intValue();
			final int pageID = Integer.valueOf(st.nextToken()).intValue();
			final boolean isOly = Boolean.parseBoolean(st.nextToken());
			
			showSearchHtml(activeChar, pageID, skillId, isOly);
		}
	}
	else if (command.startsWith("_bbs_get_skillbalance"))
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken(); // Skip command
		
		if (st.hasMoreTokens())
		{
			final int skillId = Integer.valueOf(st.nextToken()).intValue();
			final L2Skill skill = SkillTable.getInstance().getInfo(skillId, SkillTable.getInstance().getMaxLevel(skillId));
			if (skill != null)
			{
				activeChar.addSkill(skill, true);
				activeChar.sendMessage("You have learned: " + skill.getName());
			}
		}
	}
}

public void showMainHtml(Player activeChar, int pageId, boolean isolyinfo)
{
	String html = HtmCache.getInstance().getHtm("data/html/mods/balancer/skillbalance/index.htm");
	String info = getSkillBalanceInfo(SkillBalanceManager.getInstance().getAllBalances().values(), pageId, false, isolyinfo);
	
	int count = SkillBalanceManager.getInstance().getSize(isolyinfo);
	int limitInPage = 6;
	
	html = html.replace("<?title?>", isolyinfo ? "Olympiad" : "");
	html = html.replace("<?isoly?>", String.valueOf(isolyinfo));
	html = html.replace("%pageID%", String.valueOf(pageId));
	
	int totalpages = 1;
	int tmpcount = count;
	
	while (tmpcount - 6 > 0)
	{
		totalpages++;
		tmpcount -= 6;
	}
	
	html = html.replace("%totalPages%", String.valueOf(totalpages));
	html = html.replace("%info%", info);
	html = html.replace("%previousPage%", String.valueOf(pageId - 1 != 0 ? pageId - 1 : 1));
	html = html.replace("%nextPage%", String.valueOf(pageId * limitInPage >= count ? pageId : pageId + 1));
	
	separateAndSend(html, activeChar);
}

public void showSearchHtml(Player activeChar, int pageId, int skillId, boolean isolysearch)
{
	String html = HtmCache.getInstance().getHtm("data/html/mods/balancer/skillbalance/search.htm");
	String info = getSkillBalanceInfo(SkillBalanceManager.getInstance().getSkillBalances(skillId), pageId, true, isolysearch);
	
	int count = SkillBalanceManager.getInstance().getSkillBalanceSize(skillId, isolysearch);
	int limitInPage = 6;
	
	html = html.replace("%pageID%", String.valueOf(pageId));
	
	int totalpages = 1;
	int tmpcount = count;
	
	while (tmpcount - 6 > 0)
	{
		totalpages++;
		tmpcount -= 6;
	}
	
	html = html.replace("<?title?>", isolysearch ? "Olympiad" : "");
	html = html.replace("<?isoly?>", String.valueOf(isolysearch));
	html = html.replace("%totalPages%", String.valueOf(totalpages));
	html = html.replace("%info%", info);
	html = html.replace("%skillId%", String.valueOf(skillId));
	html = html.replace("%previousPage%", String.valueOf(pageId - 1 != 0 ? pageId - 1 : 1));
	html = html.replace("%nextPage%", String.valueOf(pageId * limitInPage >= count ? pageId : pageId + 1));
	
	separateAndSend(html, activeChar);
}

public void showAddHtml(Player activeChar, int pageId, int tRace, boolean isoly)
{
	String html = HtmCache.getInstance().getHtm("data/html/mods/balancer/skillbalance/" + (isoly ? "olyadd.htm" : "add.htm"));
	String tClasses = "";
	
	if (tRace < 6)
	{
		//aply to all classes
		tClasses = tClasses + "All_Classes" + ";";

		for (ClassId classId : ClassId.values())
		{
			if (classId.getRace() != null)
			{
				if (classId.level() == 3 && classId.getRace().ordinal() == tRace)
					tClasses = tClasses + classId.name() + ";";
			}
		}
	}
	else
		tClasses = "Monsters";//tRace == 6 ? "Monsters" : "All"
	
	html = html.replace("<?pageId?>", String.valueOf(pageId));
	html = html.replace("<?isoly?>", String.valueOf(isoly));
	html = html.replace("<?tClasses?>", tClasses);
	
	html = html.replace("<?trace0Checked?>", tRace == 0 ? "_checked" : "");
	html = html.replace("<?trace1Checked?>", tRace == 1 ? "_checked" : "");
	html = html.replace("<?trace2Checked?>", tRace == 2 ? "_checked" : "");
	html = html.replace("<?trace3Checked?>", tRace == 3 ? "_checked" : "");
	html = html.replace("<?trace4Checked?>", tRace == 4 ? "_checked" : "");
	html = html.replace("<?trace5Checked?>", tRace == 5 ? "_checked" : "");
	html = html.replace("<?trace6Checked?>", tRace == 6 ? "_checked" : "");
	html = html.replace("<?trace7Checked?>", tRace == 7 ? "_checked" : "");
	
	separateAndSend(html, activeChar);
}

private static String getSkillBalanceInfo(Collection<SkillBalanceHolder> collection, int pageId, boolean search, boolean isOly)
{
	if (collection == null)
		return "";
	
	String info = "";
	int count = 1;
	int limitInPage = 6;
	
	for (Iterator<SkillBalanceHolder> localIterator1 = collection.iterator(); localIterator1.hasNext();)
	{
		SkillBalanceHolder balance = localIterator1.next();
		int targetClassId = balance.getTarget();
		if (!ClassId.getClassById(targetClassId).name().equals("") || targetClassId <= -1)
		{
			Set<Map.Entry<SkillChangeType, Double>> localCollection = isOly ? balance.getOlyBalance().entrySet() : balance.getNormalBalance().entrySet();
			for (Map.Entry<SkillChangeType, Double> dt : localCollection)
			{
				if ((count > limitInPage * (pageId - 1)) && (count <= limitInPage * pageId))
				{
					double val = dt.getValue().doubleValue();
					double percents = Math.round(val * 100.0D) - 100L;
					double addedValue = Math.round((val + 0.1D) * 10.0D) / 10.0D;
					double removedValue = Math.round((val - 0.1D) * 10.0D) / 10.0D;
					
					String content = HtmCache.getInstance().getHtm("data/html/mods/balancer/skillbalance/info-template.htm");
					content = content.replace("<?pos?>", String.valueOf(count));
					content = content.replace("<?key?>", balance.getSkillId() + ";" + balance.getTarget());
					content = content.replace("<?skillId?>", String.valueOf(balance.getSkillId()));
					content = content.replace("<?skillName?>", SkillTable.getInstance().getInfo(balance.getSkillId(), SkillTable.getInstance().getMaxLevel(balance.getSkillId())).getName());
					content = content.replace("<?type?>", dt.getKey().name());
					content = content.replace("<?editedType?>", String.valueOf(dt.getKey().getId()));
					content = content.replace("<?removedValue?>", String.valueOf(removedValue));
					content = content.replace("<?search?>", String.valueOf(search));
					content = content.replace("<?isoly?>", String.valueOf(isOly));
					content = content.replace("<?addedValue?>", String.valueOf(addedValue));
					content = content.replace("<?pageId?>", String.valueOf(pageId));
					content = content.replace("<?value?>", String.valueOf(val));
					content = content.replace("<?targetClassName?>", targetClassId <= -1 ? "ALL" : targetClassId == -1 ? "Monster" : ClassId.getClassById(targetClassId).name());
					content = content.replace("<?percents?>", percents > 0.0D ? "+" : "");
					content = content.replace("<?percentValue?>", String.valueOf(percents).substring(0, String.valueOf(percents).indexOf(".")));
					content = content.replace("<?targetId?>", String.valueOf(targetClassId));
					content = content.replace("<?skillIcon?>", balance.getSkillIcon());
					
					info = info + content;
				}
				
				count++;
			}
		}
	}
	
	return info;
}
}