package com.neo.r2.gs.impl.service;

import com.neo.r2.gs.impl.persistence.PlayerUidSearchable;
import com.neo.r2.gs.impl.rest.dto.outbound.PlayerLookUpObject;
import com.neo.util.framework.api.cache.spi.CacheKeyParameterPositions;
import com.neo.util.framework.api.cache.spi.CachePut;
import com.neo.util.framework.api.cache.spi.CacheResult;
import com.neo.util.framework.api.persistence.criteria.ExplicitSearchCriteria;
import com.neo.util.framework.api.persistence.search.SearchProvider;
import com.neo.util.framework.api.persistence.search.SearchQuery;
import com.neo.util.framework.api.persistence.search.SearchResult;
import com.neo.util.framework.elastic.api.IndexNamingService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PlayerLookUpService {

    private static final String PLAYER_LOOK_UP_CACHE = "playerLookUp";


    protected String playerUid;

    @Inject
    protected SearchProvider searchProvider;

    @Inject
    public void init(IndexNamingService indexNamingService) {
        playerUid = indexNamingService.getIndexNamePrefixFromClass(PlayerUidSearchable.class, true);
    }

    @CacheResult(cacheName = PLAYER_LOOK_UP_CACHE)
    public Optional<PlayerLookUpObject> getPlayerNameByUid(String uid) {
        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setMaxResults(1);
        searchQuery.setFields(List.of(PlayerUidSearchable.PLAYER_NAME, PlayerUidSearchable.U_ID));
        searchQuery.setFilters(List.of(new ExplicitSearchCriteria("_id",uid)));
        SearchResult<PlayerLookUpObject> result = searchProvider.fetch(playerUid, searchQuery, PlayerLookUpObject.class);

        if (result.getHits().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(result.getHits().get(0));
    }

    @CacheKeyParameterPositions(0)
    @CachePut(valueParameterPosition = 1, cacheName = PLAYER_LOOK_UP_CACHE)
    public void updatePlayerLookUp(String uid, String playerName) {
        searchProvider.update(new PlayerUidSearchable(uid, playerName), true);
    }
}
