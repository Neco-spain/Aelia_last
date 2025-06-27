package Customs.mods.Achievements.Achievements;

import net.sf.l2j.gameserver.data.manager.HeroManager;
import net.sf.l2j.gameserver.model.actor.Player;
import Customs.mods.Achievements.Achievements.base.Condition;

/**
 * @author Matim
 * @version 1.0
 */
public class Pvp extends Condition
{
	int val = Integer.parseInt(getValue().toString());

	public Pvp(Object value)
	{
		super(value);
		setName("PvP Count");
	}

	@Override
	public String getStatus(Player player) {
		return "" + player.getPvpKills();
	}

	@Override
	public boolean meetConditionRequirements(Player player)
	{
		if (getValue() == null)
			return false;


		if (player.getPvpKills() >= val)
			return true;

		return false;
	}
}