package org.acme;

import java.util.List;

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

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.jboss.resteasy.annotations.SseElementType;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;

@Singleton
@Path("/")
public class GreetingResource {

    private BroadcastProcessor<Fruit> broadcaster = BroadcastProcessor.create();

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
        return PanacheEntityBase.listAll();
    }

    @Path("fruit/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Fruit fruit(@PathParam("id") Long id){
        Fruit ret = PanacheEntityBase.findById(id);
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
        broadcaster.onNext(f);
        return f;
    }

    @Path("stream")
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    public Multi<Fruit> sse(){
        return broadcaster;
    }
}