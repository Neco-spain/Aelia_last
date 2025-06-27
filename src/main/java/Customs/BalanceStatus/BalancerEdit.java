package Customs.BalanceStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;

public class BalancerEdit
{
	public static void editStat(String stat, int classId, int value, boolean add)
	{
		switch (stat)
		{
			case "patk":
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement stm = con.prepareStatement("UPDATE balance SET patk=? WHERE class_id=?");
					PreparedStatement stm2 = con.prepareStatement("SELECT patk FROM balance WHERE class_id=" + classId);
					ResultSet rset = stm2.executeQuery();

					if (rset.next())
					{
						if (add)
						{
							stm.setInt(1, rset.getInt("patk") + value);
							BalanceLoad.PAtk[classId - 88] = BalanceLoad.PAtk[classId - 88] + value;
						}
						else
						{
							stm.setInt(1, rset.getInt("patk") - value);
							BalanceLoad.PAtk[classId - 88] = BalanceLoad.PAtk[classId - 88] - value;
						}
						stm.setInt(2, classId);
					}

					stm.execute();
					stm.close();
					stm2.close();
				}
				catch (Exception e)
				{
					System.err.println("Error while saving balance stats to database.");
					e.printStackTrace();
				}
				for (Player p : World.getInstance().getPlayers())
				{
					if (p.getClassId().getId() == classId)
					{
						p.sendPacket(new UserInfo(p));
					}
				}
				break;
			}
			case "matk":
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement stm = con.prepareStatement("UPDATE balance SET matk=? WHERE class_id=?");
					PreparedStatement stm2 = con.prepareStatement("SELECT matk FROM balance WHERE class_id=" + classId);
					ResultSet rset = stm2.executeQuery();

					if (rset.next())
					{
						if (add)
						{
							stm.setInt(1, rset.getInt("matk") + value);
							BalanceLoad.MAtk[classId - 88] = BalanceLoad.MAtk[classId - 88] + value;
						}
						else
						{
							stm.setInt(1, rset.getInt("matk") - value);
							BalanceLoad.MAtk[classId - 88] = BalanceLoad.MAtk[classId - 88] - value;
						}
						stm.setInt(2, classId);
					}

					stm.execute();
					stm.close();
					stm2.close();
				}
				catch (Exception e)
				{
					System.err.println("Error while saving balance stats to database.");
					e.printStackTrace();
				}
				for (Player p : World.getInstance().getPlayers())
				{
					if (p.getClassId().getId() == classId)
					{
						p.sendPacket(new UserInfo(p));
					}
				}
				break;
			}
			case "pdef":
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement stm = con.prepareStatement("UPDATE balance SET pdef=? WHERE class_id=?");
					PreparedStatement stm2 = con.prepareStatement("SELECT pdef FROM balance WHERE class_id=" + classId);
					ResultSet rset = stm2.executeQuery();

					if (rset.next())
					{
						if (add)
						{
							stm.setInt(1, rset.getInt("pdef") + value);
							BalanceLoad.PDef[classId - 88] = BalanceLoad.PDef[classId - 88] + value;
						}
						else
						{
							stm.setInt(1, rset.getInt("pdef") - value);
							BalanceLoad.PDef[classId - 88] = BalanceLoad.PDef[classId - 88] - value;
						}
						stm.setInt(2, classId);
					}

					stm.execute();
					stm.close();
					stm2.close();
				}
				catch (Exception e)
				{
					System.err.println("Error while saving balance stats to database.");
					e.printStackTrace();
				}
				for (Player p : World.getInstance().getPlayers())
				{
					if (p.getClassId().getId() == classId)
					{
						p.sendPacket(new UserInfo(p));
					}
				}
				break;
			}
			case "mdef":
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement stm = con.prepareStatement("UPDATE balance SET mdef=? WHERE class_id=?");
					PreparedStatement stm2 = con.prepareStatement("SELECT mdef FROM balance WHERE class_id=" + classId);
					ResultSet rset = stm2.executeQuery();

					if (rset.next())
					{
						if (add)
						{
							stm.setInt(1, rset.getInt("mdef") + value);
							BalanceLoad.MDef[classId - 88] = BalanceLoad.MDef[classId - 88] + value;
						}
						else
						{
							stm.setInt(1, rset.getInt("mdef") - value);
							BalanceLoad.MDef[classId - 88] = BalanceLoad.MDef[classId - 88] - value;
						}
						stm.setInt(2, classId);
					}

					stm.execute();
					stm.close();
					stm2.close();
				}
				catch (Exception e)
				{
					System.err.println("Error while saving balance stats to database.");
					e.printStackTrace();
				}
				for (Player p : World.getInstance().getPlayers())
				{
					if (p.getClassId().getId() == classId)
					{
						p.sendPacket(new UserInfo(p));
					}
				}
				break;
			}
			case "acc":
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement stm = con.prepareStatement("UPDATE balance SET acc=? WHERE class_id=?");
					PreparedStatement stm2 = con.prepareStatement("SELECT acc FROM balance WHERE class_id=" + classId);
					ResultSet rset = stm2.executeQuery();

					if (rset.next())
					{
						if (add)
						{
							stm.setInt(1, rset.getInt("acc") + value);
							BalanceLoad.Accuracy[classId - 88] = BalanceLoad.Accuracy[classId - 88] + value;
						}
						else
						{
							stm.setInt(1, rset.getInt("acc") - value);
							BalanceLoad.Accuracy[classId - 88] = BalanceLoad.Accuracy[classId - 88] - value;
						}
						stm.setInt(2, classId);
					}

					stm.execute();
					stm.close();
					stm2.close();
				}
				catch (Exception e)
				{
					System.err.println("Error while saving balance stats to database.");
					e.printStackTrace();
				}
				for (Player p : World.getInstance().getPlayers())
				{
					if (p.getClassId().getId() == classId)
					{
						p.sendPacket(new UserInfo(p));
					}
				}
				break;
			}
			case "ev":
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement stm = con.prepareStatement("UPDATE balance SET ev=? WHERE class_id=?");
					PreparedStatement stm2 = con.prepareStatement("SELECT ev FROM balance WHERE class_id=" + classId);
					ResultSet rset = stm2.executeQuery();

					if (rset.next())
					{
						if (add)
						{
							stm.setInt(1, rset.getInt("ev") + value);
							BalanceLoad.Evasion[classId - 88] = BalanceLoad.Evasion[classId - 88] + value;
						}
						else
						{
							stm.setInt(1, rset.getInt("ev") - value);
							BalanceLoad.Evasion[classId - 88] = BalanceLoad.Evasion[classId - 88] - value;
						}
						stm.setInt(2, classId);
					}

					stm.execute();
					stm.close();
					stm2.close();
				}
				catch (Exception e)
				{
					System.err.println("Error while saving balance stats to database.");
					e.printStackTrace();
				}
				for (Player p : World.getInstance().getPlayers())
				{
					if (p.getClassId().getId() == classId)
					{
						p.sendPacket(new UserInfo(p));
					}
				}
				break;
			}
			case "patksp":
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement stm = con.prepareStatement("UPDATE balance SET patksp=? WHERE class_id=?");
					PreparedStatement stm2 = con.prepareStatement("SELECT patksp FROM balance WHERE class_id=" + classId);
					ResultSet rset = stm2.executeQuery();

					if (rset.next())
					{
						if (add)
						{
							stm.setInt(1, rset.getInt("patksp") + value);
							BalanceLoad.PAtkSpd[classId - 88] = BalanceLoad.PAtkSpd[classId - 88] + value;
						}
						else
						{
							stm.setInt(1, rset.getInt("patksp") - value);
							BalanceLoad.PAtkSpd[classId - 88] = BalanceLoad.PAtkSpd[classId - 88] - value;
						}
						stm.setInt(2, classId);
					}

					stm.execute();
					stm.close();
					stm2.close();
				}
				catch (Exception e)
				{
					System.err.println("Error while saving balance stats to database.");
					e.printStackTrace();
				}
				for (Player p : World.getInstance().getPlayers())
				{
					if (p.getClassId().getId() == classId)
					{
						p.sendPacket(new UserInfo(p));
					}
				}
				break;
			}
			case "matksp":
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement stm = con.prepareStatement("UPDATE balance SET matksp=? WHERE class_id=?");
					PreparedStatement stm2 = con.prepareStatement("SELECT matksp FROM balance WHERE class_id=" + classId);
					ResultSet rset = stm2.executeQuery();

					if (rset.next())
					{
						if (add)
						{
							stm.setInt(1, rset.getInt("matksp") + value);
							BalanceLoad.MAtkSpd[classId - 88] = BalanceLoad.MAtkSpd[classId - 88] + value;
						}
						else
						{
							stm.setInt(1, rset.getInt("matksp") - value);
							BalanceLoad.MAtkSpd[classId - 88] = BalanceLoad.MAtkSpd[classId - 88] - value;
						}
						stm.setInt(2, classId);
					}

					stm.execute();
					stm.close();
					stm2.close();
				}
				catch (Exception e)
				{
					System.err.println("Error while saving balance stats to database.");
					e.printStackTrace();
				}
				for (Player p : World.getInstance().getPlayers())
				{
					if (p.getClassId().getId() == classId)
					{
						p.sendPacket(new UserInfo(p));
					}
				}
				break;
			}
			case "cp":
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement stm = con.prepareStatement("UPDATE balance SET cp=? WHERE class_id=?");
					PreparedStatement stm2 = con.prepareStatement("SELECT cp FROM balance WHERE class_id=" + classId);
					ResultSet rset = stm2.executeQuery();

					if (rset.next())
					{
						if (add)
						{
							stm.setInt(1, rset.getInt("cp") + value);
							BalanceLoad.CP[classId - 88] = BalanceLoad.CP[classId - 88] + value;
						}
						else
						{
							stm.setInt(1, rset.getInt("cp") - value);
							BalanceLoad.CP[classId - 88] = BalanceLoad.CP[classId - 88] - value;
						}
						stm.setInt(2, classId);
					}

					stm.execute();
					stm.close();
					stm2.close();
				}
				catch (Exception e)
				{
					System.err.println("Error while saving balance stats to database.");
					e.printStackTrace();
				}
				for (Player p : World.getInstance().getPlayers())
				{
					if (p.getClassId().getId() == classId)
					{
						p.sendPacket(new UserInfo(p));
					}
				}
				break;
			}
			case "hp":
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement stm = con.prepareStatement("UPDATE balance SET hp=? WHERE class_id=?");
					PreparedStatement stm2 = con.prepareStatement("SELECT hp FROM balance WHERE class_id=" + classId);
					ResultSet rset = stm2.executeQuery();

					if (rset.next())
					{
						if (add)
						{
							stm.setInt(1, rset.getInt("hp") + value);
							BalanceLoad.HP[classId - 88] = BalanceLoad.HP[classId - 88] + value;
						}
						else
						{
							stm.setInt(1, rset.getInt("hp") - value);
							BalanceLoad.HP[classId - 88] = BalanceLoad.HP[classId - 88] - value;
						}
						stm.setInt(2, classId);
					}

					stm.execute();
					stm.close();
					stm2.close();
				}
				catch (Exception e)
				{
					System.err.println("Error while saving balance stats to database.");
					e.printStackTrace();
				}
				for (Player p : World.getInstance().getPlayers())
				{
					if (p.getClassId().getId() == classId)
					{
						p.sendPacket(new UserInfo(p));
					}
				}
				break;
			}
			case "mp":
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement stm = con.prepareStatement("UPDATE balance SET mp=? WHERE class_id=?");
					PreparedStatement stm2 = con.prepareStatement("SELECT mp FROM balance WHERE class_id=" + classId);
					ResultSet rset = stm2.executeQuery();

					if (rset.next())
					{
						if (add)
						{
							stm.setInt(1, rset.getInt("mp") + value);
							BalanceLoad.MP[classId - 88] = BalanceLoad.MP[classId - 88] + value;
						}
						else
						{
							stm.setInt(1, rset.getInt("mp") - value);
							BalanceLoad.MP[classId - 88] = BalanceLoad.MP[classId - 88] - value;
						}
						stm.setInt(2, classId);
					}

					stm.execute();
					stm.close();
					stm2.close();
				}
				catch (Exception e)
				{
					System.err.println("Error while saving balance stats to database.");
					e.printStackTrace();
				}
				for (Player p : World.getInstance().getPlayers())
				{
					if (p.getClassId().getId() == classId)
					{
						p.sendPacket(new UserInfo(p));
					}
				}
				break;
			}
			case "walk":
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement stm = con.prepareStatement("UPDATE balance SET walk=? WHERE class_id=?");
					PreparedStatement stm2 = con.prepareStatement("SELECT walk FROM balance WHERE class_id=" + classId);
					ResultSet rset = stm2.executeQuery();

					if (rset.next())
					{
						if (add)
						{
							stm.setInt(1, rset.getInt("walk") + value);
							BalanceLoad.Speed[classId - 88] = BalanceLoad.Speed[classId - 88] + value;
						}
						else
						{
							stm.setInt(1, rset.getInt("walk") - value);
							BalanceLoad.Speed[classId - 88] = BalanceLoad.Speed[classId - 88] - value;
						}
						stm.setInt(2, classId);
					}

					stm.execute();
					stm.close();
					stm2.close();
				}
				catch (Exception e)
				{
					System.err.println("Error while saving balance stats to database.");
					e.printStackTrace();
				}
				for (Player p : World.getInstance().getPlayers())
				{
					if (p.getClassId().getId() == classId)
					{
						p.sendPacket(new UserInfo(p));
					}
				}
				break;
			}
		}
	}

	public void sendBalanceWindow(int classId, Player p)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile("./data/html/admin/balance/balance.htm");
		htm.replace("%classId%", classId + "");

		p.sendPacket(htm);
	}
}