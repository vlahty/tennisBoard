package util;

import model.Match;
import model.Player;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateRunner {

    public static SessionFactory buildSessionFactory(){

        Configuration configuration = new Configuration();

        configuration.configure();
        configuration.addAnnotatedClass(Player.class);
        configuration.addAnnotatedClass(Match.class);

        return configuration.buildSessionFactory();
    }
}
