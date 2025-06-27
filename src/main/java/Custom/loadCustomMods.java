package Custom;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import Customs.Events.Tournaments.TournamentConfig;
import Customs.Events.Tournaments.data.EventTask;
import Customs.Events.Tournaments.data.EventTime;
import Customs.mods.*;
import Customs.mods.Achievements.AchievementsManager;
import Customs.mods.zoneProtections.PartyZoneManager;
import Customs.mods.zoneProtections.RaidZoneManager;
import dev.l2j.tesla.autobots.Autobot;
import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.Restart;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.handler.itemhandlers.Custom.HeroItem;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.zone.ZoneType;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

import Customs.BalanceStatus.BalanceLoad;
import Customs.Events.CTF.CTFConfig;
import Customs.Events.CTF.CTFEvent;
import Customs.Events.CTF.CTFManager;
import Customs.Events.DM.DMConfig;
import Customs.Events.DM.DMEvent;
import Customs.Events.DM.DMManager;
import Customs.Events.PartyFarm.PartyFarm;
import Customs.Events.TvT.TvTConfig;
import Customs.Events.TvT.TvTEvent;
import Customs.Events.TvT.TvTManager;
import Customs.Instance.InstanceManager;
import Customs.Managers.ClassBalanceManager;
import Customs.Managers.SkillBalanceManager;
import Customs.PvpZone.RandomZoneManager;
import Customs.VIP.VipDropManager;
import Customs.data.CharacterKillingManager;
import Customs.data.EnchantTable;
import Customs.data.IconsTable;
import Customs.data.SkinTable;
import Customs.mods.fakepc.FakePcsTable;
import Customs.tasks.IPManager;
import Customs.tasks.VipTaskManager;
import Customs.vote.data.VoteSystem;

/**
 * @author Icathialord
 *
 */
public class loadCustomMods
{
	private static final Logger _log = Logger.getLogger(loadCustomMods.class.getName());
	//for debugging
	public static int getLineNumber() {
	    return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}
	
	public static void load(){
		DonateGiverTaskManager.getInstance();

		StringUtil.printSection("Balancer");
	    ClassBalanceManager.getInstance();
	    SkillBalanceManager.getInstance();
	    BalanceLoad.LoadEm();

		StringUtil.printSection("Zone Protections");
		RaidZoneManager.getInstance();
		PartyZoneManager.getInstance();

	    StringUtil.printSection("Icon Tables");
	    IconsTable.getInstance();
	    
	    StringUtil.printSection("Enchant Scrolls");
	    EnchantTable.getInstance();
	    
	    
	    StringUtil.printSection("Instances");
	    InstanceManager.getInstance();

		StringUtil.printSection("Achievements");
		AchievementsManager.getInstance();

		TournamentConfig.init();
		if(TournamentConfig.TOURNAMENT_EVENT_ENABLED) {
			StringUtil.printSection("Tournament Events");
			if (TournamentConfig.TOURNAMENT_EVENT_TIME) {
				System.out.println("Tournament Event is enabled.");
				EventTime.getInstance().StartCalculationOfNextEventTime();
			} else if (TournamentConfig.TOURNAMENT_EVENT_START) {
				System.out.println("Tournament Event is enabled.");
				EventTask.spawnNpc1();
			} else {
				System.out.println("Tournament Event is disabled");
			}
		}

        
        StringUtil.printSection("VIP Drop Manager");
        VipDropManager.getInstance();
        
        StringUtil.printSection("Skin Manager");
        SkinTable.getInstance().load();
        
        StringUtil.printSection("Events");
        
		CTFConfig.init();
		CTFManager.getInstance();
		
		DMConfig.init();
		DMManager.getInstance();
		
        TvTConfig.init();
		TvTManager.getInstance();
		

		StringUtil.printSection("Auto Pvp Zones");
		RandomZoneManager.getInstance();
		
		
		IPManager.getInstance();
		//Fake Pc's
		FakePcsTable.getInstance();
		
		
		if(AnnounceTops.getInstance().isOn())
			_log.config("-Loaded Custom Announce System. ");
	
		if(antiFarm.getInstance().isOn())
			_log.config("-Loaded Custom Anti Farm System.");
		
		_log.config("-Vote Reward");
			VoteSystem.initialize();
		
			
		if (CustomConfig.CKM_ENABLED)
		     CharacterKillingManager.getInstance().init();
			       
			
		if(CustomConfig.PF_EVENT_ENABLED)
			PartyFarm.getInstance();


		if(CustomConfig.RESTART_BY_TIME_OF_DAY)
			Restart.getInstance().StartCalculationOfNextRestartTime();
		else
			StringUtil.printSection("Auto Restart System Is Disabled");
		System.gc();


	}
	
	public static void onEnterWorld(Player player) {
		if (player.getFirstLog() || player.getSelectArmor() || player.getSelectWeapon() || player.getSelectClasse())
			onEnterNewbie(player);

		player.sendPacket(new CreatureSay(0, Say2.HERO_VOICE, "Olympiad", "Ends in " + OlyAnnounce.olympiadEndString()));
		
		//hero check
		if (player.getMemos().getLong("TimeOfHero", 0) > 0)
			onEnterHero(player);
		
		if (player.getMemos().getLong("vip", 0) > 0)
			onEnterVip(player);


		if(CustomConfig.RESTART_BY_TIME_OF_DAY)
		{
			ShowNextRestart(player);
		}

		//The turn of colors are: First PVp/Pk color after Donate and then VIP , so VIP is the color that will remain , then donate and last pvp/pk
		PvpColor.checkColorPlayer(player);
		
		checkColor(player);

		AnnounceTops.Announce(player);
		
		
        if (CustomConfig.MULTIBOX_PROTECTION_ENABLED && !(player instanceof Autobot))
            IPManager.getInstance().validBox(player, CustomConfig.MULTIBOX_PROTECTION_CLIENTS_PER_PC, World.getInstance().getPlayers(), true);
        

        CTFEvent.onLogin(player);
        DMEvent.onLogin(player);
		TvTEvent.onLogin(player);


		for (ZoneType zone : World.getInstance().getRegion(player.getX(), player.getY()).getZones()){
			if (zone.isCharacterInZone(player) && !player.isGM() && player.isInsideZone(ZoneId.CHANGE_PVP_ZONE) ){
				if(zone.getId() != RandomZoneManager.getInstance().getCurrentZone().getId()){
					player.teleportTo(RandomZoneManager.getInstance().getCurrentZone().getLoc(),20);
					break;
				}
			}
		}

	}

	private static void ShowNextRestart(Player activeChar)
	{
		activeChar.sendMessage("Next Restart: " + Restart.getInstance().getRestartNextTime());
	}

	public static void checkColor(Player player) {
		String color = player.getDonateColor();

		if(!color.equals("")) {
			player.setDonateColor(color);
			player.getAppearance().setNameColor(Integer.decode("0x" + color.substring(4,6) + color.substring(2,4) + color.substring(0,2)));
			player.broadcastUserInfo();

		}

		if(player.isVip()) {
			
			String colorN = player.getVipNColor();

			if(CustomConfig.ALLOW_VIP_NCOLOR) {
				player.setVipNColor(colorN);
				player.getAppearance().setNameColor(Integer.decode("0x" + colorN.substring(4,6) + colorN.substring(2,4) + colorN.substring(0,2)));
				player.broadcastUserInfo();

			}
			
			String colorT = player.getVipTColor();
			
			if(CustomConfig.ALLOW_VIP_TCOLOR) {
				player.setVipTColor(colorT);
				player.getAppearance().setTitleColor(Integer.decode("0x" + colorT.substring(4,6) + colorT.substring(2,4) + colorT.substring(0,2)));
				player.broadcastUserInfo();
				
			}
		}
	}
	private static void onEnterNewbie(Player activeChar)
	{
		if (CustomConfig.STARTUP_SYSTEM_ENABLED)
		{
			//make char disappears
			activeChar.getAppearance().setVisible();
			activeChar.broadcastUserInfo();
			activeChar.decayMe();
			activeChar.spawnMe();
			//active start system
			StartupSystem.startSetup(activeChar);
		}
	}
	
    private static void onEnterVip( Player activeChar) {
        final long now = Calendar.getInstance().getTimeInMillis();
        long endDay = activeChar.getMemos().getLong("vip");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        long _daysleft;
        if (now > endDay) {

            VipTaskManager.getInstance().removeVipStatus(activeChar);
            
            if (CustomConfig.ALLOW_VIP_ITEM) {
                activeChar.getInventory().destroyItemByItemId("", CustomConfig.VIP_ITEMID, 1, activeChar, null);
                activeChar.getWarehouse().destroyItemByItemId("", CustomConfig.VIP_ITEMID, 1, activeChar, null);
            }
            activeChar.sendPacket(new CreatureSay(0, Say2.CLAN, "System", "Your VIP period is up."));
        }
        else {
        	activeChar.setVip(true);

        	
            final Date dt = new Date(endDay);
            _daysleft = (endDay - now) / 86400000L;
            if (_daysleft > 120L) {
                activeChar.sendPacket(new CreatureSay(0, Say2.PARTYROOM_COMMANDER, "VIP System", "You have VIP 4ever!"));/*period ends in " + df.format(dt) + "."));*/
            }
            else if (_daysleft > 0L) {
                activeChar.sendPacket(new CreatureSay(0, Say2.PARTYROOM_COMMANDER, "VIP System", "Your period ends in " + (int)_daysleft + " days."));
            }
            else if (_daysleft < 1L) {
                final long hour = (endDay - now) / 3600000L;
                activeChar.sendPacket(new CreatureSay(0, Say2.PARTYROOM_COMMANDER, "VIP System", "Your period ends in " + (int)hour + " hours."));
            }
        }
    }
	private static void onEnterHero(Player activeChar)
	{
		long now = Calendar.getInstance().getTimeInMillis();
		long endDay = activeChar.getMemos().getLong("TimeOfHero");
		
			if (now > endDay)
				HeroItem.RemoveHeroStatus(activeChar);
			else
			{
				activeChar.setCustomHero(true);
				activeChar.broadcastUserInfo();
			}
	}

	public static boolean isEventStarted() {
		return TvTEvent.isStarted() || CTFEvent.isStarted() || DMEvent.isStarted();
	}
	public static boolean isPlayerInEvent(Player p) {
		return TvTEvent.isPlayerParticipant(p.getObjectId()) || CTFEvent.isPlayerParticipant(p.getObjectId()) || DMEvent.isPlayerParticipant(p.getObjectId());
	}
	
	public static boolean areTeamMates(Player player , Player target) {
		
		return TvTEvent.areTeammates(player, target) || CTFEvent.areTeammates(player, target); //|| DMEvent.areTeammates(player, target);
	}

	public static boolean isRegistered(Creature p) {
		return TvTEvent.isPlayerParticipant(p.getObjectId()) || CTFEvent.isPlayerParticipant(p.getObjectId()) || DMEvent.isPlayerParticipant(p.getObjectId());
	}
}
