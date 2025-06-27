package Customs.vote.API;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.tasks.VoteManagerTask;

public class VoteDao {

    public static final Logger LOGGER = Logger.getLogger(VoteDao.class.getName());

    @SuppressWarnings("resource")
    public static void addVotedRecord(VotedRecord votedRecord) {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            PreparedStatement ps = con.prepareStatement("INSERT INTO votedrecords (accountname, ipaddress, datetimevoted, votesitename) VALUES (?,?,?,?);");
            ps.setString(1, votedRecord.getAccountName());
            ps.setString(2, votedRecord.getIpAddress());
            ps.setLong(3, votedRecord.getDateTimeVoted());
            ps.setString(4, votedRecord.getVoteSiteName());

            ps.execute();
            ps.close();
        }
        catch (SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Couldn't add voted record", e);
        }
    }

    @SuppressWarnings("resource")
    public static boolean canVoteOnSite(Player player, VoteSite voteSite) {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            PreparedStatement ps = con.prepareStatement("select datetimevoted from votedrecords " +
                    "where (accountname = ? or ipaddress = ?) and votesitename = ? and datetimevoted + ? > ? " +
                    "order by datetimevoted " +
                    "desc limit 1");

            ps.setString(1, player.getAccountName());
            ps.setString(2, player.getIpAddress());
            ps.setString(3, voteSite.getName());
            ps.setLong(4, 43200000); // 12 hours 
            ps.setLong(5, System.currentTimeMillis());
            ResultSet rs = ps.executeQuery();

            if (!rs.next())
            {
                return true;
            }
            player.setLastVotedTimestamp(voteSite, rs.getLong("datetimevoted"));
            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Couldn't check if user can vote", e);
        }

        return false;
    }






    @SuppressWarnings("resource")
    public static void addVoteBuff(Player player, long dateTimevoted) {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            //if player hwid to vote site dont exist create it , and save to db . Else replace him
            if(!player.hwidVotesContain(player.gethwid()) ) {

                PreparedStatement ps = con.prepareStatement("INSERT INTO votebuff (accountname, ipaddress, hwid, votesdone, datetimevoted) VALUES (?,?,?,?,?);");
                ps.setString(1, player.getAccountName());
                ps.setString(2, player.getIpAddress());
                ps.setString(3, player.gethwid());
                ps.setInt(4, player.getVotesToSites());
                ps.setLong(5, dateTimevoted);
                ps.execute();
                ps.close();
            }
            else {
                PreparedStatement ps = con.prepareStatement("UPDATE votebuff SET votesdone = ? WHERE hwid = ?");

                ps.setInt(1, player.getVotesToSites());
                ps.setString(2, player.gethwid());

                ps.execute();
                ps.close();
            }

        }
        catch (SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Couldn't add voted record", e);
        }
    }

    @SuppressWarnings("resource")
    public static boolean cangetVoteBuff(Player player) {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            PreparedStatement ps = con.prepareStatement("select votesdone,datetimevoted from votebuff " +
                    "where hwid = ? and votesdone > 0 " +
                    "order by votesdone " +
                    "desc limit 1");

            ps.setString(1, player.gethwid());
            ResultSet rs = ps.executeQuery();

            if (!rs.next())
            {
                return false;
            }

            VoteManagerTask.getInstance().add(player);
            rs.close();
            ps.close();
        }
        catch (SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Couldn't get vote buff", e);
        }

        return true;
    }
    @SuppressWarnings("resource")
    public static boolean initVoteBuff(Player player) {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM votebuff WHERE hwid = ?");

            ps.setString(1, player.gethwid());
            ps.executeUpdate();

            ps.close();

        }
        catch (SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Couldn't init vote buff", e);
        }

        return true;
    }

    @SuppressWarnings("resource")
    public static boolean initVoteBuffSaves(Player player)  {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM character_skills_save WHERE char_obj_id = ? and skill_id = 5413");

            ps.setInt(1, player.getObjectId());
            ps.executeUpdate();

            ps.close();

        }
        catch (SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Couldn't delete char skill save", e);
        }

        return true;
    }


    @SuppressWarnings("resource")
    public static long getVoteBuffTime(Player player) {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
        {
            PreparedStatement ps = con.prepareStatement("select datetimevoted from votebuff " +
                    "where hwid = ? ");

            ps.setString(1, player.gethwid());
            ResultSet rs = ps.executeQuery();

            if (rs.next())
            {
                return rs.getLong("datetimevoted");
            }

            rs.close();
            ps.close();

        }
        catch (SQLException e)
        {
            LOGGER.log(Level.SEVERE, "Couldn't get vote buff Time", e);
        }

        return -1;
    }
}