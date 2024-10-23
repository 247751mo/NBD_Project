package repositories;

import java.util.List;
import java.util.UUID;

public interface Repo<T> {

    T get(UUID id);
    List<T> getAll();
    T add(T object);
    void delete(T t);
    void update(T t);
}