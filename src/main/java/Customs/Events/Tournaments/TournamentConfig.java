package Customs.Events.Tournaments;

import net.sf.l2j.commons.config.ExProperties;
import net.sf.l2j.commons.logging.CLogger;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.location.Location;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TournamentConfig
{
private static final CLogger LOGGER = new CLogger(TournamentConfig.class.getName());

public static final String CONFIG_FILE = "config/Customs/events/tournament.ini";

public static List<Integer> TOURNAMENTS = new ArrayList<>();

public static int RUNNING_TIME;
public static int TELEPORT_DELAY;

public static int MIN_LEVEL;
public static int MAX_LEVEL;

public static List<Integer> RESTRICTED_CLASSES;

public static List<IntIntHolder> REWARDS = new ArrayList<>();
public static List<IntIntHolder> REWARDS2 = new ArrayList<>();
public static List<IntIntHolder> REWARDS3 = new ArrayList<>();
    public static List<IntIntHolder> REWARDS4 = new ArrayList<>();

public static List<IntIntHolder> REWARDS5 = new ArrayList<>();
    public static List<IntIntHolder> REWARDS6 = new ArrayList<>();
    public static List<IntIntHolder> REWARDS7 = new ArrayList<>();
    public static List<IntIntHolder> REWARDS8 = new ArrayList<>();

public static List<IntIntHolder> REWARDS9 = new ArrayList<>();
public static List<IntIntHolder> REQUIRES = new ArrayList<>();

public static Location TEAM_RED_LOCATION;
public static Location TEAM_BLUE_LOCATION;
public static Location BACK_LOCATION;

public static boolean REMOVE_BUFFS;

public static boolean REMOVE_BUFFS_ON_DIE;


public static boolean SCROLL_ALLOWED;

public static boolean TARGET_TEAM_MEMBERS_ALLOWED;

    public static boolean Allow_Same_HWID_On_Tournament;
    public static boolean TOURNAMENT_EVENT_START;
    public static boolean TOURNAMENT_EVENT_ENABLED;
    public static boolean TOURNAMENT_EVENT_TIME;
    public static String[] TOURNAMENT_EVENT_INTERVAL_BY_TIME_OF_DAY;
    public static int TOURNAMENT_TIME;

    public static int ARENA_NPC;
    public static int NPC_locx;
    public static int NPC_locy;
    public static int NPC_locz;
    public static int NPC_Heading;
    public static int EVENT_TIME;

    public static boolean NEED_PVPS;
    public static int PVPS_TO_JOIN;

public static boolean init()
{
    try
    {
        ExProperties tourSettings = load(CONFIG_FILE);

        String[] tournaments = tourSettings.getProperty("Tournaments", "2,4,6").split(",");

        for (String st : tournaments)
        {
            TOURNAMENTS.add(Integer.parseInt(st));
        }

        TOURNAMENT_EVENT_ENABLED = tourSettings.getProperty("TournamentEnabled", false);

        TOURNAMENT_EVENT_START = tourSettings.getProperty("TournamentStartOn", false);
        TOURNAMENT_EVENT_TIME = tourSettings.getProperty("TournamentAutoEvent", false);
        TOURNAMENT_EVENT_INTERVAL_BY_TIME_OF_DAY = tourSettings.getProperty("TournamentStartTime", "20:00").split(",");
        EVENT_TIME = Integer.parseInt(tourSettings.getProperty("TournamentEventTime", "1"));
        Allow_Same_HWID_On_Tournament = Boolean.parseBoolean(tourSettings.getProperty("Allow_Same_HWID_On_Tournament", "true"));

        NEED_PVPS = tourSettings.getProperty("needPvp", false);
        PVPS_TO_JOIN = tourSettings.getProperty("PvPAmount", 0);

        ARENA_NPC = Integer.parseInt(tourSettings.getProperty("NPCRegister", "1"));

        NPC_locx = Integer.parseInt(tourSettings.getProperty("Locx", "1"));
        NPC_locy = Integer.parseInt(tourSettings.getProperty("Locy", "1"));
        NPC_locz = Integer.parseInt(tourSettings.getProperty("Locz", "1"));
        NPC_Heading = Integer.parseInt(tourSettings.getProperty("Heading", "1"));

        RUNNING_TIME = tourSettings.getProperty("RunningTime", 4);

        TELEPORT_DELAY = tourSettings.getProperty("TeleportDelay", 5);

        MIN_LEVEL = tourSettings.getProperty("MinLevel", 1);

        MAX_LEVEL = tourSettings.getProperty("MaxLevel", 80);

        RESTRICTED_CLASSES = parseArrayList(tourSettings.getProperty("RestrictedClasses", ""));

        String[] rewards = tourSettings.getProperty("Rewards", "57,100;6393,100").split(";");

        for (String rewardSet : rewards)
        {
            REWARDS.add(new IntIntHolder(Integer.parseInt(rewardSet.split(",")[0]), Integer.parseInt(rewardSet.split(",")[1])));
        }

        rewards = tourSettings.getProperty("Rewards2x2", "57,100;6393,100").split(";");

        for (String rewardSet : rewards)
        {
            REWARDS2.add(new IntIntHolder(Integer.parseInt(rewardSet.split(",")[0]), Integer.parseInt(rewardSet.split(",")[1])));
        }

        rewards = tourSettings.getProperty("Rewards3x3", "57,100;6393,100").split(";");

        for (String rewardSet : rewards)
        {
            REWARDS3.add(new IntIntHolder(Integer.parseInt(rewardSet.split(",")[0]), Integer.parseInt(rewardSet.split(",")[1])));
        }

        rewards = tourSettings.getProperty("Rewards4x4", "57,100;6393,100").split(";");

        for (String rewardSet : rewards)
        {
            REWARDS4.add(new IntIntHolder(Integer.parseInt(rewardSet.split(",")[0]), Integer.parseInt(rewardSet.split(",")[1])));
        }

        rewards = tourSettings.getProperty("Rewards5x5", "57,100;6393,100").split(";");

        for (String rewardSet : rewards)
        {
            REWARDS5.add(new IntIntHolder(Integer.parseInt(rewardSet.split(",")[0]), Integer.parseInt(rewardSet.split(",")[1])));
        }

        rewards = tourSettings.getProperty("Rewards6x6", "57,100;6393,100").split(";");

        for (String rewardSet : rewards)
        {
            REWARDS6.add(new IntIntHolder(Integer.parseInt(rewardSet.split(",")[0]), Integer.parseInt(rewardSet.split(",")[1])));
        }

        rewards = tourSettings.getProperty("Rewards7x7", "57,100;6393,100").split(";");

        for (String rewardSet : rewards)
        {
            REWARDS7.add(new IntIntHolder(Integer.parseInt(rewardSet.split(",")[0]), Integer.parseInt(rewardSet.split(",")[1])));
        }

        rewards = tourSettings.getProperty("Rewards8x8", "57,100;6393,100").split(";");

        for (String rewardSet : rewards)
        {
            REWARDS8.add(new IntIntHolder(Integer.parseInt(rewardSet.split(",")[0]), Integer.parseInt(rewardSet.split(",")[1])));
        }

        rewards = tourSettings.getProperty("Rewards9x9", "57,100;6393,100").split(";");

        for (String rewardSet : rewards)
        {
            REWARDS9.add(new IntIntHolder(Integer.parseInt(rewardSet.split(",")[0]), Integer.parseInt(rewardSet.split(",")[1])));
        }


        String[] requires = tourSettings.getProperty("RequiredItems", "57,100").split(";");

        for (String req : requires)
        {
            REQUIRES.add(new IntIntHolder(Integer.parseInt(req.split(",")[0]), Integer.parseInt(req.split(",")[1])));
        }

        String[] location = tourSettings.getProperty("TeamRedLoc", "-115399,-213194,-3327").split(",");

        TEAM_RED_LOCATION = new Location(Integer.parseInt(location[0]), Integer.parseInt(location[1]), Integer.parseInt(location[2]));

        location = tourSettings.getProperty("TeamBlueLoc", "-113674,-213194,-3327").split(",");

        TEAM_BLUE_LOCATION = new Location(Integer.parseInt(location[0]), Integer.parseInt(location[1]), Integer.parseInt(location[2]));

        location = tourSettings.getProperty("BackLoc", "82698,148638,-3473").split(",");

        BACK_LOCATION = new Location(Integer.parseInt(location[0]), Integer.parseInt(location[1]), Integer.parseInt(location[2]));

        REMOVE_BUFFS = tourSettings.getProperty("RemoveBuffs", false);

        REMOVE_BUFFS_ON_DIE = tourSettings.getProperty("RemoveBuffsOnDie", false);

        SCROLL_ALLOWED = tourSettings.getProperty("ScrollsAllowed", false);

        TARGET_TEAM_MEMBERS_ALLOWED = tourSettings.getProperty("TargetTeamMembersAllowed", false);
    }
    catch (Exception e)
    {
        e.printStackTrace();
        return false;
    }
    return true;
}

private static final List<Integer> parseArrayList(String line)
{
    final String[] propertySplit = line.split(";");
    if (propertySplit.length == 0)
    {
        return new ArrayList<>();
    }

    final List<Integer> result = new ArrayList<>(propertySplit.length);
    for (String value : propertySplit)
    {
        try
        {
            result.add(Integer.parseInt(value));
        }
        catch (NumberFormatException e)
        {
            LOGGER.warn("Config: Error parsing ID -> \"" + value + "\"");
            return new ArrayList<>();
        }
    }

    return result;
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
        LOGGER.error("Error loading config : " + file.getName() + "!", e);
    }

    return result;
}
}