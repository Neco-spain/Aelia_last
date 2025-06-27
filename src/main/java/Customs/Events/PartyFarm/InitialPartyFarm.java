package Customs.Events.PartyFarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import net.sf.l2j.commons.concurrent.ThreadPool;

import Custom.CustomConfig;

public class InitialPartyFarm
{
	private static InitialPartyFarm _instance = null;
	protected static final Logger _log = Logger.getLogger(InitialPartyFarm.class.getName());
	private Calendar NextEvent;
	private final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
	
	public static InitialPartyFarm getInstance()
	{
		if (_instance == null)
		{
			_instance = new InitialPartyFarm();
		}
		return _instance;
	}
	
	public String getRestartNextTime()
	{
		if (NextEvent.getTime() != null)
		{
			return format.format(NextEvent.getTime());
		}
		return "Erro";
	}
	
	public Date getTime(){
		return  NextEvent.getTime() != null ? NextEvent.getTime() : null;
	}
	
	public String formatDate(){
		if(NextEvent != null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");	
			return sdf.format(NextEvent.getTime());
		}
		return null;
	}
	
	
	public void StartCalculationOfNextEventTime()
	{
		try
		{
			Calendar currentTime = Calendar.getInstance();
			Calendar testStartTime = null;
			long flush2 = 0L;
			long timeL = 0L;
			int count = 0;
			for (String timeOfDay : CustomConfig.EVENT_BEST_FARM_INTERVAL_BY_TIME_OF_DAY)
			{
				testStartTime = Calendar.getInstance();
				testStartTime.setLenient(true);
				String[] splitTimeOfDay = timeOfDay.split(":");
				testStartTime.set(11, Integer.parseInt(splitTimeOfDay[0]));
				testStartTime.set(12, Integer.parseInt(splitTimeOfDay[1]));
				testStartTime.set(13, 0);
				if (testStartTime.getTimeInMillis() < currentTime.getTimeInMillis())
				{
					testStartTime.add(5, 1);
				}
				timeL = testStartTime.getTimeInMillis() - currentTime.getTimeInMillis();
				if (count == 0)
				{
					flush2 = timeL;
					NextEvent = testStartTime;
				}
				if (timeL < flush2)
				{
					flush2 = timeL;
					NextEvent = testStartTime;
				}
				count++;
			}
			
		/*	SimpleDateFormat  myFormatObj = new SimpleDateFormat("E dd MMM yyyy HH:mm:ss");  
			
		    String formattedDate = myFormatObj.format(NextEvent.getTime());  
		    System.out.println("After Formatting: " + formattedDate);  
		  */  
	    
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");	
			//sdf.format(NextEvent.getTime());
			
			_log.info("[Party Farm]: Next Event: " + sdf.format(NextEvent.getTime()) );
			ThreadPool.schedule(new StartEventTask(), flush2);
		}
		catch (Exception e)
		{
			System.out.println("[Party Farm]: Error!");
		}
	}

	class StartEventTask implements Runnable
	{
		StartEventTask()
		{
		}
		
		@Override
		public void run()
		{
			InitialPartyFarm._log.info("[Party Farm]: Event Started.");
			PartyFarm.getInstance().bossSpawnMonster();
		}
	}
}
