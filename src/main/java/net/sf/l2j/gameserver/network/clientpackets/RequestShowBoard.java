package net.sf.l2j.gameserver.network.clientpackets;

import Custom.CustomConfig;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.communitybbs.CommunityBoard;
import net.sf.l2j.gameserver.network.GameClient;

public final class RequestShowBoard extends L2GameClientPacket
{
	@SuppressWarnings("unused")
	private int _unknown;
	
	@Override
	protected void readImpl()
	{
		_unknown = readD();
	}
	
	@Override
	protected void runImpl()
	{
		if(CustomConfig.ENABLE_CUSTOM_CB)
			CommunityBoard.getInstance().handleCommands(getClient(), CustomConfig.CB_DEFAULT);
		else
			CommunityBoard.getInstance().handleCommands(getClient(), Config.BBS_DEFAULT);
	}
}