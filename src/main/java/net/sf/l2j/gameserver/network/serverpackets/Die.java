package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.enums.SiegeSide;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.pledge.Clan;

public class Die extends L2GameServerPacket
{
	private final Creature _activeChar;
	private final int _charObjId;
	private final boolean _fake;
	
	private boolean _sweepable;
	private boolean _allowFixedRes;
	private Clan _clan;
	//custom 

	private boolean _event;
	private boolean _allowRevivePvp;

	public Die(Creature cha)
	{
		_activeChar = cha;
		_charObjId = cha.getObjectId();
		_fake = !cha.isDead();
		
		if (cha instanceof Player)
		{
			Player player = (Player) cha;
			_allowFixedRes = player.getAccessLevel().allowFixedRes();
			_clan = player.getClan();
			
			//custom 
			_event = player.isInFunEvent() || player.isInTournament();

			_allowRevivePvp = player.isInsideZone(ZoneId.CHANGE_PVP_ZONE);


		}
		else if (cha instanceof Monster)
			_sweepable = ((Monster) cha).isSpoiled();
	}
	
	@Override
	protected final void writeImpl()
	{
		if (_fake)
			return;
		
		writeC(0x06);
		writeD(_charObjId);
		
		if(_event)
			return;
		
		writeD(0x01); // to nearest village
		
		if (_clan != null)
		{
			SiegeSide side = null;
			
			final Siege siege = CastleManager.getInstance().getActiveSiege(_activeChar);
			if (siege != null)
				side = siege.getSide(_clan);
			
			writeD((_clan.hasClanHall()) ? 0x01 : 0x00); // to clanhall
			writeD((_clan.hasCastle() || side == SiegeSide.OWNER || side == SiegeSide.DEFENDER) ? 0x01 : 0x00); // to castle
			writeD((side == SiegeSide.ATTACKER && _clan.getFlag() != null) ? 0x01 : 0x00); // to siege HQ
		}
		else
		{
			writeD(0x00); // to clanhall
			writeD(0x00); // to castle
			writeD(0x00); // to siege HQ
		}
		
		writeD((_sweepable) ? 0x01 : 0x00); // sweepable (blue glow)
		writeD((_allowRevivePvp || _allowFixedRes) ? 0x01 : 0x00); // FIXED
	}
}