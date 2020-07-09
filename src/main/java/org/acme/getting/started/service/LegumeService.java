package org.acme.getting.started.service;

import org.acme.getting.started.model.Fruit;
import org.acme.getting.started.model.Legume;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class LegumeService {

    @Inject
    EntityManager em;

    @Transactional
    public void createLegume(Legume legume) {
        em.persist(legume);
    }

    public List<Legume> getAll() {
        return em.createQuery("Select legume from Legume legume", Legume.class).getResultList();
    }

    @Transactional
    public void delete(Long legumeId) {
        Legume legume = this.findById(legumeId);
        em.remove(legume);
    }

    public Legume findById(Long legumeId) {
        return em.find(Legume.class, legumeId);
    }

    @Transactional
    public void updateLegume(Legume legume) {
        em.merge(legume);
    }
}
