package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.util.Mysql;

public class Repair implements IVoicedCommandHandler
{
	static final Logger _log = Logger.getLogger(Repair.class.getName());

	private static final String[] _voicedCommands =
	{
		"repair"
	};

	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target)
	{
		if (activeChar == null)
			return false;

		// Send activeChar HTML page
		if (command.startsWith("repair"))
		{
			String htmContent = HtmCache.getInstance().getHtm("data/html/mods/repair/repair.htm");
			NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
			npcHtmlMessage.setHtml(htmContent);
			npcHtmlMessage.replace("%acc_chars%", getCharList(activeChar));
			activeChar.sendPacket(npcHtmlMessage);
			return true;
		}
		// Command for enter repairFunction from html

		// _log.warning("Repair Attempt: Failed. ");
		return false;
	}

	public static String getCharList(Player activeChar)
	{
		String result = "";
		String repCharAcc = activeChar.getAccountName();
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT char_name FROM characters WHERE account_name=?");
			statement.setString(1, repCharAcc);
			ResultSet rset = statement.executeQuery();
			while (rset.next())
			{
				if (activeChar.getName().compareTo(rset.getString(1)) != 0)
					result += rset.getString(1) + ";";
			}
			// _log.warning("Repair Attempt: Output Result for searching characters on account:"+result);
			rset.close();
			statement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			// return result;
		}
		finally
		{
			Mysql.closeQuietly(con);
		}
		return result;
	}

	public static boolean checkAcc(Player activeChar, String repairChar)
	{
		boolean result = false;
		String repCharAcc = "";
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT account_name FROM characters WHERE char_name=?");
			statement.setString(1, repairChar);
			ResultSet rset = statement.executeQuery();
			if (rset.next())
			{
				repCharAcc = rset.getString(1);
			}
			rset.close();
			statement.close();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			Mysql.closeQuietly(con);
		}
		if (activeChar.getAccountName().compareTo(repCharAcc) == 0)
			result = true;

		return result;
	}

	public static boolean checkPunish(Player activeChar, String repairChar)
	{
		boolean result = false;
		int accessLevel = 0;
		int repCharJail = 0;
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();

			PreparedStatement statement = con.prepareStatement("SELECT accesslevel,punish_level FROM characters WHERE char_name=?");
			statement.setString(1, repairChar);
			ResultSet rset = statement.executeQuery();
			if (rset.next())
			{
				accessLevel = rset.getInt(1);
				repCharJail = rset.getInt(2);
			}
			rset.close();
			statement.close();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			Mysql.closeQuietly(con);
		}
		if (repCharJail == 1 || accessLevel < 0) // 0 norm, 1 chat ban, 2 jail, 3....
			result = true;
		return result;
	}

	public static boolean checkKarma(Player activeChar, String repairChar)
	{
		boolean result = false;
		int repCharKarma = 0;
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT karma FROM characters WHERE char_name=?");
			statement.setString(1, repairChar);
			ResultSet rset = statement.executeQuery();
			if (rset.next())
			{
				repCharKarma = rset.getInt(1);
			}
			rset.close();
			statement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			Mysql.closeQuietly(con);
		}
		if (repCharKarma > 0)
			result = true;
		return result;
	}

	public static boolean checkChar(Player activeChar, String repairChar)
	{
		boolean result = false;
		if (activeChar.getName().compareTo(repairChar) == 0)
			result = true;
		return result;
	}

	public static void repairBadCharacter(String charName)
	{
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();

			PreparedStatement statement;
			statement = con.prepareStatement("SELECT obj_Id FROM characters WHERE char_name=?");
			statement.setString(1, charName);
			ResultSet rset = statement.executeQuery();

			int objId = 0;
			if (rset.next())
			{
				objId = rset.getInt(1);
			}
			rset.close();
			statement.close();
			if (objId == 0)
			{
				Mysql.closeQuietly(con);
				return;
			}
			statement = con.prepareStatement("UPDATE characters SET x=17867, y=170259, z=-3503 WHERE obj_Id=?");
			statement.setInt(1, objId);
			statement.execute();
			statement.close();
			statement = con.prepareStatement("DELETE FROM character_shortcuts WHERE char_obj_id=?");
			statement.setInt(1, objId);
			statement.execute();
			statement.close();
			statement = con.prepareStatement("UPDATE items SET loc=\"INVENTORY\" WHERE owner_id=? AND loc=\"PAPERDOLL\"");
			statement.setInt(1, objId);
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			_log.warning("GameServer: could not repair character:" + e);
		}
		finally
		{
			Mysql.closeQuietly(con);
		}
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}

}



/*
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class Repair
  implements IVoicedCommandHandler
{
  static final Logger _log = Logger.getLogger(Repair.class.getName());
  private static final String[] _voicedCommands = { "repair", "startrepair" };
  
  @Override
public boolean useVoicedCommand(String command, Player activeChar, String target)
  {
    if (activeChar == null) {
      return false;
    }
    String repairChar = null;
    try
    {
      if ((target != null) && 
        (target.length() > 1))
      {
        String[] cmdParams = target.split(" ");
        repairChar = cmdParams[0];
      }
    }
    catch (Exception e)
    {
      repairChar = null;
    }
    if (command.startsWith("repair"))
    {
      String htmContent = HtmCache.getInstance().getHtm("data/html/mods/repair/repair.htm");
      NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
      npcHtmlMessage.setHtml(htmContent);
      npcHtmlMessage.replace("%acc_chars%", getCharList(activeChar));
      activeChar.sendPacket(npcHtmlMessage);
      return true;
    }
    if ((command.startsWith("startrepair")) && (repairChar != null))
    {
      if (checkAcc(activeChar, repairChar))
      {
        if (checkChar(activeChar, repairChar))
        {
          String htmContent = HtmCache.getInstance().getHtm("data/html/mods/repair/repair-self.htm");
          NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
          npcHtmlMessage.setHtml(htmContent);
          activeChar.sendPacket(npcHtmlMessage);
          return false;
        }
        if (checkJail(activeChar, repairChar))
        {
          String htmContent = HtmCache.getInstance().getHtm("data/html/mods/repair/repair-jail.htm");
          NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
          npcHtmlMessage.setHtml(htmContent);
          activeChar.sendPacket(npcHtmlMessage);
          return false;
        }
        repairBadCharacter(repairChar);
        String htmContent = HtmCache.getInstance().getHtm("data/html/mods/repair/repair-done.htm");
        NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
        npcHtmlMessage.setHtml(htmContent);
        activeChar.sendPacket(npcHtmlMessage);
        return true;
      }
      String htmContent = HtmCache.getInstance().getHtm("data/html/mods/repair/repair-error.htm");
      NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
      npcHtmlMessage.setHtml(htmContent);
      activeChar.sendPacket(npcHtmlMessage);
      return false;
    }
    return false;
  }
  
  public static String getCharList(Player activeChar)
  {
    String result = "";
    String repCharAcc = activeChar.getAccountName();
    Connection con = null;
    try
    {
      con = L2DatabaseFactory.getInstance().getConnection();
      PreparedStatement statement = con.prepareStatement("SELECT char_name FROM characters WHERE account_name=?");
      statement.setString(1, repCharAcc);
      ResultSet rset = statement.executeQuery();
      while (rset.next()) {
        if (activeChar.getName().compareTo(rset.getString(1)) != 0) {
          result = result + rset.getString(1) + ";";
        }
      }
      rset.close();
      statement.close();
      
      return result;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return result;
    }
    finally
    {
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public static boolean checkAcc(Player activeChar, String repairChar)
  {
    boolean result = false;
    String repCharAcc = "";
    Connection con = null;
    try
    {
      con = L2DatabaseFactory.getInstance().getConnection();
      PreparedStatement statement = con.prepareStatement("SELECT account_name FROM characters WHERE char_name=?");
      statement.setString(1, repairChar);
      ResultSet rset = statement.executeQuery();
      if (rset.next()) {
        repCharAcc = rset.getString(1);
      }
      rset.close();
      statement.close();
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      if (activeChar.getAccountName().compareTo(repCharAcc) != 0) {
        return result;
      }
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      result = true;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return result;
    }
    finally
    {
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    return result;
  }
  
  private static boolean checkJail(Player activeChar, String repairChar)
  {
    boolean result = false;
    int repCharJail = 0;
    Connection con = null;
    try
    {
      con = L2DatabaseFactory.getInstance().getConnection();
      PreparedStatement statement = con.prepareStatement("SELECT punish_level FROM characters WHERE char_name=?");
      statement.setString(1, repairChar);
      ResultSet rset = statement.executeQuery();
      if (rset.next()) {
        repCharJail = rset.getInt(1);
      }
      rset.close();
      statement.close();
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      if (repCharJail <= 1) {
        return result;
      }
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      result = true;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return result;
    }
    finally
    {
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    return result;
  }
  
  public static boolean checkChar(Player activeChar, String repairChar)
  {
    boolean result = false;
    if (activeChar.getName().compareTo(repairChar) == 0) {
      result = true;
    }
    return result;
  }
  
  private static void repairBadCharacter(String charName)
  {
    Connection con = null;
    try
    {
      con = L2DatabaseFactory.getInstance().getConnection();
      
      PreparedStatement statement = con.prepareStatement("SELECT charId FROM characters WHERE char_name=?");
      statement.setString(1, charName);
      ResultSet rset = statement.executeQuery();
      
      int objId = 0;
      if (rset.next()) {
        objId = rset.getInt(1);
      }
      rset.close();
      statement.close();
      if (objId == 0)
      {
        con.close();
        return;
      }
      statement = con.prepareStatement("UPDATE characters SET x=17867, y=170259, z=-3503 WHERE charId=?");
      statement.setInt(1, objId);
      statement.execute();
      statement.close();
      statement = con.prepareStatement("DELETE FROM character_shortcuts WHERE charId=?");
      statement.setInt(1, objId);
      statement.execute();
      statement.close();
      statement = con.prepareStatement("UPDATE items SET loc=\"WAREHOUSE\" WHERE owner_id=? AND loc=\"PAPERDOLL\"");
      statement.setInt(1, objId);
      statement.execute();
      statement.close();return;
    }
    catch (Exception e)
    {
      _log.warning("GameServer: could not repair character:" + e);
    }
    finally
    {
      try
      {
        if (con != null) {
          con.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  @Override
public String[] getVoicedCommandList()
  {
    return _voicedCommands;
  }
}
*/