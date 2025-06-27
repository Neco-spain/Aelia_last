package Customs.mods.Achievements.Achievements;

import net.sf.l2j.gameserver.data.manager.HeroManager;
import net.sf.l2j.gameserver.model.actor.Player;
import Customs.mods.Achievements.Achievements.base.Condition;


/**
 * @author Matim
 * @version 1.0
 */
public class Hero extends Condition
{
	public Hero(Object value)
	{
		super(value);
		setName("Hero");
	}

	@Override
	public String getStatus(Player player) {
		if (getValue() == null)
			return "null";

		if(player.isHero())
			return "True";

		return "null";
	}

	@Override
	public boolean meetConditionRequirements(Player player)
	{
		if (getValue() == null)
			return false;

		if (player.isHero())
			return true;

		return false;
	}
}