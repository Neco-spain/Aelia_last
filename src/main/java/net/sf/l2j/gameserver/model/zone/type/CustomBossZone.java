package net.sf.l2j.gameserver.model.zone.type;

import Customs.mods.zoneProtections.RaidZoneManager;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.ZoneType;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;

public class CustomBossZone extends ZoneType
{
	private int _maxClanMembers;
	private int _maxAllyMembers;
	private int _minPartyMembers;
	private boolean _checkParty;
	private boolean _checkClan;
	private boolean _checkAlly;

	public CustomBossZone(int id)
	{
		super(id);

		_maxClanMembers = 0;
		_maxAllyMembers = 0;
		_minPartyMembers = 0;
		_checkParty = false;
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
		else if (name.equals("MinPartyMembers"))
			_minPartyMembers = Integer.parseInt(value);
		else if (name.equals("checkParty"))
			_checkParty = Boolean.parseBoolean(value);
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
		if(character instanceof Player)
		{
			Player activeChar = ((Player) character);
			
			character.setInsideZone(ZoneId.NO_STORE, true);
			character.setInsideZone(ZoneId.CUSTOMBOSS, true);
			character.sendMessage("You entered A Boss Zone");

			if (_checkParty)
			{
				if (!activeChar.isInParty() || activeChar.getParty().getMembersCount() < _minPartyMembers)
				{
					activeChar.sendPacket(new ExShowScreenMessage("Your party does not have " + _minPartyMembers + " members to enter on this zone!", 6 * 1000));
					RaidZoneManager.getInstance().RandomTeleport(activeChar);
				}
			}

			if (_checkClan)
				MaxClanMembersOnArea(activeChar);

			if (_checkAlly)
				MaxAllyMembersOnArea(activeChar);
		}
	}

	public boolean MaxClanMembersOnArea(Player activeChar)
	{
		return RaidZoneManager.getInstance().checkClanArea(activeChar, _maxClanMembers, true);
	}

	public boolean MaxAllyMembersOnArea(Player activeChar)
	{
		return RaidZoneManager.getInstance().checkAllyArea(activeChar, _maxAllyMembers, World.getInstance().getPlayers(), true);
	}

	@Override
	protected void onExit(Creature character)
	{
		if(character instanceof Player){
			Player player = (Player) character;

			character.setInsideZone(ZoneId.NO_STORE, false);
			character.setInsideZone(ZoneId.CUSTOMBOSS, false);
			character.sendMessage("You Exited A Boss Zone");

		}
		
	}
}