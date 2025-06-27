package net.sf.l2j.gameserver.model.actor.instance;

import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.MoveToPawn;
import net.sf.l2j.gameserver.network.serverpackets.MyTargetSelected;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;

import Custom.CustomConfig;

public class VipShop extends Npc
{
    public VipShop(final int objectId, final NpcTemplate template) {
        super(objectId, template);
    }
    
    @Override
    public void onAction(final Player player) {        
    	if (this != player.getTarget())
    	{
    		player.setTarget(this);
    		player.sendPacket(new MyTargetSelected(getObjectId(), 0));
    		player.sendPacket(new ValidateLocation(this));
    	}
    	else
    	{
    		if (!canInteract(player))
    			player.getAI().setIntention(IntentionType.INTERACT, this);
    		else
    		{
    			// Rotate the player to face the instance
    			player.sendPacket(new MoveToPawn(player, this, Npc.INTERACTION_DISTANCE));
    			
    			if (hasRandomAnimation())
    				onRandomAnimation(Rnd.get(8));
    			
    			showMessageWindow(player);
    			
    			// Send ActionFailed to the player in order to avoid he stucks
    			player.sendPacket(ActionFailed.STATIC_PACKET);
    		}
    	}
    }
    
    private void showMessageWindow(final Player player) {
        String filename = "data/html/mods/vipshop/start.htm";
        NpcHtmlMessage html = new NpcHtmlMessage(1);
        html.setFile(filename);
        html.replace("%objectId%", String.valueOf(this.getObjectId()));
        player.sendPacket(html);
        filename = null;
        html = null;
    }
    
    @Override
    public void onBypassFeedback(final Player player, final String command) {
        if (command.startsWith("add_vip")) {
            final StringTokenizer st = new StringTokenizer(command);
            st.nextToken();
            String priceId = null;
            String priceCount = null;
            String time = null;
            int vipPriceId = 0;
            int vipPriceCount = 0;
            int vipTime = 0;
            if (!st.hasMoreTokens()) {
               System.out.println("Could not update VIP status of player " + player.getName());
                return;
            }
            priceId = st.nextToken();
            priceCount = st.nextToken();
            time = st.nextToken();
            try {
                vipPriceId = Integer.parseInt(priceId);
                vipPriceCount = Integer.parseInt(priceCount);
                vipTime = Integer.parseInt(time);
            }
            catch (NumberFormatException ex) {}
            this.makeVipCharacter(player, vipPriceId, vipPriceCount, vipTime);
        }
        else if (command.startsWith("remove_vip")) {
            this.removeVip(player);
        }
        this.showMessageWindow(player);
    }
    
    public void makeVipCharacter(final Player player, final int itemId, final int itemCount, final int vipTime) {
        if (player.isVip()) {
            player.sendMessage("You are already an VIP.");
            return;
        }
        
        final ItemInstance itemInstance = player.getInventory().getItemByItemId(itemId);
        if (itemInstance == null || (!itemInstance.isStackable() && player.getInventory().getInventoryItemCount(itemId, -1) < itemCount)) {
            player.sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
            return;
        }
        if (itemInstance.isStackable()) {
            if (!player.destroyItemByItemId("Vip", itemId, itemCount, player.getTarget(), true)) {
                player.sendPacket(SystemMessageId.NOT_ENOUGH_ITEMS);
                return;
            }
        }
        else {
            for (int i = 0; i < itemCount; ++i) {
                player.destroyItemByItemId("Vip", itemId, 1, player.getTarget(), true);
            }
        }
        this.doVip(player, vipTime);
    }
    
    public void doVip(final Player player, final int days) {
        if (player == null) {
            return;
        }
        if (player.isVip()) {
            player.sendMessage("You are already an VIP.");
            return;
        }
        final int daysLeft = (player.getVipEndTime() <= 0L) ? 0 : ((int)((player.getVipEndTime() - System.currentTimeMillis()) / 86400000L));
        player.setVip(true);
        player.setEndTime("vip", days + daysLeft);
        long time = daysLeft ==  0 ? System.currentTimeMillis() : daysLeft;
        player.getMemos().set("vip", time + TimeUnit.DAYS.toMillis(days));
        
        player.getStat().addExp(player.getStat().getExpForLevel(81));
        player.broadcastPacket(new MagicSkillUse(player, player, 2025, 1, 100, 0));
    	
   
        if (CustomConfig.ALLOW_VIP_NCOLOR ) { //&& player.isVip()
       	player.setNameColorVip(player.getAppearance().getNameColor());
     //         	player.setNameColorVip(Integer.decode("0x") + player.getAppearance().getNameColor());
       		player.setVipNColor(CustomConfig.VIP_NCOLOR);
//            player.getAppearance().setNameColor(CustomConfig.VIP_NCOLOR);
            player.getAppearance().setNameColor(Integer.decode("0x" + CustomConfig.VIP_NCOLOR.substring(4,6) + CustomConfig.VIP_NCOLOR.substring(2,4) + CustomConfig.VIP_NCOLOR.substring(0,2))); 
            
        }
        if (CustomConfig.ALLOW_VIP_TCOLOR ) { //&& player.isVip()
        	player.setTitleColorVip(player.getAppearance().getTitleColor()); 
     //         	player.setTitleColorVip(Integer.decode("0x" + player.getAppearance().getTitleColor()));
        	player.setVipTColor(CustomConfig.VIP_TCOLOR);
            player.getAppearance().setTitleColor(Integer.decode("0x" + CustomConfig.VIP_TCOLOR.substring(4,6) + CustomConfig.VIP_TCOLOR.substring(2,4) + CustomConfig.VIP_TCOLOR.substring(0,2))); 
        }
// player.rewardVipSkills();
        if (CustomConfig.ALLOW_VIP_ITEM && player.isVip()) {
            player.getInventory().addItem("", CustomConfig.VIP_ITEMID, 1, player, null);
            player.getInventory().equipItem(player.getInventory().getItemByItemId(CustomConfig.VIP_ITEMID));
        }
        player.broadcastUserInfo();
        player.sendSkillList();
        player.sendMessage("You are now an Vip, Congratulations!");
    }
    
    public void removeVip(final Player player) {
        if (!player.isVip()) {
            player.sendMessage("You are not an Vip.");
            return;
        }
        player.setVip(false);
        player.setVipEndTime(0L);
        player.getMemos().set("vip", 0);
        if (CustomConfig.ALLOW_VIP_ITEM && !player.isVip()) {
            player.getInventory().destroyItemByItemId("", CustomConfig.VIP_ITEMID, 1, player, null);
            player.getWarehouse().destroyItemByItemId("", CustomConfig.VIP_ITEMID, 1, player, null);
        }

        
        if(CustomConfig.ALLOW_VIP_NCOLOR ) {
        	if(player.getDonateColor().equals(""))
        		player.getAppearance().setNameColor(Integer.decode("0x" + player.getOriginalNColor().substring(4,6) + player.getOriginalNColor().substring(2,4) + player.getOriginalNColor().substring(0,2)));//(Integer.decode("0x" + player.getNameColorVip()));
        	else
        		player.getAppearance().setNameColor(Integer.decode("0x" + player.getDonateColor().substring(4,6) + player.getDonateColor().substring(2,4) + player.getDonateColor().substring(0,2)));//(Integer.decode("0x" + player.getNameColorVip())); 
        }
        if(CustomConfig.ALLOW_VIP_TCOLOR )
        	 player.getAppearance().setTitleColor(Integer.decode("0x" + player.getOriginalTColor().substring(4,6) + player.getOriginalTColor().substring(2,4) + player.getOriginalTColor().substring(0,2)));
        
	
        player.broadcastUserInfo();
        player.sendSkillList();
        player.sendMessage("Now You are not an Vip...");
    }
    
    
    
   
    
}
