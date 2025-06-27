package Customs.Events.Tournaments;

import net.sf.l2j.commons.logging.CLogger;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TournamentManager {
    private static final CLogger LOGGER = new CLogger(TournamentManager.class.getName());

    private static final Map<Integer, AbstractTournament> _tournaments = new ConcurrentHashMap<>();

    public static void init() {
        /*if (!TournamentConfig.init()) {
            LOGGER.info("[TournamentManager] Failed to load configuration files. Tournaments won't load.");
            return;
        }*/

        for (int teamSize : TournamentConfig.TOURNAMENTS) {
            _tournaments.put(teamSize, new Tournaments(teamSize));
        }

        LOGGER.info("[TournamentManager] Initialized " + _tournaments.size() + " tournament holders:");

        for (AbstractTournament tournament : getTournaments()) {
            LOGGER.info(tournament.getTeamSize() + " vs " + tournament.getTeamSize());
        }
    }


    public static AbstractTournament getTournament(int size) {
        return _tournaments.get(size);
    }

    public static Collection<AbstractTournament> getTournaments() {
        return _tournaments.values();
    }

    public static void replaceTournament(int size) {
        _tournaments.put(size, new Tournaments(size));
    }
}