package com.louishoughton;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Map;

@Path("/hello")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GreetingResource {

    private static final Logger LOG = LoggerFactory.getLogger(GreetingResource.class);

    @Inject
    JsonWebToken jwt;

    @GET
    @PermitAll
    public Greeting hello(@Context SecurityContext ctx) {
        Principal caller =  ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
        LOG.debug(name);
        boolean hasJWT = jwt.getClaimNames() != null;
        String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s, claims: %s, groups: %s", name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT, jwt.getClaim("groups"), jwt.getGroups());
        LOG.debug(helloReply);
        return new Greeting(helloReply);
    }

    @POST
    @RolesAllowed("admin")
    public Greeting helloPost(@Context SecurityContext ctx) {
        Principal caller =  ctx.getUserPrincipal();
        String name = caller == null ? "anonymous" : caller.getName();
        LOG.debug(name);
        boolean hasJWT = jwt.getClaimNames() != null;

        String helloReply = String.format("hello + %s, isSecure: %s, authScheme: %s, hasJWT: %s, claims: %s, groups: %s", name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJWT, jwt.getClaimNames(), jwt.getGroups());
        LOG.debug(helloReply);
        return new Greeting(helloReply);
    }
}
