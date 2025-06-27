package Customs.mods;

import java.util.Calendar;
import java.util.logging.Logger;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.enums.OlympiadState;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class OlyAnnounce
{
		private static final Logger LOGGER = Logger.getLogger(OlyAnnounce.class.getName());
	
		public static void olyperiod(OlympiadState _period , long _olympiadEnd){
					StringUtil.printSection("---Loading Olympiad Custom period Announce.---");
					long milliToEnd;
					if (_period == OlympiadState.COMPETITION)
						milliToEnd = getMillisToOlympiadEnd(_olympiadEnd);
					else
						milliToEnd = getMillisToValidationEnd(_olympiadEnd);
								
					double numSecs = (milliToEnd / 1000) % 60;
					double countDown = ((milliToEnd / 1000) - numSecs) / 60;
					final int numMins = (int) Math.floor(countDown % 60);
					countDown = (countDown - numMins) / 60;
					int numHours = (int) Math.floor(countDown % 24);
					int numDays = (int) Math.floor((countDown - numHours) / 24);
					
				
					
					LOGGER.info("Olympiad System: " + numDays + " days, " + numHours  +" hours and " + numMins + " mins. until period ends");
					
					if (_period == OlympiadState.COMPETITION)
					{
						long milliToEnd2 = getMillisToWeekChange(_olympiadEnd);
						
						double numSecs1 = (milliToEnd2 / 1000) % 60;
						double countDown1 = ((milliToEnd2 / 1000) - numSecs1) / 60;
						final int numMins1 = (int) Math.floor(countDown1 % 60);
						countDown1 = (countDown - numMins1) / 60;
						int numHours1 = (int) Math.floor(countDown1 % 24);
						int numDays1 = (int) Math.floor((countDown1 - numHours1) / 24);
						
						LOGGER.info("Olympiad System: Next weekly change in "+  numDays1  +" days, "  +numHours1  +" hours and " + numMins1 + " mins.");
						StringUtil.printSection("---------");
					}

	
		}
		 public static void olympiadEnd(Player player)
		 { 
		  long milliToEnd;
		  milliToEnd = (Olympiad.getInstance().getPeriod() == OlympiadState.COMPETITION ?  Olympiad.getInstance().getMillisToOlympiadEndv() : Olympiad.getInstance().getMillisToValidationEndv());
		  double numSecs = milliToEnd / 1000 % 60;
		  double countDown = (milliToEnd / 1000 - numSecs) / 60;
		  int numMins = (int) Math.floor(countDown % 60);
		  countDown = (countDown - numMins) / 60;
		  int numHours = (int) Math.floor(countDown % 24);
		  int numDays = (int) Math.floor((countDown - numHours) / 24);

		  CreatureSay cs = new CreatureSay(0, Say2.ALLIANCE, "Server", "Olympiad period ends in " + numDays + " days, " + numHours + " hours and " + numMins + " mins.");
		  player.sendPacket(cs);
		 }
		 
		 public static String olympiadEndString()
		 { 
		  long milliToEnd;
		  milliToEnd = (Olympiad.getInstance().getPeriod() == OlympiadState.COMPETITION ?  Olympiad.getInstance().getMillisToOlympiadEndv() : Olympiad.getInstance().getMillisToValidationEndv());
		  double numSecs = milliToEnd / 1000 % 60;
		  double countDown = (milliToEnd / 1000 - numSecs) / 60;
		  int numMins = (int) Math.floor(countDown % 60);
		  countDown = (countDown - numMins) / 60;
		  int numHours = (int) Math.floor(countDown % 24);
		  int numDays = (int) Math.floor((countDown - numHours) / 24);

		  //CreatureSay cs = new CreatureSay(0, SayType.ALLIANCE, "Server", "Olympiad period ends in " + numDays + " days, " + numHours + " hours and " + numMins + " mins.");
		  return numDays + " Days " + numHours + " Hours and " + numMins + " Mins." ;
		 }
		 
	private static long getMillisToOlympiadEnd(long _olympiadEnd)
	{
		return (_olympiadEnd - Calendar.getInstance().getTimeInMillis());
	}
	protected static long getMillisToValidationEnd(long _validationEnd)
	{
		if (_validationEnd > Calendar.getInstance().getTimeInMillis())
			return (_validationEnd - Calendar.getInstance().getTimeInMillis());
		
		return 10L;
	}
	private static long getMillisToWeekChange(long _nextWeeklyChange)
	{
		if (_nextWeeklyChange > Calendar.getInstance().getTimeInMillis())
			return (_nextWeeklyChange - Calendar.getInstance().getTimeInMillis());
		
		return 10L;
	}
}
