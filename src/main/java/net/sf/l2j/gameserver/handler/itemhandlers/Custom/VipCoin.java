package net.sf.l2j.gameserver.handler.itemhandlers.Custom;

import java.util.concurrent.TimeUnit;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

import Custom.CustomConfig;




public class VipCoin implements IItemHandler
{
    private final int VIP_ID1;
    private final int VIP_DAYS1;
    private final int VIP_ID2;
    private final int VIP_DAYS2;
    private final int VIP_ID3;
    private final int VIP_DAYS3;
    
    public VipCoin() {
        this.VIP_ID1 = CustomConfig.VIP_COIN_ID1;
        this.VIP_DAYS1 = CustomConfig.VIP_DAYS_ID1;
        this.VIP_ID2 = CustomConfig.VIP_COIN_ID2;
        this.VIP_DAYS2 = CustomConfig.VIP_DAYS_ID2;
        this.VIP_ID3 = CustomConfig.VIP_COIN_ID3;
        this.VIP_DAYS3 = CustomConfig.VIP_DAYS_ID3;
    }
    
    @Override
    public void useItem(final Playable playable, final ItemInstance item, final boolean forceUse) {
        if (!(playable instanceof Player)) {
            return;
        }
        final Player activeChar = (Player)playable;
        final int itemId = item.getItemId();
        if (itemId == this.VIP_ID1) {
            if (activeChar.isInOlympiadMode()) {
                activeChar.sendMessage("This item cannot be used on Olympiad Games.");
                return;
            }
            if (activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false)) {
            	long remainingTime = activeChar.getMemos().getLong("vip",0);
            	if(remainingTime > 0) {
                	activeChar.getMemos().set("vip", remainingTime + TimeUnit.DAYS.toMillis(VIP_DAYS1));
                	activeChar.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Vip Manager", "Dear " + activeChar.getName() + ", your Vip status has been extended by " + VIP_DAYS1 + " day(s)."));
                }
                else {
                	activeChar.getMemos().set("vip", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(VIP_DAYS1));
                	activeChar.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Vip Manager", "Dear " + activeChar.getName() + ", your Vip status has been enabled for " + VIP_DAYS1+ " day(s)."));
                }
            	
                if (CustomConfig.ALLOW_VIP_NCOLOR && !activeChar.isVip()) {
                	activeChar.setNameColorVip(activeChar.getAppearance().getNameColor());
                	
//                	activeChar.setOriginalNColor( String.valueOf(activeChar.getAppearance().getNameColor()) );
                	activeChar.setVipNColor(CustomConfig.VIP_NCOLOR);
                	 activeChar.getAppearance().setNameColor(Integer.decode("0x" + CustomConfig.VIP_NCOLOR.substring(4,6) + CustomConfig.VIP_NCOLOR.substring(2,4) + CustomConfig.VIP_NCOLOR.substring(0,2)));
                }
                if (CustomConfig.ALLOW_VIP_TCOLOR && !activeChar.isVip()) {
                	activeChar.setVipTColor(CustomConfig.VIP_TCOLOR);
                	activeChar.setTitleColorVip(activeChar.getAppearance().getTitleColor());

                    activeChar.getAppearance().setTitleColor(Integer.decode("0x" + CustomConfig.VIP_TCOLOR.substring(4,6) + CustomConfig.VIP_TCOLOR.substring(2,4) + CustomConfig.VIP_TCOLOR.substring(0,2))); 
                }
                activeChar.setVip(true);
                
                activeChar.broadcastUserInfo();
//                activeChar.sendPacket(new EtcStatusUpdate(activeChar));
            }
        }
        if (itemId == this.VIP_ID2) {
            if (activeChar.isInOlympiadMode()) {
                activeChar.sendMessage("This item cannot be used on Olympiad Games.");
                return;
            }
            if (activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false)) {
            	long remainingTime = activeChar.getMemos().getLong("vip",0);
            	if(remainingTime > 0) {
                	activeChar.getMemos().set("vip", remainingTime + TimeUnit.DAYS.toMillis(VIP_DAYS2));
                	activeChar.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Vip Manager", "Dear " + activeChar.getName() + ", your Vip status has been extended by " + VIP_DAYS2 + " day(s)."));
                }
                else {
                	activeChar.getMemos().set("vip", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(VIP_DAYS2));
                	activeChar.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Vip Manager", "Dear " + activeChar.getName() + ", your Vip status has been enabled for " + VIP_DAYS2 + " day(s)."));
                }
            	
                if (CustomConfig.ALLOW_VIP_NCOLOR && !activeChar.isVip()) {
//                	activeChar.setVipNColor(CustomConfig.VIP_NCOLOR);
                	activeChar.setNameColorVip(activeChar.getAppearance().getNameColor());//(Integer.decode("0x" + activeChar.getAppearance().getNameColor()));
                	activeChar.setVipNColor(CustomConfig.VIP_NCOLOR);
                	 activeChar.getAppearance().setNameColor(Integer.decode("0x" + CustomConfig.VIP_NCOLOR.substring(4,6) + CustomConfig.VIP_NCOLOR.substring(2,4) + CustomConfig.VIP_NCOLOR.substring(0,2)));
                }
                if (CustomConfig.ALLOW_VIP_TCOLOR && !activeChar.isVip()) {
                	activeChar.setVipTColor(CustomConfig.VIP_TCOLOR);
                	activeChar.setTitleColorVip( activeChar.getAppearance().getTitleColor());
                    activeChar.getAppearance().setTitleColor(Integer.decode("0x" + CustomConfig.VIP_TCOLOR.substring(4,6) + CustomConfig.VIP_TCOLOR.substring(2,4) + CustomConfig.VIP_TCOLOR.substring(0,2))); 
                }
                activeChar.setVip(true);
                
                activeChar.broadcastUserInfo();
//                activeChar.sendPacket(new EtcStatusUpdate(activeChar));
            }
        }
        if (itemId == this.VIP_ID3) {
            if (activeChar.isInOlympiadMode()) {
                activeChar.sendMessage("This item cannot be used on Olympiad Games.");
                return;
            }
            if (activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false)) {
            	long remainingTime = activeChar.getMemos().getLong("vip",0);
            	if(remainingTime > 0) {
                	activeChar.getMemos().set("vip", remainingTime + TimeUnit.DAYS.toMillis(VIP_DAYS3));
                	activeChar.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Vip Manager", "Dear " + activeChar.getName() + ", your Vip status has been extended by " + VIP_DAYS3 + " day(s)."));
                }
                else {
                	activeChar.getMemos().set("vip", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(VIP_DAYS3));
                	activeChar.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Vip Manager", "Dear " + activeChar.getName() + ", your Vip status has been enabled for " + VIP_DAYS3 + " day(s)."));
                }
            	
                if (CustomConfig.ALLOW_VIP_NCOLOR && !activeChar.isVip()) {
                	activeChar.setNameColorVip( activeChar.getAppearance().getNameColor());
//                	activeChar.setNameColorVip(activeChar.getAppearance().getNameColor());
                	activeChar.setVipNColor(CustomConfig.VIP_NCOLOR);
                	 activeChar.getAppearance().setNameColor(Integer.decode("0x" + CustomConfig.VIP_NCOLOR.substring(4,6) + CustomConfig.VIP_NCOLOR.substring(2,4) + CustomConfig.VIP_NCOLOR.substring(0,2))); 
                }
                if (CustomConfig.ALLOW_VIP_TCOLOR && !activeChar.isVip()) {
                	activeChar.setVipTColor(CustomConfig.VIP_TCOLOR);
                	activeChar.setTitleColorVip( activeChar.getAppearance().getTitleColor());
                    activeChar.getAppearance().setTitleColor(Integer.decode("0x" + CustomConfig.VIP_TCOLOR.substring(4,6) + CustomConfig.VIP_TCOLOR.substring(2,4) + CustomConfig.VIP_TCOLOR.substring(0,2))); 
                }
                activeChar.setVip(true);
                
                activeChar.broadcastUserInfo();
//                activeChar.sendPacket(new EtcStatusUpdate(activeChar));
            }
        }
        
        
       
        
    }
}
