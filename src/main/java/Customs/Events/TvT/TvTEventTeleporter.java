/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package Customs.Events.TvT;


import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.entity.Duel;

public class TvTEventTeleporter implements Runnable
{
	/** The instance of the player to teleport */
	private Player _playerInstance = null;
	/** Coordinates of the spot to teleport to */
	private int[] _coordinates = new int[3];
	/** Admin removed this player from event */
	private boolean _adminRemove = false;
	
	 static L2Skill noblesse = SkillTable.getInstance().getInfo(1323, 1);
	 
	/**
	 * Initialize the teleporter and start the delayed task.
	 * @param playerInstance
	 * @param coordinates
	 * @param fastSchedule
	 * @param adminRemove
	 */
	public TvTEventTeleporter(Player playerInstance, int[] coordinates, boolean fastSchedule, boolean adminRemove)
	{
		_playerInstance = playerInstance;
		_coordinates = coordinates;
		_adminRemove = adminRemove;
		
		long delay = (TvTEvent.isStarted() ? TvTConfig.TVT_EVENT_RESPAWN_TELEPORT_DELAY : TvTConfig.TVT_EVENT_START_LEAVE_TELEPORT_DELAY) * 1000;
		
		ThreadPool.schedule(this, fastSchedule ? 0 : delay);
	}
	
	/**
	 * The task method to teleport the player
	 * 1. Unsummon pet if there is one
	 * 2. Remove all effects
	 * 3. Revive and full heal the player
	 * 4. Teleport the player
	 * 5. Broadcast status and user info
	 */
	@Override
	public void run()
	{
		if (_playerInstance == null)
			return;
		
		Summon summon = _playerInstance.getSummon();
		
		if (summon != null)
			summon.unSummon(_playerInstance);
		
		if ((TvTConfig.TVT_EVENT_EFFECTS_REMOVAL == 0) || ((TvTConfig.TVT_EVENT_EFFECTS_REMOVAL == 1) && ((_playerInstance.getTeam().getId() == 0) || (_playerInstance.isInDuel() && (_playerInstance.getDuelState() != Duel.DuelState.INTERRUPTED)))))
			_playerInstance.stopAllEffectsExceptThoseThatLastThroughDeath();
		
		if (_playerInstance.isInDuel())
			_playerInstance.setDuelState(Duel.DuelState.INTERRUPTED);
	
		
		_playerInstance.doRevive();

		if(TvTEvent.isRewarding() || TvTEvent.isInactivating() || TvTEvent.isInactive())
			_playerInstance.teleportTo(82635, 148798, -3464, 0);
		else
			_playerInstance.teleportTo((_coordinates[0] + Rnd.get(101)) - 50, (_coordinates[1] + Rnd.get(101)) - 50, _coordinates[2], 0);
		
		if(_playerInstance.getKarma() > 0)
			_playerInstance.setKarma(0);
		
		if (TvTEvent.isStarted() && !_adminRemove)
			_playerInstance.setTeamEvent(TvTEvent.getParticipantTeamId(_playerInstance.getObjectId()) + 1);
		else
			_playerInstance.setTeamEvent(0);
		
		_playerInstance.setCurrentCp(_playerInstance.getMaxCp());
		_playerInstance.setCurrentHp(_playerInstance.getMaxHp());
		_playerInstance.setCurrentMp(_playerInstance.getMaxMp());
		 noblesse.getEffects(_playerInstance, _playerInstance);
		_playerInstance.broadcastStatusUpdate();
		_playerInstance.broadcastUserInfo();
	}
}
