package dao;

import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BasicRepository<E, T extends Serializable> implements Repository<E, T> {

    private final SessionFactory sessionFactory;
    private final Class<E> clazz;

    public BasicRepository(SessionFactory sessionFactory, Class<E> clazz) {
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
    }
    
    private Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<E> findAll() {
         Session session = getCurrentSession();

        var criteria = session.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);

        return session.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<E> findByName(String name) {
         Session session = getCurrentSession();

        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(clazz);
        var root = criteria.from(clazz);

        criteria.select(root)
                .where(cb.equal(cb.lower(root.get("name")), name.toLowerCase()));

        E singleResult = session.createQuery(criteria).getSingleResult();

        return Optional.ofNullable(singleResult);
    }

    @Override
    public Optional<E> findById(T id) {
         Session session = getCurrentSession();

        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public E save(E entity) {
         Session session = getCurrentSession();

        session.persist(entity);
        //session.flush();

        return entity;
    }

    @Override
    public void update(E entity) {
         Session session = getCurrentSession();

        session.merge(entity);
    }

    @Override
    public int delete(T id) {
         Session session = getCurrentSession();

        return session.createMutationQuery(
                        "DELETE FROM " + clazz.getSimpleName() + " WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
