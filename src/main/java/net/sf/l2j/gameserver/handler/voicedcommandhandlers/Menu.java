package net.sf.l2j.gameserver.handler.voicedcommandhandlers;




import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import Customs.mods.OlyAnnounce;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;


public class Menu implements IVoicedCommandHandler{
	
	private static final String[] _voicedCommands ={
		"menu",
		
		"setPartyRefuse",
		
		"setTradeRefuse",
		
		"setMessageRefuse",
	
		"setRuneEffects",
		
		"setautoPot",
		
		"setautoSS",

		"setxpnot"
		
	};
	
	private static final String ACTIVED = "<font color=00FF00>ON</font>";
	private static final String DESATIVED = "<font color=FF0000>OFF</font>";
	
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String target){
	
		if (command.equals("menu"))// && Config.MENU_PANEL)
			showHtml(activeChar);
		
		else if (command.equals("setPartyRefuse")){
			if (activeChar.isPartyInRefuse())
				activeChar.setIsPartyInRefuse(false);
			else
				activeChar.setIsPartyInRefuse(true);
			showHtml(activeChar);
		}
		
		else if (command.equals("setTradeRefuse")){
			if (activeChar.getTradeRefusal())
				activeChar.setTradeRefusal(false);
			else
				activeChar.setTradeRefusal(true);
			showHtml(activeChar);
		}
		
		else if (command.equals("setMessageRefuse")){
			if (activeChar.isInRefusalMode())
				activeChar.setInRefusalMode(false);
			else
				activeChar.setInRefusalMode(true);
		showHtml(activeChar);
		}
		
		else if (command.equals("setautoSS")){
			if (activeChar.getautoSS())
				activeChar.setautoSS(false);
			else
				activeChar.setautoSS(true);
		showHtml(activeChar);
		}
		
		
		else if (command.equals("setautoPot")){
			if (activeChar.getAutoPot())
				activeChar.setAutoPot(false);
			else
				activeChar.setAutoPot(true);
		showHtml(activeChar);
		}

		else if (command.equals("setxpnot")){

			if (activeChar.cantGainXP())
				activeChar.cantGainXP(false);
			else
				activeChar.cantGainXP(true);
			activeChar.sendMessage(" Xp effects.");

			showHtml(activeChar);
		}
		return true;
		
	}
	
	private static void showHtml(Player activeChar){
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		
		html.setFile("data/html/mods/menu/menu.htm");
		
		html.replace("%partyRefusal%", activeChar.isPartyInRefuse() ? ACTIVED : DESATIVED);
		
		html.replace("%tradeRefusal%", activeChar.getTradeRefusal() ? ACTIVED : DESATIVED);
		
		html.replace("%messageRefusal%", activeChar.isInRefusalMode() ? ACTIVED : DESATIVED);

		html.replace("%autoPot%", activeChar.getAutoPot() ? ACTIVED : DESATIVED);
		html.replace("%autoSS%", activeChar.getautoSS() ? ACTIVED : DESATIVED);

		html.replace("%cantGainXP%", activeChar.cantGainXP() ? ACTIVED : DESATIVED);
		html.replace("%time%", GameTimeTaskManager.getInstance().getGameTimeFormated());

		html.replace("%olympiad%", OlyAnnounce.olympiadEndString());


		activeChar.sendPacket(html);
		
	}
	
	@Override
	public String[] getVoicedCommandList(){
		return _voicedCommands;
	}
	

	
}