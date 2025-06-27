package net.sf.l2j.gameserver.handler.admincommandhandlers.Custom;

import java.util.logging.Logger;

import Customs.Events.Tournaments.data.EventTask;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;


/**
 * @author IcathiaLord
 *
 */
public class AdminTournament implements IAdminCommandHandler
{
	protected static final Logger _log = Logger.getLogger(AdminCustom.class.getName());
	public static boolean _arena_manual = false;
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_tour"
	};

	@Override
	public boolean useAdminCommand(String command, Player activeChar)
	{
		if (command.equals("admin_tour"))
		{
			if (EventTask._started)
			{
				_log.info("----------------------------------------------------------------------------");
				_log.info("[Tournament]: Event Finished.");
				_log.info("----------------------------------------------------------------------------");
				EventTask._aborted = true;
				EventTask.finishEvent();

				activeChar.sendMessage("SYS: Event finished");
			}
			else
			{
				_log.info("----------------------------------------------------------------------------");
				_log.info("[Tournament]: Event Started.");
				_log.info("----------------------------------------------------------------------------");
				EventTask.SpawnEvent();
				activeChar.sendMessage("SYS: Event started");
			}
		}

		return true;
	}
	

	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
}
