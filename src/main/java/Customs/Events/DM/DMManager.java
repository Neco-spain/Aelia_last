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

import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.model.World;

public class DMManager
{
	protected static final Logger _log = Logger.getLogger(DMManager.class.getName());

	private DMStartTask _task;

	private DMManager()
	{
		if (DMConfig.DM_EVENT_ENABLED)
		{
			DMEvent.init();

			this.scheduleEventStart();
			_log.info("Deathmatch Engine: is Started.");
		}
		else
		{
			_log.info("Deathmatch Engine: Engine is disabled.");
		}
	}

	public static DMManager getInstance()
	{
		return SingletonHolder._instance;
	}

	public void scheduleEventStart()
	{
		try
		{
			Calendar currentTime = Calendar.getInstance();
			Calendar nextStartTime = null;
			Calendar testStartTime = null;
			for (String timeOfDay : DMConfig.DM_EVENT_INTERVAL)
			{
				// Creating a Calendar object from the specified interval value
				testStartTime = Calendar.getInstance();
				testStartTime.setLenient(true);
				String[] splitTimeOfDay = timeOfDay.split(":");
				testStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTimeOfDay[0]));
				testStartTime.set(Calendar.MINUTE, Integer.parseInt(splitTimeOfDay[1]));
				testStartTime.set(Calendar.SECOND,0);
				// If the date is in the past, make it the next day (Example: Checking for "1:00", when the time is 23:57.)
				if (testStartTime.getTimeInMillis() < currentTime.getTimeInMillis())
					testStartTime.add(Calendar.DAY_OF_MONTH, 1);
				// Check for the test date to be the minimum (smallest in the specified list)
				if (nextStartTime == null || testStartTime.getTimeInMillis() < nextStartTime.getTimeInMillis())
					nextStartTime = testStartTime;
			}
			_task = new DMStartTask(nextStartTime.getTimeInMillis());
			ThreadPool.schedule(_task,0);
		}
		catch (Exception e)
		{
			_log.warning("DMEventEngine: Error figuring out a start time. Check DMEventInterval in config file.");
		}
	}

	public void startReg()
	{
		if (!DMEvent.startParticipation())
		{
			World.announceToOnlinePlayers("Deathmatch: Event was cancelled.");
			_log.warning("DMEventEngine: Error spawning event npc for participation.");

			this.scheduleEventStart();
		}
		else
		{
			World.announceToOnlinePlayers("Deathmatch: Joinable in " + DMConfig.DM_NPC_LOC_NAME + "!",true);
			World.announceToOnlinePlayers("Available Commands: .dmjoin .dmleave .dminfo",true);
			
			/*if (Config.ALLOW_EVENT_COMMANDS)
				World.announceToOnlinePlayers("Deathmatch: Command: .dmjoin / .dmleave / .dminfo");
			 */
			
			// schedule registration end
			_task.setStartTime(System.currentTimeMillis() + DMConfig.DM_EVENT_PARTICIPATION_TIME);
			ThreadPool.schedule(_task,0);
		}
	}

	public void startEvent()
	{
		if (!DMEvent.startFight())
		{
			World.announceToOnlinePlayers("Deathmatch: Event cancelled due to lack of Participation.");
			_log.info("DMEventEngine: Lack of registration, abort event.");

			this.scheduleEventStart();
		}
		else
		{
			DMEvent.sysMsgToAllParticipants("Teleporting in " + DMConfig.DM_EVENT_START_LEAVE_TELEPORT_DELAY + " second(s).");
			_task.setStartTime(System.currentTimeMillis() + 60000L * DMConfig.DM_EVENT_RUNNING_TIME);
			ThreadPool.schedule(_task,0);
		}
	}

	public void endEvent()
	{
		World.announceToOnlinePlayers(DMEvent.calculateRewards());
		DMEvent.sysMsgToAllParticipants("Teleporting back town in " + DMConfig.DM_EVENT_START_LEAVE_TELEPORT_DELAY + " second(s).");
		DMEvent.stopFight();

		this.scheduleEventStart();
	}

	public void skipDelay()
	{
		if (_task.nextRun.cancel(false))
		{
			_task.setStartTime(System.currentTimeMillis());
			ThreadPool.schedule(_task,0);
		}
	}	

	class DMStartTask implements Runnable
	{
		private long _startTime;
		public ScheduledFuture<?> nextRun;

		public DMStartTask(long startTime)
		{
			_startTime = startTime;
		}

		public void setStartTime(long startTime)
		{
			_startTime = startTime;
		}

		@Override
		public void run()
		{
			int delay = (int) Math.round((_startTime - System.currentTimeMillis()) / 1000.0);

			if (delay > 0)
			{
				this.announce(delay);
			}

			int nextMsg = 0;
			if (delay > 3600)
			{
				nextMsg = delay - 3600;
			}
			else if (delay > 1800)
			{
				nextMsg = delay - 1800;
			}
			else if (delay > 900)
			{
				nextMsg = delay - 900;
			}
			else if (delay > 600)
			{
				nextMsg = delay - 600;
			}
			else if (delay > 300)
			{
				nextMsg = delay - 300;
			}
			else if (delay > 60)
			{
				nextMsg = delay - 60;
			}
			else if (delay > 5)
			{
				nextMsg = delay - 5;
			}
			else if (delay > 0)
			{
				nextMsg = delay;
			}
			else
			{
				// start
				if (DMEvent.isInactive())
				{
					DMManager.this.startReg();
				}
				else if (DMEvent.isParticipating())
				{
					DMManager.this.startEvent();
				}
				else
				{
					DMManager.this.endEvent();
				}
			}

			if (delay > 0)
			{
				nextRun = ThreadPool.schedule(this, nextMsg * 1000);
			}
		}

		private void announce(long time)
		{
			if (time >= 3600 && time % 3600 == 0)
			{
				if (DMEvent.isParticipating())
				{
					World.announceToOnlinePlayers("Deathmatch: " + (time / 60 / 60) + " hour(s) until registration is closed!");
				}
				else if (DMEvent.isStarted())
				{
					DMEvent.sysMsgToAllParticipants("" + (time / 60 / 60) + " hour(s) until event is finished!");
				}
			}
			else if (time >= 60)
			{
				if (DMEvent.isParticipating())
				{
					World.announceToOnlinePlayers("Deathmatch: " + (time / 60) + " minute(s) until registration is closed!");
				}
				else if (DMEvent.isStarted())
				{
					DMEvent.sysMsgToAllParticipants("" + (time / 60) + " minute(s) until the event is finished!");
				}
			}
			else
			{
				if (DMEvent.isParticipating())
				{
					World.announceToOnlinePlayers("Deathmatch: " + time + " second(s) until registration is closed!");
				}
				else if (DMEvent.isStarted())
				{
					DMEvent.sysMsgToAllParticipants("" + time + " second(s) until the event is finished!");
				}
			}
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DMManager _instance = new DMManager();
	}
}