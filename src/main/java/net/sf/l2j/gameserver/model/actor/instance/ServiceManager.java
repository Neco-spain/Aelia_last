package net.sf.l2j.gameserver.model.actor.instance;

import Custom.CustomConfig;
import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.xml.MultisellData;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.StringTokenizer;

/**
 * @author IcathiaLord
 *
 */
public class ServiceManager extends Folk
{

//	private int itemIdConsume = 9501;
//	private int itemCountConsume = 200;

	public ServiceManager(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	
	@Override
	public void showChatWindow(Player player, String command)
	{
		StringTokenizer st = new StringTokenizer(command," ");
		int val=0;

		try{
			val = Integer.parseInt(command.substring(5));
		}
		catch (IndexOutOfBoundsException ioobe)
		{
		}
		catch (NumberFormatException nfe)
		{
		}
		
		String filename = "data/html/mods/serviceManager/" + getNpcId() + "-" +val   + ".htm";
		
	
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", getObjectId());
		html.replace("%npcname%", getName());
		html.replace("%player",getName());
		html.replace("%playername%", player.getName());
		player.sendPacket(html);
	}
	
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (player == null)
			return;
		
		StringTokenizer st1 = new StringTokenizer(command, " ");
		String actualCommand = st1.nextToken(); // Get actual command
		
		 if (command.startsWith("clan"))
			{
				StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				try
				{
					String type = st.nextToken();
					switch (type)
					{
						case "ClanLevel":
							Clanlvl(player);
							break;
						case "ClanRep_20k":
							ClanRep(player);
							break;
						case "ClanSkills":
							ClanSkill(player);
							break;
					}
				}
				catch (Exception e)
				{
				}
			}
		 else if (command.startsWith("change_password"))
	        {
	            StringTokenizer st = new StringTokenizer(command);
	            st.nextToken();
	           
	            String currPass = null;
	            String newPass = "";
	            String repeatNewPass = "";
	           
	            try
	            {
	                if (st.hasMoreTokens())
	                {
						currPass = st.nextToken();
						newPass = st.nextToken();
						repeatNewPass = st.nextToken();
	                }
	            }
	            catch (Exception e)
	            {
	                player.sendMessage("Please fill all the blanks before requesting for a password change.");
	                return;
	            }
	           
	            if (!conditions(currPass , newPass, repeatNewPass, player))
	                return;
	           
	            changePassword(currPass,newPass, repeatNewPass, player);
	        }

		 
		else if (command.startsWith("Chat"))
			showChatWindow(player, command);
		else if (command.startsWith("multisell"))
		{
			MultisellData.getInstance().separateAndSend(command.substring(9).trim(), player, this, false);
		}
		else if (command.startsWith("exc_multisell"))
		{
			MultisellData.getInstance().separateAndSend(command.substring(13).trim(), player, this, true);
		}
		 
	}
	
	
	/* clan services */
	public static void ClanRep(Player player)
	{
		if (player.getClan() == null)
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER);
		else if (!player.isClanLeader())
			player.sendPacket(SystemMessageId.NOT_AUTHORIZED_TO_BESTOW_RIGHTS);
		else if (player.destroyItemByItemId("Consume", CustomConfig.SERVICE_ITEM, CustomConfig.SERVICE_CLAN_REP_ITEM_COUNT, player, true))
		{
			player.getClan().addReputationScore(CustomConfig.SERVICE_CLAN_REPS);
			player.getClan().broadcastClanStatus();
			player.sendMessage("Your clan reputation score has been increased.");
		}
		else
			player.sendMessage("You do not have enough Donate Coins.");
	}
	
	public static void Clanlvl(Player player)
	{
		if (player.getClan() == null)
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER);
		else if (!player.isClanLeader())
			player.sendPacket(SystemMessageId.NOT_AUTHORIZED_TO_BESTOW_RIGHTS);
		if (player.getClan().getLevel() == 8)
			player.sendMessage("Your clan is already level 8.");
		else if (player.destroyItemByItemId("Consume", CustomConfig.SERVICE_ITEM, CustomConfig.SERVICE_CLAN_ITEM_COUNT, player, true))
		{
			player.getClan().changeLevel(8);
			player.getClan().broadcastClanStatus();
			player.broadcastPacket(new MagicSkillUse(player, player, 5103, 1, 1000, 0));
		}
		else
			player.sendMessage("You do not have enough Donate Coins.");
	}
	public static void ClanSkill(Player player)
	{
		if (!player.isClanLeader())
			player.sendMessage("Only a clan leader can add clan skills.!");
		else if (player.destroyItemByItemId("Consume", CustomConfig.SERVICE_ITEM, CustomConfig.SERVICE_CLAN_SKILL_ITEM_COUNT, player, true))
		{
			for(int i=370;i<=390;i++)
				player.getClan().addNewSkill(SkillTable.getInstance().getInfo(i, 3));
			player.getClan().addNewSkill(SkillTable.getInstance().getInfo(391, 1));
		}
		else
			player.sendMessage("You do not have enough Donate Coins.");
	}
	
	
	
	
	
	/* Password changer */
    private static boolean conditions(String currPass,String newPass, String repeatNewPass, Player player)
    {
        if (newPass.length() < 3)
        {
            player.sendMessage("The new password is too short!");
            return false;
        }
        else if (newPass.length() > 22)
        {
            player.sendMessage("The new password is too long!");
            return false;
        }
        else if (!newPass.equals(repeatNewPass))
        {
            player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PASSWORD_ENTERED_INCORRECT2));
            return false;
        }
        else if ( !checkPass(currPass,newPass,player)) {
        	 return false;
        }
        return true;
    }
    private static boolean checkPass(String currPass , String newPass, Player activeChar)
    {
    	String password = null;
    	
        try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement("SELECT password FROM accounts WHERE login=?"))
        {
          ps.setString(1, activeChar.getAccountName());
            ResultSet rset = ps.executeQuery();
           
    		if (rset.next())
			{
				password = rset.getString("password");
			}
    		
    		rset.close();
    		ps.close();
    				
    		MessageDigest md = MessageDigest.getInstance("SHA");
    		byte[] raw =  MessageDigest.getInstance("SHA").digest(currPass.getBytes("UTF-8"));
			String currPassEncoded = Base64.getEncoder().encodeToString(raw);
    		
			
    		if(!currPassEncoded.equals(password)) {
    			activeChar.sendMessage("The current password you entered is incorrect. Please try again.");
				return false;
    		}
			
        }
        catch (Exception e)
        {
            System.out.println("There was an error while updating account:" + e);
        }
        
        return true;
        
    }
    private static void changePassword(String currPass , String newPass, String repeatNewPass, Player activeChar)
    {

        try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE accounts SET password=? WHERE login=?"))
		{
			byte[] newPassword = MessageDigest.getInstance("SHA").digest(newPass.getBytes("UTF-8"));
			
			ps.setString(1, Base64.getEncoder().encodeToString(newPassword));
			ps.setString(2, activeChar.getAccountName());
			ps.executeUpdate();
			
			activeChar.sendMessage("Congratulations! Your password has been changed. You will now be disconnected for security reasons. Please login again.");
			activeChar.destroyItemByItemId("Consume", CustomConfig.SERVICE_PASSWORD_ITEM, CustomConfig.SERVICE_PASSWORD_ITEM_COUNT, activeChar, true);
			ThreadPool.schedule(() -> activeChar.logout(false), 3000);
		}
		catch (Exception e)
		{
		  System.out.println("There was an error while updating account:" + e);
        }
        
    }
	

    
    
}
