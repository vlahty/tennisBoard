package controllers;

import models.Match;
import models.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repositories.PlayerRepository;
import utils.HibernateRunner;

public class MatchGeneratorController {

    public static Match generateNewMatch(String playerName1, String playerName2) {

        PlayerRepository playerRepository;
        //MatchRepository matchRepository;


        try (SessionFactory sessionFactory = HibernateRunner.buildSessionFactory()) {

            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();

            playerRepository = new PlayerRepository(sessionFactory);
            //matchRepository = new MatchRepository(sessionFactory);

            if (playerRepository.findByName(playerName1).isEmpty()) {
                playerRepository.save(Player.builder().name(playerName1).build());
            }

            if (playerRepository.findByName(playerName2).isEmpty()) {
                playerRepository.save(Player.builder().name(playerName2).build());
            }

            //Добавить проверку на player1!=player2
            Player player1 = playerRepository.findByName(playerName1).get();
            Player player2 = playerRepository.findByName(playerName2).get();

            Match currentMatch = Match.builder()
                    .player1(player1)
                    .player2(player2)
                    .build();

            //matchRepository.save(currentMatch);

            transaction.commit();

            return currentMatch;
        }
    }
}
