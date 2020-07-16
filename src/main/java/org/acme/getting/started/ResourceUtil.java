package org.acme.getting.started;

import org.acme.getting.started.model.Fruit;
import org.acme.getting.started.model.Recipe;
import org.neo4j.driver.types.Node;

public class ResourceUtil {
    private ResourceUtil() {
    }

    public static Fruit getFruitFromNeo4jNode(Node node) {
        return new Fruit(node.id(), node.get("name").asString(),
                node.get("description").asString());
    }

    public static Recipe getRecipeFromNeo4jNode(Node node) {
        return new Recipe(node.id(), node.get("name").asString(), node.get("description").toString());
    }
}
