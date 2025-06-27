/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.handler.itemhandlers.Custom;


import java.sql.Connection;
import java.sql.PreparedStatement;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

import Custom.CustomConfig;

public class RecItem implements IItemHandler
{	
   @Override
   public void useItem(Playable playable, ItemInstance item, boolean forceUse)
   {
      if(!(playable instanceof Player))
            return;
      Player activeChar = (Player)playable;
        {
           playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
           activeChar.setRecomHave(255);
   		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
   		{
   			PreparedStatement ps = con.prepareStatement("UPDATE characters SET rec_have=? WHERE obj_Id=?");
   			ps.setInt(1, 255);
   			ps.setInt(2, activeChar.getObjectId());
   			ps.execute();
   			ps.close();

   		}
   		catch (Exception e)
   		{
   			LOGGER.error("Couldn't update player recommendations.", e);
   		}
           activeChar.sendMessage("You have Gained 255 Recommends.");
           activeChar.sendMessage("Have Fun.");
           activeChar.broadcastUserInfo();
        }
      
   }
   private static final int ITEM_IDS[] = 
   {
	   CustomConfig.CUSTOM_REC_ITEM_ID
   };
   public int[] getItemIds()
   {
       return ITEM_IDS;
   }
   
}