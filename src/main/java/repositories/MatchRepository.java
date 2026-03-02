package repositories;

import models.Match;
import org.hibernate.SessionFactory;

public class MatchRepository extends BasicRepository<Match, Integer> {

    public MatchRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Match.class);
    }
}
