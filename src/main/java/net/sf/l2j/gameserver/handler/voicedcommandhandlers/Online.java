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


import Custom.CustomConfig;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;


public class Online implements IVoicedCommandHandler
{
    private static final String[] _voicedCommands = {"online","on"};
    
    @Override
    public boolean useVoicedCommand(String command, Player activeChar, String target)
    {
        if(!CustomConfig.ONLINE_ENABLED)
            return false;

        if (command.equalsIgnoreCase("online") || command.equalsIgnoreCase("on"))    
        {
            int fake = CustomConfig.FAKE_ONLINE;
            activeChar.sendMessage("There are " + (World.getInstance().getPlayers().size() * fake ) + " Player(s) Online.");
        }
        return true;
    }
    
    @Override
    public String[] getVoicedCommandList()
    {
        return _voicedCommands;
    }
}