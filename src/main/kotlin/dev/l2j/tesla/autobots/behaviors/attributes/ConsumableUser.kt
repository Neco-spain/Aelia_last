package dev.l2j.tesla.autobots.behaviors.attributes

import dev.l2j.tesla.autobots.Autobot
import dev.l2j.tesla.autobots.extensions.useItem
import net.sf.l2j.gameserver.data.ItemTable
import net.sf.l2j.gameserver.handler.itemhandlers.SoulShots
import net.sf.l2j.gameserver.model.actor.Player

internal interface ConsumableUser {
    val consumables: List<Consumable>

    fun handleConsumables(player: Player){
        if(consumables.isEmpty()) return

        consumables.forEach {
            if(!it.condition(player)) return@forEach

            val items = player.inventory.getItemsByItemId(it.consumableId)
            val item = ItemTable.getInstance().getTemplate(it.consumableId)

            if(items.isEmpty() && player is Autobot ) {
                if(item.isInfinity)
                    player.addItem("AubotoItem", it.consumableId, 1, player, false)
                else if(!item.isStackable)
                    player.addItem("AubotoItem", it.consumableId, 1, player, false)
                else
                    player.addItem("AubotoItem", it.consumableId, 500, player, false)
            }

            //custom Auto potion fixes
            if(!player.isAutoPot(it.consumableId))
                player.useItem(it.consumableId)

        }
    }
}
internal data class Consumable(val consumableId: Int, val condition: (Player) -> Boolean = { true })