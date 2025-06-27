package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import Customs.data.SkinTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

public class Visual implements IItemHandler
{
    @Override
    public void useItem(Playable playable, ItemInstance item, boolean forceUse)
    {
        if (!(playable instanceof Player))
            return;

        Player player = (Player) playable;
        int itemId = item.getItemId();

        if(!SkinTable.getInstance().getSkinId(itemId))
            return;

        if(player.getVisual() == 0)
            player.setVisual(itemId, SkinTable.getInstance().getHair(itemId), SkinTable.getInstance().getChest(itemId), SkinTable.getInstance().getLegs(itemId), SkinTable.getInstance().getGloves(itemId), SkinTable.getInstance().getBoots(itemId));
        else //reset on press Item if skin is equipped
            player.setVisual(0,0,0,0,0,0);
    }
}
