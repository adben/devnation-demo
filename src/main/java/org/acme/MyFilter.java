package org.acme;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import static java.lang.System.err;

@Provider
public class MyFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        err.println("Making a request for "+requestContext.getMethod()+" "+requestContext.getUriInfo().getRequestUri());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        err.println("Response is "+responseContext.getStatus()+" "+responseContext.getStatusInfo().getReasonPhrase()+" with body: "+responseContext.getEntity());
    }


}
