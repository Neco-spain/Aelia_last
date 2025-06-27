package Customs.mods.Achievements.Achievements;

import Customs.mods.Achievements.Achievements.base.Condition;
import net.sf.l2j.gameserver.data.manager.HeroManager;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 * @author Matim
 * @version 1.0
 */
public class CompleteAchievements extends Condition
{
	public CompleteAchievements(Object value)
	{
		super(value);
		setName("Complete Achievements");
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

		if (player.getCompletedAchievements().size() >= val)
			return true;

		return false;
	}
}