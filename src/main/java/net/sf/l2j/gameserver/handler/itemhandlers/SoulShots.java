package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.items.ShotType;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ExAutoSoulShot;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

public class SoulShots implements IItemHandler
{
	//custom auto pot
	private static final int MANA_POT_CD = 2,
		HEALING_POT_CD = 11, // DO NOT PUT LESS THAN 10
		CP_POT_CD = 1;
	
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player))
			return;
		
		final Player player = (Player) playable;
		final ItemInstance weaponInst = player.getActiveWeaponInstance();
		final Weapon weaponItem = player.getActiveWeaponItem();
		
		//custom not use it if its dead
		if(player.isDead())
			return;
		
		
		final int itemId = item.getItemId();
		if (itemId == 728 || itemId == 1539 || itemId == 5592)
		{
			switch (itemId)
			{
				case 728: // mana potion
				{
					if (player.isAutoPot(728))
					{
						player.sendPacket(new ExAutoSoulShot(728, 0));
						player.sendMessage("Deactivated auto mana potions.");
						player.setAutoPot(728, null, false);
					}
					else
					{
						if (player.getInventory().getItemByItemId(728) != null)
						{
							if (player.getInventory().getItemByItemId(728).getCount() > 0)
							{
								player.sendPacket(new ExAutoSoulShot(728, 1));
								player.sendMessage("Activated auto mana potions.");
								player.setAutoPot(728, ThreadPool.scheduleAtFixedRate(new AutoPot(728, player), 1000, MANA_POT_CD*1000), true);
							}
							else
							{
								MagicSkillUse msu = new MagicSkillUse(player, player, 2279, 2, 0, 100);
								player.broadcastPacket(msu);
								
								ItemSkills is = new ItemSkills();
								is.useItem(player, player.getInventory().getItemByItemId(728), true);
							}
						}
					}
					
					break;
				}
				case 1539: // greater healing potion
				{
					if (player.isAutoPot(1539))
					{
						player.sendPacket(new ExAutoSoulShot(1539, 0));
						player.sendMessage("Deactivated auto healing potions.");
						player.setAutoPot(1539, null, false);
					}
					else
					{
						if (player.getInventory().getItemByItemId(1539) != null)
						{
							if (player.getInventory().getItemByItemId(1539).getCount() > 0)
							{
								player.sendPacket(new ExAutoSoulShot(1539, 1));
								player.sendMessage("Activated auto healing potions.");
								player.setAutoPot(1539, ThreadPool.scheduleAtFixedRate(new AutoPot(1539, player), 1000, HEALING_POT_CD*1000), true);
							}
							else
							{
								MagicSkillUse msu = new MagicSkillUse(player, player, 2037, 1, 0, 100);
								player.broadcastPacket(msu);

								ItemSkills is = new ItemSkills();
								is.useItem(player, player.getInventory().getItemByItemId(1539), true);
							}
						}
					}
					
					break;
				}
				case 5592: // greater cp potion
				{
					if (player.isAutoPot(5592))
					{
						player.sendPacket(new ExAutoSoulShot(5592, 0));
						player.sendMessage("Deactivated auto cp potions.");
						player.setAutoPot(5592, null, false);
					}
					else
					{
						if (player.getInventory().getItemByItemId(5592) != null)
						{
							if (player.getInventory().getItemByItemId(5592).getCount() > 0)
							{
								player.sendPacket(new ExAutoSoulShot(5592, 1));
								player.sendMessage("Activated auto cp potions.");
								player.setAutoPot(5592, ThreadPool.scheduleAtFixedRate(new AutoPot(5592, player), 1000, CP_POT_CD*1000), true);
							}
							else
							{
								MagicSkillUse msu = new MagicSkillUse(player, player, 2166, 2, 0, 100);
								player.broadcastPacket(msu);
								
								ItemSkills is = new ItemSkills();
								is.useItem(player, player.getInventory().getItemByItemId(5592), true);
							}
						}
					}
					
					break;
				}
			}
			
			return;
		}

		
		// Check if soulshot can be used
		if (weaponInst == null || weaponItem.getSoulShotCount() == 0)
		{
			if (!player.getAutoSoulShot().contains(item.getItemId()))
				player.sendPacket(SystemMessageId.CANNOT_USE_SOULSHOTS);
			
			return;
		}
		
		if (weaponItem.getCrystalType() != item.getItem().getCrystalType())
		{
			if (!player.getAutoSoulShot().contains(item.getItemId()))
				player.sendPacket(SystemMessageId.SOULSHOTS_GRADE_MISMATCH);
			
			return;
		}
		
		// Check if Soulshot are already active.
		if (player.isChargedShot(ShotType.SOULSHOT))
			return;
		
		// Consume Soulshots if player has enough of them.
		int ssCount = weaponItem.getSoulShotCount();
		if (weaponItem.getReducedSoulShot() > 0 && Rnd.get(100) < weaponItem.getReducedSoulShotChance())
			ssCount = weaponItem.getReducedSoulShot();
		
		//custom

		//if(!CustomConfig.DONT_DESTROY_SS ) {
			if (!item.getItem().isInfinity() && !player.destroyItemWithoutTrace("Consume", item.getObjectId(), ssCount, null, false))
			{
				if (!player.disableAutoShot(item.getItemId()))
					player.sendPacket(SystemMessageId.NOT_ENOUGH_SOULSHOTS);
				
				return;
			}
		//}

		
		final IntIntHolder[] skills = item.getItem().getSkills();
		
		weaponInst.setChargedShot(ShotType.SOULSHOT, true);
		player.sendPacket(SystemMessageId.ENABLED_SOULSHOT);
		player.broadcastPacketInRadius(new MagicSkillUse(player, player, skills[0].getId(), 1, 0, 0), 600);
	}


	
	private class AutoPot implements Runnable
	{
		private int id;
		private Player activeChar;
		
		public AutoPot(int id, Player activeChar)
		{
			this.id = id;
			this.activeChar = activeChar;
		}
		
		@Override
		public void run()
		{
			if (activeChar.getInventory().getItemByItemId(id) == null)
			{
				activeChar.sendPacket(new ExAutoSoulShot(id, 0));
				activeChar.setAutoPot(id, null, false);
				return;
			}
			//disable auto soulshot on olympiad
			if(activeChar.isInOlympiadMode() || activeChar.isOlympiadStart()){
				activeChar.sendPacket(new ExAutoSoulShot(id, 0));
				activeChar.setAutoPot(id, null, false);
				return;
			}

			if(!activeChar.isDead()) {
				switch (id) {
					case 728: {
						if (activeChar.getCurrentMp() < 0.70 * activeChar.getMaxMp()) {
							MagicSkillUse msu = new MagicSkillUse(activeChar, activeChar, 2279, 2, 0, 100);
							activeChar.broadcastPacket(msu);

							ItemSkills is = new ItemSkills();
							is.useItem(activeChar, activeChar.getInventory().getItemByItemId(728), true);
						}

						break;
					}
					case 1539: {
						if (activeChar.getCurrentHp() < 0.95 * activeChar.getMaxHp()) {
							MagicSkillUse msu = new MagicSkillUse(activeChar, activeChar, 2037, 1, 0, 100);
							activeChar.broadcastPacket(msu);

							ItemSkills is = new ItemSkills();
							is.useItem(activeChar, activeChar.getInventory().getItemByItemId(1539), true);
						}

						break;
					}
					case 5592: {
						if (activeChar.getCurrentCp() < 0.95 * activeChar.getMaxCp()) {
							MagicSkillUse msu = new MagicSkillUse(activeChar, activeChar, 2166, 2, 0, 100);
							activeChar.broadcastPacket(msu);

							ItemSkills is = new ItemSkills();
							is.useItem(activeChar, activeChar.getInventory().getItemByItemId(5592), true);
						}

						break;
					}
				}
			}

			if (activeChar.getInventory().getItemByItemId(id) == null)
			{
				activeChar.sendPacket(new ExAutoSoulShot(id, 0));
				activeChar.setAutoPot(id, null, false);
			}
		}
	}

	public static void handleTeleportPotion(Player activeChar, int itemId) {

		if (itemId == 728 || itemId == 1539 || itemId == 5592)
		{
			switch (itemId)
			{
				case 728: // mana potion
				{
					if (activeChar.getInventory().getItemByItemId(728) == null) {
						if (activeChar.isAutoPot(728))
						{
							activeChar.sendPacket(new ExAutoSoulShot(728, 0));
							activeChar.setAutoPot(728, null, false);
						}
					}
					break;
				}
				case 1539: // greater healing potion
				{
					if (activeChar.getInventory().getItemByItemId(1539) == null)
					{
						if (activeChar.isAutoPot(1539))
						{
							activeChar.sendPacket(new ExAutoSoulShot(1539, 0));
							activeChar.setAutoPot(1539, null, false);
						}
					}
					break;
				}
				case 5592: // greater cp potion
				{
					if (activeChar.getInventory().getItemByItemId(5592) == null)
					{
						if (activeChar.isAutoPot(5592))
						{
							activeChar.sendPacket(new ExAutoSoulShot(5592, 0));
							activeChar.setAutoPot(5592, null, false);
						}
					}
					break;
				}
			}
			return;
		}
	}

}