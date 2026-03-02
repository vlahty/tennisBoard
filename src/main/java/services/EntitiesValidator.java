package services;

import repositories.PlayerRepository;
import utils.HibernateRunner;

public class EntitiesValidator{
    private final static PlayerRepository PLAYER_SERVICE = new PlayerRepository(HibernateRunner.buildSessionFactory());

    public EntitiesValidator() {
    }

    public static boolean isPlayerValid(String playerName){

        return false;
    }

}
