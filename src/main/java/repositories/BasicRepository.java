package repositories;

import models.Match;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BasicRepository<E, T extends Serializable> implements Repository<E, T> {

    private final SessionFactory sessionFactory;
    private final Class<E> clazz;

    public BasicRepository(SessionFactory sessionFactory, Class<E> clazz) {
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public List<E> findAll(int page, int pageSize, String sortedBy) {

        Session session = getCurrentSession();

        //TODO: Можно ли убрать e?
        //TODO: Когда использовать createSelectionQuery и createQuery?
        String hql = " FROM " + clazz.getSimpleName() + " e ORDER BY :id";

        return session.createSelectionQuery(hql, clazz)
                .setParameter("id", sortedBy)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }




    @Override
    public List<E> findAll() {
        Session session = getCurrentSession();

        var criteria = session.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);

        return session.createQuery(criteria).getResultList();
    }

    //TODO: Если использовать в Match будет выдавать ошибку
    @Override
    public Optional<E> findByName(String name) {
        Session session = getCurrentSession();

        String hql = " FROM " + clazz.getSimpleName() + " WHERE lower(name) = LOWER(:name) ";

        return session.createQuery(hql, clazz)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();

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
