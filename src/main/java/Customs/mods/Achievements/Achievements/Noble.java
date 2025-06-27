package Customs.mods.Achievements.Achievements;

import net.sf.l2j.gameserver.model.actor.Player;
import Customs.mods.Achievements.Achievements.base.Condition;

/**
 * @author Matim
 * @version 1.0
 */
public class Noble extends Condition
{
	public Noble(Object value)
	{
		super(value);
		setName("Noble");
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

		if (player.isNoble())
			return true;

		return false;
	}
}