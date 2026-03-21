package repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<E, K extends Serializable> {
    List<E> findAll();
    Optional<E> findById(K id);
    void save(E entity);
    void update(E entity);
    int delete(K id);
}