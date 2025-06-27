package Customs.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.l2j.L2DatabaseFactory;


public class RankingClanManager
{
	private int _posId;

	private StringBuilder _playerList = new StringBuilder();

	public RankingClanManager()
	{
		loadFromDB();
	}

	public void loadFromDB()
	{
		Connection con = null;
		try
		{
			_posId = 0;
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT clan_name,clan_level,reputation_score FROM clan_data WHERE clan_level>0 order by reputation_score desc limit 10");

			ResultSet result = statement.executeQuery();

			while (result.next())
			{
			_posId = _posId + 1;

				addPlayerToList(_posId, 
					result.getString("clan_name"),
					result.getString("clan_level"),
					result.getString("reputation_score"));

			}

			result.close();
			statement.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				con.close();
			} catch (Exception e)
			{
				System.out.println(e);
			}
		}
	}

	public String loadClans()
	{
		return _playerList.toString();
	}

	private void addPlayerToList(int objId, String name, String level, String score)
	{
		_playerList.append("<table width=270>");
		_playerList.append("<tr>");
		_playerList.append("<td></td>");
		_playerList.append("<td FIXWIDTH=22>" + objId + ".</td>");
		_playerList.append("<td FIXWIDTH=48>" + name + "</td>");
		_playerList.append("<td FIXWIDTH=34>" + level + "</td>");
		_playerList.append("<td FIXWIDTH=34>" + score + "</td>");
		_playerList.append("</tr>");
		_playerList.append("</table>");
		_playerList.append("<img src=\"L2UI.Squaregray\" width=\"300\" height=\"1\">");
	}
}