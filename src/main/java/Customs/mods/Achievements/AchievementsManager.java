package Customs.mods.Achievements;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import Customs.mods.Achievements.Achievements.*;
import Customs.mods.Achievements.Achievements.base.Achievement;
import Customs.mods.Achievements.Achievements.base.Condition;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.commons.data.xml.IXmlReader;
import net.sf.l2j.gameserver.model.Announcement;
import net.sf.l2j.gameserver.model.actor.Player;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Matim
 * @version 1.0
 */
public class AchievementsManager implements IXmlReader
{
	private Map<Integer, Achievement> _achievementList = new HashMap<>();

	private ArrayList<String> _binded = new ArrayList<>();

	private static Logger _log = Logger.getLogger(AchievementsManager.class.getName());

	public AchievementsManager()
	{
		load();
	}

	@Override
	public void load()
	{
		parseFile("./data/xml/achievements.xml");
		LOGGER.info("Loaded {} Achievements.", _achievementList.size());
	}
	@Override
	public void parseDocument(Document doc, Path path) {

		forEach(doc, "list", listNode -> forEach(listNode, "achievement", achievementNode ->
		{
			final NamedNodeMap attrs = achievementNode.getAttributes();

			final int id = parseInteger(attrs, "id");
			final String name = parseString(attrs, "name");
			final String description = parseString(attrs, "description");
			final String reward = parseString(attrs, "reward");

			ArrayList<Condition> conditions = conditionList(achievementNode.getAttributes());

			_achievementList.put(id, new Achievement(id, name, description, reward, conditions));
//			alterTable(id);
		}));
	}

	public void rewardForAchievement(int achievementID, Player player)
	{
		Achievement achievement = _achievementList.get(achievementID);

		for (int id: achievement.getRewardList().keySet())
		{
			int count = achievement.getRewardList().get(id).intValue();
			player.addItem(achievement.getName(), id, count, player, true);
		}
	}

	/**
	 * Alter table, catch exception if already exist.
	 * @param fieldID
	 */
	private static void alterTable(int fieldID)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			Statement statement = con.createStatement();
			statement.executeUpdate("ALTER TABLE achievements ADD a" + fieldID + " INT DEFAULT 0");
			statement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public ArrayList<Condition> conditionList(NamedNodeMap attributesList)
	{
		ArrayList<Condition> conditions = new ArrayList<>();

		for (int j = 0; j < attributesList.getLength(); j++)
		{
			addToConditionList(attributesList.item(j).getNodeName(), attributesList.item(j).getNodeValue(), conditions);
		}

		return conditions;
	}

	public Map<Integer, Achievement> getAchievementList()
	{
		return _achievementList;
	}

	public ArrayList<String> getBinded()
	{
		return _binded;
	}

	public boolean isBinded(int obj,int ach)
	{
		for(String binds : _binded)
		{
			String[] spl = binds.split("@");
			if(spl[0].equals(String.valueOf(obj)) && spl[1].equals(String.valueOf(ach)))
				return true;
		}
		return false;
	}
	public static AchievementsManager getInstance()
	{
		return SingletonHolder._instance;
	}

	private static class SingletonHolder
	{
		protected static final AchievementsManager _instance = new AchievementsManager();
	}

	private static void addToConditionList(String nodeName, Object value, ArrayList<Condition> conditions)
	{
		if (nodeName.equals("minPvPCount"))
			conditions.add(new Pvp(value));
		else if (nodeName.equals("minPkCount"))
			conditions.add(new Pk(value));
		else if (nodeName.equals("mustBeHero"))
			conditions.add(new Hero(value));
		else if (nodeName.equals("mustBeNoble"))
			conditions.add(new Noble(value));
		else if (nodeName.equals("minWeaponEnchant"))
			conditions.add(new WeaponEnchant(value));
		else if (nodeName.equals("mustBeMarried"))
			conditions.add(new Marry(value));
		else if (nodeName.equals("itemAmmount"))
			conditions.add(new ItemsCount(value));
		else if (nodeName.equals("lordOfCastle"))
			conditions.add(new Castle(value));      
		else if (nodeName.equals("CompleteAchievements"))
			conditions.add(new CompleteAchievements(value));
		else if (nodeName.equals("minSkillEnchant"))
			conditions.add(new SkillEnchant(value));
		else if (nodeName.equals("minOnlineTime"))
			conditions.add(new OnlineTime(value));
		else if (nodeName.equals("minHeroCount"))
			conditions.add(new HeroCount(value));
		else if (nodeName.equals("raidToKill"))
			conditions.add(new RaidKill(value));
		else if (nodeName.equals("raidToKill1"))
			conditions.add(new RaidKill(value));
		else if (nodeName.equals("raidToKill2"))
			conditions.add(new RaidKill(value));
		else if (nodeName.equals("minRaidPoints"))
			conditions.add(new RaidPoints(value));
	}

	public void loadUsed()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{                      
			PreparedStatement statement;
			ResultSet rs;
			String sql = "SELECT ";
			for (int i=1; i <= getAchievementList().size(); i++)
			{
				if(i!= getAchievementList().size())
					sql=sql+"a"+i+",";
				else
					sql=sql+"a"+i;

			}

			sql = sql + " from achievements";
			statement = con.prepareStatement(sql);

			rs = statement.executeQuery();  
			while(rs.next())
			{
				for (int i=1; i<=getAchievementList().size(); i++)
				{
					String ct = rs.getString(i);
					if(ct.length() > 1 &&  ct.startsWith("1"))
					{
						_binded.add(ct.substring(ct.indexOf("1")+1)+"@"+i);
					}
				}
			}
			statement.close();
			rs.close();
		}
		catch (SQLException e)
		{
			_log.warning("Achievement Save data via loadUsed error" );
			e.printStackTrace();
		}
	}
}