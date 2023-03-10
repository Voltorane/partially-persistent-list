package com.example.partiallypersistentlistrest;

import com.example.partiallypersistentlistrest.exceptions.ElementDoesNotExistException;
import com.example.partiallypersistentlistrest.exceptions.InvalidVersionException;
import com.example.partiallypersistentlistrest.request_data.AddElementRequestData;
import com.example.partiallypersistentlistrest.request_data.DeleteElementRequestData;
import com.example.partiallypersistentlistrest.request_data.UpdateElementRequestData;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Actual resource that will be accessed via REST API
 * */
@Path("/")
public class PartiallyPersistentListResource {
    @Inject
    private PartiallyPersistentListCache cache;

    /**
     * @return Response with status 404 - Version ID is invalid!
     * */
    private Response getInvalidVersionResponse() {
        return Response.status(404, "Version ID is invalid!").build();
    }

    /**
     * @return Response with status 404 - Data list index is invalid!!
     * */
    private Response getInvalidIndexResponse() {
        return Response.status(404, "Data list index is invalid!").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lists")
    public String getVersionData() {
        return String.format("{\"versions\":%s}\n", cache.getVersions().toString());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/{id}")
    public Response getVersionData(@PathParam("id") int version) {
        Response response;
        try {
            List<Integer> data = cache.getData(version);
            response = Response.ok().entity(data.toString()).build();
        } catch (InvalidVersionException e) {
            response = getInvalidVersionResponse();
        }
        return response;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/list/{id}/{index}")
    public Response getElementOnIndex(@PathParam("id") int version, @PathParam("index") final int index) {
        Response response;
        try {
            Integer element = cache.getElementByIndex(version, index);
            response = Response.ok().entity(element).build();
        } catch (InvalidVersionException e) {
            response = getInvalidVersionResponse();
        } catch (IndexOutOfBoundsException e) {
            response = getInvalidIndexResponse();
        }
        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/list/{id}")
    public Response pushBack(@PathParam("id") int version, AddElementRequestData data) {
        if (data == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("AddElementRequestData is null").build();
        }
        data.setVersion(version);
        Response response;
        try {
            String result = String.format("{\"listVersion\":%d}\n", cache.addElement(data));
            response = Response.ok().entity(result).build();
        } catch (InvalidVersionException e) {
            response = getInvalidVersionResponse();
        }
        return response;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/list/{id}")
    public Response updateElement(@PathParam("id") int version, UpdateElementRequestData data) {
        if (data == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("UpdateElementRequestData is null").build();
        }
        data.setVersion(version);
        Response response;
        try {
            boolean elementInList = cache.contains(version, data.getNewElement());
            if (!elementInList) {
                response = Response.status(404, "No such element in the list").build();
            } else {
                String result = String.format("{\"listVersion\":%d}\n", cache.updateElement(data));
                response = Response.ok().entity(result).build();
            }
        } catch (InvalidVersionException e) {
            response = getInvalidVersionResponse();
        } catch (ElementDoesNotExistException e) {
            response = Response.status(404, e.getMessage()).build();
        }
        return response;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/list/{id}/{index}")
    public Response deleteElement(@PathParam("id") final int version, @PathParam("index") final int index) {
        Response response;
        try {
            String result = String.format("{\"listVersion\":%d}\n", cache.deleteElementByIndex(version, index));
            response = Response.ok().entity(result).build();
        } catch (InvalidVersionException e) {
            response = getInvalidVersionResponse();
        } catch (IndexOutOfBoundsException e) {
            response = getInvalidIndexResponse();
        }
        return response;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/list/{id}")
    public Response deleteElement(@PathParam("id") int version, DeleteElementRequestData data) {
        if (data == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("DeleteElementRequestData is null").build();
        }
        data.setVersion(version);
        Response response;
        try {
            String result = String.format("{\"listVersion\":%d}\n", cache.deleteElementByValue(data));
            response = Response.ok().entity(result).build();
        } catch (InvalidVersionException e) {
            response = getInvalidVersionResponse();
        } catch (ElementDoesNotExistException e) {
            response = Response.status(404, e.getMessage()).build();
        }
        return response;
    }
}
