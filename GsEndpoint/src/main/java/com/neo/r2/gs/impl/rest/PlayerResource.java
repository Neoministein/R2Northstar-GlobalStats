package com.neo.r2.gs.impl.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.neo.r2.gs.impl.CustomConstants;
import com.neo.r2.gs.impl.persistence.PlayerUidSearchable;
import com.neo.util.common.impl.exception.NoContentFoundException;
import com.neo.util.framework.api.persistence.criteria.ExplicitSearchCriteria;
import com.neo.util.framework.api.persistence.search.SearchProvider;
import com.neo.util.framework.api.persistence.search.SearchQuery;
import com.neo.util.framework.api.persistence.search.SearchResult;
import com.neo.util.framework.elastic.api.IndexNamingService;
import com.neo.util.framework.rest.api.cache.ClientCacheControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@ApplicationScoped
@Path(PlayerResource.RESOURCE_LOCATION)
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class PlayerResource {

    public static final String RESOURCE_LOCATION = CustomConstants.URI_PREFIX + "/player";

    @Inject
    protected SearchProvider searchProvider;

    protected String playerUid;

    @Inject
    public void init(IndexNamingService indexNamingService) {
        playerUid = indexNamingService.getIndexNamePrefixFromClass(PlayerUidSearchable.class, true);
    }

    @GET
    @Path("/uid/{uid}")
    @ClientCacheControl(maxAge = 86400)
    public JsonNode getPlayerNameByUid(@PathParam("uid") String uid) {
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setMaxResults(1);
        searchQuery.setFields(List.of("playerName", "uId"));
        searchQuery.setFilters(List.of(new ExplicitSearchCriteria("_id",uid)));
        SearchResult result = searchProvider.fetch(playerUid,searchQuery);

        if (result.getHits().isEmpty()) {
            throw new NoContentFoundException(CustomConstants.EX_PLAYER_FOUND, uid);
        }

        return result.getHits().get(0);
    }

    @GET
    @Path("name/{name}")
    @ClientCacheControl(maxAge = 86400)
    public JsonNode getUidByPlayerName(@PathParam("name") String name) {
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setMaxResults(1);
        searchQuery.setFields(List.of("playerName", "uId"));
        searchQuery.setFilters(List.of(new ExplicitSearchCriteria("playerName", name)));
        SearchResult result = searchProvider.fetch(playerUid,searchQuery);

        if (result.getHits().isEmpty()) {
            throw new NoContentFoundException(CustomConstants.EX_PLAYER_FOUND, name);
        }

        return result.getHits().get(0);
    }
}
