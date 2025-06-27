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

import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Player;
import Customs.mods.Achievements.Achievements.base.Condition;

/**
 *
 *
 * @author Avaj
 */
public class SkillEnchant extends Condition
{
	public SkillEnchant(Object value)
	{
		super(value);
		setName("Skill Enchant");
	}

	@Override
	public String getStatus(Player player) {
		return "null";
	}

	@Override
	public boolean meetConditionRequirements(Player player)
	{
		if (getValue() == null)
			return false;

		int val = Integer.parseInt(getValue().toString());

		for(L2Skill s : player.getSkills().values())
		{
			String lvl = String.valueOf(s.getLevel());
			if(lvl.length() > 2)
			{
				int sklvl = Integer.parseInt(lvl.substring(1));
				if(sklvl>=val)
					return true;
			}
		}

		return false;
	}
}