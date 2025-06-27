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

import java.util.StringTokenizer;
import java.util.logging.Logger;

import Customs.Events.PartyFarm.PartyFarm;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.Instance.InstanceManager;




/**
 * @author Mega
 */
public class AdminCustom implements IAdminCommandHandler
{

	private static final String[] ADMIN_COMMANDS =
	{
		"admin_tour",
		"admin_ptfarm",
		"admin_faketown",
		"admin_getinstance",
		"admin_resetmyinstance"
		
			
	};

	protected static final Logger _log = Logger.getLogger(AdminCustom.class.getName());
	public static boolean _bestfarm_manual = false;
	public static boolean _arena_manual = false;
	
	@Override
	public boolean useAdminCommand(String command, Player activeChar)
	{
		
	 if(command.startsWith("admin_getinstance")){
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken(); // skip command
			
			if(!st.hasMoreTokens())
			{
				activeChar.sendMessage("Write the name.");
				return false;
			}
			
			String target_name = st.nextToken();
			Player player = World.getInstance().getPlayer(target_name);
			if(player == null)
			{
				activeChar.sendMessage("Player is offline");
				return false;
			}
			
			activeChar.setInstance(player.getInstance(), false);
			activeChar.sendMessage("You are with the same instance of player "+target_name);
		}
		else if (command.startsWith("admin_resetmyinstance"))
		{
			activeChar.setInstance(InstanceManager.getInstance().getInstance(0), false);
			activeChar.sendMessage("Your instance is now default");
		}

	 else if(command.startsWith("admin_ptfarm")){
		 if (PartyFarm.getInstance().isActivated())
		 {
			 _log.info("----------------------------------------------------------------------------");
			 _log.info("[Party Farm]: Event Finished.");
			 _log.info("----------------------------------------------------------------------------");
			 PartyFarm.getInstance().Finish_Event();
			 activeChar.sendMessage("SYS: Party Farm Disabled");
		 }
		 else
		 {
			 _log.info("----------------------------------------------------------------------------");
			 _log.info("[Party Farm]: Event Started.");
			 _log.info("----------------------------------------------------------------------------");
			 PartyFarm.getInstance().start();
			 _bestfarm_manual = true;
			 activeChar.sendMessage("SYS: Party Farm Enabled");
		 }
	 }
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
