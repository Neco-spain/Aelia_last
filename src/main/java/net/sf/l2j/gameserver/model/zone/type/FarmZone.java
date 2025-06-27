package net.sf.l2j.gameserver.model.zone.type;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.ZoneType;


public class FarmZone extends ZoneType
{
	L2Skill Noblesse = SkillTable.getInstance().getInfo(1323, 1);

	public FarmZone(int id)
	{ 
		super(id);
	}

	@Override
	protected void onEnter(Creature character)
	{
		if(character instanceof Player){
			Player player = (Player) character;
			
			character.setInsideZone(ZoneId.NO_STORE, true);
			character.setInsideZone(ZoneId.FARM, true);
			character.sendMessage("You entered Farm zone!");
		}
	}
	
	@Override
	protected void onExit(Creature character)
	{
		if(character instanceof Player){
			Player player = (Player) character;

			character.setInsideZone(ZoneId.NO_STORE, false);
			character.setInsideZone(ZoneId.FARM, false);
			character.sendMessage("You left Farm zone!");
			}
	}
}