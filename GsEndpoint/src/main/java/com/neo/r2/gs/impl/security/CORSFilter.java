package com.neo.r2.gs.impl.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(final ContainerRequestContext requestContext,
            final ContainerResponseContext containerRequest) {
        containerRequest.getHeaders().add("Access-Control-Allow-Origin", "*");
        containerRequest.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        containerRequest.getHeaders().add("Access-Control-Allow-Credentials", "true");
        containerRequest.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        containerRequest.getHeaders().add("Access-Control-Max-Age", "1209600");
    }

}