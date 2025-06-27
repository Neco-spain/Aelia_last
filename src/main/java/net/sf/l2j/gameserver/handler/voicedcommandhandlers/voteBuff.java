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



import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

import Customs.vote.API.VoteDao;

public class voteBuff implements IVoicedCommandHandler
{
    private static final String[] _voicedCommands = {"votebuff"};
    
    @Override
    public boolean useVoicedCommand(String command, Player player, String target)
    {
        if (command.equalsIgnoreCase("votebuff"))    
        {
        	if(VoteDao.cangetVoteBuff(player)) {
				MagicSkillUse mgc = new MagicSkillUse(player, player, 5413, 1, 1, 0);
				SkillTable.getInstance().getInfo(5413, 1).getEffects(player,player);
				player.broadcastPacket(mgc);
				player.sendMessage("You have been blessed with the effects of the Vote Buff!");	
        	}
        }
        return true;
    }
    
    @Override
    public String[] getVoicedCommandList()
    {
        return _voicedCommands;
    }
}