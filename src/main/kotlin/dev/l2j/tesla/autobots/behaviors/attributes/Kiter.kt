package dev.l2j.tesla.autobots.behaviors.attributes

import dev.l2j.tesla.autobots.behaviors.preferences.ArcherCombatPreferences
import dev.l2j.tesla.autobots.extensions.getCombatBehavior
import dev.l2j.tesla.autobots.utils.distance
import dev.l2j.tesla.autobots.extensions.getCombatBehavior
import kotlinx.coroutines.delay
import net.sf.l2j.gameserver.enums.IntentionType
import net.sf.l2j.gameserver.geoengine.GeoEngine
import net.sf.l2j.gameserver.model.actor.Creature
import net.sf.l2j.gameserver.model.actor.Player
import net.sf.l2j.gameserver.model.location.Location

internal interface Kiter {
    
    suspend fun kite(bot: Player){
        if(bot.target == null)
            return

        if (bot.getCombatBehavior().combatPreferences !is ArcherCombatPreferences) return
        
        val prefs = (bot.getCombatBehavior().combatPreferences as ArcherCombatPreferences)
        if(!prefs.isKiting) {
            return
        }

        val kiteRange = prefs.kiteRadius
        val distance = distance(bot, bot.target as Creature)
        if (distance >= kiteRange) return
        
        val posX: Int = bot.x + if (bot.target.x < bot.x) kiteRange else -kiteRange
        val posY: Int = bot.y + if (bot.target.y < bot.y) kiteRange else -kiteRange
        val posZ: Int = bot.z + 50

        if (GeoEngine.getInstance().canMoveToTarget(bot.x, bot.y, bot.z, posX, posY, posZ))
        {
            bot.ai.setIntention(
                IntentionType.MOVE_TO,
                Location(posX, posY, posZ)
            )
            delay(prefs.kitingDelay)
            return
        }
    }
}