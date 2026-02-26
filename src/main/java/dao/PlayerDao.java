package dao;

import model.Player;
import org.hibernate.SessionFactory;

public class PlayerDao extends BasicRepository<Player, Integer> {

    public PlayerDao(SessionFactory sessionFactory) {
        super(sessionFactory, Player.class);
    }



}
