package Customs.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.L2DatabaseFactory;


public class RankingOnlineManager
{
	private int _posId;

	private StringBuilder _playerList = new StringBuilder();

	public RankingOnlineManager()
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
			PreparedStatement statement = con.prepareStatement("SELECT char_name, online, OnlineTime FROM characters WHERE accesslevel=0 ORDER BY OnlineTime DESC LIMIT 10");

			ResultSet result = statement.executeQuery();

			while (result.next())
			{
			_posId = _posId + 1;

				addPlayerToList(_posId, 
						result.getString("char_name"),
						result.getInt("online"),
						result.getInt("OnlineTime"));
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

	public String loadPlayerOnline()
	{
		return _playerList.toString();
	}

	private void addPlayerToList(int objId, String name, int online, int OnlineTime)
	{
		miliConvert(OnlineTime);
		
		_playerList.append("<table width=290>");
		_playerList.append("<tr>");
		_playerList.append("<td></td>");
		_playerList.append("<td FIXWIDTH=11>" + objId + ".</td>");
		_playerList.append("<td FIXWIDTH=34>" + name + "</td>");
//		_playerList.append("<td FIXWIDTH=34>" + getDay() + "<font color=b09979>D </font>" + getHours() + "<font color=b09979>H </font>"  + getMin() + "<font color=b09979>M </font>" + "</td>"); //OnlineTime   miliConvert(OnlineTime)
		
		int days = (int)TimeUnit.SECONDS.toDays(OnlineTime); 
		
		_playerList.append("<td FIXWIDTH=18>" + (TimeUnit.SECONDS.toDays(OnlineTime) )  + "<font color=b09979>D </font></td>");
		_playerList.append("<td FIXWIDTH=16>" + (TimeUnit.SECONDS.toHours(OnlineTime)   - (days *24) )   + "<font color=b09979>H </font></td>");
		_playerList.append("<td FIXWIDTH=16>" + (TimeUnit.SECONDS.toMinutes(OnlineTime) - (TimeUnit.SECONDS.toHours(OnlineTime) * 60))   + "<font color=b09979>M </font></td>");
		
		
		if (online == 1)
		_playerList.append("<td FIXWIDTH=23><font color=00FF00>Online</font></td>");
		else
		_playerList.append("<td FIXWIDTH=23><font color=FF0000>Offline</font></td>");
		_playerList.append("</tr>");
		_playerList.append("</table>");
		_playerList.append("<img src=\"L2UI.Squaregray\" width=\"300\" height=\"1\">");
	}
	
	
	/*
	 * 
	 * 	private void addPlayerToList(int objId, String name, int online, int OnlineTime)
	{
		miliConvert(OnlineTime);
		
		_playerList.append("<table width=270>");
		_playerList.append("<tr>");
		_playerList.append("<td></td>");
		_playerList.append("<td FIXWIDTH=22>" + objId + ".</td>");
		_playerList.append("<td FIXWIDTH=48>" + name + "</td>");
		_playerList.append("<td FIXWIDTH=34>" +OnlineTime + "</td>"); //OnlineTime   miliConvert(OnlineTime)
		if (online == 1)
		_playerList.append("<td FIXWIDTH=27><font color=00FF00>Online</font></td>");
		else
		_playerList.append("<td FIXWIDTH=27><font color=FF0000>Offline</font></td>");
		_playerList.append("</tr>");
		_playerList.append("</table>");
		_playerList.append("<img src=\"L2UI.Squaregray\" width=\"300\" height=\"1\">");
	}
	
	*/
	
	
	 public static String miliConvert(int OnlineTime)
	 { 
		  int milliToEnd;
		  milliToEnd = OnlineTime;
		  
		  double numSecs = milliToEnd / 1000 % 60; 
		  double countDown = (milliToEnd / 1000 - numSecs) / 60;
		  int numMins = (int) Math.floor(countDown % 60);
		  countDown = (countDown - numMins) / 60;
		  int numHours = (int) Math.floor(countDown % 24);
		  int numDays = (int) Math.floor((countDown - numHours) / 24);
	
		  
		  setDay(numDays);
		  setHours(numHours);
		  setMin(numMins);
		  
		  return numDays + "D " + numHours + "H " + numMins + "M" ;
	 }

	 
	 private static int hours = 0;
	 public static void setHours(int x) {
		 hours = x;
	 }
	 public int getHours() {
		 return hours;
	 }
	 
	 private static int min = 0;
	 public static void setMin(int x) {
		 min = x;
	 }
	 public int getMin() {
		 return min;
	 }
	 
	 private static int day = 0;
	 public static void setDay(int x) {
		 day = x;
	 }
	 public int getDay() {
		 return day;
	 }
	 
}