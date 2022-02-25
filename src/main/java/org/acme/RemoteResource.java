package org.acme;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("")
public class RemoteResource {

    public static final int AMMOUNT_CALLS = 1000;
    public static final int CALL_DURATION_MS = 1000;

    private final AtomicInteger counter = new AtomicInteger();

    @Path("fan-out")
    @GET
    public CompletionStage<String> fanOut(@Context UriInfo uriInfo) {
        URI uri = uriInfo.getBaseUriBuilder().path(RemoteResource.class, "remote").build();
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(uri);
        CompletionStageRxInvoker invocation = target.request().rx();
        StringBuilder sb = new StringBuilder();
        return CompletableFuture.allOf((CompletableFuture<?>) IntStream.range(0, AMMOUNT_CALLS)
                        .mapToObj(__ -> invocation.get(String.class))
                        .map(x -> x.whenComplete((val, t) -> sb.append(val).append(", ")))
                        .map(CompletionStage::toCompletableFuture)
                        .collect(Collectors.toUnmodifiableList()))
                .thenApply(v -> sb.toString());
    }

    @Path("remote")
    @GET
    public int remote() throws InterruptedException {
        Thread.sleep(CALL_DURATION_MS);
        return counter.getAndIncrement();
    }
}