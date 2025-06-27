package Customs.mods;

import java.util.logging.Logger;

import dev.l2j.tesla.autobots.Autobot;
import net.sf.l2j.gameserver.model.actor.Player;

import Custom.CustomConfig;
public class antiFarm
{
	private static final Logger _log = Logger.getLogger(AnnounceTops.class.getName());
	
	public static boolean checkAntiFarm(Player player , Player targetPlayer){
		if(CustomConfig.ANTI_FARM_ENABLED){

			if(player instanceof Autobot || targetPlayer instanceof Autobot) return true;

			//Anti FARM Clan - Ally
			if(CustomConfig.ANTI_FARM_CLAN_ALLY_ENABLED && (player.getClanId() > 0 && targetPlayer.getClanId() > 0 && player.getClanId() == targetPlayer.getClanId()) || (player.getAllyId() > 0 && targetPlayer.getAllyId() > 0 && player.getAllyId() == targetPlayer.getAllyId()))
			{
				player.sendMessage("Farm is punishable with Ban! Gm informed.");
				_log.warning("PVP POINT FARM ATTEMPT, " + player.getName() + " and " + targetPlayer.getName() +". CLAN or ALLY.");
				return false;
			}

			//Anti FARM level player < 40
			if(CustomConfig.ANTI_FARM_LVL_DIFF_ENABLED && targetPlayer.getLevel() < CustomConfig.ANTI_FARM_MAX_LVL_DIFF)
			{
				player.sendMessage("Farm is punishable with Ban! Don't kill new players! Gm informed.");
				_log.warning("PVP POINT FARM ATTEMPT, " + player.getName() + " and " + targetPlayer.getName() +". LVL DIFF.");
				return false;
			}       

			//Anti FARM pdef < 300
			if(CustomConfig.ANTI_FARM_PDEF_DIFF_ENABLED && targetPlayer.getPDef(targetPlayer) < CustomConfig.ANTI_FARM_MAX_PDEF_DIFF)
			{
				player.sendMessage("Farm is punishable with Ban! Gm informed.");
				_log.warning("PVP POINT FARM ATTEMPT, " + player.getName() + " and " + targetPlayer.getName() +". MAX PDEF DIFF.");
				return false;
			}    

			//Anti FARM p atk < 300
			if(CustomConfig.ANTI_FARM_PATK_DIFF_ENABLED && targetPlayer.getPAtk(targetPlayer) < CustomConfig.ANTI_FARM_MAX_PATK_DIFF)
			{
				player.sendMessage("Farm is punishable with Ban! Gm informed.");
				_log.warning("PVP POINT FARM ATTEMPT, " + player.getName() + " and " + targetPlayer.getName() +". MAX PATK DIFF.");
				return false;
			}

			//Anti FARM Party   
			if(CustomConfig.ANTI_FARM_PARTY_ENABLED && player.getParty() != null 
					&& targetPlayer.getParty() != null 
					&& player.getParty().equals(targetPlayer.getParty()))
			{
				player.sendMessage("Farm is punishable with Ban! Gm informed.");   
				_log.warning("PVP POINT FARM ATTEMPT, " + player.getName() + " and " + targetPlayer.getName() +". SAME PARTY.");
				return false;
			}      
			
			//Anti FARM same Ip
			if(CustomConfig.ANTI_FARM_IP_ENABLED){
				
			   if(player.getClient() != null && targetPlayer.getClient() != null)
			   {
		         String ip1 = player.getClient().getConnection().getInetAddress().getHostAddress();
		         String ip2 = targetPlayer.getClient().getConnection().getInetAddress().getHostAddress();
		 
		         if (ip1.equals(ip2))
		         {
		        	 player.sendMessage("Farm is punishable with Ban! Gm informed.");
		         _log.warning("PVP POINT FARM ATTEMPT: " + player.getName() + " and " + targetPlayer.getName() +". SAME IP.");
		         return false;
		         }
			   }
			}
			
			if(CustomConfig.ANTI_FARM_HWID_ENABLED){
				if(player.getClient() != null && targetPlayer.getClient() != null){
					if(player.gethwid().equals(targetPlayer.gethwid())){
			        	 player.sendMessage("Farm is punishable with Ban! Gm informed.");
				         _log.warning("PVP POINT FARM ATTEMPT: " + player.getName() + " and " + targetPlayer.getName() +". SAME HWID.");
				         return false;
					}
				}
			}
			
			return true;
		}
		return true;
	}
	
	
    public static antiFarm getInstance()
    {
        return SingletonHolder._instance;
    }
    private static class SingletonHolder
    {
        protected static final antiFarm _instance = new antiFarm();
    }
    public boolean isOn(){
    	if(CustomConfig.ANTI_FARM_ENABLED)
    		return true;
    	return false;
    }
}
