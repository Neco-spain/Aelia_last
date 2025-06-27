package net.sf.l2j.gameserver.model.zone.type;

import Customs.mods.zoneProtections.PartyZoneManager;
import Customs.mods.zoneProtections.RaidZoneManager;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.ZoneType;


public class PartyZone extends ZoneType
{

	private int _maxClanMembers;
	private int _maxAllyMembers;
	private boolean _checkClan;
	private boolean _checkAlly;

	public PartyZone(int id)
	{ 
		super(id);

		_maxClanMembers = 0;
		_maxAllyMembers = 0;
		_checkClan = false;
		_checkAlly = false;
	}

	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("MaxClanMembers"))
			_maxClanMembers = Integer.parseInt(value);
		else if (name.equals("MaxAllyMembers"))
			_maxAllyMembers = Integer.parseInt(value);
		else if (name.equals("checkClan"))
			_checkClan = Boolean.parseBoolean(value);
		else if (name.equals("checkAlly"))
			_checkAlly = Boolean.parseBoolean(value);
		else
			super.setParameter(name, value);
	}

	@Override
	protected void onEnter(Creature character)
	{
		if(character instanceof Player){
			Player activeChar = ((Player) character);

			character.setInsideZone(ZoneId.NO_STORE, true);
			character.setInsideZone(ZoneId.PARTY, true);
			character.sendMessage("You entered Party Zone!");

			if (_checkClan)
				MaxClanMembersOnArea(activeChar);

			if (_checkAlly)
				MaxAllyMembersOnArea(activeChar);
		}
	}

	public boolean MaxClanMembersOnArea(Player activeChar)
	{
		return PartyZoneManager.getInstance().checkClanArea(activeChar, _maxClanMembers, true);
	}

	public boolean MaxAllyMembersOnArea(Player activeChar)
	{
		return PartyZoneManager.getInstance().checkAllyArea(activeChar, _maxAllyMembers, World.getInstance().getPlayers(), true);
	}


	@Override
	protected void onExit(Creature character)
	{
		if(character instanceof Player){
			
			Player player = (Player) character;

			
			character.setInsideZone(ZoneId.NO_STORE, false);
			character.setInsideZone(ZoneId.PARTY, false);
			character.sendMessage("You left Party Zone!");
		}
		
	}
}	