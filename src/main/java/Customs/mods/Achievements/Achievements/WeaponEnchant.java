package Customs.mods.Achievements.Achievements;

import net.sf.l2j.gameserver.model.actor.Player;
import Customs.mods.Achievements.Achievements.base.Condition;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;

/**
 * @author Matim
 * @version 1.0
 */
public class WeaponEnchant extends Condition
{
	public WeaponEnchant(Object value)
	{
		super(value);
		setName("Weapon Enchant");
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

		ItemInstance weapon = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);

		if (weapon != null)
			if (weapon.getEnchantLevel() >= val)
				return true;

		return false;
	}
}