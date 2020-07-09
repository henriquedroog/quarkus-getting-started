package org.acme.getting.started.resource;

import org.acme.getting.started.model.Fruit;
import org.acme.getting.started.model.Legume;
import org.acme.getting.started.service.LegumeService;

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

@Path("/legumes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LegumeResource {

    @Inject
    LegumeService legumeService;

    @Inject
    Validator validator;

    @GET
    public Response list() {
        try {
            return Response.ok(legumeService.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }

    @POST
    public Response add(Legume legume) {
        try {
            Set<ConstraintViolation<Legume>> violations = validator.validate(legume);
            if(!violations.isEmpty()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(ResourcesUtil.buildErrorMessageByViolations(violations))
                        .build();
            }

            legumeService.createLegume(legume);
            return Response.ok(legumeService.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }

    @DELETE
    public Response delete(Long legumeId) {
        try{
            if(legumeId == null)
                return Response.status(Response.Status.BAD_REQUEST).entity("FruitId must not be null").build();

            legumeService.delete(legumeId);
            return Response.ok(legumeService.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }

    }

    @PUT
    public Response update(Legume legume) {
        try {
            Set<ConstraintViolation<Legume>> violations = validator.validate(legume);
            if(!violations.isEmpty()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(ResourcesUtil.buildErrorMessageByViolations(violations))
                        .build();
            }

            legumeService.updateLegume(legume);
            return Response.ok(legumeService.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error").build();
        }
    }
}
