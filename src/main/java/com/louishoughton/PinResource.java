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
import java.util.List;

@Path("/pins")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PinResource {

    private static final Logger LOG = LoggerFactory.getLogger(PinResource.class);

    @Inject
    JsonWebToken jwt;

    @Inject
    private PinDAO pinDao;

    @POST
    @RolesAllowed("admin")
    public Pin post(@Context SecurityContext ctx, Pin pin) {
        Principal caller =  ctx.getUserPrincipal();
        String name = caller.getName();
        pin.setUser(name);
        return pinDao.save(pin);
    }

    @GET
    @RolesAllowed("admin")
    public List<Pin> get(@Context SecurityContext ctx) {
        Principal caller =  ctx.getUserPrincipal();
        String name = caller.getName();
        return pinDao.getAll(name);
    }
}
