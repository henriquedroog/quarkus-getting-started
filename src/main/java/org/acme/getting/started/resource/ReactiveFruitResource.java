package org.acme.getting.started.resource;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.acme.getting.started.model.Recipe;
import org.neo4j.driver.Driver;
import org.neo4j.driver.reactive.RxResult;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("reactivefruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReactiveFruitResource {

    @Inject
    Driver driver;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Publisher<Response> get() {
        // Create a stream from a resource we can close in a finalizer:
        return Multi.createFrom().resource(driver::rxSession,
                session -> session.readTransaction(tx -> {
                    RxResult result = tx.run("MATCH (f:Fruit) RETURN f as fruit ORDER BY f.name");
                    return Multi.createFrom().publisher(result.records())
                            .map(record -> {
                                System.out.println(record);
                                Recipe rc = Recipe.from(record.get("fruit").asNode());
                                return Response.ok(rc).build();
                            });
                })
        ).withFinalizer(session -> {
            return Uni.createFrom().publisher(session.close());
        });
    }
}
