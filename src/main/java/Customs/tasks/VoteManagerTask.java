package Customs.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.vote.API.VoteDao;


public class VoteManagerTask implements Runnable
{
	private final Map<Player, Long> _players = new ConcurrentHashMap<>();

	protected VoteManagerTask()
	{
		// Run task
		ThreadPool.scheduleAtFixedRate(this, 1000 , 1 * 60 * 1000);  // start first minute and then each 30 minutes , 30 * 60 * 1000
	}

	public final void add(Player player)
	{
		_players.put(player, System.currentTimeMillis());
	}

	public final void remove(Creature player)
	{
		_players.remove(player);
	}

	public final boolean contain(Creature player)
	{
		return _players.containsKey(player);
	}
	
	@Override
	public final void run()
	{
		if (_players.isEmpty())
			return;

		for (Map.Entry<Player, Long> entry : _players.entrySet())
		{
			final Player player = entry.getKey();

			long time = VoteDao.getVoteBuffTime(player);
			long delay = 11 * 3600 * 1000;	//After how many min/hour to delete button + SQL ( 60 * 1000 = minute  , 3600 * 1000 = hours)
			
			
			//Mame an initialize to all , clean them and refresh DB+Player data in order player vote again
			if(time != -1 && (time + delay) < System.currentTimeMillis() ) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();

				player.initVotes();				 		//make 0 the vote to Sites 
				VoteDao.initVoteBuff(player);			//Delete player from DB
				remove(player); 					 	//remove from this task
				player.remHwidVotes(player.gethwid());	//remove from player data
				VoteDao.initVoteBuffSaves(player); 		//remove from all sub of the player
				updateEffects(player);					//remove from buffs
			}

		}
	}

	public static void updateEffects(Player player) {
		for (L2Effect e : player.getAllEffects()){
			if (e != null && e.getSkill().getId() == 5413)
				e.exit();
		}
	}
	
	
	public static void deleteFromSkillSave(Player player) {
		
	}
	public static final VoteManagerTask getInstance()
	{
		return SingletonHolder._instance;
	}

	private static class SingletonHolder
	{
		protected static final VoteManagerTask _instance = new VoteManagerTask();
	}
}