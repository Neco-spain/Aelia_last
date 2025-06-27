package net.sf.l2j.gameserver.handler.itemhandlers.Custom;


import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;

import Custom.CustomConfig;

public class ClanItemFull
  implements IItemHandler
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
      player.destroyItem("Consume", item.getObjectId(), 1, null, true);
    
      if (player.getClan().getLevel() != 8)
    	  player.getClan().changeLevel(8);
      
      
      player.getClan().addReputationScore(CustomConfig.REP_ITEM_POINTS);
      player.getClan().updateClanInDB();
      
      
      for (int i = 370; i <= 391; i++) {
    	  //if(!player.getskill.getSkills().equals(i))
    		  player.getClan().addNewSkill(SkillTable.getInstance().getInfo(i, SkillTable.getInstance().getMaxLevel(i)));
      }
      
      player.sendPacket(new ExShowScreenMessage("Now your clan is Full!", 5000, 2, true));
    }
    else
    {
      player.sendMessage("Only leaders of the clans can use this item!");
    }
  }
  private static final int ITEM_IDS[] = 
  {
	   CustomConfig.CUSTOM_CLAN_FULL_ITEM_ID
  };
  public int[] getItemIds()
  {
      return ITEM_IDS;
  }
  
}



