package net.sf.l2j.gameserver.handler.itemhandlers.Custom;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;

import Custom.CustomConfig;

public class ClanItem implements IItemHandler
{
    @Override
    public void useItem(Playable playable, ItemInstance item, boolean forceUse)
    {
      if (!(playable instanceof Player)) {
        return;
      }

      Player player = (Player)playable;
      if (player.isClanLeader())
      {
        if (player.getClan().getLevel() < 1) {
          player.sendMessage("Your clan must be higher than level 0 in order to use this item!");
          return;
        }

        player.destroyItem("Consume", item.getObjectId(), 1, null, true);

        player.getClan().addReputationScore(CustomConfig.REP_ITEM_POINTS);
        player.getClan().updateClanInDB();

        player.sendPacket(new ExShowScreenMessage("Succesfully added " + CustomConfig.REP_ITEM_POINTS + " Rep points!", 5000, 2, true));
      }
      else
      {
        player.sendMessage("Only leaders of the clans can use this item!");
      }
    }
    private static final int ITEM_IDS[] =
    {
      CustomConfig.CUSTOM_CLAN_ITEM_ID
    };
}