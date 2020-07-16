package org.acme.getting.started.resource;

import org.acme.getting.started.model.Recipe;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Values;
import org.neo4j.driver.async.AsyncSession;
import org.neo4j.driver.async.ResultCursor;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

@Path("neo4j/recipes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecipeGraphResource {

    @Inject
    Driver driver;

    @GET
    public CompletionStage<Response> getRecipes() {
        AsyncSession session = driver.asyncSession();
        return session
                .runAsync("MATCH (r:Recipe) RETURN r ORDER BY r.name")
                .thenCompose(resultCursor ->
                        resultCursor.listAsync(
                                record -> Recipe.from(record.get("r").asNode())
                        )
                ).thenCompose(recipe ->
                        session.closeAsync()
                                .thenApply(signal -> recipe)
                ).thenApply(Response::ok)
                .thenApply(Response.ResponseBuilder::build);
    }

    @POST
    public CompletionStage<Response> createRecipe(Recipe recipe) {
        AsyncSession session = driver.asyncSession();
        return session
                .writeTransactionAsync(tx -> tx
                        .runAsync("CREATE (r:Recipe {name: $name, description: $description}) RETURN r",
                                Values.parameters(
                                        "name", recipe.getName(),
                                        "description", recipe.getDescription()))
                        .thenCompose(ResultCursor::singleAsync)
                )
                .thenApply(record ->
                        Recipe.from(record.get("r").asNode()))
                .thenCompose(persistedRecipe ->
                        session.closeAsync().thenApply(signal -> persistedRecipe))
                .thenApply(persistedRecipe ->
                        Response.created(URI.create("/neo4j/recipes/" + persistedRecipe.getId())).build());
    }

    @GET
    @Path("{id}")
    public CompletionStage<Response> getSingleFruit(@PathParam("id") Long id){
        AsyncSession session = driver.asyncSession();
        return session.writeTransactionAsync(tx -> tx
                .runAsync("MATCH (r:Recipe) WHERE id(r) = $id RETURN r", Values.parameters("id", id))
                .thenCompose(ResultCursor::singleAsync)
        ).handle(((record, exception) -> {
            if(exception != null) {
                Throwable source = exception;
                if(exception instanceof CompletionException) {
                    source = exception.getCause();
                }
                Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
                if(source instanceof NoSuchRecordException) {
                    status = Response.Status.NOT_FOUND;
                }
                return Response.status(status).build();
            } else {
                return Response.ok(Recipe.from(record.get("r").asNode())).build();
            }
        }));
    }

    @DELETE
    @Path("{id}")
    public CompletionStage<Response> delete(@PathParam("id") Long id) {

        AsyncSession session = driver.asyncSession();
        return session
                .writeTransactionAsync(tx -> tx
                        .runAsync("MATCH (r:Recipe) WHERE id(r) = $id DELETE r", Values.parameters("id", id))
                        .thenCompose(ResultCursor::consumeAsync)
                )
                .thenCompose(response -> session.closeAsync())
                .thenApply(signal -> Response.noContent().build());
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/attach/fruit")
    public CompletionStage<Response> mapFruitToRecipe(@FormParam("fruit") String fruit,
                                                      @FormParam("recipe") String recipe){
        String matchQuery = "MATCH (r:Recipe {name: $recipe}) MATCH (f:Fruit {name: $fruit}) MERGE (r)" +
                "-[rel:HAS_INGREDIENT]-(f) RETURN r, rel, f";
        AsyncSession session = driver.asyncSession();
        return session.writeTransactionAsync(tx -> tx
                    .runAsync(matchQuery, Values.parameters("recipe", recipe, "fruit", fruit))
                    .thenCompose(ResultCursor::singleAsync)
                )
                .thenCompose(response -> session.closeAsync()).thenApply(signal -> Response.ok().build());
    }
}
