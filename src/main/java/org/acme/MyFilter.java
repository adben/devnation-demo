package org.acme;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;

import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

public class MyFilter {

    @ServerRequestFilter
    public void filter(ContainerRequestContext requestContext) {
        System.err.println("Making a request for "+requestContext.getMethod()+" "+requestContext.getUriInfo().getRequestUri());
    }

    @ServerResponseFilter
    public void filter(ContainerResponseContext responseContext) {
        System.err.println("Response is "+responseContext.getStatus()+" "+responseContext.getStatusInfo().getReasonPhrase()+" with body: "+responseContext.getEntity());
    }


}
