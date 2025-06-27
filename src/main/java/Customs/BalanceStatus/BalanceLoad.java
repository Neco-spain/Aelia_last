package Customs.BalanceStatus;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.l2j.L2DatabaseFactory;

/**
 * @author Anarchy
 */
public class BalanceLoad
{
	public static int[] Evasion = new int[31], Accuracy = new int[31], Speed = new int[31], PAtk = new int[31],
			MAtk = new int[31], PDef = new int[31], MDef = new int[31], HP = new int[31], CP = new int[31],
			MP = new int[31], MAtkSpd = new int[31], PAtkSpd = new int[31];

	
	public static void LoadEm()
	{
		int z;

		for (z = 0; z < 31; z++)
		{
			Accuracy[z] = loadAccuracy(z + 88);
			Evasion[z] = loadEvasion(88 + z);
			Speed[z] = loadSpeed(z + 88);
			MAtk[z] = loadMAtk(z + 88);
			PAtk[z] = loadPAtk(z + 88);
			PDef[z] = loadPDef(z + 88);
			MDef[z] = loadMDef(z + 88);
			HP[z] = loadHP(z + 88);
			CP[z] = loadCP(z + 88);
			MP[z] = loadMP(z + 88);
			MAtkSpd[z] = loadMAtkSpd(z + 88);
			PAtkSpd[z] = loadPAtkSpd(z + 88);
		}

	}

	public static int loadEvasion(int classId)
	{
		int i = 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stm = con.prepareStatement("SELECT ev FROM balance WHERE class_id=" + classId);
			ResultSet rset = stm.executeQuery();

			if (rset.next())
			{
				i = rset.getInt("ev");
			}

			stm.close();
		}
		catch (Exception e)
		{
			System.err.println("Error while loading balance stats from database.");
			e.printStackTrace();
		}
		return i;
	}

	public static int loadAccuracy(int classId)
	{
		int i = 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stm = con.prepareStatement("SELECT acc FROM balance WHERE class_id=" + classId);
			ResultSet rset = stm.executeQuery();

			if (rset.next())
			{
				i = rset.getInt("acc");
			}

			stm.close();
		}
		catch (Exception e)
		{
			System.err.println("Error while loading balance stats from database.");
			e.printStackTrace();
		}
		return i;
	}

	public static int loadSpeed(int classId)
	{
		int i = 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stm = con.prepareStatement("SELECT walk FROM balance WHERE class_id=" + classId);
			ResultSet rset = stm.executeQuery();

			if (rset.next())
			{
				i = rset.getInt("walk");
			}

			stm.close();
		}
		catch (Exception e)
		{
			System.err.println("Error while loading balance stats from database.");
			e.printStackTrace();
		}
		return i;
	}

	public static int loadPAtk(int classId)
	{
		int i = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stm = con.prepareStatement("SELECT patk FROM balance WHERE class_id=" + classId);
			ResultSet rset = stm.executeQuery();

			if (rset.next())
			{
				i = rset.getInt("patk");
			}

			stm.close();
		}
		catch (Exception e)
		{
			System.err.println("Error while loading balance stats from database.");
			e.printStackTrace();
		}
		return i;
	}

	public static int loadMAtk(int classId)
	{
		int i = 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stm = con.prepareStatement("SELECT matk FROM balance WHERE class_id=" + classId);
			ResultSet rset = stm.executeQuery();

			if (rset.next())
			{
				i = rset.getInt("matk");
			}

			stm.close();
		}
		catch (Exception e)
		{
			System.err.println("Error while loading balance stats from database.");
			e.printStackTrace();
		}
		return i;
	}

	public static int loadPDef(int classId)
	{
		int i = 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stm = con.prepareStatement("SELECT pdef FROM balance WHERE class_id=" + classId);
			ResultSet rset = stm.executeQuery();

			if (rset.next())
			{
				i = rset.getInt("pdef");
			}

			stm.close();
		}
		catch (Exception e)
		{
			System.err.println("Error while loading balance stats from database.");
			e.printStackTrace();
		}

		return i;
	}

	public static int loadMDef(int classId)
	{
		int i = 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stm = con.prepareStatement("SELECT mdef FROM balance WHERE class_id=" + classId);
			ResultSet rset = stm.executeQuery();

			if (rset.next())
			{
				i = rset.getInt("mdef");
			}

			stm.close();
		}
		catch (Exception e)
		{
			System.err.println("Error while loading balance stats from database.");
			e.printStackTrace();
		}

		return i;
	}

	public static int loadHP(int classId)
	{
		int i = 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stm = con.prepareStatement("SELECT hp FROM balance WHERE class_id=" + classId);
			ResultSet rset = stm.executeQuery();

			if (rset.next())
			{
				i = rset.getInt("hp");
			}

			stm.close();
		}
		catch (Exception e)
		{
			System.err.println("Error while loading balance stats from database.");
			e.printStackTrace();
		}

		return i;
	}

	public static int loadCP(int classId)
	{
		int i = 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stm = con.prepareStatement("SELECT cp FROM balance WHERE class_id=" + classId);
			ResultSet rset = stm.executeQuery();

			if (rset.next())
			{
				i = rset.getInt("cp");
			}

			stm.close();
		}
		catch (Exception e)
		{
			System.err.println("Error while loading balance stats from database.");
			e.printStackTrace();
		}

		return i;
	}

	public static int loadMP(int classId)
	{
		int i = 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stm = con.prepareStatement("SELECT mp FROM balance WHERE class_id=" + classId);
			ResultSet rset = stm.executeQuery();

			if (rset.next())
			{
				i = rset.getInt("mp");
			}

			stm.close();
		}
		catch (Exception e)
		{
			System.err.println("Error while loading balance stats from database.");
			e.printStackTrace();
		}

		return i;
	}

	public static int loadMAtkSpd(int classId)
	{
		int i = 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stm = con.prepareStatement("SELECT matksp FROM balance WHERE class_id=" + classId);
			ResultSet rset = stm.executeQuery();

			if (rset.next())
			{
				i = rset.getInt("matksp");
			}

			stm.close();
		}
		catch (Exception e)
		{
			System.err.println("Error while loading balance stats from database.");
			e.printStackTrace();
		}

		return i;
	}

	public static int loadPAtkSpd(int classId)
	{
		int i = 0;

		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement stm = con.prepareStatement("SELECT patksp FROM balance WHERE class_id=" + classId);
			ResultSet rset = stm.executeQuery();

			if (rset.next())
			{
				i = rset.getInt("patksp");
			}

			stm.close();
		}
		catch (Exception e)
		{
			System.err.println("Error while loading balance stats from database.");
			e.printStackTrace();
		}

		return i;
	}
}