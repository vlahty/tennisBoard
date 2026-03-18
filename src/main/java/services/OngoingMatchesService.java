package services;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import models.Player;
import models.additional.MatchScore;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repositories.PlayerRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class OngoingMatchesService implements ServletContextListener {

    private final Map<UUID, MatchScore> ongoingMatches = new ConcurrentHashMap<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("ongoingMatchesService", this);
    }

    public Optional<MatchScore> getOngoingMatchScore(UUID matchId) {
        return Optional.ofNullable(ongoingMatches.get(matchId));
    }

    public void deleteFromOngoingMatches(UUID matchId) {
        ongoingMatches.remove(matchId);
    }

    public MatchScore generateNewMatchScore(HttpServletRequest req, String playerName1, String playerName2) {
        SessionFactory sessionFactory = (SessionFactory)
                req.getServletContext().getAttribute("sessionFactory");
        Session session = sessionFactory.getCurrentSession();

        Transaction transaction = session.beginTransaction();

        PlayerRepository playerRepository = new PlayerRepository(sessionFactory);

        if (playerRepository.findByName(playerName1).isEmpty())
            playerRepository.save(Player.builder().name(playerName1).build());
        if (playerRepository.findByName(playerName2).isEmpty())
            playerRepository.save(Player.builder().name(playerName2).build());

        MatchScore matchScore = new MatchScore(UUID.randomUUID(), playerName1, playerName2);

        this.ongoingMatches.put(matchScore.getMatchId(), matchScore);

        transaction.commit();

        return matchScore;
    }
}
