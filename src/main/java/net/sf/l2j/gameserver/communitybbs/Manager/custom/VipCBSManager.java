package net.sf.l2j.gameserver.communitybbs.Manager.custom;

import Custom.CustomConfig;
import net.sf.l2j.gameserver.communitybbs.Manager.BaseBBSManager;
import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ItemList;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

import java.util.concurrent.TimeUnit;

public class VipCBSManager extends BaseBBSManager {

    public void doVIP(Player player, int days, int item, int count){

        if (player.isInOlympiadMode()) {
            player.sendMessage("This item cannot be used on Olympiad Games.");
            return;
        }

        if (player.getInventory().getInventoryItemCount(item, 0) >= count) {
            player.getInventory().destroyItemByItemId("VIP CBS", item, count, player,null);

            long remainingTime = player.getMemos().getLong("vip",0);
            if(remainingTime > 0) {
                player.getMemos().set("vip", remainingTime + TimeUnit.DAYS.toMillis(days));
                player.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Vip Manager", "Dear " + player.getName() + ", your Vip status has been extended by " + days + " day(s)."));
            }
            else {
                player.getMemos().set("vip", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(days));
                player.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Vip Manager", "Dear " + player.getName() + ", your Vip status has been enabled for " + days + " day(s)."));
            }

            if (CustomConfig.ALLOW_VIP_NCOLOR && !player.isVip()) {
                player.setNameColorVip( player.getAppearance().getNameColor());
                player.setVipNColor(CustomConfig.VIP_NCOLOR);
                player.getAppearance().setNameColor(Integer.decode("0x" + CustomConfig.VIP_NCOLOR.substring(4,6) + CustomConfig.VIP_NCOLOR.substring(2,4) + CustomConfig.VIP_NCOLOR.substring(0,2)));
            }
            if (CustomConfig.ALLOW_VIP_TCOLOR && !player.isVip()) {
                player.setVipTColor(CustomConfig.VIP_TCOLOR);
                player.setTitleColorVip( player.getAppearance().getTitleColor());
                player.getAppearance().setTitleColor(Integer.decode("0x" + CustomConfig.VIP_TCOLOR.substring(4,6) + CustomConfig.VIP_TCOLOR.substring(2,4) + CustomConfig.VIP_TCOLOR.substring(0,2)));
            }

            player.getStat().addExp(player.getStat().getExpForLevel(81));
            player.broadcastPacket(new MagicSkillUse(player, player, 2025, 1, 100, 0));

            player.setVip(true);
            player.broadcastUserInfo();

            String name = ItemTable.getInstance().getTemplate(item).getName();
            if(name == null) name = "";
            player.sendMessage(count + " " + name + " has been destroyed!");

            player.sendPacket(new InventoryUpdate());
            player.sendPacket(new ItemList(player, true));

            IndexCBSManager.getInstance().SendHome(player);
        }
        else {
            player.sendMessage("You do not have " + count + " " + ItemTable.getInstance().getTemplate(item).getName() + "'s.");
            IndexCBSManager.getInstance().SendHome(player);
        }
    }

    protected String getFolder() {
        return "custom/";
    }

    public static VipCBSManager getInstance() {
        return VipCBSManager.SingletonHolder.INSTANCE;
    }
    private static class SingletonHolder {
        protected static final VipCBSManager INSTANCE = new VipCBSManager();
    }
}
