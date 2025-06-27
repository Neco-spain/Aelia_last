package Customs.mods.Achievements.Achievements;

import net.sf.l2j.gameserver.model.actor.Player;
import Customs.mods.Achievements.Achievements.base.Condition;

/**
 * @author Matim
 * @version 1.0
 */
public class Marry extends Condition
{
	public Marry(Object value)
	{
		super(value);
		setName("Married");
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

//		if (player.isMarried())
//			return true;

		return false;
	}
}