package repositories;

import models.Player;
import org.hibernate.SessionFactory;

public class PlayerRepository extends BasicRepository<Player, Integer> {

    public PlayerRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Player.class);
    }
}
