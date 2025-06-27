package net.sf.l2j.gameserver.model.holder.skillnode;

import net.sf.l2j.commons.util.StatsSet;

import net.sf.l2j.gameserver.model.holder.IntIntHolder;

/**
 * A datatype used by enchant skill types. It extends {@link IntIntHolder}.
 */
public class EnchantSkillNode extends IntIntHolder
{
	private final int _exp;
	private final int _sp;

	private final int[] _enchantRates = new int[5]; //custom old 5

	private IntIntHolder _item;

	public EnchantSkillNode(StatsSet set)
	{
		super(set.getInteger("id"), set.getInteger("lvl"));

		_exp = set.getInteger("exp");
		_sp = set.getInteger("sp");

		_enchantRates[0] = set.getInteger("rate76");
		_enchantRates[1] = set.getInteger("rate77");
		_enchantRates[2] = set.getInteger("rate78");

		//custom disable these 2 for change enchant rates
		_enchantRates[3] = set.getInteger("rate78");
		_enchantRates[4] = set.getInteger("rate78");

		if (set.containsKey("itemNeeded"))
			_item = set.getIntIntHolder("itemNeeded");
	}

	public int getExp()
	{
		return _exp;
	}

	public int getSp()
	{
		return _sp;
	}

	public int getEnchantRate(int level)
	{
		//custom , it was 76 now 78
		return _enchantRates[level - 76];
	}

	public IntIntHolder getItem()
	{
		return _item;
	}
}