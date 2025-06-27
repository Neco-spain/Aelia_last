package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.handler.ITutorialHandler;
import net.sf.l2j.gameserver.handler.TutorialHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.QuestState;

public class RequestTutorialLinkHtml extends L2GameClientPacket
{
	String _bypass;
	
	@Override
	protected void readImpl()
	{
		_bypass = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		//custom startup
		if (_bypass.startsWith("-h"))
		{
			_bypass = _bypass.substring(2);
			
			if (_bypass.startsWith("_"))
				_bypass = _bypass.substring(1);
		}
		
		final ITutorialHandler handler = TutorialHandler.getInstance().getHandler(_bypass);

		if (handler != null)
		{
			String command = _bypass;
			String params = "";
			if (_bypass.indexOf("_") != -1)
			{
				command = _bypass.substring(0, _bypass.indexOf("_"));
				params = _bypass.substring(_bypass.indexOf("_")+1, _bypass.length());
			}
			handler.useLink(command, player, params);
		}
		else
		{
			System.out.println(getClient() + " sent not handled RequestTutorialLinkHtml: [" + _bypass + "]");
		}
		
		QuestState qs = player.getQuestState("Tutorial");
		if (qs != null)
			qs.getQuest().notifyEvent(_bypass, null, player);
	}
}