package org.acme.getting.started.resource;

import org.acme.getting.started.model.Recipe;
import org.acme.getting.started.service.RecipeService;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/recipes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecipeResource {

    @Inject
    RecipeService recipeService;

    @Inject
    Validator validator;

    @GET
    public Response list() {
        try {
            return Response.ok(recipeService.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }

    @POST
    public Response add(Recipe recipe) {
        try {
            Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
            if(!violations.isEmpty()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(ResourcesUtil.buildErrorMessageByViolations(violations))
                        .build();
            }

            recipeService.createLegume(recipe);
            return Response.ok(recipeService.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }

    @DELETE
    public Response delete(Long recipeId) {
        try{
            if(recipeId == null)
                return Response.status(Response.Status.BAD_REQUEST).entity("FruitId must not be null").build();

            recipeService.delete(recipeId);
            return Response.ok(recipeService.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }

    }

    @PUT
    public Response update(Recipe recipe) {
        try {
            Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
            if(!violations.isEmpty()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(ResourcesUtil.buildErrorMessageByViolations(violations))
                        .build();
            }

            recipeService.updateLegume(recipe);
            return Response.ok(recipeService.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }
}
