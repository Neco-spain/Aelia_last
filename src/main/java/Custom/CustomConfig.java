package Custom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.commons.config.ExProperties;
import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.model.holder.IntIntHolder;

import Customs.VIP.RewardHolder;

/**
 * @author Icathialord
 *
 */
public class CustomConfig
{
	/** Custom Properties File */
	private static final String Customs = "./config/Customs/Customs.properties";


	public static int SAFE_ENCHANT;

	public static boolean ENABLE_FREEZE;
	public static int OPENING_MONTH;
	public static int OPENING_DAY;
	public static int OPENING_YEAR;
	public static int OPENING_HOUR;
	public static int OPENING_MINUTE;

	public static boolean ENTER_ZONE_BOSS;

	/** Pc bang points*/
	public static int PCB_INTERVAL;
	public static int PCB_MIN_LEVEL;
	public static int PCB_POINT_MIN;
	public static int PCB_POINT_MAX;
	public static int PCB_CHANCE_DUAL_POINT;
	public static int PCB_AFK_TIMER;

	public static boolean FLAG_RB;


	public static double MCRIT;
	public static int REP_ITEM_POINTS;

	public static int PVP_CHAT_SHOUT;
	public static int PVP_CHAT_TRADE;

	public static int CLAN_MEMBERS_LVL6;
	public static int CLAN_MEMBERS_LVL7;
	public static int CLAN_MEMBERS_LVL8;


	public static String FIGHTER_SET;
	public static List<Integer> LIST_FIGHTER_SET = new ArrayList<>();

	public static String FIGHTER_SET_BERS;
	public static List<Integer> LIST_FIGHTER_SET_BERS = new ArrayList<>();

	public static String MAGE_SET;
	public static List<Integer> LIST_MAGE_SET = new ArrayList<>();

	public static String MAGE_SET_BERS;
	public static List<Integer> LIST_MAGE_SET_BERS = new ArrayList<>();


	public static boolean ENABLE_CUSTOM_CB;
	public static String CB_DEFAULT;

	public static List<Integer> LIST_OLY_RESTRICTED_ITEMS;
	public static boolean ALT_OLY_GRADE_RESTRICTION_ITEMS;
	public static List<Integer> ALT_OLY_LIST_OF_GRADE_RESTRICTION_ITEMS;

	public static boolean ENABLE_SIEGE_HIT;

	public static boolean ONLINE_ENABLED;
	public static int FAKE_ONLINE;

	public static String REMOVE_FROM_DROP;
	public static List<Integer> LIST_REMOVE_FROM_DROP = new ArrayList<>();

	public static boolean BALANCER_ALLOW;

	public static String STARTING_TITLE;

	/* DRESS ME */
   	public static boolean ALLOW_DRESS_ME_SYSTEM;
	public static boolean DRESS_ME_NEED_PVPS;
	public static int PVPS_TO_USE_DRESS_ME;

	public static boolean RESTART_BY_TIME_OF_DAY;
	public static int RESTART_SECONDS;
	public static String[] RESTART_INTERVAL_BY_TIME_OF_DAY;

	
	//sub class
	public static boolean ALT_GAME_SUBCLASS_EVERYWHERE;
	public static int ALLOWED_SUBCLASS;
	public static int	CUSTOM_SUBCLASS_LVL;
	
    public static boolean ALLOW_CUSTOM_SPAWN_LOCATION;	  
    public static int[] CUSTOM_SPAWN_LOCATION = new int[3]; 
    
    
	/** PVP ChangeZone */
	public static boolean flagZone, deleteNpc, rewardPvp;
	public static int changeZoneTime, announceTimer, rewardId, rewardCount;
    
	
    public static int DEFAULT_PUNISH;
    public static int DEFAULT_PUNISH_PARAM;
    public static boolean MULTIBOX_PROTECTION_ENABLED;
    public static int MULTIBOX_PROTECTION_CLIENTS_PER_PC;
    public static int MULTIBOX_PROTECTION_PUNISH;
    
	//rb manager instance
	public static String RAID_INFO_IDS;
	public static List<Integer> RAID_INFO_IDS_LIST = new CopyOnWriteArrayList<>();
	
	//olympiad period,day
	public static boolean ALT_OLY_ALLOW_DUALBOX_OLY;
	
	public static int OlympiadDay = 1;   //by default 1 week
	public static int OlympiadEvery = 1; //by default Sunday
	public static int OlympiadHour = 00;
	public static int OlympiadMinute = 01;
	public static int OlympiadEnchant = 6;
	public static int ALT_OLY_MIN_MATCHES;
    
	
 	// --------------------------------------------------
	// Custom Siege Config
	// --------------------------------------------------
	
    public static boolean CustomSiegeDateEnable;
    public static boolean SiegeFrSatSund;
    
	/** Siege day of each castle */
	// Gludio
	public static int SIEGEDAYCASTLEGludio;
	// Dion
	public static int SIEGEDAYCASTLEDion;
	// Giran
	public static int SIEGEDAYCASTLEGiran;
	// Oren
	public static int SIEGEDAYCASTLEOren;
	// Aden
	public static int SIEGEDAYCASTLEAden;
	// Innadril/Heine
	public static int SIEGEDAYCASTLEInnadril;
	// Goddard
	public static int SIEGEDAYCASTLEGoddard;
	// Rune
	public static int SIEGEDAYCASTLERune;
	// Schuttgart
	public static int SIEGEDAYCASTLESchuttgart;
	/** Next siege time config (Retail 2)*/
	public static int NEXT_SIEGE_TIME;
	public static int HOUR_OF_SIEGE;
	// Gludio
	public static int SIEGEHOURCASTLEGludio;
	// Dion
	public static int SIEGEHOURCASTLEDion;
	// Giran
	public static int SIEGEHOURCASTLEGiran;
	// Oren
	public static int SIEGEHOURCASTLEOren;
	// Aden
	public static int SIEGEHOURCASTLEAden;
	// Innadril/Heine
	public static int SIEGEHOURCASTLEInnadril;
	// Goddard
	public static int SIEGEHOURCASTLEGoddard;
	// Rune
	public static int SIEGEHOURCASTLERune;
	// Schuttgart
	public static int SIEGEHOURCASTLESchuttgart;
	
	/** Baking System */
	public static int BANKING_SYSTEM_GOLDBARS;
	public static int BANKING_SYSTEM_ADENA;
	
	
	  /** Custom Coins */

	  public static boolean NOBLE_CUSTOM_ITEMS;
	  public static int NOBLE_CUSTOM_ITEM_ID;
	  
	 	public static int CUSTOM_REC_ITEM_ID;
	  
	 	public static int CUSTOM_CLAN_ITEM_ID;
	 	
	 	public static int CUSTOM_CLAN_FULL_ITEM_ID;
	
	 	
		
		//cancelation system
		public static boolean ALLOW_CUSTOM_CANCEL;
		public static int CUSTOM_CANCEL_SECONDS;
		
		
		public static int	STARTING_LVL;
	public static int STARTING_ADENA;


	public static int MAX_PATK_SPEED;
	       public static int MAX_MATK_SPEED;
	       

	   	public static boolean ANNOUNCE_KILL;
		public static String ANNOUNCE_PK_MSG;
		public static String ANNOUNCE_PVP_MSG;
		
		
		public static int RB_RETURN_SPAWN_RAD;


		 public static boolean ALT_GAME_VIEWNPC;
		 
		 public static boolean REWARD_PVP_KILL;
		 public static int PVP_REWARD_ID;
		 public static int PVP_REWARD_AMMOUNT;
		 
		 
		 
			/** Disable attack Npcs */
			public static boolean DISABLE_ATTACK_NPC_TYPE;
			public static String ALLOWED_NPC_TYPES;
			public static ArrayList<String> LIST_ALLOWED_NPC_TYPES = new ArrayList<>();

	/** Character Killing Monument settings */
			public static boolean CKM_ENABLED;
			public static long CKM_CYCLE_LENGTH;
			public static String CKM_PVP_NPC_TITLE;
			public static int CKM_PVP_NPC_TITLE_COLOR;
			public static int CKM_PVP_NPC_NAME_COLOR;
			public static String CKM_PK_NPC_TITLE;
			public static int CKM_PK_NPC_TITLE_COLOR;
			public static int CKM_PK_NPC_NAME_COLOR;
			public static IntIntHolder[] MONUMENT_EVENT_REWARDS;


			
			public static boolean DONT_DESTROY_SS;
			public static boolean DONT_DESTROY_ARROWS;
			
			public static int calcCrirMod300;
			public static int calcCrirMod400;
			public static double calcCrirMod500;
			
			
			public static int MAX_CRITICAL_Continuously;
			
			
			public static String SERVER_NAME;
			public static String ON_ENTER_MESSAGE;
			

			public static String TEAM_1_COLOR;
			public static String TEAM_2_COLOR;
			
	public static void loadCustomPrp(){
		final ExProperties custom = initProperties(Customs);



		SAFE_ENCHANT = custom.getProperty("safeEnchant", 3);

		ENABLE_FREEZE = custom.getProperty("enableFreeze", true);
		OPENING_DAY = custom.getProperty("openingDay", 1);
		OPENING_MONTH = custom.getProperty("openingMonth", 1);
		OPENING_YEAR = custom.getProperty("openingYear", 124);
		OPENING_HOUR = custom.getProperty("openingHour", 20);
		OPENING_MINUTE = custom.getProperty("openingMinute", 20);

		MCRIT = custom.getProperty("mCritDamage", 2.1);

		REP_ITEM_POINTS = custom.getProperty("repItemPoints", 30);

		PVP_CHAT_SHOUT = custom.getProperty("pvpToSpeakOnShoutChat", 30);
		PVP_CHAT_TRADE = custom.getProperty("pvpToSpeakOnTradeChat", 10);

		CLAN_MEMBERS_LVL6 = custom.getProperty("clanMembersForLevel6", 30);
		CLAN_MEMBERS_LVL7 = custom.getProperty("clanMembersForLevel7", 50);
		CLAN_MEMBERS_LVL8 =	custom.getProperty("clanMembersForLevel8", 60);

		ENTER_ZONE_BOSS = custom.getProperty("DirectTeleportToBossRoom", true);

		PCB_INTERVAL = custom.getProperty("PcBangPointTime", 0);
		PCB_MIN_LEVEL = custom.getProperty("PcBangPointMinLevel", 20);
		PCB_POINT_MIN = custom.getProperty("PcBangPointMinCount", 1);
		PCB_POINT_MAX = custom.getProperty("PcBangPointMaxCount", 5);
		PCB_CHANCE_DUAL_POINT = custom.getProperty("PcBangPointDualChance", 100);
		PCB_AFK_TIMER = custom.getProperty("PcBangTimerAFK", 1);

		FLAG_RB = custom.getProperty("FlagRbZones", false);


		FIGHTER_SET = custom.getProperty("FighterBuffSet", "272");
		LIST_FIGHTER_SET = new ArrayList<>();
		for (String listid : FIGHTER_SET.split(","))
		{
			LIST_FIGHTER_SET.add(Integer.parseInt(listid));
		}
		FIGHTER_SET_BERS = custom.getProperty("FighterBuffSetBers", "272");
		LIST_FIGHTER_SET_BERS = new ArrayList<>();
		for (String listid : FIGHTER_SET_BERS.split(","))
		{
			LIST_FIGHTER_SET_BERS.add(Integer.parseInt(listid));
		}

		MAGE_SET = custom.getProperty("MageBuffSet", "272");
		LIST_MAGE_SET = new ArrayList<>();
		for (String listid : MAGE_SET.split(","))
		{
			LIST_MAGE_SET.add(Integer.parseInt(listid));
		}
		MAGE_SET_BERS = custom.getProperty("MageBuffSetBers", "272");
		LIST_MAGE_SET_BERS = new ArrayList<>();
		for (String listid : MAGE_SET_BERS.split(","))
		{
			LIST_MAGE_SET_BERS.add(Integer.parseInt(listid));
		}

		ENABLE_CUSTOM_CB = custom.getProperty("enableCustomCommunityBoard", false);
		CB_DEFAULT = custom.getProperty("CBDefault", "_cbshome");

		ENABLE_SIEGE_HIT = custom.getProperty("hitWithoutControlInSiege", false);

		LIST_OLY_RESTRICTED_ITEMS = new ArrayList<>();
		for (String items : custom.getProperty("AltOlyRestrictedItems", "").split(","))
			LIST_OLY_RESTRICTED_ITEMS.add(Integer.parseInt(items));


		ALT_OLY_GRADE_RESTRICTION_ITEMS = custom.getProperty("AltOlyGradeRestrictionItems", false);
		ALT_OLY_LIST_OF_GRADE_RESTRICTION_ITEMS = new ArrayList<>();
		for (String items : custom.getProperty("AltOlyGradeRestrictionItemsList", "").split(","))
			ALT_OLY_LIST_OF_GRADE_RESTRICTION_ITEMS.add(Integer.parseInt(items));


		ONLINE_ENABLED = custom.getProperty("enableOnlineCommand", false);
		FAKE_ONLINE = custom.getProperty("fakeOnline", 1);

		REMOVE_FROM_DROP = custom.getProperty("skipItemFromDroplist", "1341,1342,1343,1344,1345,1047,1048,1049,1050,1051");
		LIST_REMOVE_FROM_DROP = new ArrayList<>();
		for (String listid : REMOVE_FROM_DROP.split(","))
		{
			LIST_REMOVE_FROM_DROP.add(Integer.parseInt(listid));
		}

		RESTART_BY_TIME_OF_DAY = Boolean.parseBoolean(custom.getProperty("EnableRestartSystem", "false"));
		RESTART_SECONDS = Integer.parseInt(custom.getProperty("RestartSeconds", "360"));
		RESTART_INTERVAL_BY_TIME_OF_DAY = custom.getProperty("RestartByTimeOfDay", "20:00").split(",");

		TEAM_1_COLOR = custom.getProperty("Team1Color", "88AA88");
		TEAM_2_COLOR = custom.getProperty("Team2Color", "88AA88");
		
		BALANCER_ALLOW = custom.getProperty("BalancerAllow", true);

	    ALLOW_DRESS_ME_SYSTEM = custom.getProperty("AllowDressMeSystem", false);
	    DRESS_ME_NEED_PVPS = custom.getProperty("DressMeNeedPvp", false);
	    PVPS_TO_USE_DRESS_ME = custom.getProperty("DressMePvPAmount", 0);

		// Heavy
		String temp = custom.getProperty("HeavyChests", "");
		String[] temp2 = temp.split(";");

		//sub class
		ALT_GAME_SUBCLASS_EVERYWHERE = custom.getProperty("AltSubclassEverywhere", false);
		ALLOWED_SUBCLASS = Integer.parseInt(custom.getProperty("AllowedSubclass", "3"));
		CUSTOM_SUBCLASS_LVL = Integer.parseInt(custom.getProperty("CustomSubclassLvl", "40"));
		
		STARTING_LVL = Integer.parseInt(custom.getProperty("StartingLvl", "80"));
		STARTING_TITLE = custom.getProperty("StartingTitle", "");
		STARTING_ADENA = Integer.parseInt(custom.getProperty("StartingAdena", "100000000"));

		//starting loc
        ALLOW_CUSTOM_SPAWN_LOCATION = Boolean.parseBoolean(custom.getProperty("AllowCustomSpawnLocation", "false"));
        String custom_spawn_location = custom.getProperty("CustomSpawnLocation", "113852,-108766,-851");
        String custom_spawn_location_splitted[] = custom_spawn_location.split(",");
        CUSTOM_SPAWN_LOCATION[0] = Integer.parseInt(custom_spawn_location_splitted[0]);
        CUSTOM_SPAWN_LOCATION[1] = Integer.parseInt(custom_spawn_location_splitted[1]);
        CUSTOM_SPAWN_LOCATION[2] = Integer.parseInt(custom_spawn_location_splitted[2]);

        
        CustomConfig.DEFAULT_PUNISH_PARAM = custom.getProperty("DefaultPunishParam", 0);
        CustomConfig.MULTIBOX_PROTECTION_ENABLED = custom.getProperty("MultiboxProtectionEnabled", false);
        CustomConfig.MULTIBOX_PROTECTION_CLIENTS_PER_PC = custom.getProperty("ClientsPerPc", 2);
        CustomConfig.MULTIBOX_PROTECTION_PUNISH = custom.getProperty("MultiboxPunish", 2);
        
		RAID_INFO_IDS = custom.getProperty("RaidInfoIDs", "");
		RAID_INFO_IDS_LIST = new CopyOnWriteArrayList<>();
		for (final String id : RAID_INFO_IDS.split(","))
		{
			RAID_INFO_IDS_LIST.add(Integer.parseInt(id));
		}
			
		
		ALT_OLY_ALLOW_DUALBOX_OLY = custom.getProperty("AltOlyAllowSameIPInOly", false);
		OlympiadEvery = Integer.parseInt(custom.getProperty("olympiadEvery", "1"));
		OlympiadDay = Integer.parseInt(custom.getProperty("olympiadDay", "1"));
		OlympiadHour = Integer.parseInt(custom.getProperty("olympiadHour", "00"));
		OlympiadMinute = Integer.parseInt(custom.getProperty("olympiadMinute", "01"));
		OlympiadEnchant = Integer.parseInt(custom.getProperty("olympiadEnchant","0"));
		ALT_OLY_MIN_MATCHES = custom.getProperty("ShowRankingAfterXcompetitionsDone", 1);
		
		
		/** Siege day of each castle */
		
		CustomSiegeDateEnable = custom.getProperty("CustomSiegeDateEnable", false);
		SiegeFrSatSund = custom.getProperty("SiegeFrSatSund", false);
		 	  		 	  
		// Gludio
		SIEGEDAYCASTLEGludio = Integer.parseInt(custom.getProperty("SiegeGludio", "7"));
		// Dion			
		SIEGEDAYCASTLEDion = Integer.parseInt(custom.getProperty("SiegeDion", "7"));
		// Giran
		SIEGEDAYCASTLEGiran = Integer.parseInt(custom.getProperty("SiegeGiran", "7"));
		// Oren
		SIEGEDAYCASTLEOren = Integer.parseInt(custom.getProperty("SiegeOren", "7"));
		// Aden
		SIEGEDAYCASTLEAden = Integer.parseInt(custom.getProperty("SiegeAden", "1"));
		// Innadril/Heine
		SIEGEDAYCASTLEInnadril = Integer.parseInt(custom.getProperty("SiegeInnadril", "1"));
		// Goddard
		SIEGEDAYCASTLEGoddard = Integer.parseInt(custom.getProperty("SiegeGoddard", "1"));
		// Rune
		SIEGEDAYCASTLERune = Integer.parseInt(custom.getProperty("SiegeRune", "1"));
		// Schuttgart
		SIEGEDAYCASTLESchuttgart = Integer.parseInt(custom.getProperty("SiegeSchuttgart", "1"));
		/** Next siege time config (Retail 2)*/
		NEXT_SIEGE_TIME = Integer.parseInt(custom.getProperty("NextSiegeTime", "2"));
		/** Hour of the siege will start*/
		HOUR_OF_SIEGE = Integer.parseInt(custom.getProperty("HourOfSiege", "18"));
		
		/** Siege hour of each castle */
		// Gludio
		SIEGEHOURCASTLEGludio = Integer.parseInt(custom.getProperty("SiegeGludioHour", "18"));
		// Dion			
		SIEGEHOURCASTLEDion = Integer.parseInt(custom.getProperty("SiegeDionHour", "18"));
		// Giran
		SIEGEHOURCASTLEGiran = Integer.parseInt(custom.getProperty("SiegeGiranHour", "18"));
		// Oren
		SIEGEHOURCASTLEOren = Integer.parseInt(custom.getProperty("SiegeOrenHour", "18"));
		// Aden
		SIEGEHOURCASTLEAden = Integer.parseInt(custom.getProperty("SiegeAdenHour", "18"));
		// Innadril/Heine
		SIEGEHOURCASTLEInnadril = Integer.parseInt(custom.getProperty("SiegeInnadrilHour", "18"));
		// Goddard
		SIEGEHOURCASTLEGoddard = Integer.parseInt(custom.getProperty("SiegeGoddardHour", "18"));
		// Rune
		SIEGEHOURCASTLERune = Integer.parseInt(custom.getProperty("SiegeRuneHour", "18"));
		// Schuttgart
		SIEGEHOURCASTLESchuttgart = Integer.parseInt(custom.getProperty("SiegeSchuttgartHour", "18"));

		
		
		BANKING_SYSTEM_GOLDBARS = Integer.parseInt(custom.getProperty("BankingGoldbarCount", "1"));
		BANKING_SYSTEM_ADENA = Integer.parseInt(custom.getProperty("BankingAdenaCount", "500000000"));
		
        
		
		NOBLE_CUSTOM_ITEMS = Boolean.parseBoolean(custom.getProperty("EnableNobleCustomItem", "true"));
		NOBLE_CUSTOM_ITEM_ID = Integer.parseInt(custom.getProperty("NobleCustomItemId", "3481"));

		CUSTOM_REC_ITEM_ID = Integer.parseInt(custom.getProperty("CustomRecItemId", "3481"));
		
		CUSTOM_CLAN_ITEM_ID = Integer.parseInt(custom.getProperty("CustomClanItemId", "3481"));
		
		CUSTOM_CLAN_FULL_ITEM_ID = Integer.parseInt(custom.getProperty("CustomClanItemId", "3481"));
		
        
        //cancelation system
			ALLOW_CUSTOM_CANCEL = custom.getProperty("AllowCustomCancelTask", true);
			CUSTOM_CANCEL_SECONDS = custom.getProperty("CustomCancelSeconds", 5);
			
			
			
		     MAX_PATK_SPEED = custom.getProperty("MaxPAtkSpeed", 1400);
	 	     MAX_MATK_SPEED = custom.getProperty("MaxMAtkSpeed", 1999);
	 	     
	 		ANNOUNCE_KILL = custom.getProperty("AnnounceKill", false);
	 		ANNOUNCE_PVP_MSG = custom.getProperty("AnnouncePvpMsg", "$killer has defeated $target");
	 		ANNOUNCE_PK_MSG = custom.getProperty("AnnouncePkMsg", "$killer has slaughtered $target");
	 		  
	 		
	 		RB_RETURN_SPAWN_RAD = custom.getProperty("RaidReturbToSpawnRad", 300);
	 		
	 		
	 		ALT_GAME_VIEWNPC = custom.getProperty("enableShiftToRb", false);
	 		
	 		
	 		
			 
			 REWARD_PVP_KILL= custom.getProperty("EnablePvpReward", false);
			 PVP_REWARD_ID= custom.getProperty("pvpRewardId", 57);
			 PVP_REWARD_AMMOUNT= custom.getProperty("pvpRewardCount", 1);
			 
			 
			 
			 DISABLE_ATTACK_NPC_TYPE = custom.getProperty("DisableAttackToNpcs", false);
				ALLOWED_NPC_TYPES = custom.getProperty("AllowedNPCTypes");
				LIST_ALLOWED_NPC_TYPES = new ArrayList<>();
				for (String npc_type : ALLOWED_NPC_TYPES.split(","))
				{
					LIST_ALLOWED_NPC_TYPES.add(npc_type);
				}


		CKM_ENABLED = custom.getProperty("CKMEnabled", false);
				CKM_CYCLE_LENGTH = custom.getProperty("CKMCycleLength", 86400000);
				CKM_PVP_NPC_TITLE = custom.getProperty("CKMPvPNpcTitle", "%kills% PvPs in last 24h");
				CKM_PVP_NPC_TITLE_COLOR = Integer.decode("0x" + custom.getProperty("CKMPvPNpcTitleColor", "00CCFF"));
				CKM_PVP_NPC_NAME_COLOR = Integer.decode("0x"+ custom.getProperty("CKMPvPNpcNameColor", "FFFFFF"));
				CKM_PK_NPC_TITLE = custom.getProperty("CKMPKNpcTitle", "%kills% PKs in last 24h");
				CKM_PK_NPC_TITLE_COLOR = Integer.decode("0x" + custom.getProperty("CKMPKNpcTitleColor", "00CCFF"));
				CKM_PK_NPC_NAME_COLOR = Integer.decode("0x" + custom.getProperty("CKMPKNpcNameColor", "FFFFFF"));
				MONUMENT_EVENT_REWARDS = custom.parseIntIntList("CKMReward", "1-268");
				


				// Destroy arrows
				DONT_DESTROY_ARROWS = Boolean.parseBoolean(custom.getProperty("DontDestroyArrows", "false"));
				
				
				calcCrirMod300 = custom.getProperty("CriticalModifier300", 1000);
				calcCrirMod400 = custom.getProperty("CriticalModifier400", 1000);
				calcCrirMod500 = custom.getProperty("CriticalModifier500", 1000);
				
				MAX_CRITICAL_Continuously = custom.getProperty("MaxCriticalHitsContinuously", 4);
				
				
				
				
	}
	
	
	

 	
 	

	public static final String PARTY_FILE = "./config/Customs/PartyFarm.properties";
	/** Party Farm */
	//party teleporter
	public static int NPC_ID_PT_TELEPORTER;
	public static int[] PARTY_TELE_LOCATION = new int[3];
	public static int NPC_PT_ZONEID;
	public static int NPC_PT_MINPT_MEMBERS;
	public static int NPC_PT_ITEMCONSUME_ID;
	public static int NPC_PT_ITEMCOMSUME_QT;
	public static boolean NPC_PT_SHOWINSIDE_PLAYERS;
	public static boolean NPC_PT_SHOWINSIDE_PARTIES;
	
	public static List<int[]> PARTY_TELE_LOCATION1 = new ArrayList<>();
	public int pt_tele_ids;
	
 	public static boolean PF_EVENT_ENABLED;
	public static int PF_FIRST_EVENT_INTERVAL;
 	public static int PF_EVENT_INTERVAL;
 	
 	
	public static boolean PARTY_MESSAGE_ENABLED;
	public static long NPC_SERVER_DELAY;
	public static boolean PARTY_FARM_BY_TIME_OF_DAY;
	public static boolean START_PARTY;
	public static int EVENT_BEST_FARM_TIME;
	public static String[] EVENT_BEST_FARM_INTERVAL_BY_TIME_OF_DAY;
	public static int PARTY_FARM_MONSTER_DALAY;
	public static String PARTY_FARM_MESSAGE_TEXT;
	public static int PARTY_FARM_MESSAGE_TIME;
	public static int monsterId;
	public static int MONSTER_LOCS_COUNT;
	public static int[][] MONSTER_LOCS;
	/** Party Drop Config */
	public static int CHANCE_PARTY_DROP;
	public static String NPC_LIST;
	public static int[] NPC_LIST_SET;
	public static Map<Integer, Integer> PARTY_DROP_REWARDS = new HashMap<>();
	
	
	public static int MINIMUM_PLAYERS_IN_PARTY_ZONE;
	public static boolean PARTY_FARM_HWID_CHECK;
	
	public static boolean PARTY_FARM_PVP_CHECK;
	public static int PARTY_FARM_PVPS;
	
	
	/**
	 * Loads Ptfarm settings.
	 */
	private static final void loadPtfarm()
	{
		final ExProperties BestFarm = initProperties(CustomConfig.PARTY_FILE);
		
	
        String[] propertySplit = BestFarm.getProperty("PartyZoneSpawnLocations", "123,123,123").split(";");
        for (String x : propertySplit)
        {
            String[] xyzSplit = x.split(",");
           
            if (xyzSplit.length != 3)
                System.out.println("spots[Config.load()]: invalid config property -> spots \"" + x + "\"");
            else
            {
                try
                {
                	PARTY_TELE_LOCATION1.add(new int[]
                    {
                        Integer.valueOf(xyzSplit[0]),
                        Integer.valueOf(xyzSplit[1]),
                        Integer.valueOf(xyzSplit[2])
                    });
                }
                catch (NumberFormatException nfe)
                {
                    if (!x.equals(""))
                        System.out.println("spots[Config.load()]: invalid config property -> spots \"" + x + "\" Line:" + nfe.getStackTrace()[0].getLineNumber());
                }
            }
        }  
		
	       // Party Teleproter 
	    		NPC_ID_PT_TELEPORTER = BestFarm.getProperty("NpcPtTeleporterId", 36614);
	    		
	    		PARTY_FARM_HWID_CHECK = BestFarm.getProperty("checkHwid", true);
	    		
	    		
	    		PARTY_FARM_PVP_CHECK = BestFarm.getProperty("checkPvp", true);
	    		
	    		PARTY_FARM_PVPS= BestFarm.getProperty("numberofPvptoJoin", 1);
	    		
	    		String[] propertyPtLoc = BestFarm.getProperty("PartyTeleLocation", "0,0,0").split(",");
	    		if (propertyPtLoc.length < 3)
	    		{
	    			System.out.println("Error : config/customs/npcs_manager.properties \"PartyTeleLocation\" coord locations");
	    		}
	    		else
	    		{
	    			PARTY_TELE_LOCATION[0] = Integer.parseInt(propertyPtLoc[0]);
	    			PARTY_TELE_LOCATION[1] = Integer.parseInt(propertyPtLoc[1]);
	    			PARTY_TELE_LOCATION[2] = Integer.parseInt(propertyPtLoc[2]);
	    		}
	    		NPC_PT_ZONEID = BestFarm.getProperty("NpcPtZoneID", 155);
	    		NPC_PT_MINPT_MEMBERS = BestFarm.getProperty("NpcPtMinPartyMembers", 2);
	    		NPC_PT_ITEMCONSUME_ID = BestFarm.getProperty("NpcPtConsumeItemId", 57);
	    		NPC_PT_ITEMCOMSUME_QT = BestFarm.getProperty("NpcPtConsumeItemQt", 100);
	    		NPC_PT_SHOWINSIDE_PLAYERS = BestFarm.getProperty("NpcPtShowInsidePlayers", true);
	    		NPC_PT_SHOWINSIDE_PARTIES = BestFarm.getProperty("NpcPtShowInsideParties", true);
	            
	    		
	    		
		PF_EVENT_ENABLED = BestFarm.getProperty("PFEventEnabled", false);
		PF_FIRST_EVENT_INTERVAL = BestFarm.getProperty("PFfirstEventInterval", 18000);
		PF_EVENT_INTERVAL = BestFarm.getProperty("PFEventInterval", 18000);
		
		NPC_LIST = BestFarm.getProperty("NpcListPartyDrop", "10506,10507");
		PARTY_FARM_MONSTER_DALAY = Integer.parseInt(BestFarm.getProperty("MonsterDelay", "10"));

		EVENT_BEST_FARM_TIME = Integer.parseInt(BestFarm.getProperty("EventBestFarmTime", "1"));
		EVENT_BEST_FARM_INTERVAL_BY_TIME_OF_DAY = BestFarm.getProperty("BestFarmStartTime", "20:00").split(",");

		String[] monsterLocs2 = BestFarm.getProperty("MonsterLoc", "").split(";");
		String[] locSplit3 = null;
		
		monsterId = Integer.parseInt(BestFarm.getProperty("MonsterId", "1"));
		
		MONSTER_LOCS_COUNT = monsterLocs2.length;
		MONSTER_LOCS = new int[MONSTER_LOCS_COUNT][3];
		int g;
		for (int e = 0; e < MONSTER_LOCS_COUNT; e++)
		{
			locSplit3 = monsterLocs2[e].split(",");
			for (g = 0; g < 3; g++)
			{
				MONSTER_LOCS[e][g] = Integer.parseInt(locSplit3[g].trim());
			}
		}
		
		
		
		MINIMUM_PLAYERS_IN_PARTY_ZONE = BestFarm.getProperty("MinPlayersInPartyZone", 2);
		
	}
	
	
	//Announce Top Pvp,Pk
	
	private static final String ANNOUNCE_FILE = "./config/Customs/Announce.properties";
	
	public static boolean ANNOUNCE_TOP_PVP;
	public static boolean ANNOUNCE_TOP_PK;
 /** Hero and castle lord announce! */
    public static boolean ANNOUNCE_HERO_ON_ENTER;
    public static boolean ANNOUNCE_CASTLE_LORDS;
	

	
	
	public static void loadAnnounce(){
		final ExProperties CustomConfig = initProperties(ANNOUNCE_FILE);
			ANNOUNCE_TOP_PVP = CustomConfig.getProperty("AnnounceTopPvp", false);
			ANNOUNCE_TOP_PK = CustomConfig.getProperty("AnnounceTopPk", false);
            ANNOUNCE_HERO_ON_ENTER = Boolean.parseBoolean(CustomConfig.getProperty("AnnounceHeroOnEnter", "false"));
            ANNOUNCE_CASTLE_LORDS = Boolean.parseBoolean(CustomConfig.getProperty("AnnounceCastleLords", "false"));
            
	}
	
	
	/** anti farm system */
	
	private static final String ANTI_FARM = "./config/Customs/antifarm.properties";
	
	public static boolean ANTI_FARM_ENABLED;
 	public static boolean ANTI_FARM_CLAN_ALLY_ENABLED;
 	public static boolean ANTI_FARM_LVL_DIFF_ENABLED;
 	public static int ANTI_FARM_MAX_LVL_DIFF;
 	public static boolean ANTI_FARM_PDEF_DIFF_ENABLED;
 	public static int ANTI_FARM_MAX_PDEF_DIFF;
 	public static boolean ANTI_FARM_PATK_DIFF_ENABLED;
 	public static int ANTI_FARM_MAX_PATK_DIFF;
 	public static boolean ANTI_FARM_PARTY_ENABLED;
 	public static boolean ANTI_FARM_IP_ENABLED; 
 	public static boolean ANTI_FARM_HWID_ENABLED; 
 	
	public static void loadAntiFarm(){
		final ExProperties antifarm = initProperties(ANTI_FARM);
		
        ANTI_FARM_ENABLED = Boolean.parseBoolean(antifarm.getProperty("AntiFarmEnabled", "False"));
        ANTI_FARM_CLAN_ALLY_ENABLED = Boolean.parseBoolean(antifarm.getProperty("AntiFarmClanAlly", "False"));
        ANTI_FARM_LVL_DIFF_ENABLED = Boolean.parseBoolean(antifarm.getProperty("AntiFarmLvlDiff", "False"));
		ANTI_FARM_MAX_LVL_DIFF = Integer.parseInt(antifarm.getProperty("AntiFarmMaxLvlDiff", "40"));
		ANTI_FARM_PDEF_DIFF_ENABLED = Boolean.parseBoolean(antifarm.getProperty("AntiFarmPdefDiff", "False"));
		ANTI_FARM_MAX_PDEF_DIFF = Integer.parseInt(antifarm.getProperty("AntiFarmMaxPdefDiff", "300"));
		ANTI_FARM_PATK_DIFF_ENABLED = Boolean.parseBoolean(antifarm.getProperty("AntiFarmPatkDiff", "False"));
		ANTI_FARM_MAX_PATK_DIFF = Integer.parseInt(antifarm.getProperty("AntiFarmMaxPatkDiff", "300"));
		ANTI_FARM_PARTY_ENABLED = Boolean.parseBoolean(antifarm.getProperty("AntiFarmParty", "False"));
		ANTI_FARM_IP_ENABLED = Boolean.parseBoolean(antifarm.getProperty("AntiFarmIP", "False"));
		ANTI_FARM_HWID_ENABLED = Boolean.parseBoolean(antifarm.getProperty("AntiFarmHWID", "False"));
		
	}
	
	
	
	/** Vote Settings File */
	private static final String VOTE = "./config/Customs/Vote.properties";


	public static int VOTE_MANAGER_REWARD_ID;
	public static int VOTE_MANAGER_REWARD_AMOUNT;


	public static int TOPZONE_SERVER_ID;
	public static int TOPZONE_VOTES_DIFFERENCE;
	public static int TOPZONE_DUALBOXES_ALLOWED;
	public static boolean ALLOW_TOPZONE_GAME_SERVER_REPORT;
	public static int TOPZONE_REWARD_CHECK_TIME;
	public static Map<Integer, Integer> TOPZONE_REWARD = new HashMap<>();
	

	public static String TOPZONE_SERVER_API_KEY;
	public static String HOPZONE_SERVER_API_KEY;

	public static String TOPCO_SERVER_API_KEY;
	public static String L2VOTES_SERVER_API_KEY;
	public static String NETWORK_SERVER_API_KEY;
	public static String NETWORK_SERVER_NAME;
	public static String BRASIL_SERVER_API_KEY;
	public static String L2SERVERS_SERVER_API_KEY;



  public static boolean ALLOW_NETWORK_VOTE_REWARD;
  public static String NETWORK_SERVER_LINK;
  public static int NETWORK_VOTES_DIFFERENCE;
  public static int NETWORK_REWARD_CHECK_TIME;
  public static Map<Integer, Integer> NETWORK_REWARD = new HashMap<>();
  public static int NETWORK_DUALBOXES_ALLOWED;
  public static boolean ALLOW_NETWORK_GAME_SERVER_REPORT;
  public static boolean ALLOW_TOPZONE_VOTE_REWARD;
  public static String TOPZONE_SERVER_LINK;


  public static boolean ALLOW_HOPZONE_VOTE_REWARD;
  public static String HOPZONE_SERVER_LINK;
  public static int HOPZONE_VOTES_DIFFERENCE;
  public static int HOPZONE_REWARD_CHECK_TIME;
  public static Map<Integer, Integer> HOPZONE_REWARD = new HashMap<>();
  public static int HOPZONE_DUALBOXES_ALLOWED;
  public static boolean ALLOW_HOPZONE_GAME_SERVER_REPORT;
  
  
  
  public static boolean ALLOW_TOPCO_VOTE_REWARD;
  public static String TOPCO_SERVER_LINK;
  public static int TOPCO_VOTES_DIFFERENCE;
  public static int TOPCO_REWARD_CHECK_TIME;
  public static Map<Integer, Integer> TOPCO_REWARD = new HashMap<>();
  public static int TOPCO_DUALBOXES_ALLOWED;
  public static boolean ALLOW_TOPCO_GAME_SERVER_REPORT;
  
  
  public static boolean ALLOW_MMO_VOTE_REWARD;
  public static int MMO_VOTES_DIFFERENCE;
  public static int MMO_REWARD_CHECK_TIME;
  public static Map<Integer, Integer> MMO_REWARD = new HashMap<>();
  public static int MMO_DUALBOXES_ALLOWED;
  public static boolean ALLOW_MMO_GAME_SERVER_REPORT;
  public static String MMOTOP_API_KEY;
  
  
  public static String VOTE_TOPZONE_API_URL;
  public static String VOTE_TOP_ZONE_TOKEN;
  
	public static void loadVotes(){
		final ExProperties Customs = initProperties(VOTE);


		VOTE_MANAGER_REWARD_ID = Customs.getProperty("VoteManagerRewardId", 0);
		VOTE_MANAGER_REWARD_AMOUNT = Customs.getProperty("VoteManagerRewardCount", 0);

		TOPZONE_SERVER_ID = Customs.getProperty("TopzoneServerID", 0);
		TOPZONE_SERVER_API_KEY = Customs.getProperty("TopzoneServerAPI", "");
		HOPZONE_SERVER_API_KEY = Customs.getProperty("HopzoneServerAPI", "");
		NETWORK_SERVER_API_KEY = Customs.getProperty("NetworkAPI", "");
		NETWORK_SERVER_NAME = Customs.getProperty("NetworkName", ""); //for vote manager
		TOPCO_SERVER_API_KEY = Customs.getProperty("L2TopcoAPI", "");
		L2VOTES_SERVER_API_KEY = Customs.getProperty("L2VotesAPI", "");
		BRASIL_SERVER_API_KEY = Customs.getProperty("L2BrasilAPI", "");


	       ALLOW_TOPCO_VOTE_REWARD = Customs.getProperty("AllowTOPCOVoteReward", false);
	       TOPCO_SERVER_LINK = Customs.getProperty("TOPCOServerLink", "");
	       TOPCO_VOTES_DIFFERENCE = Customs.getProperty("TOPCOVotesDifference", 5);
	       TOPCO_REWARD_CHECK_TIME = Customs.getProperty("TOPCORewardCheckTime", 5);
	        String TOPCO_SMALL_REWARD_VALUE = Customs.getProperty("TOPCOReward", "57,100000000;");
	        String[] TOPCO_small_reward_splitted_1 =TOPCO_SMALL_REWARD_VALUE.split(";");
	        for (String i : TOPCO_small_reward_splitted_1)
	        {
	               String[] TOPCO_small_reward_splitted_2 = i.split(",");
	               TOPCO_REWARD.put(Integer.parseInt(TOPCO_small_reward_splitted_2[0]), Integer.parseInt(TOPCO_small_reward_splitted_2[1]));
	        }
	        TOPCO_DUALBOXES_ALLOWED = Customs.getProperty("TOPCODualboxesAllowed", 1);
	        
	        

			   MMOTOP_API_KEY = Customs.getProperty("MMOApiKey", "0123456789abcdef");
		       ALLOW_MMO_VOTE_REWARD = Customs.getProperty("AllowMMOVoteReward", false);
		       MMO_VOTES_DIFFERENCE = Customs.getProperty("MMOVotesDifference", 5);
		       MMO_REWARD_CHECK_TIME = Customs.getProperty("MMORewardCheckTime", 5);
		        String MMO_SMALL_REWARD_VALUE = Customs.getProperty("MMOReward", "57,100000000;");
		        String[] MMO_small_reward_splitted_1 =MMO_SMALL_REWARD_VALUE.split(";");
		        for (String i : MMO_small_reward_splitted_1)
		        {
		               String[] MMO_small_reward_splitted_2 = i.split(",");
		               MMO_REWARD.put(Integer.parseInt(MMO_small_reward_splitted_2[0]), Integer.parseInt(MMO_small_reward_splitted_2[1]));
		        }
		        MMO_DUALBOXES_ALLOWED = Customs.getProperty("MMODualboxesAllowed", 1);
		        
	        
	        
	        
	        
	        
	        
	        
	       ALLOW_NETWORK_VOTE_REWARD = Customs.getProperty("AllowNetworkVoteReward", false);
	        NETWORK_SERVER_LINK = Customs.getProperty("NetworkServerLink", "");
	        NETWORK_VOTES_DIFFERENCE = Customs.getProperty("NetworkVotesDifference", 5);
	               NETWORK_REWARD_CHECK_TIME = Customs.getProperty("NetworkRewardCheckTime", 5);
	        String NETWORK_SMALL_REWARD_VALUE = Customs.getProperty("NetworkReward", "57,100000000;");
	        String[] NETWORK_small_reward_splitted_1 = NETWORK_SMALL_REWARD_VALUE.split(";");
	        for (String i : NETWORK_small_reward_splitted_1)
	        {
	               String[] NETWORK_small_reward_splitted_2 = i.split(",");
	               NETWORK_REWARD.put(Integer.parseInt(NETWORK_small_reward_splitted_2[0]), Integer.parseInt(NETWORK_small_reward_splitted_2[1]));
	        }
	        NETWORK_DUALBOXES_ALLOWED = Customs.getProperty("NetworkDualboxesAllowed", 1);
	        ALLOW_NETWORK_GAME_SERVER_REPORT = Customs.getProperty("AllowNetworkGameServerReport", false);
	        ALLOW_TOPZONE_VOTE_REWARD = Customs.getProperty("AllowTopzoneVoteReward", false);
	        TOPZONE_SERVER_LINK = Customs.getProperty("TopzoneServerLink", "");
	        TOPZONE_VOTES_DIFFERENCE = Customs.getProperty("TopzoneVotesDifference", 5);
	               TOPZONE_REWARD_CHECK_TIME = Customs.getProperty("TopzoneRewardCheckTime", 5);
	        String TOPZONE_SMALL_REWARD_VALUE = Customs.getProperty("TopzoneReward", "57,100000000;");
	        String[] topzone_small_reward_splitted_1 = TOPZONE_SMALL_REWARD_VALUE.split(";");
	        for (String i : topzone_small_reward_splitted_1)
	        {
	               String[] topzone_small_reward_splitted_2 = i.split(",");
	               TOPZONE_REWARD.put(Integer.parseInt(topzone_small_reward_splitted_2[0]), Integer.parseInt(topzone_small_reward_splitted_2[1]));
	        }
	        TOPZONE_DUALBOXES_ALLOWED = Customs.getProperty("TopzoneDualboxesAllowed", 1);
	        ALLOW_TOPZONE_GAME_SERVER_REPORT = Customs.getProperty("AllowTopzoneGameServerReport", false);
	        ALLOW_HOPZONE_VOTE_REWARD = Customs.getProperty("AllowHopzoneVoteReward", false);
	        HOPZONE_SERVER_LINK = Customs.getProperty("HopzoneServerLink", "");
	        HOPZONE_VOTES_DIFFERENCE = Customs.getProperty("HopzoneVotesDifference", 5);
	        HOPZONE_REWARD_CHECK_TIME = Customs.getProperty("HopzoneRewardCheckTime", 5);
	        String HOPZONE_SMALL_REWARD_VALUE = Customs.getProperty("HopzoneReward", "57,100000000;");
	        String[] hopzone_small_reward_splitted_1 = HOPZONE_SMALL_REWARD_VALUE.split(";");
	        for (String i : hopzone_small_reward_splitted_1)
	        {
	               String[] hopzone_small_reward_splitted_2 = i.split(",");
	               HOPZONE_REWARD.put(Integer.parseInt(hopzone_small_reward_splitted_2[0]), Integer.parseInt(hopzone_small_reward_splitted_2[1]));
	        }
	        HOPZONE_DUALBOXES_ALLOWED = Customs.getProperty("HopzoneDualboxesAllowed", 1);
	        ALLOW_HOPZONE_GAME_SERVER_REPORT = Customs.getProperty("AllowHopzoneGameServerReport", false);
	        
            VOTE_TOPZONE_API_URL = Customs.getProperty("TopZoneApiUrl", "");
            VOTE_TOP_ZONE_TOKEN = Customs.getProperty("TopZoneToken", "");
            
	}
	
	
	
	/** Donate Shop  */
	private static final String Donate = "./config/Customs/DonateShop.properties";

	
	public static int DONATE_NPC;
	public static int DONATE_ITEM;
	public static int DONATION_SKILL_ENCH_COUNT;
	
	public static int NOBL_ITEM_COUNT;
	public static int SEX_ITEM_COUNT;
	public static int PK_ITEM_COUNT;
	public static int PK_CLEAN;
	public static int CLAN_ITEM_COUNT;
	public static int CLAN_REP_ITEM_COUNT;
	public static int CLAN_REPS;
	public static int AUGM_ITEM_COUNT;
	public static int CLAN_SKILL_ITEM_COUNT;
	public static int REC_ITEM_COUNT;
	public static int PASSWORD_ITEM_COUNT;
	public static int COLOR_ITEM_COUNT;
	public static int NAME_ITEM_COUNT;
	public static int ENCHANT_ITEM_COUNT;
	public static int ENCHANT_MAX_VALUE;
	public static int CLASS_ITEM_COUNT;
	
	public static int HERO_1_COUNT;
	public static int HERO_7_COUNT;
	public static int HERO_14_COUNT;
	
	public static void loadDonate(){
		final ExProperties custom = initProperties(Donate);
		DONATE_NPC = custom.getProperty("Npc", 57);
		DONATE_ITEM = custom.getProperty("DonateItemId", 57);
		NOBL_ITEM_COUNT = custom.getProperty("NoblesseItemCount", 100);
		SEX_ITEM_COUNT = custom.getProperty("SexItemCount", 100);
		PK_ITEM_COUNT = custom.getProperty("PkItemCount", 100);
		PK_CLEAN = custom.getProperty("PkCleanValue", 50);
		CLAN_ITEM_COUNT = custom.getProperty("ClanItemCount", 100);
		CLAN_REP_ITEM_COUNT = custom.getProperty("ClanRepsCount", 100);
		CLAN_REPS = custom.getProperty("ClanReps", 20000);
		AUGM_ITEM_COUNT = custom.getProperty("AugmentionItemCount", 100);
		NOBL_ITEM_COUNT = custom.getProperty("ClanSkillsItemCount", 100);
		REC_ITEM_COUNT = custom.getProperty("RecItemCount", 100);
		PASSWORD_ITEM_COUNT = custom.getProperty("PasswordItemCount", 100);
		COLOR_ITEM_COUNT = custom.getProperty("ColorItemCount", 100);
		NAME_ITEM_COUNT = custom.getProperty("NameItemCount", 100);
		ENCHANT_ITEM_COUNT = custom.getProperty("EnchantItemCount", 100);
		ENCHANT_MAX_VALUE = custom.getProperty("MaxEnchantValue", 15);
		CLASS_ITEM_COUNT = custom.getProperty("ClassItemCount", 100);
		HERO_1_COUNT = custom.getProperty("hero1count", 100);
		HERO_7_COUNT = custom.getProperty("hero7count", 100);
		HERO_14_COUNT = custom.getProperty("hero14count", 100);
		DONATION_SKILL_ENCH_COUNT = custom.getProperty("SkillEnchantCount", 5);
	}
	
	
	
	
	/** Donate Shop  */
	private static final String Services = "./config/Customs/ServiceManager.properties";

	
	public static int SERVICE_ITEM;
	public static int SERVICE_CLAN_ITEM_COUNT;
	public static int SERVICE_CLAN_REP_ITEM_COUNT;
	public static int SERVICE_CLAN_REPS;
	public static int SERVICE_CLAN_SKILL_ITEM_COUNT;
	public static int SERVICE_PASSWORD_ITEM;
	public static int SERVICE_PASSWORD_ITEM_COUNT;
	
	public static void loadServices(){
		final ExProperties custom = initProperties(Services);
		SERVICE_ITEM = custom.getProperty("ServiceItemId", 9501);
		SERVICE_CLAN_ITEM_COUNT = custom.getProperty("ClanItemCount", 100);
		SERVICE_CLAN_REP_ITEM_COUNT = custom.getProperty("ClanRepsCount", 100);
		SERVICE_CLAN_REPS = custom.getProperty("ClanReps", 10000);
		SERVICE_CLAN_SKILL_ITEM_COUNT = custom.getProperty("ClanSkillsItemCount", 100);
		SERVICE_PASSWORD_ITEM = custom.getProperty("PassChangeItemId", 57);
		SERVICE_PASSWORD_ITEM_COUNT = custom.getProperty("PassChangeItemCount", 100);
		
		
	}
	
	
	
	
	/** Skill Duration */
	private static final String DURATION = "./config/Customs/skillDuration.properties";
	
	public static boolean ENABLE_MODIFY_SKILL_DURATION;
	public static Map<Integer, Integer> SKILL_DURATION_LIST; 
	 
	public static void loadSkilDuration(){
		 final ExProperties skillduration = initProperties(DURATION);
		
	 	 ENABLE_MODIFY_SKILL_DURATION = Boolean.parseBoolean(skillduration.getProperty("EnableModifySkillDuration", "false"));
	 	
	 	 if(ENABLE_MODIFY_SKILL_DURATION)
	 	 {
	 	 SKILL_DURATION_LIST = new HashMap<>();
	 	
	 	 String[] propertySplit;
	 	 propertySplit = skillduration.getProperty("SkillDurationList", "").split(";");
	 	
		 	 for(String skill : propertySplit)
		 	 {
		 		 String[] skillSplit = skill.split(",");
		 		 if(skillSplit.length != 2){
		 			System.out.println("[SkillDurationList]: invalid config property -> SkillDurationList \"" + skill + "\"");
		 		 }
		 		 else{
		 			 try{
		 				 SKILL_DURATION_LIST.put(Integer.parseInt(skillSplit[0]), Integer.parseInt(skillSplit[1]));
		 			 }
		 			 catch(NumberFormatException nfe){
		 				 nfe.printStackTrace();
		 				 if(!skill.equals("")) {
		 					 System.out.println("[SkillDurationList]: invalid config property -> SkillList \"" + skillSplit[0] + "\"" + skillSplit[1]);
		 				 }
		 			 }
		 		 }
		 	 }
	 	 }
	}
	
	

	
	/** Vup */
	private static final String VIP = "./config/Customs/vip.properties";
	
	  public static boolean ENABLE_VIP_SYSTEM;
	    public static boolean VIP_EFFECT;
	    public static Map<Integer, Integer> VIP_SKILLS;
	    public static boolean ALLOW_VIP_NCOLOR;
	    public static String VIP_NCOLOR;
	    public static boolean ALLOW_VIP_TCOLOR;
	    public static String VIP_TCOLOR;
	    public static double VIP_XP_SP_RATE;
	    public static double VIP_ADENA_RATE;
	    public static int VIP_DROP_RATE;
	    public static double VIP_SPOIL_RATE;
	    public static boolean ALLOW_VIP_ITEM;
	    public static int VIP_ITEMID;

	    public static int VIP_COIN_ID1;
	    public static int VIP_DAYS_ID1;
	    public static int VIP_COIN_ID2;
	    public static int VIP_DAYS_ID2;
	    public static int VIP_COIN_ID3;
	    public static int VIP_DAYS_ID3;
	    
	    public static String VIP_MONSTERS;

	    public static List<Integer> VIP_MONSTERS_ID;
	    public static List<RewardHolder> VIP_MONSTER_REWARDS;

		public static List<Integer> VIP_RAID_IDS;
		public static List<RewardHolder> VIP_RAID_REWARDS;

		public static List<Integer> VIP_PARTY_IDS;
		public static List<RewardHolder> VIP_PARTY_REWARDS;
	    
	  //  public static double VIP_RB_RATE;
	   // public static List<Integer> VIP_RB_ITEM_IDS;
	    
	    
	public static void loadVip(){
		 final ExProperties vip = initProperties(VIP);
		
         ENABLE_VIP_SYSTEM = vip.getProperty("EnableVipSystem", false);
         ALLOW_VIP_NCOLOR = vip.getProperty("AllowVipNameColor", false);
         VIP_NCOLOR = vip.getProperty("VipNameColor", "88AA88");//Integer.decode("0x" + vip.getProperty("VipNameColor", "88AA88"));
         ALLOW_VIP_TCOLOR = vip.getProperty("AllowVipTitleColor", false);
         VIP_TCOLOR = vip.getProperty("VipTitleColor", "88AA88");//Integer.decode("0x" + vip.getProperty("VipTitleColor", "88AA88"));
         VIP_XP_SP_RATE = vip.getProperty("VIPXpSpRate", 1.0);
         VIP_ADENA_RATE = vip.getProperty("VIPAdenaRate", 1.0);
         VIP_DROP_RATE = vip.getProperty("VIPDropRate", 1);
         VIP_SPOIL_RATE = vip.getProperty("VIPSpoilRate", 1.0);
         VIP_ITEMID = vip.getProperty("ItemIdVip", 0);
         ALLOW_VIP_ITEM = vip.getProperty("AllowVIPItem", false);
         VIP_EFFECT = vip.getProperty("VipEffect", false);
         if (ENABLE_VIP_SYSTEM) {
             final String[] VipSkillsSplit = vip.getProperty("VipSkills", "").split(";");
             VIP_SKILLS = new HashMap<>(VipSkillsSplit.length);
             for (final String skill : VipSkillsSplit) {
                 final String[] skillSplit = skill.split(",");
                 if (skillSplit.length != 2) {
                     System.out.println("[VIP System]: invalid config property in players.properties -> VipSkills \"" + skill + "\"");
                 }
                 else {
                     try {
                         VIP_SKILLS.put(Integer.parseInt(skillSplit[0]), Integer.parseInt(skillSplit[1]));
                     }
                     catch (NumberFormatException nfe2) {
                         if (!skill.equals("")) {
                             System.out.println("[VIP System]: invalid config property in players.props -> VipSkills \"" + skillSplit[0] + "\"" + skillSplit[1]);
                         }
                     }
                 }
             }
         }
         
         VIP_COIN_ID1 = vip.getProperty("VipCoin1", 57);
         VIP_DAYS_ID1 = vip.getProperty("VipCoinDays1", 57);
         VIP_COIN_ID2 = vip.getProperty("VipCoin2", 57);
         VIP_DAYS_ID2 = vip.getProperty("VipCoinDays2", 57);
         VIP_COIN_ID3 = vip.getProperty("VipCoin3", 57);
         VIP_DAYS_ID3 = vip.getProperty("VipCoinDays3", 57);
         
      /*   VIP_MONSTERS = vip.getProperty("ListVipMonsters");
         VIP_MONSTERS_ID = new ArrayList<>();
         for (final String id6 : VIP_MONSTERS.split(",")) {
        	 VIP_MONSTERS_ID.add(Integer.parseInt(id6));
         }
         */
         
         VIP_MONSTERS_ID = new ArrayList<>();
	 	 String[] propertySplit;
	 	 propertySplit = vip.getProperty("ListVipMonsters", "").split(",");
		 	 for(String id : propertySplit)
		 	 {
		 		 VIP_MONSTERS_ID.add(Integer.parseInt(id));
		 	 }
		 	 
		 	 

			 	
		 	
//         PARTY_ZONE_REWARDS = parseReward(vip, "PartyZoneReward");
         
      /*  	final String[] aux = vip.getProperty("VipMonsterReward").split(";");
            for (final String randomReward : aux/*.split(";")*/ /*) {
                final String[] infos = randomReward.split(",");
                if (infos.length > 2) {
                	VIP_MONSTER_REWARDS.add(new RewardHolder(Integer.valueOf(infos[0]), Integer.valueOf(infos[1]), Integer.valueOf(infos[2])));
                }
                else {
                	VIP_MONSTER_REWARDS.add(new RewardHolder(Integer.valueOf(infos[0]), Integer.valueOf(infos[1])));
                }
            }*/
            
		 	VIP_MONSTER_REWARDS = new ArrayList<>();
   	 	 	propertySplit = vip.getProperty("VipMonsterReward", "").split(";");
   		 	 for(String id : propertySplit)
   		 	 {
   		 		 String[] idSplit = id.split(",");
   		 		 
   		 		 if(idSplit.length == 4) 
   		 			VIP_MONSTER_REWARDS.add(new RewardHolder(Integer.valueOf(idSplit[0]), Integer.valueOf(idSplit[1]), Integer.valueOf(idSplit[2]), Integer.valueOf(idSplit[3]) ));
   		 		 
   		 		 else if (idSplit.length == 3) 
   		 			VIP_MONSTER_REWARDS.add(new RewardHolder(Integer.valueOf(idSplit[0]), Integer.valueOf(idSplit[1]), Integer.valueOf(idSplit[2])));
   		 		
   		 		 else if (idSplit.length == 2) 
   		 			VIP_MONSTER_REWARDS.add(new RewardHolder(Integer.valueOf(idSplit[0]), Integer.valueOf(idSplit[1])));

   		 		 else
   		 			 System.out.println("Error");
   		 	 }
   		 	 
   		 	 
 		 	VIP_RAID_REWARDS = new ArrayList<>();
   	 	 	propertySplit = vip.getProperty("VipRaidReward", "").split(";");
   		 	 for(String id : propertySplit)
   		 	 {
   		 		 String[] idSplit = id.split(",");
   		 		 
   		 		 if(idSplit.length == 4) 
   		 			VIP_RAID_REWARDS.add(new RewardHolder(Integer.valueOf(idSplit[0]), Integer.valueOf(idSplit[1]), Integer.valueOf(idSplit[2]), Integer.valueOf(idSplit[3]) ));
   		 		 
   		 		 else if (idSplit.length == 3) 
   		 			VIP_RAID_REWARDS.add(new RewardHolder(Integer.valueOf(idSplit[0]), Integer.valueOf(idSplit[1]), Integer.valueOf(idSplit[2])));
   		 		
   		 		 else if (idSplit.length == 2) 
   		 			VIP_RAID_REWARDS.add(new RewardHolder(Integer.valueOf(idSplit[0]), Integer.valueOf(idSplit[1])));

   		 		 else
   		 			 System.out.println("Error");
   		 	 }


		VIP_RAID_IDS = new ArrayList<>();
		propertySplit = vip.getProperty("ListVipRaids", "").split(",");
		for(String id : propertySplit)
		{
			VIP_RAID_IDS.add(Integer.parseInt(id));
		}

		VIP_PARTY_IDS = new ArrayList<>();
		propertySplit = vip.getProperty("ListVipPartyIds", "").split(",");
		for(String id : propertySplit)
		{
			VIP_PARTY_IDS.add(Integer.parseInt(id));
		}
   		 	 
   		 	VIP_PARTY_REWARDS = new ArrayList<>();
   	 	 	propertySplit = vip.getProperty("VipPartyReward", "").split(";");
   		 	 for(String id : propertySplit)
   		 	 {
   		 		 String[] idSplit = id.split(",");
   		 		 
   		 		 if(idSplit.length == 4) 
   		 			VIP_PARTY_REWARDS.add(new RewardHolder(Integer.valueOf(idSplit[0]), Integer.valueOf(idSplit[1]), Integer.valueOf(idSplit[2]), Integer.valueOf(idSplit[3]) ));
   		 		 
   		 		 else if (idSplit.length == 3) 
   		 			VIP_PARTY_REWARDS.add(new RewardHolder(Integer.valueOf(idSplit[0]), Integer.valueOf(idSplit[1]), Integer.valueOf(idSplit[2])));
   		 		
   		 		 else if (idSplit.length == 2) 
   		 			VIP_PARTY_REWARDS.add(new RewardHolder(Integer.valueOf(idSplit[0]), Integer.valueOf(idSplit[1])));

   		 		 else
   		 			 System.out.println("Error party vip drop");
   		 	 }

	}
	

	
	
	/** Custom Pvp Pk color System */
	private static final String COLORPVPPK = "./config/Customs/CustomPvpPkColor.properties";
	
	// Configs ColorAccordingAmountPvPorPk //
	public static boolean ENABLE_ColorAccordingAmountPvPorPk;
	public static Map<Integer, String> PVP_COLOR_NAME = new LinkedHashMap<>();
	public static Map<Integer, String> PK_COLOR_TITLE = new LinkedHashMap<>();
	
	private static void loadColorAmountPvp()
	{
		final ExProperties config = initProperties(COLORPVPPK);
		String aux = "";
		
		ENABLE_ColorAccordingAmountPvPorPk = config.getProperty("Enable_ColorAccordingAmountPvPorPk", false);
		aux = config.getProperty("PvpColorName").trim();
		for (String colorInfo : aux.split(";"))
		{
			final String[] infos = colorInfo.split(",");
			PVP_COLOR_NAME.put(Integer.valueOf(infos[0]), infos[1]);
		}
		aux = config.getProperty("PkColorTitle").trim();
		for (String colorInfo : aux.split(";"))
		{
			final String[] infos = colorInfo.split(",");
			PK_COLOR_TITLE.put(Integer.valueOf(infos[0]), infos[1]);
		}
	}
	
	public static boolean STARTUP_SYSTEM_ENABLED;
	public static String START_FIGHTER_BUFF;
	public static ArrayList<Integer> START_FIGHTER_BUFF_LIST;
	public static String START_MAGE_BUFF;
	public static ArrayList<Integer> START_MAGE_BUFF_LIST;
	
	  public static String BYBASS_HEAVY_ITEMS;
	  public static String BYBASS_LIGHT_ITEMS;
	  public static String BYBASS_ROBE_ITEMS;
	  public static List<int[]> SET_HEAVY_ITEMS = new ArrayList<>();
	  public static int[] SET_HEAVY_ITEMS_LIST;
	  public static List<int[]> SET_LIGHT_ITEMS = new ArrayList<>();
	  public static int[] SET_LIGHT_ITEMS_LIST;
	  public static List<int[]> SET_ROBE_ITEMS = new ArrayList<>();
	  public static int[] SET_ROBE_ITEMS_LIST;
	  public static String BYBASS_WP_01_ITEM;
	  public static String BYBASS_WP_02_ITEM;
	  public static String BYBASS_WP_03_ITEM;
	  public static String BYBASS_WP_04_ITEM;
	  public static String BYBASS_WP_05_ITEM;
	  public static String BYBASS_WP_06_ITEM;
	  public static String BYBASS_WP_07_ITEM;
	  public static String BYBASS_WP_08_ITEM;
	  public static String BYBASS_WP_09_ITEM;
	  public static String BYBASS_WP_10_ITEM;
	  public static String BYBASS_WP_11_ITEM;
	  public static String BYBASS_WP_12_ITEM;
	  public static String BYBASS_WP_13_ITEM;
	  public static String BYBASS_WP_14_ITEM;
	  public static String BYBASS_WP_15_ITEM;
	  public static String BYBASS_WP_16_ITEM;
	  public static String BYBASS_WP_17_ITEM;
	  public static String BYBASS_WP_18_ITEM;
	  public static String BYBASS_WP_19_ITEM;
	  public static String BYBASS_WP_20_ITEM;
	  public static String BYBASS_WP_21_ITEM;
	  public static String BYBASS_WP_22_ITEM;
	  public static String BYBASS_WP_23_ITEM;
	  public static String BYBASS_WP_24_ITEM;
	  public static String BYBASS_WP_25_ITEM;
	  public static String BYBASS_WP_26_ITEM;
	  public static String BYBASS_WP_27_ITEM;
	  public static String BYBASS_WP_28_ITEM;
	  public static String BYBASS_WP_29_ITEM;
	  public static String BYBASS_WP_30_ITEM;
	  public static String BYBASS_WP_31_ITEM;
	  public static String BYBASS_WP_SHIELD;
	  public static String BYBASS_ARROW;
	  public static int WP_01_ID;
	  public static int WP_02_ID;
	  public static int WP_03_ID;
	  public static int WP_04_ID;
	  public static int WP_05_ID;
	  public static int WP_06_ID;
	  public static int WP_07_ID;
	  public static int WP_08_ID;
	  public static int WP_09_ID;
	  public static int WP_10_ID;
	  public static int WP_11_ID;
	  public static int WP_12_ID;
	  public static int WP_13_ID;
	  public static int WP_14_ID;
	  public static int WP_15_ID;
	  public static int WP_16_ID;
	  public static int WP_17_ID;
	  public static int WP_18_ID;
	  public static int WP_19_ID;
	  public static int WP_20_ID;
	  public static int WP_21_ID;
	  public static int WP_22_ID;
	  public static int WP_23_ID;
	  public static int WP_24_ID;
	  public static int WP_25_ID;
	  public static int WP_26_ID;
	  public static int WP_27_ID;
	  public static int WP_28_ID;
	  public static int WP_29_ID;
	  public static int WP_30_ID;
	  public static int WP_31_ID;
	  public static int WP_ARROW;
	  public static int WP_SHIELD;
	  
		private static final String STARTUP = "./config/Customs/startup.properties";
		private static void loadStartUp()
		{
			final ExProperties start = initProperties(STARTUP);
			
			STARTUP_SYSTEM_ENABLED = start.getProperty("EnableStartupSystem", false);

			
			

	        START_FIGHTER_BUFF = start.getProperty("FighterBuffList", "0");
	        START_FIGHTER_BUFF_LIST = new ArrayList<>();
	        for (final String id : CustomConfig.START_FIGHTER_BUFF.trim().split(",")) {
	        	CustomConfig.START_FIGHTER_BUFF_LIST.add(Integer.parseInt(id.trim()));
	        }
	        START_MAGE_BUFF = start.getProperty("MageBuffList", "0");
	        START_MAGE_BUFF_LIST = new ArrayList<>();
	        for (final String id : START_MAGE_BUFF.trim().split(",")) {
	        	START_MAGE_BUFF_LIST.add(Integer.parseInt(id.trim()));
	        }
	        
	        
			String[] propertySplit = start.getProperty("SetRobe", "4223,1").split(";");
			SET_ROBE_ITEMS.clear();
			for (String reward : propertySplit)
			{
				String[] rewardSplit = reward.split(",");
				if (rewardSplit.length != 2) 
				{
					System.out.println("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
				} 
				else 
				{
					try
					{
						SET_ROBE_ITEMS.add(new int[] { Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1]) });
					}
					catch (NumberFormatException nfe)
					{
						if (!reward.isEmpty()) 
						{
							System.out.println("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
						}
					}
				}
			}
			propertySplit = start.getProperty("SetLight", "4223,1").split(";");
			SET_LIGHT_ITEMS.clear();
			for (String reward : propertySplit)
			{
				String[] rewardSplit = reward.split(",");
				if (rewardSplit.length != 2) 
				{
					System.out.println("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
				} 
				else 
				{
					try
					{
						SET_LIGHT_ITEMS.add(new int[] { Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1]) });
					}
					catch (NumberFormatException nfe)
					{
						if (!reward.isEmpty())
						{
							System.out.println("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
						}
					}
				}
			}
			propertySplit = start.getProperty("SetHeavy", "4223,1").split(";");
			SET_HEAVY_ITEMS.clear();
			for (String reward : propertySplit)
			{
				String[] rewardSplit = reward.split(",");
				if (rewardSplit.length != 2) 
				{
					System.out.println("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
				} 
				else 
				{
					try
					{
						SET_HEAVY_ITEMS.add(new int[] { Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1]) });
					}
					catch (NumberFormatException nfe)
					{
						if (!reward.isEmpty()) 
						{
							System.out.println("StartingCustomItemsFighter[Config.load()]: invalid config property -> StartingCustomItemsFighter \"" + reward + "\"");
						}
					}
				}
			}

			BYBASS_ROBE_ITEMS = start.getProperty("htm_robe", "startup");
			BYBASS_LIGHT_ITEMS = start.getProperty("htm_light", "startup");
			BYBASS_HEAVY_ITEMS = start.getProperty("htm_heavy", "startup");

			BYBASS_WP_01_ITEM = start.getProperty("BpWeapon_01", "startup");
			WP_01_ID = start.getProperty("Wp_01_ID", 5);

			BYBASS_WP_02_ITEM = start.getProperty("BpWeapon_02", "startup");
			WP_02_ID = start.getProperty("Wp_02_ID", 5);

			BYBASS_WP_03_ITEM = start.getProperty("BpWeapon_03", "startup");
			WP_03_ID = start.getProperty("Wp_03_ID", 5);

			BYBASS_WP_04_ITEM = start.getProperty("BpWeapon_04", "startup");
			WP_04_ID = start.getProperty("Wp_04_ID", 5);

			BYBASS_WP_05_ITEM = start.getProperty("BpWeapon_05", "startup");
			WP_05_ID = start.getProperty("Wp_05_ID", 5);

			BYBASS_WP_06_ITEM = start.getProperty("BpWeapon_06", "startup");
			WP_06_ID = start.getProperty("Wp_06_ID", 5);

			BYBASS_WP_07_ITEM = start.getProperty("BpWeapon_07", "startup");
			WP_07_ID = start.getProperty("Wp_07_ID", 5);

			BYBASS_WP_08_ITEM = start.getProperty("BpWeapon_08", "startup");
			WP_08_ID = start.getProperty("Wp_09_ID", 5);

			BYBASS_WP_09_ITEM = start.getProperty("BpWeapon_09", "startup");
			WP_09_ID = start.getProperty("Wp_09_ID", 5);

			BYBASS_WP_10_ITEM = start.getProperty("BpWeapon_10", "startup");
			WP_10_ID = start.getProperty("Wp_10_ID", 5);

			BYBASS_WP_11_ITEM = start.getProperty("BpWeapon_11", "startup");
			WP_11_ID = start.getProperty("Wp_11_ID", 5);

			BYBASS_WP_12_ITEM = start.getProperty("BpWeapon_12", "startup");
			WP_12_ID = start.getProperty("Wp_12_ID", 5);

			BYBASS_WP_13_ITEM = start.getProperty("BpWeapon_13", "startup");
			WP_13_ID = start.getProperty("Wp_13_ID", 5);

			BYBASS_WP_14_ITEM = start.getProperty("BpWeapon_14", "startup");
			WP_14_ID = start.getProperty("Wp_14_ID", 5);

			BYBASS_WP_15_ITEM = start.getProperty("BpWeapon_15", "startup");
			WP_15_ID = start.getProperty("Wp_15_ID", 5);

			BYBASS_WP_16_ITEM = start.getProperty("BpWeapon_16", "startup");
			WP_16_ID = start.getProperty("Wp_16_ID", 5);

			BYBASS_WP_17_ITEM = start.getProperty("BpWeapon_17", "startup");
			WP_17_ID = start.getProperty("Wp_17_ID", 5);

			BYBASS_WP_18_ITEM = start.getProperty("BpWeapon_18", "startup");
			WP_18_ID = start.getProperty("Wp_18_ID", 5);

			BYBASS_WP_19_ITEM = start.getProperty("BpWeapon_19", "startup");
			WP_19_ID = start.getProperty("Wp_19_ID", 5);

			BYBASS_WP_20_ITEM = start.getProperty("BpWeapon_20", "startup");
			WP_20_ID = start.getProperty("Wp_20_ID", 5);

			BYBASS_WP_21_ITEM = start.getProperty("BpWeapon_21", "startup");
			WP_21_ID = start.getProperty("Wp_21_ID", 5);

			BYBASS_WP_22_ITEM = start.getProperty("BpWeapon_22", "startup");
			WP_22_ID = start.getProperty("Wp_22_ID", 5);

			BYBASS_WP_23_ITEM = start.getProperty("BpWeapon_23", "startup");
			WP_23_ID = start.getProperty("Wp_23_ID", 5);

			BYBASS_WP_24_ITEM = start.getProperty("BpWeapon_24", "startup");
			WP_24_ID = start.getProperty("Wp_24_ID", 5);

			BYBASS_WP_25_ITEM = start.getProperty("BpWeapon_25", "startup");
			WP_25_ID = start.getProperty("Wp_25_ID", 5);

			BYBASS_WP_26_ITEM = start.getProperty("BpWeapon_26", "startup");
			WP_26_ID = start.getProperty("Wp_26_ID", 5);

			BYBASS_WP_27_ITEM = start.getProperty("BpWeapon_27", "startup");
			WP_27_ID = start.getProperty("Wp_27_ID", 5);

			BYBASS_WP_28_ITEM = start.getProperty("BpWeapon_28", "startup");
			WP_28_ID = start.getProperty("Wp_28_ID", 5);

			BYBASS_WP_29_ITEM = start.getProperty("BpWeapon_29", "startup");
			WP_29_ID = start.getProperty("Wp_29_ID", 5);

			BYBASS_WP_30_ITEM = start.getProperty("BpWeapon_30", "startup");
			WP_30_ID = start.getProperty("Wp_30_ID", 5);

			BYBASS_WP_31_ITEM = start.getProperty("BpWeapon_31", "startup");
			WP_31_ID = start.getProperty("Wp_31_ID", 5);

			WP_ARROW = start.getProperty("Arrow_ID", 5);
			WP_SHIELD = start.getProperty("Shield_ID", 5);

			
		}
	
	public static final void LoadCustomConfig()
	{
		StringUtil.printSection("Loading Custom configuration files.");
		loadCustomPrp();
		
		loadSkilDuration();
		System.out.println(ENABLE_MODIFY_SKILL_DURATION ? "Buff Duration System Enabled " : "Buff Duration System Disabled");
		
		
		loadVip();
		
		loadColorAmountPvp();
		
		loadStartUp();
		
		loadDonate();
		
		loadAnnounce();
		
		System.out.println("Loaded Vote Configs...");
		loadVotes();
		
		loadAntiFarm();
		
		loadServices();
		
		loadPtfarm();

		
		//must be in the end , load all configs and then the other things
		loadCustomMods.load();
	}
	
	public static final ExProperties initProperties(String filename)
	{
		final ExProperties result = new ExProperties();
		
		try
		{
			result.load(new File(filename));
		}
		catch (IOException e)
		{
			System.out.println("CustomConfig: Error loading \"" + filename + "\" CustomConfig.");
		}
		
		return result;
	}
	
}
