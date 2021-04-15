package org.acme;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.vertx.core.json.JsonObject;

@Provider
public class MyExceptionMapper implements ExceptionMapper<NotFoundException> {
    
    @Override
    public Response toResponse(NotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new JsonObject()
                        .put("type", "https://example.com/probs/cant-touch-this")
                        .put("title", "we could not find it :("))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
