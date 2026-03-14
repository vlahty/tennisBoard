package services;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import models.Player;
import models.additional.MatchScore;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repositories.PlayerRepository;
import utils.HibernateRunner;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class OngoingMatchesService implements ServletContextListener {

    private final Map<UUID, MatchScore> ongoingMatches = new ConcurrentHashMap<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("ongoingMatchesService", this);
    }

    public MatchScore getOngoingMatchScore(UUID matchId) {

        return ongoingMatches.get(matchId);
    }

    public void deleteFromOngoingMatches(UUID matchId){
        ongoingMatches.remove(matchId);
    }

    public MatchScore generateNewMatchScore(String playerName1, String playerName2) {

        try (SessionFactory sessionFactory = HibernateRunner.buildSessionFactory()) {
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();

            PlayerRepository playerRepository = new PlayerRepository(sessionFactory);

            if (playerRepository.findByName(playerName1).isEmpty()) {
                playerRepository.save(Player.builder().name(playerName1).build());
            }

            if (playerRepository.findByName(playerName2).isEmpty()) {
                playerRepository.save(Player.builder().name(playerName2).build());
            }

            MatchScore matchScore = new MatchScore(UUID.randomUUID(), playerName1, playerName2);

            this.ongoingMatches.put(matchScore.getMatchId(), matchScore);

            transaction.commit();

            return matchScore;
        }
    }

}
