package com.postsquad.scoup.web;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.function.Consumer;

@Service
@Transactional
public class TestEntityManager {

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Object entity) {
        entityManager.persist(entity);
    }

    public <T> T find(Class<T> entityClass, Long id) {
        return entityManager.find(entityClass, id);
    }

    public <T> void findAndConsume(Class<T> entityClass, Long id, Consumer<T> consumer) {
        T t = entityManager.find(entityClass, id);
        consumer.accept(t);
    }
}
