package Customs.tasks;



import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.handler.itemhandlers.Custom.HeroItem;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Baggos
 */
public class HeroTaskManager implements Runnable
{
	private final Map<Player, Long> _players = new ConcurrentHashMap<>();

	protected HeroTaskManager()
	{
		// Run task each 10 second.
		ThreadPool.scheduleAtFixedRate(this, 10000, 10000);
	}

	public final void add(Player player)
	{
		_players.put(player, System.currentTimeMillis());
	}

	public final void remove(Creature player)
	{
		_players.remove(player);
	}

	@Override
	public final void run()
	{
		if (_players.isEmpty())
			return;

		for (Map.Entry<Player, Long> entry : _players.entrySet())
		{
			final Player player = entry.getKey();

			if (player.isCustomHero() /*!player.isHero()*/)
			{
				if(player.getMemos().get("TimeOfHero") != null && player.getMemos().getLong("TimeOfHero") < System.currentTimeMillis() ) {
					HeroItem.RemoveHeroStatus(player);
					remove(player);
				}
			}
		}
	}

	public static final HeroTaskManager getInstance()
	{
		return SingletonHolder._instance;
	}

	private static class SingletonHolder
	{
		protected static final HeroTaskManager _instance = new HeroTaskManager();
	}
}