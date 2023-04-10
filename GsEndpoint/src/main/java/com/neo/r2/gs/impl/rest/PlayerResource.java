package com.neo.r2.gs.impl.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.neo.r2.gs.impl.CustomConstants;
import com.neo.r2.gs.impl.rest.dto.inbound.PlayerLookUpSearchDto;
import com.neo.r2.gs.impl.rest.dto.outbound.PlayerLookUpObject;
import com.neo.r2.gs.impl.service.PlayerLookUpService;
import com.neo.util.common.impl.exception.NoContentFoundException;
import com.neo.util.common.impl.json.JsonUtil;
import com.neo.util.framework.rest.api.cache.ClientCacheControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path(PlayerResource.RESOURCE_LOCATION)
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class PlayerResource {

    public static final String RESOURCE_LOCATION = CustomConstants.URI_PREFIX + "/player";

    protected static final int SECONDS_IN_DAY = 86400;

    @Inject
    protected PlayerLookUpService playerLookUpService;

    @GET
    @Path("/uid/{uid}")
    @ClientCacheControl(maxAge = SECONDS_IN_DAY)
    public PlayerLookUpObject getPlayerNameByUid(@PathParam("uid") String uid) {
        return playerLookUpService.getPlayerNameByUid(uid)
                .orElseThrow(() -> new NoContentFoundException(CustomConstants.EX_PLAYER_FOUND, uid));
    }

    @POST
    @Path("/uid/search")
    @ClientCacheControl(maxAge = SECONDS_IN_DAY)
    public ObjectNode searchForPlayerNameByUid(PlayerLookUpSearchDto playerLookUpDto) {
        ObjectNode result = JsonUtil.emptyObjectNode();
        for (String player: playerLookUpDto.players()) {
            result.put(player, playerLookUpService.getPlayerNameByUid(player).map(PlayerLookUpObject::playerName).orElse("UNKNOWN_PLAYER"));
        }

        return result;
    }
}
