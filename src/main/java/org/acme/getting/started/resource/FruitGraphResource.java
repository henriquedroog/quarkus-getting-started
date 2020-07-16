package org.acme.getting.started.resource;

import org.acme.getting.started.ResourceUtil;
import org.acme.getting.started.model.Fruit;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Values;
import org.neo4j.driver.async.AsyncSession;
import org.neo4j.driver.async.ResultCursor;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

@Path("neo4j/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitGraphResource {

    @Inject
    Driver driver;

    @GET
    public CompletionStage<Response> get() {
        AsyncSession session = driver.asyncSession();
        return session
                .runAsync("MATCH (f:Fruit) RETURN f ORDER BY f.name")
                .thenCompose(cursor ->
                        cursor.listAsync(
                                record -> ResourceUtil.getFruitFromNeo4jNode(record.get("f").asNode())
                        )
                ).thenCompose(fruit ->
                        session.closeAsync().thenApply(signal -> fruit)
                )
                .thenApply(Response::ok)
                .thenApply(Response.ResponseBuilder::build);
    }

    @POST
    public CompletionStage<Response> create(Fruit fruit) {
        AsyncSession session = driver.asyncSession();
        return session
                .writeTransactionAsync(tx -> tx
                        .runAsync("CREATE (f:Fruit {name: $name}) RETURN f", Values.parameters("name", fruit.getName()))
                        .thenCompose(ResultCursor::singleAsync)
                )
                .thenApply(record -> ResourceUtil.getFruitFromNeo4jNode(record.get("f").asNode()))
                .thenCompose(persistedFruit -> session.closeAsync().thenApply(signal -> persistedFruit))
                .thenApply(persistedFruit -> Response.created(URI.create("/neo4j/fruits/" + persistedFruit.getId())).build());
    }

    @GET
    @Path("{id}")
    public CompletionStage<Response> getSingleFruit(@PathParam("id") Long id){
        AsyncSession session = driver.asyncSession();
        return session.writeTransactionAsync(tx -> tx
                .runAsync("MATCH (f:Fruit) WHERE id(f) = $id RETURN f", Values.parameters("id", id))
                .thenCompose(ResultCursor::singleAsync)
                ).handle(((record, exception) -> {
                    if(exception != null) {
                        Throwable source = exception;
                        if(exception instanceof CompletionException) {
                            source = exception.getCause();
                        }
                        Response.Status status = Status.INTERNAL_SERVER_ERROR;
                        if(source instanceof NoSuchRecordException) {
                            status = Status.NOT_FOUND;
                        }
                        return Response.status(status).build();
                    } else {
                        return Response.ok(ResourceUtil.getFruitFromNeo4jNode(record.get("f").asNode())).build();
                    }
                }));
    }

    @DELETE
    @Path("{id}")
    public CompletionStage<Response> delete(@PathParam("id") Long id) {

        AsyncSession session = driver.asyncSession();
        return session
                .writeTransactionAsync(tx -> tx
                        .runAsync("MATCH (f:Fruit) WHERE id(f) = $id DELETE f", Values.parameters("id", id))
                        .thenCompose(ResultCursor::consumeAsync)
                )
                .thenCompose(response -> session.closeAsync())
                .thenApply(signal -> Response.noContent().build());
    }
}
