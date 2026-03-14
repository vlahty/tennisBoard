package mappers;

import models.Match;
import models.Player;
import models.additional.MatchScore;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repositories.PlayerRepository;
import utils.HibernateRunner;

public class MatchScoreToMatchMapper {


    public static Match getMatch(MatchScore matchScore) {


        try (SessionFactory sessionFactory = HibernateRunner.buildSessionFactory()) {
            var session = sessionFactory.getCurrentSession();
            var transaction = session.beginTransaction();

            PlayerRepository playerRepository = new PlayerRepository(sessionFactory);
            Player player1;
            Player player2;

            if (playerRepository.findByName(matchScore.getPlayer1Name()).isPresent()) {
                player1 = playerRepository.findByName(matchScore.getPlayer1Name()).get();
            } else player1 = new Player();
            if (playerRepository.findByName(matchScore.getPlayer2Name()).isPresent()) {
                player2 = playerRepository.findByName(matchScore.getPlayer2Name()).get();
            } else player2 = new Player();

            transaction.commit();


            return Match.builder()
                    .id(matchScore.getMatchId())
                    .player1(player1)
                    .player2(player2)
                    .winner(matchScore.getWinner() == 1 ? player1 : player2)
                    .build();
        }
    }


}
