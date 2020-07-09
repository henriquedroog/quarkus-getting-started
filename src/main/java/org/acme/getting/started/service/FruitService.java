package org.acme.getting.started.service;

import org.acme.getting.started.model.Fruit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class FruitService {

    @Inject
    EntityManager em;

    @Transactional
    public void createFruit(Fruit fruit) {
        em.persist(fruit);
    }

    public List<Fruit> getAll() {
        return em.createQuery("Select fruit from Fruit fruit", Fruit.class).getResultList();
    }

    @Transactional
    public void delete(Long fruitId) {
        Fruit fruit = this.findById(fruitId);
        em.remove(fruit);
    }

    public Fruit findById(Long fruitId) {
        return em.find(Fruit.class, fruitId);
    }

    @Transactional
    public void updateFruit(Fruit fruit) {
        em.merge(fruit);
    }
}
