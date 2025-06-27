package net.sf.l2j.gameserver.handler;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.handler.itemhandlers.*;
import net.sf.l2j.gameserver.handler.itemhandlers.Books;
import net.sf.l2j.gameserver.handler.itemhandlers.Calculators;
import net.sf.l2j.gameserver.handler.itemhandlers.Elixirs;
import net.sf.l2j.gameserver.handler.itemhandlers.Harvesters;
import net.sf.l2j.gameserver.handler.itemhandlers.PetFoods;
import net.sf.l2j.gameserver.handler.itemhandlers.RollingDices;
import net.sf.l2j.gameserver.handler.itemhandlers.SevenSignsRecords;
import net.sf.l2j.gameserver.handler.itemhandlers.Custom.ClanItem;
import net.sf.l2j.gameserver.handler.itemhandlers.Custom.ClanItemFull;
import net.sf.l2j.gameserver.handler.itemhandlers.Custom.Habilitys;
import net.sf.l2j.gameserver.handler.itemhandlers.Custom.HeroItem;
import net.sf.l2j.gameserver.handler.itemhandlers.Custom.NobleCustomItem;
import net.sf.l2j.gameserver.handler.itemhandlers.Custom.RecItem;
import net.sf.l2j.gameserver.handler.itemhandlers.Custom.VipCoin;
import net.sf.l2j.gameserver.handler.voicedcommandhandlers.Visual;
import net.sf.l2j.gameserver.model.item.kind.EtcItem;

public class ItemHandler
{
	private final Map<Integer, IItemHandler> _entries = new HashMap<>();
	
	protected ItemHandler()
	{
		registerHandler(new BeastSoulShots());
		registerHandler(new BeastSpices());
		registerHandler(new BeastSpiritShots());
		registerHandler(new BlessedSpiritShots());
		registerHandler(new Books());
		registerHandler(new Calculators());
		registerHandler(new Elixirs());
		registerHandler(new EnchantScrolls());
		registerHandler(new FishShots());
		registerHandler(new Harvesters());
		registerHandler(new ItemSkills());
		registerHandler(new Keys());
		registerHandler(new Maps());
		registerHandler(new MercenaryTickets());
		registerHandler(new PaganKeys());
		registerHandler(new PetFoods());
		registerHandler(new Recipes());
		registerHandler(new RollingDices());
		registerHandler(new ScrollOfResurrection());
		registerHandler(new Seeds());
		registerHandler(new SevenSignsRecords());
		registerHandler(new SoulShots());
		registerHandler(new SpecialXMas());
		registerHandler(new SoulCrystals());
		registerHandler(new SpiritShots());
		registerHandler(new SummonItems());
		
		//customs
		registerHandler(new VipCoin());
		registerHandler(new HeroItem());
		registerHandler(new ClanItem());
		registerHandler(new ClanItemFull());
		registerHandler(new RecItem());
		registerHandler(new NobleCustomItem());
		registerHandler(new Habilitys());
		registerHandler(new Visual());

		
	}
	
	private void registerHandler(IItemHandler handler)
	{
		_entries.put(handler.getClass().getSimpleName().intern().hashCode(), handler);
	}
	
	public IItemHandler getHandler(EtcItem item)
	{
		if (item == null || item.getHandlerName() == null)
			return null;
		
		return _entries.get(item.getHandlerName().hashCode());
	}
	
	public int size()
	{
		return _entries.size();
	}
	
	public static ItemHandler getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ItemHandler INSTANCE = new ItemHandler();
	}
}