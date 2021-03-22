package org.acme;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

import org.jboss.resteasy.plugins.providers.sse.SseImpl;

@Singleton
@Path("/")
public class GreetingResource {

    @Context
    private Sse sse = new SseImpl();
    private SseBroadcaster broadcaster;

    @PostConstruct
    void init() {
        broadcaster = sse.newBroadcaster();
    }
    
    @Path("hello")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@Context UriInfo uriInfo) {
        return "Hello RESTEasy from "+uriInfo.getRequestUri();
    }

    @Path("fruits")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Fruit> fruits(@QueryParam("color") String color){
        if(color != null)
            return Fruit.findByColor(color);
        return Fruit.listAll();
    }

    @Path("fruit/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Fruit fruit(@PathParam("id") Long id){
        Fruit ret = Fruit.findById(id);
        if(ret == null)
            throw new NotFoundException();
        return ret;
    }

    @Path("fruits")
    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Fruit addFruit(Fruit f){
        f.persist();
        broadcaster.broadcast(sse.newEventBuilder()
                              .data(f)
                              .mediaType(MediaType.APPLICATION_JSON_TYPE)
                              .build());
        return f;
    }

    @Path("stream")
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void sse(@Context SseEventSink sink, @Context Sse sse){
        broadcaster.register(sink);
    }
}