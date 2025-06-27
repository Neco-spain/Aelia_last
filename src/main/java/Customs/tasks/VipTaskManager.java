package Customs.tasks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;

import Custom.CustomConfig;

/**
 * @author icathiaLord
 */
public class VipTaskManager implements Runnable
{
	private final Map<Player, Long> _players = new ConcurrentHashMap<>();

	protected VipTaskManager()
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

			if (player.getMemos().getLong("vip") < System.currentTimeMillis())
			{
	           /* player.setVip(false);
	            player.getMemos().set("vip", 0);*/
				removeVipStatus(player);
				remove(player);
			}
		}
	}
	
	public void removeVipStatus(Player player) {
        player.setVip(false);
        player.getMemos().set("vip", 0);
		_players.remove(player);
        
        
  
		if(CustomConfig.ALLOW_VIP_NCOLOR )
			 player.getAppearance().setNameColor(Integer.decode("0x" + player.getOriginalNColor().substring(4,6) + player.getOriginalNColor().substring(2,4) + player.getOriginalNColor().substring(0,2)));//(Integer.decode("0x" + player.getNameColorVip()));
	     if(CustomConfig.ALLOW_VIP_TCOLOR)
//			 player.getAppearance().setTitleColor(Integer.decode("0x" + player.getOriginalTColor().substring(4,6) + player.getOriginalTColor().substring(2,4) + player.getOriginalTColor().substring(0,2)));//player.getNameColorVip());//(player.getAppearance().getNameColor());
			player.getAppearance().setTitleColor(Integer.decode("0x" + player.getOriginalTColor().substring(4,6) + player.getOriginalTColor().substring(2,4) + player.getOriginalTColor().substring(0,2)));
        
		player.broadcastCharInfo();
		player.broadcastUserInfo();
	}
	public static final VipTaskManager getInstance()
	{
		return SingletonHolder._instance;
	}

	private static class SingletonHolder
	{
		protected static final VipTaskManager _instance = new VipTaskManager();
	}
}