package services;

import controllers.MatchGeneratorController;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import models.Match;
import models.Player;
import models.secondary.MatchDTO;
import models.secondary.Score;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utils.HibernateRunner;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class OngoingMatchesService implements ServletContextListener {

    private final Map<UUID, MatchDTO> ongoingMatches = new ConcurrentHashMap<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println(">>> OngoingMatchesService: CONTEXT INITIALIZED! <<<");

        ServletContext servletContext = sce.getServletContext();

        servletContext.setAttribute("ongoingMatchesService", this);
    }

    public Match createNewMatch(String playerName1, String playerName2) {

        try (SessionFactory sessionFactory = HibernateRunner.buildSessionFactory()) {
            Session currentSession = sessionFactory.getCurrentSession();
            Transaction transaction = currentSession.beginTransaction();

            Match match = MatchGeneratorController.generateNewMatch(playerName1, playerName2);
            UUID uuid = UUID.randomUUID();
            match.setId(uuid);


            ongoingMatches.put(uuid, MatchDTO.builder()
                            .id(uuid)
                            .player1(Player.builder().name(playerName1).build())
                            .player2(Player.builder().name(playerName2).build())
                            .score(new Score(20))
                    .build());

            transaction.commit();

            return match;
        }

    }

    public Score getOngoingMatchScore(UUID matchUUID) {

        return ongoingMatches.get(matchUUID).getScore();
    }

    public MatchDTO getOngoingMatchDTO(UUID matchUUID) {

        return ongoingMatches.get(matchUUID);
    }

}
