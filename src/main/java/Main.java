import dao.PlayerDao;
import model.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateRunner;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        //Завтра сделать тоже самое в тестовой БД в test
        try (SessionFactory sessionFactory = HibernateRunner.buildSessionFactory()) {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();


            PlayerDao playerDao = new PlayerDao(sessionFactory);
            Player vlad = Player.builder().name("Vlad").build();
            playerDao.save(vlad);

            List<Player> all = playerDao.findAll();
            all.forEach(System.out::println);

            playerDao.findById(1);

            playerDao.findByName("Vlad");

            vlad.setName("Vlad Popov");
            playerDao.findByName("Vlad Popov");

           // playerDao.delete(vlad.getId());

            System.out.println("Ну вот и всё!");

            session.getTransaction().commit();
        }

    }
}
