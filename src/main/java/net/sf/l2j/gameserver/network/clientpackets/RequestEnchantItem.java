package net.sf.l2j.gameserver.network.clientpackets;

import java.util.Collection;

import Custom.CustomConfig;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.xml.ArmorSetData;
import net.sf.l2j.gameserver.enums.items.WeaponType;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Gatekeeper;
import net.sf.l2j.gameserver.model.actor.instance.WarehouseKeeper;
import net.sf.l2j.gameserver.model.item.ArmorSet;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Armor;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.model.itemcontainer.Inventory;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.EnchantResult;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

import Customs.data.EnchantTable;
import Customs.data.L2EnchantScroll;

public final class RequestEnchantItem extends L2GameClientPacket
{
	private int _objectId;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null || _objectId == 0)
			return;
		
		if (!player.isOnline() || getClient().isDetached())
		{
			player.setActiveEnchantItem(null);
			return;
		}

		//Custom  , cant enchant near wh,gk,PartyTeleporter
	    Collection<Creature> knowns  = player.getKnownTypeRadius(150);
	                       for (WorldObject wh : knowns)
	                       {
	                           if (wh != null && wh instanceof WarehouseKeeper)
	                           {
	                        	   player.sendMessage("You cannot enchant near warehouse.");
	                               return;
	                           }
	                           if(wh != null && wh instanceof Gatekeeper){
	                        	   player.sendMessage("You cannot enchant near Gk.");
	                               return;
	                           }
	                     /*      if(wh != null && wh instanceof PartyTeleporter){
	                               player.sendMessage("You cannot enchant near Party Teleporter.");
	                               return;
	                           }*/
	                       }
	   
	              if(player.isTeleporting())
	              {
	                       player.setActiveEnchantItem(null);
	                       player.sendMessage("Can't enchant while You Teleporting");
	                       return;
	              }
	               if(player.isDead())
	               	            {
	               	            player.setActiveEnchantItem(null);
	               	            player.sendMessage("Can't enchant while You Are Dead");
	               	            return;
	               	            }
	               if(player.isParalyzed())
	               	            {
	               	            player.setActiveEnchantItem(null);
	               	            player.sendMessage("Can't enchant while You Are In Para");
	               	            return;
	               	            }
	               if(player.isCastingNow())
	               	           {
	               	            player.setActiveEnchantItem(null);
	               	            player.sendMessage("Can't enchant while Casting");
	               	            return;
	               	            }
	               if(player.isMoving())
	               	        {
	               	        player.setActiveEnchantItem(null);
	               	        player.sendMessage("Can't enchant while moving");
	               	       return;
	               	        }
	               if(player.isProcessingTransaction())
	               	        {
	               	        player.setActiveEnchantItem(null);
	               	        player.sendMessage("Can't enchant while trading");
	               	        return;
	               	        }
	               if(player.isFakeDeath())
	               	        {
	               	        player.setActiveEnchantItem(null);
	               	        player.sendMessage("Can't enchant while fake death");
	               	        return;
	               	        }
	               if(player.isInJail())
	               	       {
	               	        player.setActiveEnchantItem(null);
	               	       player.sendMessage("Can't enchant while in jail");
	               	        return;
	               	        }
	               if(player.isFlying())
	               	        {
	               	        player.setActiveEnchantItem(null);
	               	       player.sendMessage("Can't enchant while flying");
	               	        return;
	               	        }
	               if(player.isSitting()) 
	               	        { 
	               	        player.setActiveEnchantItem(null);
	               	        player.sendMessage("Can't enchant while sitting"); 
	               	        return; 
	               	       } 
		              // player trading
	               if (player.getActiveTradeList() != null)
	               {
	            	   player.cancelActiveTrade();
	            	   player.sendPacket(SystemMessageId.TRADE_ATTEMPT_FAILED);
	            	   player.setActiveEnchantItem(null);
	            	   player.sendPacket(EnchantResult.CANCELLED);
	                   return;
	              }
	               
	                       
		if (player.isProcessingTransaction() || player.isInStoreMode())
		{
			player.sendPacket(SystemMessageId.CANNOT_ENCHANT_WHILE_STORE);
			player.setActiveEnchantItem(null);
			player.sendPacket(EnchantResult.CANCELLED);
			return;
		}
		
		final ItemInstance item = player.getInventory().getItemByObjectId(_objectId);
		ItemInstance scroll = player.getActiveEnchantItem();
		
		if (item == null || scroll == null)
		{
			player.setActiveEnchantItem(null);
			player.sendPacket(SystemMessageId.ENCHANT_SCROLL_CANCELLED);
			player.sendPacket(EnchantResult.CANCELLED);
			return;
		}
		
		  // get scroll enchant data
		  L2EnchantScroll enchant = EnchantTable.getInstance().getEnchantScroll(scroll);
		  if (enchant == null)
			return;
		
		// first validation check
		if (!isEnchantable(item) || !enchant.isValid(item) || item.getOwnerId() != player.getObjectId())		
		{
			player.sendPacket(SystemMessageId.INAPPROPRIATE_ENCHANT_CONDITION);
			player.setActiveEnchantItem(null);
			player.sendPacket(EnchantResult.CANCELLED);
			return;
		}
		
		// attempting to destroy scroll
		scroll = player.getInventory().destroyItem("Enchant", scroll.getObjectId(), 1, player, item);
		if (scroll == null)
		{
			player.sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
			player.setActiveEnchantItem(null);
			player.sendPacket(EnchantResult.CANCELLED);
			return;
		}
		
		if (player.getActiveTradeList() != null)
		{
			player.cancelActiveTrade();
			player.sendPacket(SystemMessageId.TRADE_ATTEMPT_FAILED);
			return;
		}
		
		synchronized (item)
		{
			// success
			
			int scrollChance = enchant.getChance(item);
			
			//custom VIP +5% chance to enchant iteme
			if(scrollChance <= 95 && player.isVip()) 
				scrollChance += 5;
			
			
			 if (Rnd.get(100) < scrollChance)
			{
				// send message
				SystemMessage sm;
				
				if (item.getEnchantLevel() == 0)
				{
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1_SUCCESSFULLY_ENCHANTED);
					player.sendPacket(sm);
				}
				else
				{
					sm = SystemMessage.getSystemMessage(SystemMessageId.S1_S2_SUCCESSFULLY_ENCHANTED);
				    sm.addNumber(item.getEnchantLevel());
				    player.sendPacket(sm);

				}
				sm.addItemName(item.getItemId());
				

				item.setEnchantLevel(item.getEnchantLevel() + 1);
				item.updateDatabase();
				

				// If item is equipped, verify the skill obtention (+4 duals, +6 armorset).
				if (item.isEquipped())
				{
					final Item it = item.getItem();
					
					// Add skill bestowed by +4 duals.
					if (it instanceof Weapon && item.getEnchantLevel() == 4)
					{
						final L2Skill enchant4Skill = ((Weapon) it).getEnchant4Skill();
						if (enchant4Skill != null)
						{
							player.addSkill(enchant4Skill, false);
							player.sendSkillList();
						}
					}
					// Add skill bestowed by +6 armorset.
					else if (it instanceof Armor && item.getEnchantLevel() == 6)
					{
						// Checks if player is wearing a chest item
						final ItemInstance chestItem = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST);
						if (chestItem != null)
						{
							final ArmorSet armorSet = ArmorSetData.getInstance().getSet(chestItem.getItemId());
							if (armorSet != null && armorSet.isEnchanted6(player)) // has all parts of set enchanted to 6 or more
							{
								final int skillId = armorSet.getEnchant6skillId();
								if (skillId > 0)
								{
									final L2Skill skill = SkillTable.getInstance().getInfo(skillId, 1);
									if (skill != null)
									{
										player.addSkill(skill, false);
										player.sendSkillList();
									}
								}
							}
						}
					}
				}
				player.sendPacket(EnchantResult.SUCCESS);
			}
			else
			{
				// Drop passive skills from items.
				if (item.isEquipped())
				{
					final Item it = item.getItem();
					
					// Remove skill bestowed by +4 duals.
					if (it instanceof Weapon && item.getEnchantLevel() >= 4)
					{
						final L2Skill enchant4Skill = ((Weapon) it).getEnchant4Skill();
						if (enchant4Skill != null)
						{
							player.removeSkill(enchant4Skill.getId(), false);
							player.sendSkillList();
						}
					}
					// Add skill bestowed by +6 armorset.
					else if (it instanceof Armor && item.getEnchantLevel() >= 6)
					{
						// Checks if player is wearing a chest item
						final ItemInstance chestItem = player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_CHEST);
						if (chestItem != null)
						{
							final ArmorSet armorSet = ArmorSetData.getInstance().getSet(chestItem.getItemId());
							if (armorSet != null && armorSet.isEnchanted6(player)) // has all parts of set enchanted to 6 or more
							{
								final int skillId = armorSet.getEnchant6skillId();
								if (skillId > 0)
								{
									player.removeSkill(skillId, false);
									player.sendSkillList();
								}
							}
						}
					}
				}
				
				 if (!enchant.canBreak())
				{
				    if (!enchant.canMaintain()) {
						item.setEnchantLevel(CustomConfig.SAFE_ENCHANT);
						item.updateDatabase();
						player.sendMessage("The Enchant failed, new value of the item became +" + CustomConfig.SAFE_ENCHANT + "!");
		            }
				    else{
				    		player.sendMessage("Failed in Crystal Enchant.The enchant value is the same.");
				    }
				    player.sendPacket(EnchantResult.UNSUCCESS);
				}
				else
				{
					 // destroy item
					ItemInstance destroyItem = player.getInventory().destroyItem("Enchant", item, player, null);
					if (destroyItem == null)
					{
						//Util.handleIllegalPlayerAction(player, "Unable to delete item on enchant failure from player " + player.getName() + ", possible cheater !", Config.DEFAULT_PUNISH);
						player.setActiveEnchantItem(null);
						player.sendPacket(EnchantResult.CANCELLED);
						return;
					}
					
					 // add crystals, if item crystalizable
				      int crystalType = item.getItem().getCrystalItemId();
			     	 ItemInstance crystals = null;                                  
                     if (crystalType != 0)
					{
                    	 // get crystals count
                    	 int crystalCount = item.getCrystalCount() - (item.getItem().getCrystalCount() + 1) / 2;
                    	 
                         if (crystalCount < 1)
                    	       crystalCount = 1;                                                                                   
                    	// add crystals to inventory
                        crystals = player.getInventory().addItem("Enchant", crystalType, crystalCount, player, destroyItem);
                    	player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S).addItemName(crystals.getItemId()).addItemNumber(crystalCount));
                    	                                         
					
					}
					
                     // update inventory
					InventoryUpdate iu = new InventoryUpdate();
					if (destroyItem.getCount() == 0)
						iu.addRemovedItem(destroyItem);
					else
						iu.addModifiedItem(destroyItem);
					
					player.sendPacket(iu);
					
		            // remove item
				      World.getInstance().removeObject(destroyItem);
			                                      
					if (item.getEnchantLevel() > 0)
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ENCHANTMENT_FAILED_S1_S2_EVAPORATED).addNumber(item.getEnchantLevel()).addItemName(item.getItemId()));
					else
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ENCHANTMENT_FAILED_S1_EVAPORATED).addItemName(item.getItemId()));
					
                   // send enchant result
					if (crystalType == 0)
						player.sendPacket(EnchantResult.UNK_RESULT_4);
					else
						player.sendPacket(EnchantResult.UNK_RESULT_1);
                  

                    
					StatusUpdate su = new StatusUpdate(player);
					su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
					player.sendPacket(su);
				}
			}
			
			 // send item list
             player.sendPacket(new ItemList(player, false));
             
         	// update appearance
			player.broadcastUserInfo();
			player.setActiveEnchantItem(null);
		}
			
	}
	
    private static final boolean isEnchantable(ItemInstance item)
    {
            if (item.isHeroItem() || item.isShadowItem() || item.isEtcItem() || item.getItem().getItemType() == WeaponType.FISHINGROD)
                    return false;
          
            // only equipped items or in inventory can be enchanted
            if (item.getLocation() != ItemInstance.ItemLocation.INVENTORY && item.getLocation() != ItemInstance.ItemLocation.PAPERDOLL)
                    return false;
           
            return true;
    } 
}