package org.acme.getting.started.service;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.acme.getting.started.model.Fruit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@DisplayName("FruitService test cases")
public class FruitServiceTest {

    @Inject
    FruitService fruitService;

    @Inject
    EntityManager entityManager;

    @BeforeEach
    @Transactional
    public void init() {
        Query deleteQuery = entityManager.createQuery("DELETE FROM Fruit fruit");
        deleteQuery.executeUpdate();
    }

    @Test
    @DisplayName("Create Fruit with a valid object")
    public void createFruitTest_success() {
        Fruit newFruit =  new Fruit("Banana", "Tem Calcio");
        fruitService.createFruit(newFruit);
        Assertions.assertNotNull(newFruit);
        Assertions.assertNotNull(newFruit.getId());
        Fruit expectedFruit = fruitService.findById(newFruit.getId());
        Assertions.assertNotNull(expectedFruit);
        Assertions.assertEquals(expectedFruit.getName(), newFruit.getName());
        Assertions.assertEquals(expectedFruit.getId(), newFruit.getId());
        Assertions.assertEquals(expectedFruit.getDescription(), newFruit.getDescription());
    }

    @Test
    @DisplayName("Create Fruit with null object, should get an exception")
    public void createFruit_nullExceptionExpected() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fruitService.createFruit(null);
        });
    }

    @Test
    @DisplayName("Should get All fruits")
    public void getAllTest() {
        List<Fruit> expectedFruits = new ArrayList<>();
        expectedFruits.add(new Fruit("Banana", "Tem Calcio"));
        expectedFruits.add(new Fruit("Laranja", "Acida"));

        expectedFruits.forEach(fruit -> fruitService.createFruit(fruit));

        List<Fruit> listFruits = fruitService.getAll();
        expectedFruits.forEach(fruit -> Assertions.assertTrue(listFruits.stream().anyMatch(lFruit -> lFruit.getId().equals(fruit.getId()) && lFruit.getName().contentEquals(fruit.getName()))));
    }

    @Test
    @DisplayName("Get Fruits, empty table")
    public void getAllTest_emptyTable() {
        List<Fruit> expectedFruits = fruitService.getAll();
        Assertions.assertTrue(expectedFruits.isEmpty());
    }
}
