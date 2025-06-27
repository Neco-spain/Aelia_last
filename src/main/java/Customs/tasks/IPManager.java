package Customs.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import dev.l2j.tesla.autobots.Autobot;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.GameClient;

import Custom.CustomConfig;
import Custom.Util;

public class IPManager
{
    private static final Logger _log;
    
    public static final IPManager getInstance() {
        return SingletonHolder._instance;
    }
    
    public IPManager() {
        IPManager._log.log(Level.INFO, "IPManager - Loaded.");
    }
    
    private static boolean multiboxKickTask(final Player activeChar, final Integer numberBox, final Collection<Player> world) {
        final Map<String, List<Player>> ipMap = new HashMap<>();
        for (final Player player : world) {
            if (player.getClient() != null && !(player instanceof Autobot)) {
                if (player.getClient().isDetached()) {
                    continue;
                }
                final String ip = activeChar.getClient().getConnection().getInetAddress().getHostAddress();
                final String playerIp = player.getClient().getConnection().getInetAddress().getHostAddress();
                if (!ip.equals(playerIp)) {
                    continue;
                }
                if (ipMap.get(ip) == null) {
                    ipMap.put(ip, new ArrayList<Player>());
                }
                ipMap.get(ip).add(player);
                if (ipMap.get(ip).size() >= numberBox) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public boolean validBox(final Player activeChar, final Integer numberBox, final Collection<Player> world, final Boolean forcedLogOut) {
        if (multiboxKickTask(activeChar, numberBox, world)) {
            if (forcedLogOut) {
                final GameClient client = activeChar.getClient();
                IPManager._log.warning("Multibox Protection: " + client.getConnection().getInetAddress().getHostAddress() + " was trying to use over " + numberBox + " clients!");
                Util.handleIllegalPlayerAction(activeChar, "Multibox Protection: " + client.getConnection().getInetAddress().getHostAddress() + " was trying to use over " + numberBox + " clients!", CustomConfig.MULTIBOX_PROTECTION_PUNISH);
            }
            return true;
        }
        return false;
    }
    
    static {
        _log = Logger.getLogger(IPManager.class.getName());
    }
    
    private static class SingletonHolder
    {
        protected static final IPManager _instance;
        
        static {
            _instance = new IPManager();
        }
    }
}
