package org.acme;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

public class MyFilter {

    @ServerRequestFilter
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.err.println("Making a request for "+requestContext.getMethod()+" "+requestContext.getUriInfo().getRequestUri());
    }

    @ServerResponseFilter
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        System.err.println("Response is "+responseContext.getStatus()+" "+responseContext.getStatusInfo().getReasonPhrase()+" with body: "+responseContext.getEntity());
    }


}
