// 
// Decompiled by Procyon v0.5.36
// 

package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.SpawnZoneType;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.taskmanager.PvpFlagTaskManager;

public class TournamentZone extends SpawnZoneType  
{
    public TournamentZone(final int id) {
        super(id);
    }

    @Override
    protected void onEnter(Creature character)
    {
        character.setInsideZone(ZoneId.PVP, true);
        character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);
        character.setInsideZone(ZoneId.NO_RESTART, true);

        if (character instanceof Player)
        {
            final Player player = ((Player) character);
            player.sendPacket(SystemMessageId.ENTERED_COMBAT_ZONE);
        }
    }

    @Override
    protected void onExit(Creature character)
    {
        character.setInsideZone(ZoneId.PVP, false);
        character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
        character.setInsideZone(ZoneId.NO_RESTART, false);

        if (character instanceof Player)
        {
            final Player player = ((Player) character);
            player.sendPacket(SystemMessageId.LEFT_COMBAT_ZONE);
        }
    }
    

}
