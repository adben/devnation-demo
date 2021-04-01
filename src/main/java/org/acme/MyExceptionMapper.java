package org.acme;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import io.vertx.core.json.JsonObject;

public class MyExceptionMapper {
    
    @ServerExceptionMapper
    public Response toResponse(NotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new JsonObject()
                        .put("type", "https://example.com/probs/cant-touch-this")
                        .put("title", "we could not find it :("))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
