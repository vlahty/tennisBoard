package repositories;

import models.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.Optional;

public class PlayerRepository extends BasicRepository<Player, Integer> {

    SessionFactory sessionFactory;

    public PlayerRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Player.class);
        this.sessionFactory = sessionFactory;
    }

    public Optional<Player> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();

        String hql = " FROM Player WHERE lower(name) = LOWER(:name) ";

        return session.createSelectionQuery(hql, Player.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }
}