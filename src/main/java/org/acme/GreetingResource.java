package org.acme;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestSseElementType;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;

@Singleton
@Path("/")
public class GreetingResource {

    private BroadcastProcessor<Fruit> broadcaster = BroadcastProcessor.create();

    @Path("hello")
    @GET
    public String hello(UriInfo uriInfo) {
        return "Hello RESTEasy from "+uriInfo.getRequestUri();
    }

    @Path("fruits")
    @GET
    public Uni<List<Fruit>> fruits(@RestQuery String color){
        if(color != null)
            return Fruit.findByColor(color);
        return Fruit.listAll();
    }

    @Path("fruit/{id}")
    @GET
    public Uni<Fruit> fruit(Long id){
        return Fruit.<Fruit>findById(id).onItem().ifNull().failWith(() -> new NotFoundException());
    }

    @Path("fruits")
    @POST
    public Uni<Fruit> addFruit(Fruit f){
        return Panache.withTransaction(() -> f.persist()
                                       .invoke(() -> broadcaster.onNext(f))
                                       .map(v -> f));
    }

    @Path("stream")
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestSseElementType(MediaType.APPLICATION_JSON)
    public Multi<Fruit> sse(){
        return broadcaster;
    }
}