import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateRunner;

public class Main {

    public static void main(String[] args) {

        //Завтра сделать тоже самое в тестовой БД в test
        try (SessionFactory sessionFactory = HibernateRunner.buildSessionFactory()) {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();


            session.getTransaction().commit();
        }

    }
}
