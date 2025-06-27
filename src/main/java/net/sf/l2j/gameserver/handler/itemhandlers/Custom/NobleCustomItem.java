package net.sf.l2j.gameserver.handler.itemhandlers.Custom;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;

import Custom.CustomConfig;

public class NobleCustomItem
  implements IItemHandler
{
  @Override
public void useItem(Playable playable, ItemInstance item, boolean forceUse)
  {
    if (CustomConfig.NOBLE_CUSTOM_ITEMS)
    {
      if (!(playable instanceof Player)) {
        return;
      }
      Player activeChar = (Player)playable;
      if (activeChar.isNoble())
      {
        activeChar.sendMessage("You Are Already A Noblesse!.");
      }
      else
      {
        activeChar.broadcastPacket(new SocialAction(activeChar, 16));
        activeChar.setNoble(true, true);
        activeChar.sendMessage("You Are Now a Noble,You Are Granted With Noblesse Status , And Noblesse Skills.");
        activeChar.broadcastUserInfo();
        playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
      }
    }
  }
  
  public int[] getItemIds()
  {
    return ITEM_IDS;
  }
  
  private static final int[] ITEM_IDS = { CustomConfig.NOBLE_CUSTOM_ITEM_ID };


}
