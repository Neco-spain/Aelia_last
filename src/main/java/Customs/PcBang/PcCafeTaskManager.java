package Customs.PcBang;

import Custom.CustomConfig;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.World;

/**
 * @author Williams
 *
 */
public class PcCafeTaskManager implements Runnable
{
	public PcCafeTaskManager()
	{
		ThreadPool.scheduleAtFixedRate(this, CustomConfig.PCB_INTERVAL * 1000 * 60, CustomConfig.PCB_INTERVAL * 1000 * 60);
	}
	
	@Override
	public void run()
	{
		World.getInstance().getPlayers().stream().filter(player -> player.getLevel() >= CustomConfig.PCB_MIN_LEVEL && !player.getClient().isDetached()).forEach(player ->
		{	
			if (!player.isPlayerAfk())
				player.increasePcCafePoints(Rnd.get(CustomConfig.PCB_POINT_MIN, CustomConfig.PCB_POINT_MAX), Rnd.get(100) <= CustomConfig.PCB_CHANCE_DUAL_POINT);
		});
	}
	
	public static final PcCafeTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final PcCafeTaskManager INSTANCE = new PcCafeTaskManager();
	}	
}