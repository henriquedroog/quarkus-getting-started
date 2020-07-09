package org.acme.getting.started.resource;

import org.acme.getting.started.model.Fruit;
import org.acme.getting.started.service.FruitService;

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

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitResource {

    @Inject
    FruitService fruitService;

    @Inject
    Validator validator;

    @GET
    public Response list() {
        try {
            return Response.ok(fruitService.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }

    @POST
    public Response add(Fruit fruit) {
        try {
            Set<ConstraintViolation<Fruit>> violations = validator.validate(fruit);
            if(!violations.isEmpty()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(ResourcesUtil.buildErrorMessageByViolations(violations))
                        .build();
            }

            fruitService.createFruit(fruit);
            return Response.ok(fruitService.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }

    @DELETE
    public Response delete(Long fruitId) {
        try{
            if(fruitId == null)
                return Response.status(Response.Status.BAD_REQUEST).entity("FruitId must not be null").build();

            fruitService.delete(fruitId);
            return Response.ok(fruitService.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }

    }

    @PUT
    public Response update(Fruit fruit) {
        try {
            Set<ConstraintViolation<Fruit>> violations = validator.validate(fruit);
            if(!violations.isEmpty()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(ResourcesUtil.buildErrorMessageByViolations(violations))
                        .build();
            }

            fruitService.updateFruit(fruit);
            return Response.ok(fruitService.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }
}
