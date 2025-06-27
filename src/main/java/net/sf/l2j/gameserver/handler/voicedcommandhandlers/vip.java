package net.sf.l2j.gameserver.handler.voicedcommandhandlers;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;


public class vip implements IVoicedCommandHandler{
	
	private static final String[] _voicedCommands ={
		"vip" , "color"
	};

	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target){
	
		if (command.equals("vip") && activeChar.isVip())// && Config.MENU_PANEL)
			showHtml(activeChar);

		else if (command.startsWith("color"))
		{
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
//			String colorId = null; //1Green 0x009900, 2Blue 0xff7f00, 3Purple 0xff00ff,4Yellow 0x00ffff,5Gold 0x0099ff
			try 
			{
				String type = null;
				String color = null;
				
				if (st.hasMoreTokens()) 
					type = st.nextToken();
				
				if (st.hasMoreTokens())
					color= st.nextToken();

					
//				System.out.println(type + " " + color );
				
				if(type != null && color != null) {
					switch (type)
					{
						case "name":
							nameColor(activeChar,color);
							//GreenColor(player);
							break;
						case "title":
							titleColor(activeChar,color);
							//BlueColor(player);
							break;
	
					}
				}
				
			}
			catch (Exception e)
			{
			}
		}

		
		return true;
		
	}
	
	public static void nameColor(Player player,String color)
	{
		if (player.isVip())
		{
			player.setVipNColor(color);
			player.getAppearance().setNameColor(Integer.decode("0x" + color.substring(4,6) + color.substring(2,4) + color.substring(0,2)));  // int , color
			player.broadcastUserInfo();
			player.sendMessage("Your name color has been changed!");
		}

	}
	
	public static void titleColor(Player player,String color)
	{
		if (player.isVip())
		{
			player.setVipTColor(color);
			player.getAppearance().setTitleColor(Integer.decode("0x" + color.substring(4,6) + color.substring(2,4) + color.substring(0,2)));  // int , color
			player.broadcastUserInfo();
			player.sendMessage("Your title color has been changed!");
		}

	}
	
	private static void showHtml(Player activeChar){
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		
		html.setFile("data/html/mods/vip/vip.htm");

		html.replace("%VIP%",  activeChar.isVip() ? getVipTime(activeChar) : "<br1>");
		html.replace("%switchColor%",  doSwitch(activeChar));

		activeChar.sendPacket(html);
		
	}
	
	public static String doSwitch(Player player) {
		

		
		
		return "br1";
	}
	
	@Override
	public String[] getVoicedCommandList(){
		return _voicedCommands;
	}
	
	
	   private static String getVipTime( Player activeChar) {
	        final long now = Calendar.getInstance().getTimeInMillis();
	        long endDay = activeChar.getMemos().getLong("vip");
	        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	        
	        long _daysleft;
	        if (now > endDay) 
	        	return "VIP System: Your VIP period is up.";

            final Date dt = new Date(endDay);
            _daysleft = (endDay - now) / 86400000L;
            if (_daysleft > 30L) {
                return "VIP System: Your period ends in " + df.format(dt) + ".";
            }
            else if (_daysleft > 0L) {
            	return "VIP System: Your period ends in " + (int)_daysleft + " days.";
            }
            else if (_daysleft < 1L) {
                final long hour = (endDay - now) / 3600000L;
                return "VIP System: Your period ends in " + (int)hour + " hours.";
            }
   
	        
	        return "";
	    }
	
}