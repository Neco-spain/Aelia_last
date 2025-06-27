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
package Customs.Events.CTF;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.l2j.commons.config.ExProperties;
import net.sf.l2j.commons.lang.StringUtil;

public class CTFConfig
{
	protected static final Logger _log = Logger.getLogger(CTFConfig.class.getName());
	
	private static final String CTF_FILE = "./config/Customs/events/capturetheflag.properties";
	
	public static boolean CTF_EVENT_ENABLED;
	public static String[] CTF_EVENT_INTERVAL;
	public static int CTF_EVENT_PARTICIPATION_TIME;
	public static int CTF_EVENT_RUNNING_TIME;
	public static String CTF_NPC_LOC_NAME;
	public static int CTF_EVENT_PARTICIPATION_NPC_ID;
	public static int CTF_EVENT_TEAM_1_HEADQUARTERS_ID;
	public static int CTF_EVENT_TEAM_2_HEADQUARTERS_ID;
	public static int CTF_EVENT_TEAM_1_FLAG;
	public static int CTF_EVENT_TEAM_2_FLAG;
	public static int CTF_EVENT_CAPTURE_SKILL;
	public static int[] CTF_EVENT_PARTICIPATION_NPC_COORDINATES = new int[4];
	public static int[] CTF_EVENT_PARTICIPATION_FEE = new int[2];
	public static int CTF_EVENT_MIN_PLAYERS_IN_TEAMS;
	public static int CTF_EVENT_MAX_PLAYERS_IN_TEAMS;
	public static int CTF_EVENT_RESPAWN_TELEPORT_DELAY;
	public static int CTF_EVENT_START_LEAVE_TELEPORT_DELAY;
	public static String CTF_EVENT_TEAM_1_NAME;
	public static int[] CTF_EVENT_TEAM_1_COORDINATES = new int[3];
	public static String CTF_EVENT_TEAM_2_NAME;
	public static int[] CTF_EVENT_TEAM_2_COORDINATES = new int[3];
	public static int[] CTF_EVENT_TEAM_1_FLAG_COORDINATES = new int[4];
	public static int[] CTF_EVENT_TEAM_2_FLAG_COORDINATES = new int[4];
	public static List<int[]> CTF_EVENT_REWARDS;

	public static boolean CTF_EVENT_TARGET_TEAM_MEMBERS_ALLOWED;
	public static boolean CTF_EVENT_SCROLL_ALLOWED;
	public static boolean CTF_EVENT_POTIONS_ALLOWED;
	public static boolean CTF_EVENT_SUMMON_BY_ITEM_ALLOWED;
	public static List<Integer> CTF_DOORS_IDS_TO_OPEN;
	public static List<Integer> CTF_DOORS_IDS_TO_CLOSE;
	public static boolean CTF_REWARD_TEAM_TIE;
	public static byte CTF_EVENT_MIN_LVL;
	public static byte CTF_EVENT_MAX_LVL;
	public static int CTF_EVENT_EFFECTS_REMOVAL;
	public static Map<Integer, Integer> CTF_EVENT_FIGHTER_BUFFS;
	public static Map<Integer, Integer> CTF_EVENT_MAGE_BUFFS;
	public static boolean CTF_EVENT_MULTIBOX_PROTECTION_ENABLE;
	public static int CTF_EVENT_NUMBER_BOX_REGISTER;
	
	public static List<int[]> CTF_EVENT_REWARDS_TIE;
	
	public static boolean CLOSE_DOORS;
	
	public static void init()
	{
		ExProperties events = load(CTF_FILE);
		
		CLOSE_DOORS = events.getProperty("CloseDoors", false);
		
		CTF_EVENT_ENABLED = events.getProperty("CTFEventEnabled", false);
		CTF_EVENT_INTERVAL = events.getProperty("CTFEventInterval", "20:00").split(",");
		CTF_EVENT_PARTICIPATION_TIME = events.getProperty("CTFEventParticipationTime", 3600);
		CTF_EVENT_RUNNING_TIME = events.getProperty("CTFEventRunningTime", 1800);
		CTF_NPC_LOC_NAME = events.getProperty("CTFNpcLocName", "Giran Town");
		CTF_EVENT_PARTICIPATION_NPC_ID = events.getProperty("CTFEventParticipationNpcId", 0);
		CTF_EVENT_TEAM_1_HEADQUARTERS_ID = events.getProperty("CTFEventFirstTeamHeadquartersId", 0);
		CTF_EVENT_TEAM_2_HEADQUARTERS_ID = events.getProperty("CTFEventSecondTeamHeadquartersId", 0);
		CTF_EVENT_TEAM_1_FLAG = events.getProperty("CTFEventFirstTeamFlag", 0);
		CTF_EVENT_TEAM_2_FLAG = events.getProperty("CTFEventSecondTeamFlag", 0);
		CTF_EVENT_CAPTURE_SKILL = events.getProperty("CTFEventCaptureSkillId", 0);

		if (CTF_EVENT_PARTICIPATION_NPC_ID == 0)
		{
				CTF_EVENT_ENABLED = false;
				_log.warning("CTFEventEngine[Config.load()]: invalid config property -> CTFEventParticipationNpcId");
		}
		else
		{
			String[] ctfNpcCoords = events.getProperty("CTFEventParticipationNpcCoordinates", "0,0,0").split(",");
			if (ctfNpcCoords.length < 3)
			{
				CTF_EVENT_ENABLED = false;
				_log.warning("CTFEventEngine[Config.load()]: invalid config property -> CTFEventParticipationNpcCoordinates");
			}
			else
			{
				CTF_EVENT_PARTICIPATION_NPC_COORDINATES = new int[4];
				CTF_EVENT_PARTICIPATION_NPC_COORDINATES[0] = Integer.parseInt(ctfNpcCoords[0]);
				CTF_EVENT_PARTICIPATION_NPC_COORDINATES[1] = Integer.parseInt(ctfNpcCoords[1]);
				CTF_EVENT_PARTICIPATION_NPC_COORDINATES[2] = Integer.parseInt(ctfNpcCoords[2]);
				if (ctfNpcCoords.length == 4)
				{
					CTF_EVENT_PARTICIPATION_NPC_COORDINATES[3] = Integer.parseInt(ctfNpcCoords[3]);
				}
				
				CTF_EVENT_REWARDS = new ArrayList<>();
				CTF_DOORS_IDS_TO_OPEN = new ArrayList<>();
				CTF_DOORS_IDS_TO_CLOSE = new ArrayList<>();
				CTF_EVENT_TEAM_1_COORDINATES = new int[3];
				CTF_EVENT_TEAM_2_COORDINATES = new int[3];

				CTF_EVENT_REWARDS_TIE = new ArrayList<>();
				
				CTF_EVENT_MIN_PLAYERS_IN_TEAMS = events.getProperty("CTFEventMinPlayersInTeams", 1);
				CTF_EVENT_MAX_PLAYERS_IN_TEAMS = events.getProperty("CTFEventMaxPlayersInTeams", 20);
				CTF_EVENT_MIN_LVL = Byte.parseByte(events.getProperty("CTFEventMinPlayerLevel", "1"));
				CTF_EVENT_MAX_LVL = Byte.parseByte(events.getProperty("CTFEventMaxPlayerLevel", "80"));
				CTF_EVENT_RESPAWN_TELEPORT_DELAY = events.getProperty("CTFEventRespawnTeleportDelay", 20);
				CTF_EVENT_START_LEAVE_TELEPORT_DELAY = events.getProperty("CTFEventStartLeaveTeleportDelay", 20);
				CTF_EVENT_EFFECTS_REMOVAL = events.getProperty("CTFEventEffectsRemoval", 0);
				CTF_EVENT_TEAM_1_NAME = events.getProperty("CTFEventTeam1Name", "Team1");
				CTF_EVENT_TEAM_2_NAME = events.getProperty("CTFEventTeam2Name", "Team2");
				ctfNpcCoords = events.getProperty("CTFEventTeam1Coordinates", "0,0,0").split(",");
				if (ctfNpcCoords.length < 3)
				{
					CTF_EVENT_ENABLED = false;
					_log.warning("CTFEventEngine[Config.load()]: invalid config property -> CTFEventTeam1Coordinates");
				}
				else
				{
					CTF_EVENT_TEAM_1_COORDINATES[0] = Integer.parseInt(ctfNpcCoords[0]);
					CTF_EVENT_TEAM_1_COORDINATES[1] = Integer.parseInt(ctfNpcCoords[1]);
					CTF_EVENT_TEAM_1_COORDINATES[2] = Integer.parseInt(ctfNpcCoords[2]);
					ctfNpcCoords = events.getProperty("CTFEventTeam2Coordinates", "0,0,0").split(",");
					if (ctfNpcCoords.length < 3)
					{
						CTF_EVENT_ENABLED = false;
						_log.warning("CTFEventEngine[Config.load()]: invalid config property -> CTFEventTeam2Coordinates");
					}
					else
					{
						CTF_EVENT_TEAM_2_COORDINATES[0] = Integer.parseInt(ctfNpcCoords[0]);
						CTF_EVENT_TEAM_2_COORDINATES[1] = Integer.parseInt(ctfNpcCoords[1]);
						CTF_EVENT_TEAM_2_COORDINATES[2] = Integer.parseInt(ctfNpcCoords[2]);
						
						//QUARTEL 1
						
						if (CTF_EVENT_TEAM_1_HEADQUARTERS_ID == 0)
						{
								CTF_EVENT_ENABLED = false;
								_log.warning("CTFEventEngine[Config.load()]: invalid config property -> CTFEventFirstTeamHeadquartersId");
						}
						else
						{
							ctfNpcCoords = events.getProperty("CTFEventTeam1FlagCoordinates", "0,0,0").split(",");
							if (ctfNpcCoords.length < 3)
							{
								CTF_EVENT_ENABLED = false;
								_log.warning("CTFEventEngine[Config.load()]: invalid config property -> CTFEventTeam1FlagCoordinates");
							}
							else
							{
								CTF_EVENT_TEAM_1_FLAG_COORDINATES = new int[4];
								CTF_EVENT_TEAM_1_FLAG_COORDINATES[0] = Integer.parseInt(ctfNpcCoords[0]);
								CTF_EVENT_TEAM_1_FLAG_COORDINATES[1] = Integer.parseInt(ctfNpcCoords[1]);
								CTF_EVENT_TEAM_1_FLAG_COORDINATES[2] = Integer.parseInt(ctfNpcCoords[2]);
								if (ctfNpcCoords.length == 4)
								{
									CTF_EVENT_TEAM_1_FLAG_COORDINATES[3] = Integer.parseInt(ctfNpcCoords[3]);
								}
							}
							
							//QUARTEL 2
							
							if (CTF_EVENT_TEAM_2_HEADQUARTERS_ID == 0)
							{
									CTF_EVENT_ENABLED = false;
									_log.warning("CTFEventEngine[Config.load()]: invalid config property -> CTFEventSecondTeamHeadquartersId");
							}
							else
							{
								ctfNpcCoords = events.getProperty("CTFEventTeam2FlagCoordinates", "0,0,0").split(",");
								if (ctfNpcCoords.length < 3)
								{
									CTF_EVENT_ENABLED = false;
									_log.warning("CTFEventEngine[Config.load()]: invalid config property -> CTFEventTeam2FlagCoordinates");
								}
								else
								{
									CTF_EVENT_TEAM_2_FLAG_COORDINATES = new int[4];
									CTF_EVENT_TEAM_2_FLAG_COORDINATES[0] = Integer.parseInt(ctfNpcCoords[0]);
									CTF_EVENT_TEAM_2_FLAG_COORDINATES[1] = Integer.parseInt(ctfNpcCoords[1]);
									CTF_EVENT_TEAM_2_FLAG_COORDINATES[2] = Integer.parseInt(ctfNpcCoords[2]);
									if (ctfNpcCoords.length == 4)
									{
										CTF_EVENT_TEAM_2_FLAG_COORDINATES[3] = Integer.parseInt(ctfNpcCoords[3]);
									}
								}
							
								ctfNpcCoords = events.getProperty("CTFEventParticipationFee", "0,0").split(",");
								try
								{
									CTF_EVENT_PARTICIPATION_FEE[0] = Integer.parseInt(ctfNpcCoords[0]);
									CTF_EVENT_PARTICIPATION_FEE[1] = Integer.parseInt(ctfNpcCoords[1]);
								}
								catch (NumberFormatException nfe)
								{
									if (ctfNpcCoords.length > 0)
									{
										_log.warning("CTFEventEngine[Config.load()]: invalid config property -> CTFEventParticipationFee");
									}
								}
								ctfNpcCoords = events.getProperty("CTFEventReward", "57,100000").split(";");
								for (String reward : ctfNpcCoords)
								{
									String[] rewardSplit = reward.split(",");
									if (rewardSplit.length != 2)
									{
										_log.warning(StringUtil.concat("CTFEventEngine[Config.load()]: invalid config property -> CTFEventReward \"", reward, "\""));
									}
									else
									{
										try
										{
											CTF_EVENT_REWARDS.add(new int[]
											{
												Integer.parseInt(rewardSplit[0]),
												Integer.parseInt(rewardSplit[1])
											});
										}
										catch (NumberFormatException nfe)
										{
											if (!reward.isEmpty())
											{
												_log.warning(StringUtil.concat("CTFEventEngine[Config.load()]: invalid config property -> CTFEventReward \"", reward, "\""));
											}
										}
									}
								}
								
								ctfNpcCoords = events.getProperty("CTFEventRewardTie", "57,100000").split(";");
								for (String reward : ctfNpcCoords)
								{
									String[] rewardSplit = reward.split(",");
									if (rewardSplit.length != 2)
									{
										_log.warning(StringUtil.concat("CTFEventEngine[Config.load()]: invalid config property -> CTFEventRewardTie \"", reward, "\""));
									}
									else
									{
										try
										{
											CTF_EVENT_REWARDS_TIE.add(new int[]
											{
												Integer.parseInt(rewardSplit[0]),
												Integer.parseInt(rewardSplit[1])
											});
										}
										catch (NumberFormatException nfe)
										{
											if (!reward.isEmpty())
											{
												_log.warning(StringUtil.concat("CTFEventEngine[Config.load()]: invalid config property -> CTFEventRewardTie \"", reward, "\""));
											}
										}
									}
								}
								
								
								
								
								
							}
						}
							
                        CTF_EVENT_MULTIBOX_PROTECTION_ENABLE = events.getProperty("CTFEventMultiBoxEnable", false);
                        CTF_EVENT_NUMBER_BOX_REGISTER = events.getProperty("CTFEventNumberBoxRegister", 1);
						CTF_EVENT_TARGET_TEAM_MEMBERS_ALLOWED = events.getProperty("CTFEventTargetTeamMembersAllowed", true);
						CTF_EVENT_SCROLL_ALLOWED = events.getProperty("CTFEventScrollsAllowed", false);
						CTF_EVENT_POTIONS_ALLOWED = events.getProperty("CTFEventPotionsAllowed", false);
						CTF_EVENT_SUMMON_BY_ITEM_ALLOWED = events.getProperty("CTFEventSummonByItemAllowed", false);
						CTF_REWARD_TEAM_TIE = events.getProperty("CTFRewardTeamTie", false);
						ctfNpcCoords = events.getProperty("CTFDoorsToOpen", "").split(";");
						for (String door : ctfNpcCoords)
						{
							try
							{
								CTF_DOORS_IDS_TO_OPEN.add(Integer.parseInt(door));
							}
							catch (NumberFormatException nfe)
							{
								if (!door.isEmpty())
								{
									_log.warning(StringUtil.concat("CTFEventEngine[Config.load()]: invalid config property -> CTFDoorsToOpen \"", door, "\""));
								}
							}
						}
							
						ctfNpcCoords = events.getProperty("CTFDoorsToClose", "").split(";");
						for (String door : ctfNpcCoords)
						{
							try
							{
								CTF_DOORS_IDS_TO_CLOSE.add(Integer.parseInt(door));
							}
							catch (NumberFormatException nfe)
							{
								if (!door.isEmpty())
								{
									_log.warning(StringUtil.concat("CTFEventEngine[Config.load()]: invalid config property -> CTFDoorsToClose \"", door, "\""));
								}
							}
						}
							
						ctfNpcCoords = events.getProperty("CTFEventFighterBuffs", "").split(";");
						if (!ctfNpcCoords[0].isEmpty())
						{
							CTF_EVENT_FIGHTER_BUFFS = new HashMap<>(ctfNpcCoords.length);
							for (String skill : ctfNpcCoords)
							{
								String[] skillSplit = skill.split(",");
								if (skillSplit.length != 2)
								{
									_log.warning(StringUtil.concat("CTFEventEngine[Config.load()]: invalid config property -> CTFEventFighterBuffs \"", skill, "\""));
								}
								else
								{
									try
									{
										CTF_EVENT_FIGHTER_BUFFS.put(Integer.parseInt(skillSplit[0]), Integer.parseInt(skillSplit[1]));
									}
									catch (NumberFormatException nfe)
									{
										if (!skill.isEmpty())
										{
											_log.warning(StringUtil.concat("CTFEventEngine[Config.load()]: invalid config property -> CTFEventFighterBuffs \"", skill, "\""));
										}
									}
								}
							}
						}
							
						ctfNpcCoords = events.getProperty("CTFEventMageBuffs", "").split(";");
						if (!ctfNpcCoords[0].isEmpty())
						{
							CTF_EVENT_MAGE_BUFFS = new HashMap<>(ctfNpcCoords.length);
							for (String skill : ctfNpcCoords)
							{
								String[] skillSplit = skill.split(",");
								if (skillSplit.length != 2)
								{
									_log.warning(StringUtil.concat("CTFEventEngine[Config.load()]: invalid config property -> CTFEventMageBuffs \"", skill, "\""));
								}
								else
									{
									try
									{
										CTF_EVENT_MAGE_BUFFS.put(Integer.parseInt(skillSplit[0]), Integer.parseInt(skillSplit[1]));
									}
									catch (NumberFormatException nfe)
									{
										if (!skill.isEmpty())
										{
											_log.warning(StringUtil.concat("CTFEventEngine[Config.load()]: invalid config property -> CTFEventMageBuffs \"", skill, "\""));
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static ExProperties load(String filename)
	{
		return load(new File(filename));
	}
	
	public static ExProperties load(File file)
	{
		ExProperties result = new ExProperties();
		
		try
		{
			result.load(file);
		}
		catch (IOException e)
		{
			_log.warning("Error loading config : " + file.getName() + "!");
		}
		
		return result;
	}
}