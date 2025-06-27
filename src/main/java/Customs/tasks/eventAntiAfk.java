package Customs.tasks;

import java.util.ArrayList;
import java.util.logging.Logger;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.Events.CTF.CTFEvent;
import Customs.Events.TvT.TvTEvent;


/**
 * @author @IcathiaLord
 *
 */

public class eventAntiAfk {
		protected static final Logger _log = Logger.getLogger(eventAntiAfk.class.getName());
		static ArrayList<Player> _players;
		
		eventAntiAfk(){
			_players = new ArrayList<>();
			_log.info("Event Anti AFk Task: System initiated.");
			ThreadPool.scheduleAtFixedRate(new AntiAfk(), 5000 , 1000);
		}

		private class AntiAfk implements Runnable{
			AntiAfk(){}
			@Override
			public void run(){
					synchronized (World.getInstance().getPlayers()){
						// Iterate over all players
						for (Player p : World.getInstance().getPlayers()){
								if (p != null && p.isOnline() && !p.isDead() && p.isInFunEventRan()  && !p.isGM() && !p.isImmobilized() && !p.isParalyzed()){
									synchronized (p) {
										SpawnInfo(p,p.getX(),p.getY(),p.getZ());
									}
								}
						}
					}
			}
		}
		
		public static ArrayList<Player> getPlayers(){
				return _players;
		}

		public static void removePlayerAfk(Player p) {
			if(_players != null && _players.contains(p))
				_players.remove(p);
			
		}
		
		
		static void SpawnInfo(Player p, int _x, int _y, int _z)
		{
			if (!_players.contains(p) ){
				p.setLastCordsEvent(_x, _y, _z);
				p.setSameLocEvent(0);
				_players.add(p);
			}
			else{
				if(_players.contains(p)){
					if (_x == p.getLastXEvent() && _y == p.getLastYEvent() && _z == p.getLastZEvent() && !p.isAttackingNow() && !p.isCastingNow() && p.isOnline() && !p.isParalyzed() ){
						p.increaseSameLocEvent();
						
						if (p.getSameLocEvent() >= 1 * 60 ){	
							p.sendMessage("You kicked from event because you were afk.");
							
							if(TvTEvent.isPlayerParticipant(p.getObjectId())) 
								TvTEvent.onLogout(p);
							
							if(CTFEvent.isPlayerParticipant(p.getObjectId())) 
								CTFEvent.onLogout(p);
							
							removePlayerAfk(p);
							return;
						}

						p.setLastCordsEvent(_x, _y, _z);
						p.setSameLocEvent(p.getSameLocEvent());
						return;
					}

					p.setLastCordsEvent(_x, _y, _z);
					p.setSameLocEvent(0);
				}
			}
		}


		public static eventAntiAfk getInstance()
		{
			return SingletonHolder._instance;
		}
		
		private static class SingletonHolder
		{
			protected static final eventAntiAfk _instance = new eventAntiAfk();
		}

}
