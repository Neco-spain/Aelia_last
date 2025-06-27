package net.sf.l2j.gameserver.model.zone.type;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.l2j.tesla.autobots.Autobot;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.zone.SpawnZoneType;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;

import Customs.PvpZone.RandomZoneManager;
import net.sf.l2j.gameserver.taskmanager.PvpFlagTaskManager;

public class RandomZone extends SpawnZoneType{
	L2Skill noblesse = SkillTable.getInstance().getInfo(1323, 1);
	private int _id;
	public String _name;
	public int _time;
    public List<Location> _locations = new ArrayList<>();
    public static List<String> _classes = new ArrayList<>();
    public static List<String> _items = new ArrayList<>();
    public static Map<Integer,Integer> _rewards = new ConcurrentHashMap<>();
    
    public RandomZone(int id)
	{
    	super(id);
	}

     @Override
     public void setParameter(String name, String value)
     {
         if (name.equals("id"))
             _id = Integer.parseInt(value);
         else if (name.equals("name"))
             _name = value;
         else if (name.equals("time"))
             _time = Integer.parseInt(value);
         else if (name.equals("locs"))
         {
             for (String locs : value.split(";"))
                 _locations.add(new Location(Integer.valueOf(locs.split(",")[0]), Integer.valueOf(locs.split(",")[1]), Integer.valueOf(locs.split(",")[2])));
         }
         else if (name.equals("disabledClasses"))
         {
             for (String classes : value.split(","))
            	 _classes.add(classes);
         }
         else if (name.equals("disabledItems"))
         {
             for (String items : value.split(","))
            	 _items.add(items);
         }
         else if(name.equals("rewards")) {
        	 for(String id : value.split(";")) 
        			 _rewards.put( Integer.valueOf(id.split(",")[0]) ,  Integer.valueOf(id.split(",")[1]) ); 
        	 
         }
         
         else
             super.setParameter(name, value);
     }
  
  
@Override
protected void onEnter(Creature character)
{
	 character.setInsideZone(ZoneId.CHANGE_PVP_ZONE, true);
	  
	 if (character instanceof Player || character instanceof Autobot) {
	         Player activeChar = ((Player) character);


	         activeChar.sendPacket(new CreatureSay(0,Say2.PARTYROOM_ALL,"PvP Zone","will be changed in " + RandomZoneManager.getInstance().getLeftTime()));
	         if (_classes != null && _classes.contains(""+activeChar.getClassId().getId()))
	         {
	                 activeChar.teleportTo(82725, 148596, -3468, 0);
	                 activeChar.sendMessage("Your class is not allowed in the Random PvP zone.");
	                 return;
	         }
             for (ItemInstance o : activeChar.getInventory().getItems())
             {
                     if (o.isEquipable() && o.isEquipped() && !checkItem(o))
                     {
                             int slot = activeChar.getInventory().getSlotFromItem(o);
                             activeChar.getInventory().unEquipItemInBodySlotAndRecord(slot);
                             activeChar.sendMessage(o.getItemName()+ " unequiped because is not allowed inside this zone.");
                     }
             }
    	 
	      if (!character.isInsideZone(ZoneId.CHANGE_PVP_ZONE)) {
	        ((Player)character).sendPacket(SystemMessageId.ENTERED_COMBAT_ZONE);
	      }
 
	      noblesse.getEffects(character, character);

        if (activeChar.getPvpFlag() > 0) {
            PvpFlagTaskManager.getInstance().remove(activeChar);
            activeChar.updatePvPFlag(0);
        }

		activeChar.updatePvPStatus();
        activeChar.broadcastUserInfo();

  }
}
@Override
protected void onExit(Creature character)
{
    character.setInsideZone(ZoneId.CHANGE_PVP_ZONE, false);
    if (character instanceof Player || character instanceof Autobot) {
    	Player player = (Player) character;

    	player.updatePvPStatus();
    	player.broadcastUserInfo();

	  if (!character.isInsideZone(ZoneId.CHANGE_PVP_ZONE)) {
        ((Player)character).sendPacket(SystemMessageId.LEFT_COMBAT_ZONE);
      }
      
    }
}

  public static boolean checkItem (ItemInstance item)
  {
          if (_items != null && (_items.contains(""+item.getItemId()) ))
                  return false;
         
          return true;
  }
  

     @Override
     public int getId()
     {
         return _id;
     }
    
     public String getName()
     {
         return _name;
     }
    
     public int getTime()
     {
         return _time;
     }
   
     public Location getLoc()
     {
         return _locations.get(Rnd.get(0, _locations.size() - 1));
     }
     public Location getCurentZoneLocs()
     {
         return _locations.get(RandomZoneManager.getInstance().getCurrentZone().getId());
     }
     public boolean asdasd(Player p){
    	 for(Location locs : _locations){
    		 if(locs.getX() != p.getX())
    			 return true;
    	 }
    	 return false;
     }
     
     
 	boolean pvpZone = true;
 	public boolean isPvpZone(){
 		return pvpZone;
 	}
 	
 	public boolean isActive(){
 		return RandomZoneManager.getInstance().getZoneId() == getId();
 	}

}
