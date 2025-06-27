package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.ZoneType;


public class CustomZone extends ZoneType
{
	public CustomZone(int id)
	{
		super(id);
	}
	
	L2Skill Noblesse = SkillTable.getInstance().getInfo(1323, 1);
	
	
	@Override
	protected void onEnter(Creature character)
	{

	//	character.setInsideZone(ZoneId.RESPAWN, true);
	//	character.setInsideZone(ZoneId.FLAG_ONHIT, true);
		if(character instanceof Player)
		{
			character.setInsideZone(ZoneId.NO_STORE, true);
			character.setInsideZone(ZoneId.CUSTOM, true);
			//character.setInsideZone(ZoneId.PVP, true);
			Noblesse.getEffects(character, character);		
			character.sendMessage("You entered A Custom Zone");
		}
	}
	
	@Override
	protected void onExit(Creature character)
	{

	//	character.setInsideZone(ZoneId.RESPAWN, false);
	//	character.setInsideZone(ZoneId.FLAG_ONHIT, false);
		if(character instanceof Player){
			character.setInsideZone(ZoneId.NO_STORE, false);
			character.setInsideZone(ZoneId.CUSTOM, false);
			//character.setInsideZone(ZoneId.PVP, false);
			character.sendMessage("You Exited A Custom Zone");
		}
		
	}
}