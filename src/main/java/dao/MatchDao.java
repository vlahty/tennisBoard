package dao;

import model.Match;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class MatchDao extends BasicRepository<Match, Integer> {

    public MatchDao(SessionFactory sessionFactory, Class<Match> clazz) {
        super(sessionFactory, clazz);
    }
}
