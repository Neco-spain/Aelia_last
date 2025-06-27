package Customs.mods;


import java.util.Map.Entry;

import net.sf.l2j.gameserver.model.actor.Player;

import Custom.CustomConfig;

public class PvpColor 
{
	public static void checkColorPlayer(Player activeChar)
	{
		// ignore gms
		/*if (activeChar.isGM())
		{
			return;
		}*/
		
		// init vars
		String colorPvp = "";
		String colorPk = "";
		
		// the amount of PvP character is checked to see what color you assign
		for (Entry<Integer, String> pvp : CustomConfig.PVP_COLOR_NAME.entrySet())
		{
			if (activeChar.getPvpKills() >= pvp.getKey())
			{
				colorPvp = pvp.getValue();
			}
		}
		// the amount of Pk character is checked to see what color you assign
		for (Entry<Integer, String> pk : CustomConfig.PK_COLOR_TITLE.entrySet())
		{
			if (activeChar.getPkKills() >= pk.getKey())
			{
				colorPk = pk.getValue();
			}
		}
		
		// set color name
		if (!colorPvp.equals("") && activeChar.getDonateColor().equals("") && !activeChar.isVip() ) //update color only if player dont have dona color and isnt vip
		{
			activeChar.setOriginalNColor(colorPvp);
			
			if(!activeChar.isInFunEvent())
				activeChar.getAppearance().setNameColor(Integer.decode("0x" + colorPvp.substring(4,6) + colorPvp.substring(2,4) + colorPvp.substring(0,2)));
			
			activeChar.broadcastUserInfo();
		}
		
		// set title name
		if (!colorPk.equals("") && !activeChar.isVip() )  //update color only if player dont have dona color and isnt vip
		{
			activeChar.setOriginalTColor(colorPk);
			
			if(!activeChar.isInFunEvent())
				activeChar.getAppearance().setTitleColor(Integer.decode("0x" + colorPk.substring(4,6) + colorPk.substring(2,4) + colorPk.substring(0,2)));
			
			activeChar.broadcastUserInfo();
		}

	}
}