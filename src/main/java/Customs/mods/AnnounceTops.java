package Customs.mods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.data.manager.HeroManager;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

import Custom.CustomConfig;

public class AnnounceTops
{
	private static final Logger _log = Logger.getLogger(AnnounceTops.class.getName());
	
	public static void Announce(Player activeChar){
		pvp(activeChar);
		pk(activeChar);
		hero(activeChar);
		castle(activeChar);
	}
	
	public static void pvp(Player activeChar){
		if (CustomConfig.ANNOUNCE_TOP_PVP)
		{
			int objId = 0;
			
			try(Connection con = L2DatabaseFactory.getInstance().getConnection())
			{
				PreparedStatement statement = con.prepareStatement("SELECT obj_Id FROM characters WHERE accesslevel = 0 ORDER BY pvpkills DESC LIMIT 1");
				ResultSet rset = statement.executeQuery();
				
				while (rset.next())
				{
					objId = rset.getInt("obj_Id");
				}
				rset.close();
				statement.close();
			}
			catch (Exception e)
			{
				_log.warning("[EnterWorldServices]: Failed to get top Pvp killer.");
				e.printStackTrace();
			}
			
			if (objId != 0 && objId == activeChar.getObjectId() && activeChar.getPvpKills() > 0)
				World.toAllOnlinePlayers(new CreatureSay(0,3,activeChar.getName(), "Most PvP killer with " + activeChar.getPvpKills() + " kills Joined in!"));
			else
				return;
		}
	}
	
	public static void pk(Player activeChar){
		if (CustomConfig.ANNOUNCE_TOP_PK && !activeChar.isGM())
		{
			int objId = 0;
			
			try(Connection con = L2DatabaseFactory.getInstance().getConnection())
			{
				PreparedStatement statement = con.prepareStatement("SELECT obj_Id FROM characters WHERE accesslevel = 0 ORDER BY pkkills DESC LIMIT 1");
				ResultSet rset = statement.executeQuery();
				
				while (rset.next())
				{
					objId = rset.getInt("obj_Id");
				}
				rset.close();
				statement.close();
			}
			catch (Exception e)
			{
				_log.warning("[EnterWorldServices]: Failed to get top Pk killer.");
				e.printStackTrace();
			}
			
			if (objId != 0 && objId == activeChar.getObjectId() && activeChar.getPkKills() > 0)
				World.toAllOnlinePlayers(new CreatureSay(0,3,activeChar.getName(), "Most Pk killer with " + activeChar.getPkKills() + " kills Joined in!"));
			else
     			return;
		}
		
	}
	
	public static void hero(Player activeChar){
		if (CustomConfig.ANNOUNCE_HERO_ON_ENTER && activeChar.isHero())
		{
			if(activeChar.isGM())
				return;

			if(HeroManager.getInstance().getHeroByClass(activeChar.getBaseClassDona()) != 0 ) {
				World.toAllOnlinePlayers(new CreatureSay(0,Say2.HERO_VOICE,"Lord of" ,  activeChar.getClassNameOnChangedClass() + " " + activeChar.getName() + " has logged in!" ));
			}
			else if(HeroManager.getInstance().getHeroByClass(activeChar.getBaseClass()) != 0)
				World.toAllOnlinePlayers(new CreatureSay(0,Say2.HERO_VOICE,"Lord of" ,  activeChar.getClassName() + " " + activeChar.getName() + " has logged in!" ));

		}
	}
	
	public static void castle(Player activeChar){
		if (CustomConfig.ANNOUNCE_CASTLE_LORDS)
		{
			if(activeChar.isGM())
				return;
			
			if (activeChar.getClan() != null)
			{
				if (activeChar.getClan().getLeaderName().equals(activeChar.getName()))
				{
					if (CastleManager.getInstance().getCastleByOwner(activeChar.getClan()) != null)
					{
						World.announceToOnlinePlayers(activeChar.getName()+" leader of "+CastleManager.getInstance().getCastleByOwner(activeChar.getClan()).getName()+" has logged in!");
					}
				}
			}
		}
	}
	
    public static AnnounceTops getInstance()
    {
        return SingletonHolder._instance;
    }
    private static class SingletonHolder
    {
        protected static final AnnounceTops _instance = new AnnounceTops();
    }
    public boolean isOn(){
    		return true;
    }
	
}
