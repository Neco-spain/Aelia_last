package net.sf.l2j.gameserver.handler.itemhandlers.Custom;

import java.util.concurrent.TimeUnit;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;

import Customs.tasks.HeroTaskManager;

/**
 * @author Baggos
 */
public class HeroItem implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player))
			return;

		Player activeChar = (Player) playable;
		if (activeChar.isCustomHero() || activeChar.isHero())
		{
			activeChar.sendMessage("Your character has already Hero Status!");
			return;
		}
		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		AddHeroStatus(activeChar, activeChar,1 /*CustomConfig.HERO_DAYS*/);
		activeChar.broadcastPacket(new SocialAction(activeChar, 16));
		playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
	}

	public static void AddHeroStatus(Player target, Player player, int time)
	{
		target.broadcastPacket(new SocialAction(target, 16));
		target.setCustomHero(true);
		HeroTaskManager.getInstance().add(target);
		long remainingTime = target.getMemos().getLong("TimeOfHero", 0);
		if (remainingTime > 0)
		{
			target.getMemos().set("TimeOfHero", remainingTime + TimeUnit.DAYS.toMillis(time));
			target.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Hero Manager", "Dear " + player.getName() + ", your Hero status has been extended by " + time + " day(s)."));
		}
		else
		{
			target.getMemos().set("TimeOfHero", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(time));
			target.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Hero Manager", "Dear " + player.getName() + ", you got Hero Status for " + time + " day(s)."));
			target.broadcastUserInfo();
		}
	}

	public static void RemoveHeroStatus(Player target)
	{
		HeroTaskManager.getInstance().remove(target);
		target.getMemos().set("TimeOfHero", 0);
		target.setCustomHero(false);

		if(target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND) != null && target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND).isHeroItem())
			target.disarmWeapons();
		if( target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_FACE) != null && target.getInventory().getPaperdollItem(Inventory.PAPERDOLL_FACE).getItemId() == 6842) {
			ItemInstance[] unequipped = target.getInventory().unEquipItemInBodySlotAndRecord(Item.SLOT_FACE);

			// show the update in the inventory
			InventoryUpdate iu = new InventoryUpdate();
			for (ItemInstance itm : unequipped)
			{
				itm.unChargeAllShots();
				iu.addModifiedItem(itm);
			}
			target.sendPacket(iu);
		}

		target.broadcastUserInfo();
	}
}