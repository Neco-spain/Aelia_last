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
package Customs.Events.DM;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.ItemTable;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.cache.HtmCache;
import net.sf.l2j.gameserver.data.sql.SpawnTable;
import net.sf.l2j.gameserver.data.xml.DoorData;
import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.enums.MessageType;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.instance.Pet;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.itemcontainer.PcInventory;
import net.sf.l2j.gameserver.model.spawn.L2Spawn;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.StatusUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

import Custom.loadCustomMods;

public class DMEvent
{
	enum EventState
	{
		INACTIVE,
		INACTIVATING,
		PARTICIPATING,
		STARTING,
		STARTED,
		REWARDING
	}

	protected static final Logger _log = Logger.getLogger(DMEvent.class.getName());
	/** html path **/
	private static final String htmlPath = "data/html/mods/events/dm/";
	/** The state of the DMEvent<br> */
	private static EventState _state = EventState.INACTIVE;
	/** The spawn of the participation npc<br> */
	private static L2Spawn _npcSpawn = null;
	/** the npc instance of the participation npc<br> */
	private static Npc _lastNpcSpawn = null;

	private static Map<Integer, DMPlayer> _dmPlayer = new HashMap<>();

	
	public DMEvent()
	{
	}

	/**
	 * DM initializing<br>
	 */
	public static void init()
	{
		// ?
	}

	/**
	 * Sets the DMEvent state<br><br>
	 *
	 * @param state as EventState<br>
	 */
	private static void setState(EventState state)
	{
		synchronized (_state)
		{
			_state = state;
		}
	}

	/**
	 * Is DMEvent inactive?<br><br>
	 *
	 * @return boolean: true if event is inactive(waiting for next event cycle), otherwise false<br>
	 */
	public static boolean isInactive()
	{
		boolean isInactive;

		synchronized (_state)
		{
			isInactive = _state == EventState.INACTIVE;
		}

		return isInactive;
	}

	/**
	 * Is DMEvent in inactivating?<br><br>
	 *
	 * @return boolean: true if event is in inactivating progress, otherwise false<br>
	 */
	public static boolean isInactivating()
	{
		boolean isInactivating;

		synchronized (_state)
		{
			isInactivating = _state == EventState.INACTIVATING;
		}

		return isInactivating;
	}

	/**
	 * Is DMEvent in participation?<br><br>
	 *
	 * @return boolean: true if event is in participation progress, otherwise false<br>
	 */
	public static boolean isParticipating()
	{
		boolean isParticipating;

		synchronized (_state)
		{
			isParticipating = _state == EventState.PARTICIPATING;
		}

		return isParticipating;
	}

	/**
	 * Is DMEvent starting?<br><br>
	 *
	 * @return boolean: true if event is starting up(setting up fighting spot, teleport players etc.), otherwise false<br>
	 */
	public static boolean isStarting()
	{
		boolean isStarting;

		synchronized (_state)
		{
			isStarting = _state == EventState.STARTING;
		}

		return isStarting;
	}

	/**
	 * Is DMEvent started?<br><br>
	 *
	 * @return boolean: true if event is started, otherwise false<br>
	 */
	public static boolean isStarted()
	{
		boolean isStarted;

		synchronized (_state)
		{
			isStarted = _state == EventState.STARTED;
		}

		return isStarted;
	}

	/**
	 * Is DMEvent rewadrding?<br><br>
	 *
	 * @return boolean: true if event is currently rewarding, otherwise false<br>
	 */
	public static boolean isRewarding()
	{
		boolean isRewarding;

		synchronized (_state)
		{
			isRewarding = _state == EventState.REWARDING;
		}

		return isRewarding;
	}

	/**
	 * Close doors specified in configs
	 * @param doors 
	 */
/*	private static void closeDoors(List<Integer> doors)
	{
		for (int doorId : doors)
		{
			Door doorInstance = DoorData.getInstance().getDoor(doorId);

			if (doorInstance != null)
			{
				doorInstance.closeMe();
			}
		}
	}


	private static void openDoors(List<Integer> doors)
	{
		for (int doorId : doors)
		{
			Door doorInstance = DoorData.getInstance().getDoor(doorId);

			if (doorInstance != null)
			{
				doorInstance.openMe();
			}
		}
	}
*/
/**
 * Close doors specified in CustomConfigs
 */
private static void closeDoors()
{
	for (int doorId : DMConfig.DM_DOORS_IDS_TO_CLOSE)
	{
		Door doorInstance = DoorData.getInstance().getDoor(doorId);
		
		if (doorInstance != null)
			doorInstance.closeMe();
	}
}

/**
 * Open doors specified in CustomConfigs
 */
private static void openDoors()
{
	for (int doorId : DMConfig.DM_DOORS_IDS_TO_OPEN)
	{
		Door doorInstance = DoorData.getInstance().getDoor(doorId);
		
		if (doorInstance != null)
			doorInstance.openMe();
	}
}
	
	
	

	/**
	 * UnSpawns the DMEvent npc
	 */
	private static void unSpawnNpc()
	{
		// Delete the npc
		_lastNpcSpawn.deleteMe();
		_lastNpcSpawn.getSpawn().setRespawnState(false);
		SpawnTable.getInstance().deleteSpawn(_lastNpcSpawn.getSpawn(), false);
		// Stop respawning of the npc
		_npcSpawn = null;
		_lastNpcSpawn = null;
	}

	/**
	 * Starts the participation of the DMEvent<br>
	 * 1. Get NpcTemplate by Config.DM_EVENT_PARTICIPATION_NPC_ID<br>
	 * 2. Try to spawn a new npc of it<br><br>
	 *
	 * @return boolean: true if success, otherwise false<br>
	 */
	public static boolean startParticipation()
	{
		NpcTemplate tmpl = NpcData.getInstance().getTemplate(DMConfig.DM_EVENT_PARTICIPATION_NPC_ID);

		if (tmpl == null)
		{
			_log.warning("DMEventEngine[DMEvent.startParticipation()]: NpcTemplate is a NullPointer -> Invalid npc id in configs?");
			return false;
		}

		try
		{
			_npcSpawn = new L2Spawn(tmpl);

	/*		_npcSpawn.setLocx(DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES[0]);
			_npcSpawn.setLocy(DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES[1]);
			_npcSpawn.setLocz(DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES[2]);
			_npcSpawn.getAmount();
			_npcSpawn.setHeading(DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES[3]);
			_npcSpawn.setRespawnDelay(1);
			// later no need to delete spawn from db, we don't store it (false)
			SpawnTable.getInstance().addNewSpawn(_npcSpawn, false);
			_npcSpawn.init();
			_lastNpcSpawn = _npcSpawn.getLastSpawn();
			_lastNpcSpawn.setCurrentHp(_lastNpcSpawn.getMaxHp());
			_lastNpcSpawn.setTitle("DM Event");
			_lastNpcSpawn.isAggressive();
			_lastNpcSpawn.decayMe();
			_lastNpcSpawn.spawnMe(_npcSpawn.getLastSpawn().getX(), _npcSpawn.getLastSpawn().getY(), _npcSpawn.getLastSpawn().getZ());
			_lastNpcSpawn.broadcastPacket(new MagicSkillUse(_lastNpcSpawn, _lastNpcSpawn, 1034, 1, 1, 1));
			*/
			
			_npcSpawn.setLoc(DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES[0],DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES[1],DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES[2], DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES[3]);
			_npcSpawn.setRespawnDelay(1);
			_npcSpawn.setRespawnState(true);
			SpawnTable.getInstance().addSpawn(_npcSpawn, false);		      	
			_npcSpawn.doSpawn(false);
			_lastNpcSpawn = _npcSpawn.getNpc();
			_lastNpcSpawn.getStatus().setCurrentHp(9.99999999E8D);
			_lastNpcSpawn.isAggressive();
			_lastNpcSpawn.decayMe();
			_lastNpcSpawn.spawnMe(_npcSpawn.getNpc().getX(), _npcSpawn.getNpc().getY(), _npcSpawn.getNpc().getZ());
			_lastNpcSpawn.broadcastPacket(new MagicSkillUse(_lastNpcSpawn, _lastNpcSpawn, 1034, 1, 1, 1));
			
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "DMEventEngine[DMEvent.startParticipation()]: exception: " + e.getMessage(), e);
			return false;
		}

		setState(EventState.PARTICIPATING);
		return true;
	}

	/**
	 * Starts the DMEvent fight<br>
	 * 1. Set state EventState.STARTING<br>
	 * 2. Close doors specified in configs<br>
	 * 3. Abort if not enought participants(return false)<br>
	 * 4. Set state EventState.STARTED<br>
	 * 5. Teleport all participants to team spot<br><br>
	 *
	 * @return boolean: true if success, otherwise false<br>
	 */
	public static boolean startFight()
	{
		// Set state to STARTING
		setState(EventState.STARTING);

		// Check the number of participants
		if (_dmPlayer.size() < DMConfig.DM_EVENT_MIN_PLAYERS)
		{
			// Set state INACTIVE
			setState(EventState.INACTIVE);

			// Cleanup of participants
			_dmPlayer.clear();

			// Unspawn the event NPC
			unSpawnNpc();
			return false;
		}

		// Closes all doors specified in configs for dm
		if(DMConfig.CLOSE_DOORS)
			closeDoors();//(DMConfig.DM_DOORS_IDS_TO_CLOSE);
		
		// Set state STARTED
		setState(EventState.STARTED);

		for (DMPlayer player: _dmPlayer.values())
		{
			if (player != null)
			{
				// Teleporter implements Runnable and starts itself
				
				
				Player playerInstance = player.getPlayer();
				if (DMConfig.DM_EVENT_ON_KILL.equalsIgnoreCase("title"))
				{
					playerInstance._originalTitleDM = playerInstance.getTitle();
					playerInstance.setTitle("Kills: " + playerInstance.getPointScore());
					playerInstance.broadcastTitleInfo();				
				}
				
				new DMEventTeleporter(player.getPlayer(), false, false);
				
			}
		} 

		return true;
	}

	public static TreeSet<DMPlayer> orderPosition(Collection<DMPlayer> listPlayer)
	{
		TreeSet<DMPlayer> players = new TreeSet<>(new Comparator<DMPlayer>()
				{
			@Override
			public int compare(DMPlayer p1, DMPlayer p2)
			{
				Integer c1 = Integer.valueOf(p2.getPoints() - p1.getPoints());
				Integer c2 = Integer.valueOf(p1.getDeath() - p2.getDeath());
				Integer c3 = p1.getHexCode().compareTo(p2.getHexCode());

				if (c1 == 0)
				{
					if (c2 == 0) return c3;
					return c2;
				}		
				return c1;
			}
				});
		players.addAll(listPlayer);
		return players;
	}

	/**
	 * Calculates the DMEvent reward<br>
	 * 1. If both teams are at a tie(points equals), send it as system message to all participants, if one of the teams have 0 participants left online abort rewarding<br>
	 * 2. Wait till teams are not at a tie anymore<br>
	 * 3. Set state EvcentState.REWARDING<br>
	 * 4. Reward team with more points<br>
	 * 5. Show win html to wining team participants<br><br>
	 *
	 * @return String: winning team name<br>
	 */
	public static String calculateRewards()
	{
		TreeSet<DMPlayer> players = orderPosition(_dmPlayer.values());
		String msg = "";
		for (int j = 0; j < DMConfig.DM_REWARD_FIRST_PLAYERS; j++)
		{
			if (players.isEmpty()) break;

			DMPlayer player = players.first();

			if (player.getPoints() == 0) break;

			rewardPlayer(player, j + 1);
			players.remove(player);
			int playerPointPrev = player.getPoints();

			if (!DMConfig.DM_REWARD_PLAYERS_TIE)
				continue;

			while(!players.isEmpty())
			{
				player = players.first();
				
				if(player.getPoints() != playerPointPrev)
					break;
				
				rewardPlayer(player, j + 1);
				players.remove(player);
				msg += " Player: " + player.getPlayer().getName();
				msg += " Killed: " + player.getPoints();
				msg += "\n";
			}
		}

		// Set state REWARDING so nobody can point anymore
		setState(EventState.REWARDING);

		return "Deathmatch: ended, thanks to everyone who participated!\nWinner(s):\n" + msg;
	}

	private static void rewardPlayer(DMPlayer p, int pos)
	{
		Player activeChar = p.getPlayer();

		// Check for nullpointer
		if (activeChar == null)
			return;

		SystemMessage systemMessage = null;

		List<int[]> rewards = DMConfig.DM_EVENT_REWARDS.get(pos);

		for (int[] reward : rewards)
		{
			PcInventory inv = activeChar.getInventory();

			// Check for stackable item, non stackabe items need to be added one by one
//			if (ItemData.getInstance().createDummyItem(reward[0]).isStackable())
			ItemInstance item = activeChar.addItem("DM", reward[0], reward[1] /** CustomConfig.VIP_DROP_RATE*/ , null, true);
/*			if(item.isStackable()) // Possible a temp item
			{
				//inv.addItem("Deathmatch", reward[0], reward[1], activeChar, activeChar);

				if (reward[1] > 1)
				{
					systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S);
					systemMessage.addItemName(reward[0]);
					systemMessage.addItemNumber(reward[1]);
				}
				else
				{
					systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
					systemMessage.addItemName(reward[0]);
				}

				activeChar.sendPacket(systemMessage);
			}
			else
			{
				for (int i = 0; i < reward[1]; ++i)
				{
					//inv.addItem("Deathmatch", reward[0], 1, activeChar, activeChar);
					systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
					systemMessage.addItemName(reward[0]);
					activeChar.sendPacket(systemMessage);
				}
			}
	*/		
		}

		StatusUpdate statusUpdate = new StatusUpdate(activeChar);
		NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(0);

		statusUpdate.addAttribute(StatusUpdate.CUR_LOAD, activeChar.getCurrentLoad());
		npcHtmlMessage.setHtml(HtmCache.getInstance().getHtm(htmlPath + "Reward.htm"));
		activeChar.sendPacket(statusUpdate);
		activeChar.sendPacket(npcHtmlMessage);
	}

	/**
	 * Stops the DMEvent fight<br>
	 * 1. Set state EventState.INACTIVATING<br>
	 * 2. Remove DM npc from world<br>
	 * 3. Open doors specified in configs<br>
	 * 4. Send Top Rank<br>
	 * 5. Teleport all participants back to participation npc location<br>
	 * 6. List players cleaning<br>
	 * 7. Set state EventState.INACTIVE<br>
	 */
	public static void stopFight()
	{
		// Set state INACTIVATING
		setState(EventState.INACTIVATING);
		//Unspawn event npc
		unSpawnNpc();
		// Opens all doors specified in configs for DM
		
		if(DMConfig.CLOSE_DOORS)
			openDoors();//(DMConfig.DM_DOORS_IDS_TO_CLOSE);
		
		// Closes all doors specified in Configs for DM
		//closeDoors();//(DMConfig.DM_DOORS_IDS_TO_OPEN);

		String[] topPositions;
		String htmltext = "";
		if (DMConfig.DM_SHOW_TOP_RANK)
		{
			topPositions = getFirstPosition(DMConfig.DM_TOP_RANK);
			Boolean c = true;
			String c1 = "D9CC46";
			String c2 = "FFFFFF";
			if (topPositions != null)
				for (int i = 0; i < topPositions.length; i++)
				{
					String color = (c ? c1 : c2);
					String[] row = topPositions[i].split("\\,");
					htmltext += "<tr>";
					htmltext += "<td width=\"35\" align=\"center\"><font color=\"" + color + "\">" + String.valueOf(i + 1) + "</font></td>";
					htmltext += "<td width=\"100\" align=\"left\"><font color=\"" + color + "\">" + row[0] + "</font></td>";
					htmltext += "<td width=\"125\" align=\"right\"><font color=\"" + color + "\">" + row[1] + "</font></td>";
					htmltext += "</tr>";
					c = !c;
				}
		}

		for (DMPlayer player : _dmPlayer.values())
		{
			if (player != null)
			{
				//seset karma
				if(player.getPlayer().getKarma() > 0)
					player.getPlayer().setKarma(0);
				
				// Top Rank
				if (DMConfig.DM_SHOW_TOP_RANK)
				{
					NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(0);
					npcHtmlMessage.setHtml(HtmCache.getInstance().getHtm(htmlPath + "TopRank.htm"));
					npcHtmlMessage.replace("%toprank%", htmltext);
					player.getPlayer().sendPacket(npcHtmlMessage);
				}
				
			//	if (DMConfig.DM_EVENT_ON_KILL.equalsIgnoreCase("title"))
			//	{
					Player playerInstance = player.getPlayer();
					playerInstance.setTitle(playerInstance._originalTitleDM);
					playerInstance.broadcastTitleInfo();		
					playerInstance.clearPoints();
				//}
				new DMEventTeleporter(player.getPlayer(), DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES, false, false);
				
			}
		}

		// Cleanup list
		_dmPlayer = new HashMap<>();
		// Set state INACTIVE
		setState(EventState.INACTIVE);
	}

	/**
	 * Adds a player to a DMEvent<br>
	 *
	 * @param activeChar as Player<br>
	 * @return boolean: true if success, otherwise false<br>
	 */
	public static synchronized boolean addParticipant(Player activeChar)
	{
		// Check for nullpoitner
		if (activeChar == null) return false;

		if (isPlayerParticipant(activeChar)) return false;

		String hexCode = hexToString(generateHex(16));
		_dmPlayer.put(activeChar.getObjectId(), new DMPlayer(activeChar, hexCode));
		return true;
	}

	public static boolean isPlayerParticipant(Player activeChar)
	{
		if (activeChar == null)
			return false;
		try
		{
			if (_dmPlayer.containsKey(activeChar.getObjectId()))
				return true;
		}
		catch (Exception e) 
		{
			return false;
		}
		return false;
	}

	public static boolean areTeammates(Player player, Player target)
	{
//		if (isParticipating() && !isStarting() && !isStarted() )
//			return false;
		
		if (_dmPlayer.isEmpty())
			return false;
		
//		if (_dmPlayer.containsKey(player.getObjectId()) == _dmPlayer.containsKey(target.getObjectId()))
//			return true;
		
		return false;
	}
	
	public static boolean isPlayerParticipant(int objectId)
	{
		Player activeChar = World.getInstance().getPlayer(objectId);
		if (activeChar == null)
			return false; 

		return isPlayerParticipant(activeChar);
	}

	public static boolean isPlayerParticipantCustom(int objectId)
	{
		Player activeChar = World.getInstance().getPlayer(objectId);
		if (activeChar == null)
			return false; 

		if (isParticipating() && !isStarting() && !isStarted())
			return false;
		
		return isPlayerParticipant(activeChar);
	}
	
	public static boolean isPlayerParticipantCustom1(int objectId)
	{
		Player activeChar = World.getInstance().getPlayer(objectId);
		if (activeChar == null)
			return false; 

		if (isStarted())
			return isPlayerParticipant(activeChar);
			
		
		return false;
	}
	
	/**
	 * Removes a DMEvent player<br>
	 *
	 * @param activeChar as Player<br>
	 * @return boolean: true if success, otherwise false<br>
	 */
	public static boolean removeParticipant(Player activeChar)
	{
		if (activeChar == null)
			return false;

		if (!isPlayerParticipant(activeChar))
			return false;

		try
		{
			_dmPlayer.remove(activeChar.getObjectId());
		}
		catch (Exception e) 
		{
			return false;
		}

		return true;
	}

	public static boolean payParticipationFee(Player activeChar)
	{
		int itemId = DMConfig.DM_EVENT_PARTICIPATION_FEE[0];
		int itemNum = DMConfig.DM_EVENT_PARTICIPATION_FEE[1];
		if (itemId == 0 || itemNum == 0)
			return true;

		if (activeChar.getInventory().getInventoryItemCount(itemId, -1) < itemNum)
			return false;

		return activeChar.destroyItemByItemId("DM Participation Fee", itemId, itemNum, _lastNpcSpawn, true);
	}

	public static String getParticipationFee()
	{
		int itemId = DMConfig.DM_EVENT_PARTICIPATION_FEE[0];
		int itemNum = DMConfig.DM_EVENT_PARTICIPATION_FEE[1];

		if (itemId == 0 || itemNum == 0)
			return "-";

		return StringUtil.concat(String.valueOf(itemNum), " ", ItemTable.getInstance().getTemplate(itemId).getName());
	}

	/**
	 * Send a SystemMessage to all participated players<br>
	 *
	 * @param message as String<br>
	 */
	public static void sysMsgToAllParticipants(String message)
	{
		CreatureSay cs = new CreatureSay(0, Say2.PARTYROOM_ALL, "DM EVENT", message);

		for (DMPlayer player : _dmPlayer.values())
			if (player != null)
				player.getPlayer().sendPacket(cs);
	}

	/**
	 * Called when a player logs in<br><br>
	 *
	 * @param activeChar as Player<br>
	 */
	public static void onLogin(Player activeChar)
	{
		if (activeChar == null || (!isStarting() && !isStarted()))
			return;

		if (!isPlayerParticipant(activeChar))
			return;

		new DMEventTeleporter(activeChar, true, false);
	}

	public static boolean removeParticipant1(Player activeChar)
	{
		if (activeChar == null)
			return false;

		if (!isPlayerParticipant(activeChar))
			return false;

		if(isParticipating())
			_dmPlayer.remove(activeChar.getObjectId());
		
		
		for(DMPlayer x : _dmPlayer.values())
			System.out.println("a: " + x.getPlayer().getName());
		
	/*	try
		{
			_dmPlayer.remove(activeChar.getObjectId());
		}
		catch (Exception e) 
		{
			return false;
		}
*/
		return true;
	}
	
	/**
	 * Called when a player logs out<br><br>
	 *
	 * @param activeChar as Player<br>
	 */
	public static void onLogout(Player activeChar)
	{
		if (activeChar != null && (isStarting() || isStarted() || isParticipating()))
		{

			if (removeParticipant(activeChar))
				activeChar.teleportTo(DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES[0] + Rnd.get(101)-50, DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES[1] + Rnd.get(101)-50, DMConfig.DM_EVENT_PARTICIPATION_NPC_COORDINATES[2], 0);
		
			
			if (DMConfig.DM_EVENT_ON_KILL.equalsIgnoreCase("title"))
			{
				Player playerInstance = activeChar;
				playerInstance.setTitle(playerInstance._originalTitleDM);
				playerInstance.broadcastTitleInfo();				
			}
		
		}
	}

	/**
	 * Called on every bypass by npc of type L2DMEventNpc<br>
	 * Needs synchronization cause of the max player check<br><br>
	 *
	 * @param command as String<br>
	 * @param activeChar as Player<br>
	 */
	public static synchronized void onBypass(String command, Player activeChar)
	{
		if (activeChar == null || !isParticipating())
			return;

		final String htmContent;

		if (command.equals("dm_event_participation"))
		{
			NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(0);
			int playerLevel = activeChar.getLevel();

			if (activeChar.isCursedWeaponEquipped())
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "CursedWeaponEquipped.htm");
				if (htmContent != null)
					npcHtmlMessage.setHtml(htmContent);
			}	
			else if(activeChar.isSupportClass(activeChar.getActiveClass(),true)) {
				activeChar.sendMessage("Your class is not allowed in this event.");
				return ;
			}
			else if ( activeChar.isRegisteredInTournament())
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "Tournament.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
				}
			}
			else if (activeChar.isInOlympiadMode())
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "Olympiad.htm");
				if (htmContent != null)
					npcHtmlMessage.setHtml(htmContent);
			}
			else if (activeChar.getKarma() > 0)
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "Karma.htm");
				if (htmContent != null)
					npcHtmlMessage.setHtml(htmContent);
			}
			else if (DMConfig.DISABLE_ID_CLASSES.contains(activeChar.getClassId().getId()))
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "Class.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
				}
			}
			else if (playerLevel < DMConfig.DM_EVENT_MIN_LVL || playerLevel > DMConfig.DM_EVENT_MAX_LVL)
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "Level.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
					npcHtmlMessage.replace("%min%", String.valueOf(DMConfig.DM_EVENT_MIN_LVL));
					npcHtmlMessage.replace("%max%", String.valueOf(DMConfig.DM_EVENT_MAX_LVL));
				}
			}
			else if (_dmPlayer.size() == DMConfig.DM_EVENT_MAX_PLAYERS)
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "Full.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
					npcHtmlMessage.replace("%max%", String.valueOf(DMConfig.DM_EVENT_MAX_PLAYERS));
				}
			}
			else if (DMConfig.DM_EVENT_MULTIBOX_PROTECTION_ENABLE && onMultiBoxRestriction(activeChar))
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "MultiBox.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
					npcHtmlMessage.replace("%maxbox%", String.valueOf(DMConfig.DM_EVENT_NUMBER_BOX_REGISTER));
				}
			}
			else if (!payParticipationFee(activeChar))
			{
				htmContent = HtmCache.getInstance().getHtm(htmlPath + "ParticipationFee.htm");
				if (htmContent != null)
				{
					npcHtmlMessage.setHtml(htmContent);
					npcHtmlMessage.replace("%fee%", getParticipationFee());
				}
			}
			else if (isPlayerParticipant(activeChar))
				npcHtmlMessage.setHtml(HtmCache.getInstance().getHtm(htmlPath + "Registered.htm"));
			else if (addParticipant(activeChar))
				npcHtmlMessage.setHtml(HtmCache.getInstance().getHtm(htmlPath + "Registered.htm"));
			else
				return;

			activeChar.sendPacket(npcHtmlMessage);
		}
		else if (command.equals("dm_event_remove_participation"))
		{
			if (isPlayerParticipant(activeChar))
			{
				removeParticipant(activeChar);

				NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(0);

				npcHtmlMessage.setHtml(HtmCache.getInstance().getHtm(htmlPath + "Unregistered.htm"));
				activeChar.sendPacket(npcHtmlMessage);
			}
		}
	}

	/**
	 * Called on every onAction in L2PcIstance<br><br>
	 *
	 * @param activeChar as Player<br>
	 * @param targetedPlayerObjectId 
	 * @return boolean: true if player is allowed to target, otherwise false<br>
	 */
	public static boolean onAction(Player activeChar, int targetedPlayerObjectId)
	{
		if (activeChar == null || !isStarted()) 
			return true;		

		if (activeChar.isGM())
			return true;

		if (!isPlayerParticipant(activeChar) && isPlayerParticipant(targetedPlayerObjectId)) 
			return false;		

		if (isPlayerParticipant(activeChar) && !isPlayerParticipant(targetedPlayerObjectId)) 
			return false;

		return true;
	}

	/**
	 * Called on every scroll use<br><br>
	 *
	 * @param objectId as Integer<br>
	 * @return boolean: true if player is allowed to use scroll, otherwise false<br>
	 */
	public static boolean onScrollUse(int objectId)
	{
		if (!isStarted())
			return true;

		if (isPlayerParticipant(objectId) && !DMConfig.DM_EVENT_SCROLL_ALLOWED)
			return false;

		return true;
	}

	/**
	 * Called on every potion use<br><br>
	 *
	 * @param objectId as Integer<br>
	 * @return boolean: true if player is allowed to use potions, otherwise false<br>
	 */
	public static boolean onPotionUse(int objectId)
	{
		if (!isStarted())
			return true;

		if (isPlayerParticipant(objectId) && !DMConfig.DM_EVENT_POTIONS_ALLOWED)
			return false;

		return true;
	}

	/**
	 * Called on every escape use<br><br>
	 *
	 * @param objectId as Integer<br>
	 * @return boolean: true if player is not in DM Event, otherwise false<br>
	 */
	public static boolean onEscapeUse(int objectId)
	{
		if (!isStarted())
			return true;

		if (isPlayerParticipant(objectId))
			return false;

		return true;
	}

	/**
	 * Called on every summon item use<br><br>
	 *
	 * @param objectId as Integer<br>
	 * @return boolean: true if player is allowed to summon by item, otherwise false<br>
	 */
	public static boolean onItemSummon(int objectId)
	{
		if (!isStarted())
			return true;


		if (isPlayerParticipant(objectId) && !DMConfig.DM_EVENT_SUMMON_BY_ITEM_ALLOWED)
			return false;

		return true;
	}

	/**
	 * Is called when a player is killed<br><br>
	 * 
	 * @param killerCharacter as L2Character<br>
	 * @param killedPlayerInstance as Player<br>
	 */
	public static void onKill(Creature killerCharacter, Player killedPlayerInstance)
	{
		if (killedPlayerInstance == null || !isStarted()) 
			return;

		if (!isPlayerParticipant(killedPlayerInstance.getObjectId())) 
			return;

		new DMEventTeleporter(killedPlayerInstance, false, false);

		if (killerCharacter == null) 
			return;

		Player killerPlayerInstance = null;

		if (killerCharacter instanceof Pet || killerCharacter instanceof Summon)
		{
			killerPlayerInstance = ((Summon) killerCharacter).getOwner();		
			if (killerPlayerInstance == null) 
				return;
		}
		else if (killerCharacter instanceof Player)
			killerPlayerInstance = (Player) killerCharacter;
		else
			return;

		if (isPlayerParticipant(killerPlayerInstance))
		{
			_dmPlayer.get(killerPlayerInstance.getObjectId()).increasePoints();
			//sysMsgToAllParticipants(killerPlayerInstance.getName() + " Hunted Player " + killedPlayerInstance.getName() + "!");

			_dmPlayer.get(killedPlayerInstance.getObjectId()).increaseDeath();
		}

		// if (DMConfig.DM_EVENT_ON_KILL.equalsIgnoreCase("title"))
		//	{
			 	killerPlayerInstance.increasePointScore();
				killerPlayerInstance.setTitle("Kills: " + killerPlayerInstance.getPointScore());
				killerPlayerInstance.broadcastTitleInfo();
			//}

//		}
	}

	/**
	 * Called on Appearing packet received (player finished teleporting)<br><br>
	 * @param activeChar 
	 * 
	 */
	public static void onTeleported(Player activeChar)
	{
		if (!isStarted() || activeChar == null || !isPlayerParticipant(activeChar.getObjectId()))
			return;

		if (activeChar.isMageClass())
		{
			if (DMConfig.DM_EVENT_MAGE_BUFFS != null && !DMConfig.DM_EVENT_MAGE_BUFFS.isEmpty())
			{
				for (int i : DMConfig.DM_EVENT_MAGE_BUFFS.keySet())
				{
					L2Skill skill = SkillTable.getInstance().getInfo(i, DMConfig.DM_EVENT_MAGE_BUFFS.get(i));
					if (skill != null)
						skill.getEffects(activeChar, activeChar);
				}
			}
		}
		else
		{
			if (DMConfig.DM_EVENT_FIGHTER_BUFFS != null && !DMConfig.DM_EVENT_FIGHTER_BUFFS.isEmpty())
			{
				for (int i : DMConfig.DM_EVENT_FIGHTER_BUFFS.keySet())
				{
					L2Skill skill = SkillTable.getInstance().getInfo(i, DMConfig.DM_EVENT_FIGHTER_BUFFS.get(i));
					if (skill != null)
						skill.getEffects(activeChar, activeChar);
				}
			}
		}

		removeParty(activeChar);
	}

	/*
	 * Return true if player valid for skill
	 */
	public static final boolean checkForDMSkill(Player source, Player target, L2Skill skill)
	{
		if (!isStarted())
			return true;

		// DM is started
		final boolean isSourceParticipant = isPlayerParticipant(source);
		final boolean isTargetParticipant = isPlayerParticipant(target);

		// both players not participating
		if (!isSourceParticipant && !isTargetParticipant)
			return true;
		// one player not participating
		if (!(isSourceParticipant && isTargetParticipant))
			return false;

		return true;
	}

	public static int getPlayerCounts()
	{
		return _dmPlayer.size();
	}

	public static String[] getFirstPosition(int countPos)
	{
		TreeSet<DMPlayer> players = orderPosition(_dmPlayer.values());
		String text = "";
		for (int j = 0; j < countPos; j++)
		{
			if (players.isEmpty()) break;

			DMPlayer player = players.first();

			if (player.getPoints() == 0) break;

			text += player.getPlayer().getName() + "," + String.valueOf(player.getPoints()) + ";"; 
			players.remove(player);

			int playerPointPrev = player.getPoints();

			if (!DMConfig.DM_REWARD_PLAYERS_TIE) continue;

			while(!players.isEmpty()){
				player = players.first();
				if(player.getPoints() != playerPointPrev)
					break;
				text += player.getPlayer().getName() + "," + String.valueOf(player.getPoints()) + ";";
				players.remove(player);
			}
		}

		if (text != "") return text.split("\\;");

		return null;
	}

	
	
	public static void removeParty(Player activeChar)
	{
		if (activeChar.getParty() != null)
		{
			Party party = activeChar.getParty();
			party.removePartyMember(activeChar, MessageType.LEFT);
		}
	}

	public static byte[] generateHex(int size)
	{
		byte[] array = new byte[size];
		Rnd.nextBytes(array);
		return array;
	}

	public static String hexToString(byte[] hex)
	{
		return new BigInteger(hex).toString(16);
	}

	public static Map<Integer, Player> allParticipants()
	{
		Map<Integer, Player> all = new HashMap<>();
		if (getPlayerCounts() > 0)
		{
			for (DMPlayer dp : _dmPlayer.values())
				all.put(dp.getPlayer().getObjectId(), dp.getPlayer());
			return all;
		}
		return all;
	}

/*	public static boolean onMultiBoxRestriction(Player activeChar)
	{
		return IPManager.getInstance().validBox(activeChar, DMConfig.DM_EVENT_NUMBER_BOX_REGISTER, allParticipants().values(), false);
	}*/
	
    public static boolean  onMultiBoxRestriction(Player player) {
    	for(Player p : allParticipants().values()) {
    		if(p.gethwid().equals(player.gethwid()))
    			return true;
    	}

    	return false;
    }
}