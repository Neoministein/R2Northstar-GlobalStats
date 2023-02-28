package com.neo.r2.gs.impl.rest;

import com.neo.r2.gs.impl.CustomConstants;
import com.neo.r2.gs.impl.persistence.MatchResultSearchable;
import com.neo.r2.gs.impl.persistence.PlayerUidSearchable;
import com.neo.r2.gs.impl.rest.dto.inbound.MatchResultDto;
import com.neo.util.common.impl.MathUtils;
import com.neo.util.framework.api.persistence.aggregation.*;
import com.neo.util.framework.api.persistence.criteria.ExplicitSearchCriteria;
import com.neo.util.framework.api.persistence.search.SearchProvider;
import com.neo.util.framework.api.persistence.search.SearchQuery;
import com.neo.util.framework.elastic.api.IndexNamingService;
import com.neo.util.framework.elastic.api.aggregation.BucketScriptAggregation;
import com.neo.util.framework.rest.api.cache.CacheControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
@CacheControl(maxAge = 60)
@Path(ResultResource.RESOURCE_LOCATION)
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class ResultResource {

    public static final String RESOURCE_LOCATION = CustomConstants.URI_PREFIX + "/result";

    @Inject
    protected SearchProvider searchProvider;

    protected String resultIndexName;

    @Inject
    public void init(IndexNamingService indexNamingService) {
        resultIndexName = indexNamingService.getIndexNamePrefixFromClass(MatchResultSearchable.class, true);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public void createMatch(MatchResultDto matchResultDto) {
        if (searchProvider.enabled()) {
            if (matchResultDto.matchId() != null) {
                searchProvider.index(matchResultDto.players().stream().map(player -> new MatchResultSearchable(matchResultDto, player)).toList());
            } else {
                String matchId = UUID.randomUUID().toString();
                searchProvider.index(matchResultDto.players().stream().map(player -> new MatchResultSearchable(matchResultDto, player).addMatchId(matchId)).toList());
            }

            searchProvider.update(matchResultDto.players().stream().map(player -> new PlayerUidSearchable(player.uId(), player.playerName())).toList(), true);
        }
    }

    @GET
    @Path("top/npc-kills")
    public AggregationResult getNpcKills(@QueryParam("max") @DefaultValue("100") String queryMaxResult) {
        int maxResult;
        try {
            maxResult = MathUtils.clamp(Integer.parseInt(queryMaxResult), 1, 10000) ;
        } catch (NumberFormatException ex) {
            maxResult = 100;
        }

        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setAggregations(List.of(new TermAggregation("npc-kills","uId", maxResult,new TermAggregation.Order("PGS_NPC_KILLS"), null, List.of(new SimpleFieldAggregation("PGS_NPC_KILLS", "PGS_NPC_KILLS", SimpleFieldAggregation.Type.SUM)))));

        return searchProvider.fetch(resultIndexName, searchQuery).getAggregations().get("npc-kills");
    }

    @GET
    @Path("top/player-kills")
    public AggregationResult getPLayerKills(@QueryParam("max") @DefaultValue("100") String queryMaxResult) {
        int maxResult;
        try {
            maxResult = MathUtils.clamp(Integer.parseInt(queryMaxResult), 1, 10000) ;
        } catch (NumberFormatException ex) {
            maxResult = 100;
        }

        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setAggregations(List.of(new TermAggregation("player-kills","uId", maxResult,new TermAggregation.Order("PGS_PILOT_KILLS"), null, List.of(new SimpleFieldAggregation("PGS_PILOT_KILLS", "PGS_PILOT_KILLS", SimpleFieldAggregation.Type.SUM)))));

        return searchProvider.fetch(resultIndexName, searchQuery).getAggregations().get("player-kills");
    }

    @GET
    @Path("top/player-kd")
    public AggregationResult getPLayerKd(@QueryParam("max") @DefaultValue("100") String queryMaxResult) {
        int maxResult;
        try {
            maxResult = MathUtils.clamp(Integer.parseInt(queryMaxResult), 1, 10000) ;
        } catch (NumberFormatException ex) {
            maxResult = 100;
        }

        SearchQuery searchQuery = new SearchQuery();
        BucketScriptAggregation bucketScriptAggregation = new BucketScriptAggregation("kd", "params.kills / params.deaths", Map.of("kills", "PGS_PILOT_KILLS", "deaths", "PGS_DEATHS"));
        searchQuery.setAggregations(List.of(new TermAggregation("player-kd","uId", maxResult,new TermAggregation.Order("kd"), null, List.of(new SimpleFieldAggregation("PGS_PILOT_KILLS", "PGS_PILOT_KILLS", SimpleFieldAggregation.Type.SUM), new SimpleFieldAggregation("PGS_DEATHS", "PGS_DEATHS", SimpleFieldAggregation.Type.SUM), bucketScriptAggregation))));

        return searchProvider.fetch(resultIndexName, searchQuery).getAggregations().get("player-kd");
    }

    @GET
    @Path("top/win")
    public AggregationResult getWin(@QueryParam("max") @DefaultValue("100") String queryMaxResult) {
        int maxResult;
        try {
            maxResult = MathUtils.clamp(Integer.parseInt(queryMaxResult), 1, 10000) ;
        } catch (NumberFormatException ex) {
            maxResult = 100;
        }

        SearchQuery searchQuery = new SearchQuery();
        searchQuery.setFilters(List.of(new ExplicitSearchCriteria("hasWon",true)));
        searchQuery.setAggregations(List.of(new TermAggregation("win","uId", maxResult, new TermAggregation.Order("win"), null, List.of(new SimpleFieldAggregation("win","matchId", SimpleFieldAggregation.Type.COUNT)))));

        return searchProvider.fetch(resultIndexName, searchQuery).getAggregations().get("win");
    }

    @GET
    @Path("top/win-ratio")
    public AggregationResult getWinRatio(@QueryParam("max") @DefaultValue("100") String queryMaxResult) {
        int maxResult;
        try {
            maxResult = MathUtils.clamp(Integer.parseInt(queryMaxResult), 1, 10000) ;
        } catch (NumberFormatException ex) {
            maxResult = 100;
        }

        SearchQuery searchQuery = new SearchQuery();
        BucketScriptAggregation bucketScriptAggregation = new BucketScriptAggregation("ratio", "params.wins / (params.wins + params.loses) * 100", Map.of("wins", "filters['win']>count", "loses", "filters['lose']>count"));
        searchQuery.setAggregations(List.of(new TermAggregation("win-ratio","uId", maxResult, new TermAggregation.Order("ratio"),null, List.of(new CriteriaAggregation("filters", Map.of("win", new ExplicitSearchCriteria("hasWon",true),"lose", new ExplicitSearchCriteria("hasWon",false)), new SimpleFieldAggregation("count","matchId", SimpleFieldAggregation.Type.COUNT)), bucketScriptAggregation))));

        return searchProvider.fetch(resultIndexName, searchQuery).getAggregations().get("win-ratio");
    }
}