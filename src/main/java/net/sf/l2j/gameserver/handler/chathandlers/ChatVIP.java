package net.sf.l2j.gameserver.handler.chathandlers;

import net.sf.l2j.gameserver.handler.IChatHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.FloodProtectors;
import net.sf.l2j.gameserver.network.FloodProtectors.Action;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class ChatVIP implements IChatHandler
{
	private static final int[] COMMAND_IDS =
	{
		18
	};
	
	@Override
	public void handleChat(int type, Player activeChar, String target, String text)
	{
		if (!activeChar.isVip())
			return;
		
		if (!FloodProtectors.performAction(activeChar.getClient(), Action.HERO_VOICE))
			return;
		
		final CreatureSay cs = new CreatureSay(activeChar.getObjectId(), Say2.CRITICAL_ANNOUNCE , activeChar.getName(), text);
		for (Player player : World.getInstance().getPlayers())
			player.sendPacket(cs);
	}
	
	@Override
	public int[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}