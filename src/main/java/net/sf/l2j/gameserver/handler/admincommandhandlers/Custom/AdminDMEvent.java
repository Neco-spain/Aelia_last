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
package net.sf.l2j.gameserver.handler.admincommandhandlers.Custom;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.Events.DM.DMConfig;
import Customs.Events.DM.DMEvent;
import Customs.Events.DM.DMEventTeleporter;
import Customs.Events.DM.DMManager;

public class AdminDMEvent implements IAdminCommandHandler
{
    private static final String[] ADMIN_COMMANDS =
    {
        "admin_dm_add",
        "admin_dm_remove",
        "admin_dm_advance"
    };
   
    @Override
	public boolean useAdminCommand(String command, Player activeChar)
    {
        if (command.startsWith("admin_dm_add"))
        {
            Object target = activeChar.getTarget();
           
            if (!(target instanceof Player))
            {
                activeChar.sendMessage("You should select a player!");
                return true;
            }
           
            add(activeChar, (Player) target);
        }
        else if (command.startsWith("admin_dm_remove"))
        {
            Object target = activeChar.getTarget();
           
            if (!(target instanceof Player))
            {
                activeChar.sendMessage("You should select a player!");
                return true;
            }
           
            remove(activeChar, (Player) target);
        }
        else if (command.startsWith( "admin_dm_advance" ))
        {
            DMManager.getInstance().skipDelay();
        }
       
        return true;
    }
   
    @Override
	public String[] getAdminCommandList()
    {
        return ADMIN_COMMANDS;
    }
   
    private static void add(Player activeChar, Player playerInstance)
    {
        if (DMEvent.isPlayerParticipant(playerInstance.getObjectId()))
        {
            activeChar.sendMessage("Player already participated in the event!");
            return;
        }
       
        if (!DMEvent.addParticipant(playerInstance))
        {
            activeChar.sendMessage("Player instance could not be added, it seems to be null!");
            return;
        }
       
        if (DMEvent.isStarted())
        {
        	new DMEventTeleporter(playerInstance, true, false);
        }
    }
   
    private static void remove(Player activeChar, Player playerInstance)
    {
        if (!DMEvent.removeParticipant(playerInstance))
        {
            activeChar.sendMessage("Player is not part of the event!");
            return;
        }
       
        new DMEventTeleporter(playerInstance, DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES, true, true);
    }
}