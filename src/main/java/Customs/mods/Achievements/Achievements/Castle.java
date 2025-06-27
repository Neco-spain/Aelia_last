package Customs.mods.Achievements.Achievements;

import Customs.mods.Achievements.Achievements.base.Condition;
import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 * @author Matim
 * @version 1.0
 */
public class Castle extends Condition
{
       public Castle(Object value)
       {
               super(value);
               setName("Have Castle");
       }

        @Override
        public String getStatus(Player player) {
            if (getValue() == null)
                return "null";

            if (player.getClan() != null)
                if (player.getClan().getCastleId() > 0)
                    return CastleManager.getInstance().getCastleById(player.getClan().getCastleId()).getName();

            return "null";
        }

       @Override
       public boolean meetConditionRequirements(Player player)
       {
               if (getValue() == null)
                       return false;
              
               if (player.getClan() != null)
               {
                       if (player.getClan().getCastleId() > 0)
                               return true;
               }
               return false;
       }
}