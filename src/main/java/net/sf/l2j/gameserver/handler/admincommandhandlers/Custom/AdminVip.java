
package net.sf.l2j.gameserver.handler.admincommandhandlers.Custom;

import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

import Custom.CustomConfig;
import Customs.tasks.VipTaskManager;


/**
 * @author IcathiaLord
 */
public class AdminVip implements IAdminCommandHandler
{

	private static final String[] ADMIN_COMMANDS =
	{
		"admin_setvip",
		"admin_vipoff"	
	};

	protected static final Logger _log = Logger.getLogger(AdminCustom.class.getName());
	
	@Override
	public boolean useAdminCommand(String command, Player activeChar)
	{

		String player = "";
		Player target = null;
		int time = -1;

		if(command.startsWith("admin_setvipt")){
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
			if(st.hasMoreTokens()) {
				player = st.nextToken();
				target  = World.getInstance().getPlayer(player);
				if(st.hasMoreTokens()) {
					try
					{
						time = Integer.parseInt(st.nextToken());
					}
					catch (NumberFormatException  e)
					{
						activeChar.sendMessage("Invalid number format used: " + e);
						return false;
					}
				}
			}

			if (target != null && time != -1) 
				setPlayerVip(activeChar,target,time);
			else{
				activeChar.sendMessage("Usage: //setvipt <char_name> [days]");
				return false;
			}
			
		}
		
		else if(command.startsWith("admin_setvip")){
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
			if (activeChar.getTarget() != null && activeChar.getTarget() instanceof Player) {
				target = (Player) activeChar.getTarget();
				if(st.hasMoreTokens()) {
					try
					{
						time = Integer.parseInt(st.nextToken());
					}
					catch (NumberFormatException  e)
					{
						activeChar.sendMessage("Invalid number format used: " + e);
						return false;
					}
				}
			}
			
			if (target != null && time != -1) 
				setPlayerVip(activeChar,target,time);
			else {
				activeChar.sendMessage("Usage: Target a player first and then use //setvip <days>");
				return false;
			}
			
		}
		else if (command.startsWith("admin_vipoff"))
		{
			if (activeChar.getTarget() != null && activeChar.getTarget() instanceof Player) 
				target = (Player) activeChar.getTarget();
				
			if (target == null && player.equals(""))
			{
				activeChar.sendMessage("Usage: //removevip <char_name>");
				return false;
			}
			if (target != null)
			{
				if (target.isVip()) {
					VipTaskManager.getInstance().removeVipStatus(target);
					target.sendMessage("Your VIP Status has been removed.");
					activeChar.sendMessage(target.getName() + "'s VIP Status has been removed.");
				}
				else
					activeChar.sendMessage("Player " +target.getName() + " is not an VIP.");
			}
		}
		
		return true;
	}
	
	
	public static void setPlayerVip(Player gm,Player target , int time) {
		
    	long remainingTime = target.getMemos().getLong("vip",0);
    	if(remainingTime > 0) {
    		target.getMemos().set("vip", remainingTime + TimeUnit.DAYS.toMillis(time));
    		target.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Vip Manager", "Dear " + target.getName() + ", your Vip status has been extended by " + time + " day(s)."));
    		gm.sendMessage(target.getName() + " VIP status has beed extended by" + time + " day(s)");
        }
        else {
        	target.getMemos().set("vip", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(time));
        	target.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Vip Manager", "Dear " + target.getName() + ", your Vip status has been enabled for " + time+ " day(s)."));
        	gm.sendMessage(target.getName() + " VIP status has beed enabled by" + time + " day(s)");
        }
    	
        if (CustomConfig.ALLOW_VIP_NCOLOR && !target.isVip()) {
        	target.setNameColorVip(target.getAppearance().getNameColor());
        	
//        	activeChar.setOriginalNColor( String.valueOf(activeChar.getAppearance().getNameColor()) );
        	target.setVipNColor(CustomConfig.VIP_NCOLOR);
        	target.getAppearance().setNameColor(Integer.decode("0x" + CustomConfig.VIP_NCOLOR.substring(4,6) + CustomConfig.VIP_NCOLOR.substring(2,4) + CustomConfig.VIP_NCOLOR.substring(0,2)));
        }
        if (CustomConfig.ALLOW_VIP_TCOLOR && !target.isVip()) {
        	target.setVipTColor(CustomConfig.VIP_TCOLOR);
        	target.setTitleColorVip(target.getAppearance().getTitleColor());

        	target.getAppearance().setTitleColor(Integer.decode("0x" + CustomConfig.VIP_TCOLOR.substring(4,6) + CustomConfig.VIP_TCOLOR.substring(2,4) + CustomConfig.VIP_TCOLOR.substring(0,2))); 
        }
        target.setVip(true);
        
        target.broadcastUserInfo();   
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
