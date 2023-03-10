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
import org.json.JSONObject;

import java.util.List;

/**
 * Actual resource that will be accessed via REST API
 * */
@Path("/")
public class PartiallyPersistentListResource {
    @Inject
    private PartiallyPersistentListCache cache;

    /**
     * @param dataType string representation of type of data that was invalid
     * @return Response with status 406 - dataType is invalid!!!
     * */
    private Response getInvalidDataResponse(String dataType) {
        return Response.status(Response.Status.BAD_REQUEST).entity(dataType + " is invalid!").build();
    }

    /**
     * @return Response with status 404 - Version ID is invalid!
     * */
    private Response getInvalidVersionResponse() {
        return Response.status(Response.Status.NOT_FOUND.getStatusCode(),
                "Version ID is invalid!").build();
    }

    /**
     * @return Response with status 406 - Data list index is invalid!!
     * */
    private Response getInvalidIndexResponse() {
        return Response.status(Response.Status.NOT_ACCEPTABLE.getStatusCode(),
                "Data list index is invalid!").build();
    }

    /**
     * @param version list version that will be returned with Response
     * @return OK Response with status 200 and {"listVersion":version} as body
     * */
    private Response getListVersionResponse(int version) {
        JSONObject listVersion = new JSONObject().put("listVersion", version);
        return Response.ok(listVersion.toString(), MediaType.APPLICATION_JSON).build();
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
            return getInvalidDataResponse(AddElementRequestData.class.toString());
        }
        data.setVersion(version);
        if (!data.isValid()) {
            return getInvalidDataResponse(AddElementRequestData.class.toString());
        }
        Response response;
        try {
            response = getListVersionResponse(cache.addElement(data));
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
            return getInvalidDataResponse(UpdateElementRequestData.class.toString());
        }
        data.setVersion(version);
        if (!data.isValid()) {
            return getInvalidDataResponse(UpdateElementRequestData.class.toString());
        }
        Response response;
        try {
            boolean elementInList = cache.contains(version, data.getNewElement());
            if (!elementInList) {
                response = Response.status(Response.Status.NOT_ACCEPTABLE.getStatusCode(), "No such element in the list").build();
            } else {
                response = getListVersionResponse(cache.updateElement(data));
            }
        } catch (InvalidVersionException e) {
            response = getInvalidVersionResponse();
        } catch (ElementDoesNotExistException e) {
            response = Response.status(Response.Status.NOT_ACCEPTABLE.getStatusCode(), e.getMessage()).build();
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
            response = getListVersionResponse(cache.deleteElementByIndex(version, index));
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
            return getInvalidDataResponse(DeleteElementRequestData.class.toString());
        }
        data.setVersion(version);
        if (!data.isValid()) {
            return getInvalidDataResponse(DeleteElementRequestData.class.toString());
        }
        Response response;
        try {
            response = getListVersionResponse(cache.deleteElementByValue(data));
        } catch (InvalidVersionException e) {
            response = getInvalidVersionResponse();
        } catch (ElementDoesNotExistException e) {
            response = Response.status(Response.Status.NOT_ACCEPTABLE.getStatusCode(), e.getMessage()).build();
        }
        return response;
    }
}
