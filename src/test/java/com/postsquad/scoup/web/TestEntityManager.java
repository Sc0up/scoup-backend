package com.postsquad.scoup.web;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class TestEntityManager {

    @PersistenceContext
    private EntityManager entityManager;

    public void persist(Object entity) {
        entityManager.persist(entity);
    }

    public <T> T findById(Class<T> entityClass, Long id) {
        return entityManager.find(entityClass, id);
    }
}
