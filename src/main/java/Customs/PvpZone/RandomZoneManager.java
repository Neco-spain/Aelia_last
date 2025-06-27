package Customs.PvpZone;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import dev.l2j.tesla.autobots.AutobotsManager;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.data.manager.ZoneManager;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.zone.type.RandomZone;

import Custom.CustomConfig;


public final class RandomZoneManager implements Runnable
{
	public int _zoneId;
	private int _timer;
	//reward
	private static Map<Integer, TheHourHolder> _player;
	private static TheHourHolder _topPlayer;
	
	public RandomZoneManager()
	{
		_player = new ConcurrentHashMap<>();
		if (getTotalZones() > 1)
			ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}
	
	@Override
	public void run()
	{
		if (_timer > 0)
			_timer--;
		
		switch (_timer)
		{
			case 0:
				selectNextZone();
				World.announceToOnlinePlayers("PvP zone has been changed to: " + getCurrentZone().getName(), true);
				for (Player player : World.getInstance().getPlayers())
					if (player != null && player.isOnline() && player.isInsideZone(ZoneId.CHANGE_PVP_ZONE))
						player.teleportTo( getCurrentZone().getLoc() , 20);

				AutobotsManager.INSTANCE.changePvPzone();

				break;
			case 60:
			case 300:
			case 600:
			case 900:
			case 1800:
				World.announceToOnlinePlayers("PvP Zone: " + _timer /60 + " minute(s) remaining until Zone will be changed." , false);
				break;
			case 3600:
			case 7200:
				World.announceToOnlinePlayers("PvP Zone: "+ (_timer / 60) / 60 + " hour(s) remaining until Zone will be changed." , false);
				
				break;
		}
	}

	public int getZoneId()
	{
		return _zoneId;
	}

	public void selectNextZone()
	{
		if (_topPlayer != null){
			if(!_topPlayer.getName().equals("")){
				World.announceToOnlinePlayers("PvP Zone Most PvP Player was: " + _topPlayer.getName() + " With " + _topPlayer.getKills() + " PvPs");
				giveReward(_topPlayer.getObj());
			}
		}
		
		int nextZoneId = Rnd.get(1, getTotalZones());
		while (getZoneId() == nextZoneId)
			nextZoneId = Rnd.get(1, getTotalZones());
		_zoneId = nextZoneId;
		
		_timer = getCurrentZone().getTime() + 10;
		
		_player.clear();
		_topPlayer = null;
	}

	public final RandomZone getCurrentZone()
	{
		return ZoneManager.getInstance().getAllZones(RandomZone.class).stream().filter(t -> t.getId() == getZoneId()).findFirst().orElse(null);
	}

	
	public String getLeftTime() {
		return timeToLeft(_timer);
	}
	
	private static String timeToLeft(int timer)
	{
		long time = timer;
		return String.format("%d mins, %d sec", TimeUnit.SECONDS.toMinutes(time), TimeUnit.SECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(time)));
	}
	
	public static int getTotalZones()
	{
		int size = 0;
		for (RandomZone i : ZoneManager.getInstance().getAllZones(RandomZone.class))
		{
			if (i == null)
				continue;
			
			size++;
		}
		return size;
	}
	
	
	public static void giveReward(int obj)
	{
		Player player = World.getInstance().getPlayer(obj);
		
		if (player != null && player.isOnline())
		{
			for(Integer id : RandomZone._rewards.keySet())
				player.addItem("PlayerOfTheHour", id , RandomZone._rewards.get(id) , player, true);
		}
		else {
			for (Integer id : RandomZone._rewards.keySet()) {
				ItemInstance item = new ItemInstance(IdFactory.getInstance().getNextId(), id);

				try (Connection con = L2DatabaseFactory.getInstance().getConnection(); PreparedStatement stm_items = con.prepareStatement("INSERT INTO items (owner_id,item_id,count,loc,loc_data,enchant_level,object_id,custom_type1,custom_type2,mana_left,time) VALUES (?,?,?,?,?,?,?,?,?,?,?)")) {
					stm_items.setInt(1, obj);
					stm_items.setInt(2, item.getItemId());
					stm_items.setInt(3, RandomZone._rewards.get(id));
					stm_items.setString(4, "INVENTORY");
					stm_items.setInt(5, 0);
					stm_items.setInt(6, item.getEnchantLevel());
					stm_items.setInt(7, item.getObjectId());
					stm_items.setInt(8, 0);
					stm_items.setInt(9, 0);
					stm_items.setInt(10, -60);
					stm_items.setLong(11, System.currentTimeMillis());
					stm_items.executeUpdate();
					stm_items.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void addKillsInZone(Player activeChar)
	{
		if (!_player.containsKey(activeChar.getObjectId()))
			_player.put(activeChar.getObjectId(), new TheHourHolder(activeChar.getName(), 1, activeChar.getObjectId()));
		else
			_player.get(activeChar.getObjectId()).setPvpKills();
		
		if (_topPlayer == null)
			_topPlayer = new TheHourHolder(activeChar.getName(), 1, activeChar.getObjectId());
		
		else if (_player.get(activeChar.getObjectId()).getKills() > _topPlayer.getKills())
			_topPlayer = _player.get(activeChar.getObjectId());
	}
	
	
	public static final RandomZoneManager getInstance()
	{
		return SingletonHolder.INSTANCE;	
		}
	
	private static class SingletonHolder
	{
		protected static final RandomZoneManager INSTANCE = new RandomZoneManager();
	}
}
