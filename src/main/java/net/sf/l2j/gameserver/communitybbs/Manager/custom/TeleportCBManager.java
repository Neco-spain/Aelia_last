package net.sf.l2j.gameserver.communitybbs.Manager.custom;

import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import Custom.CustomConfig;
import Customs.Events.PartyFarm.PartyFarm;
import Customs.PvpZone.RandomZoneManager;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.communitybbs.CommunityBoard;
import net.sf.l2j.gameserver.communitybbs.Manager.BaseBBSManager;
import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.data.xml.TeleportLocationData;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Gatekeeper;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.location.TeleportLocation;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class TeleportCBManager extends BaseBBSManager {
    public void parseCmd(String command, Player player) {
        if (command.startsWith("_cbsGoto")) {
            try {
                StringTokenizer st = new StringTokenizer(command, " ");
                st.nextToken();
                teleport(player, Integer.parseInt(st.nextToken()));
            } catch (Exception e) {
                player.sendPacket(ActionFailed.STATIC_PACKET);
            }
        } else if (command.startsWith("_cbsTelePage")) {
            StringTokenizer st = new StringTokenizer(command, " ");
            st.nextToken();
            int val = Integer.parseInt(st.nextToken());
            showGK(player, val);
        }
        else if (command.startsWith("_cbspvpzone")){
            if (!isTeleportAllowed(player)) {
                sendHome(player);
                return;
            }

            if (RandomZoneManager.getInstance().getCurrentZone() != null)
                player.teleportTo(RandomZoneManager.getInstance().getCurrentZone().getLoc(), 20);
        }
    }

    private static void showGK(Player player, int val) {
        String content = HtmCache.getInstance().getHtm("data/html/CommunityBoard/custom/teleport-" + val + ".htm");
        content = content.replaceAll("%name%", player.getName());
        separateAndSend(content, player);
        BaseBBSManager.separateAndSend(content, player);
    }

    protected boolean isTeleportAllowed(Player player) {
        if(player.isSitting()){
            player.sendMessage("You cannot use Teleport while sitting.");
            return false;
        }
        if (!Config.KARMA_PLAYER_CAN_USE_GK && player.getKarma() > 0) {
            player.sendMessage("You cannot use Teleport while you have Karma.");
            return false;
        }
        if(player.isInCombat() || player.getPvpFlag() > 0){
            player.sendMessage("You cannot use Teleport while in combat/flag.");
            return false;
        }
        if(player.isDead()){
            player.sendMessage("You cannot use Teleport while you are dead.");
            return false;
        }
        if(player.isInOlympiadMode() || player.isOlympiadStart()){
            player.sendMessage("You cannot use Teleport while in olympiad.");
            return false;
        }
        if(player.isInJail()){
            player.sendMessage("You cannot use Teleport while in Jail.");
            return false;
        }
        if(player.isFreezed()){
            player.sendMessage("You cannot use Teleport while you are freezed.");
            return false;
        }

        return true;
    }

    protected void teleport(Player player, int index) {
        if (!isTeleportAllowed(player)) {
            sendHome(player);
            return;
        }

        final TeleportLocation list = TeleportLocationData.getInstance().getTeleportLocation(index);

        if (list == null) {
            sendHome(player);
            return;
        }

        // Retrieve price list. Potentially cut it by 2 depending of current date.
        int price = list.getPrice();

        if (!list.isNoble())
        {
            Calendar cal = Calendar.getInstance();
            if (cal.get(Calendar.HOUR_OF_DAY) >= 20 && cal.get(Calendar.HOUR_OF_DAY) <= 23 && (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7))
                price /= 2;
        }

        // Delete related items, and if successful teleport the player to the location.
        if (player.destroyItemByItemId("Teleport", (list.isNoble()) ? 6651 : 57, price, null, true))
            player.teleportTo(list, 20);

        player.sendPacket(ActionFailed.STATIC_PACKET);
        IndexCBSManager.getInstance().SendHome(player);
    }

    public void sendHome(Player player){
        IndexCBSManager.getInstance().parseCmd("_bbsLink;teleport", player);
    }

    public static TeleportCBManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        protected static final TeleportCBManager INSTANCE = new TeleportCBManager();
    }
}
