package net.sf.l2j.gameserver.handler.admincommandhandlers.Custom;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import Customs.BalanceStatus.BalanceLoad;


public class AdminBalanceStat implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_balance"
	};

	@Override
	public boolean useAdminCommand(String command, Player activeChar)
	{
		if (command.equals("admin_balance") && activeChar.isGM())
		{
			NpcHtmlMessage htm = new NpcHtmlMessage(0);
			htm.setFile("./data/html/admin/balance/main.htm");
			activeChar.sendPacket(htm);
		}
		else if (command.equals("admin_reloadbalance") && activeChar.isGM()){
			BalanceLoad.LoadEm();
			activeChar.sendMessage("Balance stats for classes has been reloaded.");
		}
		return true;
	}

	public static void sendBalanceWindow(int classId, Player p)
	{
		NpcHtmlMessage htm = new NpcHtmlMessage(0);
		htm.setFile("./data/html/admin/balance/balance.htm");
		
		htm.replace("%classId%", classId + "");
		htm.replace("%Patk%", BalanceLoad.loadPAtk(classId) + "");
		htm.replace("%Matk%", BalanceLoad.loadMAtk(classId) + "");
		htm.replace("%Pdef%", BalanceLoad.loadPDef(classId) + "");
		htm.replace("%Mdef%", BalanceLoad.loadMDef(classId) + "");
		htm.replace("%Acc%", BalanceLoad.loadAccuracy(classId) + "");
		htm.replace("%Eva%", BalanceLoad.loadEvasion(classId) + "");
		htm.replace("%AtkSp%", BalanceLoad.loadPAtkSpd(classId) + "");
		htm.replace("%CastSp%", BalanceLoad.loadMAtkSpd(classId) + "");
		htm.replace("%Cp%", BalanceLoad.loadCP(classId) + "");
		htm.replace("%Hp%", BalanceLoad.loadHP(classId) + "");
		htm.replace("%Mp%", BalanceLoad.loadMP(classId) + "");
		htm.replace("%Speed%", BalanceLoad.loadSpeed(classId) + "");
		
		p.sendPacket(htm);
	}

	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}