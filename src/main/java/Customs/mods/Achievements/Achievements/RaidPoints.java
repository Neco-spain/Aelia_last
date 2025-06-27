/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package Customs.mods.Achievements.Achievements;


import net.sf.l2j.gameserver.data.manager.RaidPointManager;
import net.sf.l2j.gameserver.model.actor.Player;
import Customs.mods.Achievements.Achievements.base.Condition;

/**
 *
 *
 * @author Avaj
 */
public class RaidPoints extends Condition
{
	public RaidPoints(Object value)
	{
		super(value);
		setName("Raid Points");
	}

	@Override
	public String getStatus(Player player) {
		return "" + RaidPointManager.getInstance().getPointsByOwnerId(player.getObjectId());
	}

	@Override
	public boolean meetConditionRequirements(Player player)
	{
		if (getValue() == null)
			return false;

		int val = Integer.parseInt(getValue().toString());

		if (RaidPointManager.getInstance().getPointsByOwnerId(player.getObjectId()) >= val)
			return true;

		return false;
	}
}