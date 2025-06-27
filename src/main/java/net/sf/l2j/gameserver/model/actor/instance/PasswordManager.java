package net.sf.l2j.gameserver.model.actor.instance;
 
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.StringTokenizer;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

/**
 * @author SweeTs
 */
public class PasswordManager extends Npc
{
    public PasswordManager(int objectId, NpcTemplate template)
    {
        super(objectId, template);
    }
   
    @Override
    public void onBypassFeedback(Player player, String command)
    {
        if (command.startsWith("change_password"))
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
    }
   
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
   
    @Override
    public void showChatWindow(Player activeChar)
    {
        final NpcHtmlMessage html = new NpcHtmlMessage(0);
        final StringBuilder sb = new StringBuilder();
       
        sb.append("<html><title>Account Manager</title>");
        sb.append("<body><center>");
        sb.append("<br><td align=\"center\"><img src=\"L2Server.logo\" width=286 height=100></td>");
		
        sb.append("<br><br>");
        sb.append("<img src=\"L2UI.SquareGray\" width=300 height=1>");
        
        sb.append("Current password: <edit var=\"curr\" width=100 height=15><br>");
        sb.append("New password: <edit var=\"new\" width=100 height=15><br>");
        sb.append("Repeat: <edit var=\"repeatnew\" width=100 height=15><br>");
        sb.append("<br><br>");
        sb.append("<a action=\"bypass -h npc_%objectId%_change_password $curr $new $repeatnew\">Change password</a>");
        
        sb.append("<img src=\"L2UI.SquareGray\" width=300 height=1>");
        sb.append("</center></body></html>");
       
        html.setHtml(sb.toString());
        html.replace("%objectId%", getObjectId());
        activeChar.sendPacket(html);
    }
  
    
    private static boolean checkPass(String currPass , String newPass, Player activeChar)
    {
    	//if (!BCrypt.checkpw(password, account.getPassword()))
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
//			byte[] raw = currPass.getBytes("UTF-8");
    		byte[] raw =  MessageDigest.getInstance("SHA").digest(currPass.getBytes("UTF-8"));
//			raw = md.digest(raw);
			String currPassEncoded = Base64.getEncoder().encodeToString(raw);
    		
			System.out.println("cur: " + currPass + "pass: " + password + "en: " + currPassEncoded);
			
    		if(!currPassEncoded.equals(password)) {
    			activeChar.sendMessage("The current password you entered is incorrect. Please try again.");
				return false;
    		}
			/*Account account = AccountTable.getInstance().getAccount(activeChar.getAccountName());
			if (!BCrypt.checkpw(currPass, account.getPassword())) {
				activeChar.sendMessage("The password you entered is incorrect. Please try again.");
				
				return false;	
			}*/
			
        }
        catch (Exception e)
        {
            System.out.println("There was an error while updating account:" + e);
        }
        
        return true;
        
    }
    private static void changePassword(String currPass , String newPass, String repeatNewPass, Player activeChar)
    {
       /* try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE accounts SET password=? WHERE login=?"))
        {
//            byte[] newPassword = MessageDigest.getInstance("SHA").digest(newPass.getBytes("UTF-8"));
           
            ps.setString(1, BCrypt.hashpw(newPass, BCrypt.gensalt()));
            ps.setString(2, activeChar.getAccountName());
            ps.executeUpdate();
           
            activeChar.sendMessage("Congratulations! Your password has been changed. You will now be disconnected for security reasons. Please login again.");
            ThreadPool.schedule(() -> activeChar.logout(false), 3000);
        }
        catch (Exception e)
        {
            System.out.println("There was an error while updating account:" + e);
        }
        */
        
        
        try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement ps = con.prepareStatement("UPDATE accounts SET password=? WHERE login=?"))
		{
			byte[] newPassword = MessageDigest.getInstance("SHA").digest(newPass.getBytes("UTF-8"));
			
			ps.setString(1, Base64.getEncoder().encodeToString(newPassword));
			ps.setString(2, activeChar.getAccountName());
			ps.executeUpdate();
			
			activeChar.sendMessage("Congratulations! Your password has been changed. You will now be disconnected for security reasons. Please login again.");
			activeChar.destroyItemByItemId("Consume", 57,1 /*CustomConfig.DONATE_ITEM, CustomConfig.PASSWORD_ITEM_COUNT*/, activeChar, true);
			ThreadPool.schedule(() -> activeChar.logout(false), 3000);
		}
		catch (Exception e)
		{
		  System.out.println("There was an error while updating account:" + e);
        }
        
        
    }
}