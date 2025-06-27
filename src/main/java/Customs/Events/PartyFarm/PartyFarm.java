package Customs.Events.PartyFarm;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;

import Customs.Events.TvT.TvTEvent;
import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.data.sql.SpawnTable;
import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.spawn.L2Spawn;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;

import Custom.CustomConfig;

public class PartyFarm
{
	protected static EventState eventState = EventState.INACTIVE;
	private enum EventState
	{
		START, END , INACTIVE //,ACTIVE
	}

	private Event task = new Event();
	protected Clock _task ;

	protected static ArrayList<L2Spawn> monsters = new ArrayList<>();
	public boolean isManual = false;

	public boolean isStarted(){
		if(eventState != null && eventState.equals(EventState.START))
			return true;
		return false;
	}

	public boolean isActivated(){
		if(eventState != null && eventState == EventState.START)
			return true;
		return false;
	}

	public EventState getState(){
		return eventState;
	}

	protected PartyFarm()
	{
		if (CustomConfig.PF_EVENT_ENABLED){
			_task = new Clock(CustomConfig.PF_FIRST_EVENT_INTERVAL * 60);
			ThreadPool.schedule(_task, 0);
			System.out.println("Party Farm Event: is Started.");
		}
		else
			System.out.println("Party farm Event: Engine is disabled.");
	}

	protected class Event implements Runnable
	{
		@Override
		public void run(){
			try{
				_task.setTime(CustomConfig.EVENT_BEST_FARM_TIME * 60 );
				ThreadPool.schedule(_task,0);
				bossSpawnMonster();
			}
			catch (NullPointerException e) //Throwable e
			{
				e.printStackTrace();
			}
		}
	}

	public void start()
	{
		if (_task.nextRun.cancel(false)) {
			_task.setTime(CustomConfig.EVENT_BEST_FARM_TIME * 60);
			ThreadPool.schedule(_task,0);
			bossSpawnMonster();
		}
	}

	protected void schedule(int time)
	{
		ThreadPool.schedule(task, time);
	}

	protected static void setStatus(EventState s)
	{
		eventState = s;
	}

	public void bossSpawnMonster()
	{
		setStatus(EventState.START);
		World.announceToOnlinePlayers("[Party Farm]: Duration: " + CustomConfig.EVENT_BEST_FARM_TIME + " minute(s)!", true);

		spawnMonsters();
	}

	public static void spawnMonsters()
	{
		for (int i = 0; i < CustomConfig.MONSTER_LOCS_COUNT; i++)
		{
			int[] coord = CustomConfig.MONSTER_LOCS[i];
			monsters.add(spawnNPC(coord[0], coord[1], coord[2], CustomConfig.monsterId));
		}
	}

	public void Finish_Event()
	{
		for (Player player : World.getInstance().getPlayers())
			if (player != null && player.isOnline() && player.isInsideZone(ZoneId.PARTY)) //ZoneId.PARTYFARM to be added.
				player.teleportTo( 82698, 148638, -3468 , 50);

		unSpawnMonsters();
		setStatus(EventState.END);

		if (_task.nextRun.cancel(false)) {
			_task.setTime(CustomConfig.PF_EVENT_INTERVAL * 60);
			ThreadPool.schedule(_task,0);
		}

		World.announceToOnlinePlayers("[Party Farm]: Event Finished!", true);
	}


	protected static L2Spawn spawnNPC(int xPos, int yPos, int zPos, int npcId)
	{
		NpcTemplate template = NpcData.getInstance().getTemplate(npcId);
		try
		{
			L2Spawn spawn = new L2Spawn(template);
			spawn.setLoc(xPos, yPos, zPos, 0);
			spawn.setRespawnDelay(CustomConfig.PARTY_FARM_MONSTER_DALAY);

			SpawnTable.getInstance().addSpawn(spawn, false);

			spawn.setRespawnState(true);
			spawn.doSpawn(false);
			spawn.getNpc().isAggressive();
			spawn.getNpc().decayMe();
			spawn.getNpc().spawnMe(spawn.getNpc().getX(), spawn.getNpc().getY(), spawn.getNpc().getZ());
			spawn.getNpc().broadcastPacket(new MagicSkillUse(spawn.getNpc(), spawn.getNpc(), 1034, 1, 1, 1));
			return spawn;
		}
		catch (Exception e)
		{
		}
		return null;
	}

	protected static void unSpawnMonsters()
	{
		for (L2Spawn s : monsters)
		{
			if (s == null)
			{
				monsters.remove(s);
				return;
			}

			s.getNpc().deleteMe();
			s.setRespawnState(false);
			SpawnTable.getInstance().deleteSpawn(s, true);

		}
	}

	protected class Clock implements Runnable
	{
		protected int time ;
		public ScheduledFuture<?> nextRun;

		public Clock(int i)
		{
			time = i ;
		}

		protected void setTime(int t)
		{
			time = t;
		}

		protected String getTime()
		{
			int tm = time / 60 / 60;
			String hours = (tm < 10 ? "0" + tm  : "" + tm) ;
//			String mins = (time / 60 < 10 ? "0" + time / 60 : "" + time / 60) ;
			String mins = ( (time / 60) % 60 < 10 ? "0" + (time / 60) % 60 : "" + (time / 60) % 60) ;
			String secs = (time % 60 < 10 ? "0" + time % 60 : "" + time % 60);
			return hours + ":" + mins + ":" + secs;
		}
		protected String getNpcTime()
		{
			int tm = time / 60 / 60;
			String hours = (tm < 10 ? "0" + tm  : "" + tm) ;
//			String mins = (time / 60 < 10 ? "0" + time / 60 : "" + time / 60) ;
			String mins = ( (time / 60) % 60 < 10 ? "0" + (time / 60) % 60 : "" + (time / 60) % 60) ;
			String secs = (time % 60 < 10 ? "0" + time % 60 : "" + time % 60);
			return hours + " Hour(s) :" + mins + " Min :" + secs + " Sec.";
		}

		@Override
		public void run()
		{
			if(isStarted()) {
				switch (time) {
					case 1800:
					case 900:
					case 600:
					case 300:
					case 240:
					case 180:
					case 120: {
						World.announceToOnlinePlayers("[Party Farm]: " + time / 60 + " minute(s) till event finish!", true);
						break;
					}
					case 60:
					case 30:
					case 15:
					case 10:
					case 5:
					case 4:
					case 3:
					case 2:
					case 1: {
						World.announceToOnlinePlayers("[Party Farm]: " + time + " second(s) till event finish!", true);
						break;
					}
				}

				if (time <= 0)
					Finish_Event();
				else {
					time--;
					nextRun = ThreadPool.schedule(this, 1000);
				}
			}
			else{
				if (time == 0 && !isActivated())
					schedule(1);
				else{
					time--;
					nextRun = ThreadPool.schedule(this, 1000);
				}
			}
		}
	}

	public static PartyFarm getInstance()
	{
		return SingletonHolder._instance;
	}

	private static class SingletonHolder
	{
		protected static final PartyFarm _instance = new PartyFarm();
	}

	public String formatDate(){
		if(_task != null){
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			return sdf.format(_task.getTime());
		}
		return null;
	}

	public String getNpcTime(){
		return _task.getNpcTime();
	}
	public String getTime(){
		return _task.getTime();
	}
	public String getTimeEnd(){
		if(_task != null)
			return _task.getTime();
		return null;
	}
	public String getNpcTimeEnd(){
		if(_task != null)
			return _task.getNpcTime();
		return null;
	}
}