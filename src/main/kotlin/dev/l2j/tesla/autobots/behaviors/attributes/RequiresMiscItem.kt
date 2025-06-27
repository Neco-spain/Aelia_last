package dev.l2j.tesla.autobots.behaviors.attributes

import dev.l2j.tesla.autobots.Autobot
import net.sf.l2j.gameserver.model.actor.Player
import net.sf.l2j.gameserver.model.item.instance.ItemInstance

internal interface RequiresMiscItem {

    val miscItems: List<MiscItem>

    fun handleMiscItems(fakePlayer: Player) {


        miscItems.forEach {
            if (fakePlayer.inventory.getItemByItemId(it.miscItemId(fakePlayer)) != null && fakePlayer is Autobot) {

                if (fakePlayer.inventory.getItemByItemId(it.miscItemId(fakePlayer)).item.isInfinity ){
                    if(fakePlayer.inventory.getItemByItemId(it.miscItemId(fakePlayer)).count < 1)
                        fakePlayer.inventory.addItem("", it.miscItemId(fakePlayer), 1, fakePlayer, null)
                }
                else{
                    if (!fakePlayer.inventory.getItemByItemId(it.miscItemId(fakePlayer)).isStackable && fakePlayer.inventory.getItemByItemId(it.miscItemId(fakePlayer)).count < 1)
                        fakePlayer.inventory.addItem("", it.miscItemId(fakePlayer), 1, fakePlayer, null)
                    else if ( fakePlayer.inventory.getItemByItemId(it.miscItemId(fakePlayer)).isStackable && fakePlayer.inventory.getItemByItemId(it.miscItemId(fakePlayer)).count <= it.minimumAmountBeforeGive )
                        fakePlayer.inventory.addItem("", it.miscItemId(fakePlayer), it.countToGive, fakePlayer, null)
                }
            } else {
                if(fakePlayer is Autobot){
                    fakePlayer.inventory.addItem("", it.miscItemId(fakePlayer), 1, fakePlayer, null) //it.countToGive
                }

                val consumable: ItemInstance? = fakePlayer.inventory.getItemByItemId(it.miscItemId(fakePlayer)) ?: return@forEach
                if (consumable!!.isEquipable) fakePlayer.inventory.equipItem(consumable)
            }
        }

    }
}


internal data class MiscItem(val miscItemId: (Player) -> Int, val countToGive: Int = 500, val minimumAmountBeforeGive: Int =  20)  //from 500 to 1 cause of automatic potions , 20