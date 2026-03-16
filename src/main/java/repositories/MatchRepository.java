package repositories;

import models.Match;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateRunner;

import java.util.ArrayList;
import java.util.List;

public class MatchRepository extends BasicRepository<Match, Integer> {

    SessionFactory sessionFactory;


    public MatchRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Match.class);
        this.sessionFactory = sessionFactory;
    }

    public List<Match> findAllByName(String name, int page, int pageSize) {
        Session session = sessionFactory.getCurrentSession();

        String hql = " SELECT m FROM Match m" +
                     " WHERE LOWER(m.player1.name) LIKE LOWER(:name) " +
                     " OR LOWER(m.player2.name) LIKE LOWER(:name) " +
                     " ORDER BY m.id";

        return session.createQuery(hql, Match.class)
                .setParameter("name", name)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public long totalPagesForAllMatches() {
        Session session = sessionFactory.getCurrentSession();

        String hql = " SELECT count(m) FROM Match m";

        return session.createQuery(hql, Long.class)
                .getSingleResult();

    }

    public long totalPagesForNamedMatches(String name) {
        Session session = sessionFactory.getCurrentSession();

        String hql = " SELECT COUNT(m) FROM Match m " +
                     " WHERE LOWER(m.player1.name) LIKE LOWER(:name) " +
                     " OR LOWER(m.player2.name) LIKE LOWER(:name)";

        return session.createQuery(hql, Long.class)
                .setParameter("name", name)
                .getSingleResult();

    }


}
