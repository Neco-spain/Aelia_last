/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.handler.voicedcommandhandlers;


import java.util.logging.Logger;

import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;



public class exit implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS = {"exit"};

	private static final Logger _log = Logger.getLogger(exit.class.getName());

	

	@Override
	public boolean useVoicedCommand(String command, Player activeChar,String target)
	{
		if (command.equalsIgnoreCase("exit"))
		{
		/*    if(activeChar.isInOlympiadMode())
			{
				activeChar.sendMessage("You cant use this command in Olympiad");
				return false;
			}*/
			 if (activeChar.isInJail())
			{
				activeChar.sendMessage("You cant use this command in Jail.");
				return false;
			}
		    else if (activeChar.isInObserverMode())
		    {
		    activeChar.sendMessage("You can't use this command while you are in observer mode");
		    return false;
		     }
             else if (activeChar.isDead())
		     {
		     activeChar.sendMessage("You can't use this command while you are dead.");
		     return false;
		     }
             else if (activeChar.isInDuel())
             {
             activeChar.sendMessage("You can't teleport while you are doing a duel.");
             return false;
             }
             else if (activeChar.isInCombat())
             {
             activeChar.sendMessage("You can't teleport while you are in combat.");
             return false;
              } 
		    
				if	(activeChar.isInsideZone(ZoneId.CHANGE_PVP_ZONE))
				{
				   activeChar.teleportTo(82725, 148596, -3473, 0);
				   // activeChar.teleToLocation(x, y, z, 0);
				  //  activeChar.sendMessage("You Exit PvP Zone");
				}
				else 
				{
					activeChar.sendMessage("You can use this command only in Pvp zone");
				}
			 
		}
		return false;
	}
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}