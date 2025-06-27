package Customs.Events.Tournaments.data;


import Customs.Events.Tournaments.TournamentConfig;
import net.sf.l2j.commons.concurrent.ThreadPool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

public class EventTime
{
    private static EventTime _instance = null;
    protected static final Logger _log = Logger.getLogger(EventTime.class.getName());
    private Calendar NextEvent;
    private final SimpleDateFormat format = new SimpleDateFormat("E d/M HH:mm");

    public static EventTime getInstance()
    {
        if (_instance == null) {
            _instance = new EventTime();
        }
        return _instance;
    }

    public String getNextTime()
    {
        if (this.NextEvent.getTime() != null) {
            format.setTimeZone(TimeZone.getTimeZone("Europe/Athens"));
            return this.format.format(this.NextEvent.getTime());
        }
        return "Erro";
    }

    public void StartCalculationOfNextEventTime()
    {
        try
        {
            Calendar currentTime = Calendar.getInstance();
            Calendar testStartTime = null;
            long flush2 = 0L;long timeL = 0L;
            int count = 0;
            for (String timeOfDay : TournamentConfig.TOURNAMENT_EVENT_INTERVAL_BY_TIME_OF_DAY)
            {
                testStartTime = Calendar.getInstance();
                testStartTime.setLenient(true);
                String[] splitTimeOfDay = timeOfDay.split(":");
                testStartTime.set(11, Integer.parseInt(splitTimeOfDay[0]));
                testStartTime.set(12, Integer.parseInt(splitTimeOfDay[1]));
                testStartTime.set(13, 0);
                if (testStartTime.getTimeInMillis() < currentTime.getTimeInMillis()) {
                    testStartTime.add(5, 1);
                }
                timeL = testStartTime.getTimeInMillis() - currentTime.getTimeInMillis();
                if (count == 0)
                {
                    flush2 = timeL;
                    this.NextEvent = testStartTime;
                }
                if (timeL < flush2)
                {
                    flush2 = timeL;
                    this.NextEvent = testStartTime;
                }
                count++;
            }
            _log.info("Tournament: Next Event " + this.NextEvent.getTime().toString());
            ThreadPool.schedule(new StartEventTask(), flush2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("[Tournament]: " + e);
        }
    }

    class StartEventTask implements Runnable
    {
        StartEventTask() {}

        @Override
        public void run()
        {
            _log.info("----------------------------------------------------------------------------");
            _log.info("TournamentNEW: Event Started.");
            _log.info("----------------------------------------------------------------------------");
            EventTask.SpawnEvent();
        }
    }
}
