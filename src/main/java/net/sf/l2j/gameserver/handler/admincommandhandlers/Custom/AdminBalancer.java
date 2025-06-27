package net.sf.l2j.gameserver.handler.admincommandhandlers.Custom;


import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.Balance.ClassBalanceGui;

/**
 * Admin handler for calling Balancer system in Community Board.
 */
public class AdminBalancer implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_balancer"
	};
	
	@Override
	public boolean useAdminCommand(String command, Player activeChar)
	{
		if (command.equals("admin_balancer"))
		{
			ClassBalanceGui.getInstance().parseCmd("_bbs_balancer", activeChar);
			return true;
		}
		
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}