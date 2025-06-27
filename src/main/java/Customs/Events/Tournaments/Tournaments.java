package Customs.Events.Tournaments;

import Customs.Events.Tournaments.data.TournamentState;
import net.sf.l2j.gameserver.enums.TeamType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.clientpackets.Say2;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class Tournaments extends AbstractTournament
{
public Tournaments(int size)
{
	super(size);
}

@Override
public void onDisconnect(Player player)
{
	super.onDisconnect(player);

	if (_state == TournamentState.STARTED)
	{
		TeamType winner = player.getTeam() == TeamType.RED ? TeamType.BLUE : TeamType.RED;

		getAllPlayer().forEach(p -> p.sendPacket(new CreatureSay(0, Say2.TRADE, "[Tournament]", player.getName() + " left the event. " + winner.name() + " team won the match!")));

		stopFight(winner);
		cancel();
	}
}

@Override
public void onDie(Player player)
{
	if (player.getTeam() == TeamType.RED)
	{
		_redLives--;
	}
	else if (player.getTeam() == TeamType.BLUE)
	{
		_blueLives--;
	}

//	stopFightAll();

	if (_redLives <= 0)
	{
		TeamType winner = TeamType.BLUE;

		getAllPlayer().forEach(p -> p.sendPacket(new CreatureSay(0, Say2.TRADE, "[Tournament]", "Event finish. Team (" + winner.name() + ") won the match. Congratulations!")));

		stopFight(winner);
		cancel();
	}
	else if (_blueLives <= 0)
	{
		TeamType winner = TeamType.RED;

		getAllPlayer().forEach(p -> p.sendPacket(new CreatureSay(0, Say2.TRADE, "[Tournament]", "Event finish. Team ("+  winner.name() + ") won the match. Congratulations!")));

		stopFight(winner);
		cancel();
	}
}

@Override
public void startFight()
{
	onStart();

	super.startFight();
}

@Override
public void onEnd()
{
	if (_state == TournamentState.STARTED)
	{
		if (_tick <= 0)
		{
			getAllPlayer().forEach(s -> s.sendPacket(new CreatureSay(0, Say2.TRADE, "[Tournament]", "Time is up! The event has ended with both teams tied.")));
			stopFight(null);
			cancel();
			return;
		}

		_tick--;
	}
}
}