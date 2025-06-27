package Customs.mods.zoneProtections;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PartyZoneManager
{
    private static final Logger _log = Logger.getLogger(PartyZoneManager.class.getName());

    public PartyZoneManager()
    {
        _log.log(Level.INFO, "PartyZoneManager - Loaded.");
    }

    private static boolean checkClanAreaKickTask(Player activeChar, Integer numberBox)
    {
        Map<String, List<Player>> zergMap = new HashMap<>();
        Clan clan = activeChar.getClan();

        if (clan != null)
        {
            for (Player player : clan.getOnlineMembers())
            {
                if (!player.isInsideZone(ZoneId.PARTY) || player.getClan() == null)
                    continue;
                String zerg1 = activeChar.getClan().getName();
                String zerg2 = player.getClan().getName();

                if (zerg1.equals(zerg2))
                {
                    if (zergMap.get(zerg1) == null)
                        zergMap.put(zerg1, new ArrayList<>());

                    zergMap.get(zerg1).add(player);

                    if (zergMap.get(zerg1).size() > numberBox)
                        return true;
                }
            }
        }
        return false;
    }

    private static boolean checkAllyAreaKickTask(Player activeChar, Integer numberBox, Collection<Player> world)
    {
        Map<String, List<Player>> zergMap = new HashMap<>();

        if (activeChar.getAllyId() != 0)
        {
            for (Player player : world)
            {
                if (!player.isInsideZone(ZoneId.PARTY) || player.getAllyId() == 0)
                    continue;
                String zerg1 = activeChar.getClan().getAllyName();
                String zerg2 = player.getClan().getAllyName();

                if (zerg1.equals(zerg2))
                {
                    if (zergMap.get(zerg1) == null)
                        zergMap.put(zerg1, new ArrayList<>());

                    zergMap.get(zerg1).add(player);

                    if (zergMap.get(zerg1).size() > numberBox)
                        return true;
                }
            }
        }
        return false;
    }

    public boolean checkClanArea(Player activeChar, Integer numberBox, Boolean forcedTeleport)
    {
        if (checkClanAreaKickTask(activeChar, numberBox))
        {
            if (forcedTeleport)
            {
                activeChar.sendMessage("Allowed only " + numberBox + " clan members on this area!");
                activeChar.sendPacket(new ExShowScreenMessage("Allowed only " + numberBox + " clans members on this area!", 6 * 1000));
                RandomTeleport(activeChar);
            }
            return true;
        }
        return false;
    }


    public boolean checkAllyArea(Player activeChar, Integer numberBox, Collection<Player> world, Boolean forcedTeleport)
    {
        if (checkAllyAreaKickTask(activeChar, numberBox, world))
        {
            if (forcedTeleport)
            {
                activeChar.sendMessage("Allowed only " + numberBox + " ally members on this area!");
                activeChar.sendPacket(new ExShowScreenMessage("Allowed only " + numberBox + " ally members on this area!", 6 * 1000));
                RandomTeleport(activeChar);
            }
            return true;
        }
        return false;
    }

    //Giran Coord's
    public void RandomTeleport(Player activeChar)
    {
        switch (Rnd.get(5))
        {
            case 0:
            {
                int x = 82533 + Rnd.get(100);
                int y = 149122 + Rnd.get(100);
                activeChar.teleportTo(x, y, -3474, 0);
                break;
            }
            case 1:
            {
                int x = 82571 + Rnd.get(100);
                int y = 148060 + Rnd.get(100);
                activeChar.teleportTo(x, y, -3467, 0);
                break;
            }
            case 2:
            {
                int x = 81376 + Rnd.get(100);
                int y = 148042 + Rnd.get(100);
                activeChar.teleportTo(x, y, -3474, 0);
                break;
            }
            case 3:
            {
                int x = 81359 + Rnd.get(100);
                int y = 149218 + Rnd.get(100);
                activeChar.teleportTo(x, y, -3474, 0);
                break;
            }
            case 4:
            {
                int x = 82862 + Rnd.get(100);
                int y = 148606 + Rnd.get(100);
                activeChar.teleportTo(x, y, -3474, 0);
                break;
            }
        }
    }

    private static class SingletonHolder
    {
        protected static final PartyZoneManager _instance = new PartyZoneManager();
    }

    public static final PartyZoneManager getInstance()
    {
        return SingletonHolder._instance;
    }
}