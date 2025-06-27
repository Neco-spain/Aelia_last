package Customs.mods.Achievements.Achievements;

import net.sf.l2j.gameserver.model.actor.Player;
import Customs.mods.Achievements.Achievements.base.Condition;


public class ItemsCount extends Condition
{
	public ItemsCount(Object value)
	{
		super(value);
		setName("Items Count");
	}

	@Override
	public String getStatus(Player player) {
		if (getValue() == null)
			return "null";

		String[] split = getValue().toString().split(",");
		int id = Integer.valueOf(split[0]);
		return (player.getInventory().getInventoryItemCount(id, 0) > 0) ? "" + player.getInventory().getInventoryItemCount(id, 0) : "null";

	}

	@Override
	public boolean meetConditionRequirements(Player player)
	{
		if (getValue() == null)
			return false;

		String[] split = getValue().toString().split(",");

		int id = Integer.valueOf(split[0]);
		int	amount = Integer.valueOf(split[1]);

		if (player.getInventory().getInventoryItemCount(id, 0) >= amount)
			return true;

		return false;
	}
}