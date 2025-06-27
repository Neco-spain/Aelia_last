package net.sf.l2j.gameserver.handler.tutorialhandlers;

import net.sf.l2j.gameserver.handler.ITutorialHandler;
import net.sf.l2j.gameserver.model.actor.Player;

import Customs.mods.StartupSystem;

public class StartupHandler implements ITutorialHandler
{
	private static final String[] LINK_COMMANDS =
	{
		"start"
	};

	@Override
	public boolean useLink(String _command, Player activeChar, String params)
	{
		if (_command.startsWith("start"))
		{
			StartupSystem.handleCommands(activeChar, params);
		}
		return true;
	}

	@Override
	public String[] getLinkList()
	{
		return LINK_COMMANDS;
	}
}