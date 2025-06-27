package Custom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;

public class DonateGiverTaskManager
{
    private static Logger _log = Logger.getLogger(DonateGiverTaskManager.class.getName());

    public static DonateGiverTaskManager getInstance()
    {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder
    {
        protected static final DonateGiverTaskManager _instance = new DonateGiverTaskManager();
    }

    protected DonateGiverTaskManager()
    {
        ThreadPool.scheduleAtFixedRate(() -> start(), 5000, 5000);
        _log.info("DonateGiver: started.");
    }

    private static void start()
    {
        String charName = null;
        int id = 0;
        int count = 0;
        String playerName = "";
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
             PreparedStatement statement = con.prepareStatement("SELECT id, count, playername FROM donate_holder;"))
        {
            try (ResultSet rset = statement.executeQuery())
            {
                while (rset.next())
                {
                    id = rset.getInt("id");
                    count = rset.getInt("count");
                    playerName = rset.getString("playername");
                    if (id > 0 && count > 0 && playerName != "")
                    {
                        for (Player activeChar : World.getInstance().getPlayers())
                        {
                            if (activeChar == null)
                                continue;
                            if (activeChar.getName().toLowerCase().equals(playerName.toLowerCase()))
                            {
                                charName = activeChar.getName();
                                activeChar.getInventory().addItem("Donate", id, count, activeChar, null);
                                activeChar.getInventory().updateDatabase();
                                activeChar.sendPacket(new ItemList(activeChar, true));
                                activeChar.sendMessage("Received donation coins.");
                                RemoveDonation(charName);
                                activeChar.sendPacket(ActionFailed.STATIC_PACKET);
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                _log.warning("Donate rewarder fail: for character: " + charName + " " + count + " Donate Coins! " + e.getMessage());
            }
        }
        catch (Exception e)
        {
            _log.warning("Check donate items failed. " + e.getMessage());
        }
        return;
    }

    /**
     * @param playername
     */
    private static void RemoveDonation(String playername)
    {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection();
             PreparedStatement statement = con.prepareStatement("DELETE FROM donate_holder WHERE playername=?;"))
        {
            statement.setString(1, playername);
            statement.execute();
        }
        catch (SQLException e)
        {
            _log.warning("Failed to remove donation from database (to pire kai tha to ksanaparei) char: " + playername);
            _log.warning(e.getMessage());
        }
    }
}