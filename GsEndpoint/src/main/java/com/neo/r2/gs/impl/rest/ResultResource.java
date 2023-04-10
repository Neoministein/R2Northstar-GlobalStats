package com.neo.r2.gs.impl.rest;

import com.neo.r2.gs.impl.CustomConstants;
import com.neo.r2.gs.impl.persistence.MatchResultSearchable;
import com.neo.r2.gs.impl.rest.dto.inbound.MatchResultDto;
import com.neo.r2.gs.impl.service.MatchStatsService;
import com.neo.r2.gs.impl.service.PlayerLookUpService;
import com.neo.util.common.impl.MathUtils;
import com.neo.util.common.impl.StringUtils;
import com.neo.util.framework.api.persistence.aggregation.*;
import com.neo.util.framework.api.persistence.search.SearchProvider;
import com.neo.util.framework.rest.api.cache.ClientCacheControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;
import java.util.function.UnaryOperator;

@ApplicationScoped
@ClientCacheControl(maxAge = 60)
@Path(ResultResource.RESOURCE_LOCATION)
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class ResultResource {

    public static final String RESOURCE_LOCATION = CustomConstants.URI_PREFIX + "/result";

    @Inject
    protected PlayerLookUpService playerLookUpService;

    @Inject
    protected MatchStatsService matchStatsService;

    @Inject
    protected SearchProvider searchProvider;

    @POST
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public void createMatch(MatchResultDto matchResultDto) {
        if (searchProvider.enabled()) {
            if (matchResultDto.matchId().isPresent()) {
                saveResult(matchResultDto, searchable -> searchable);
            } else {
                String matchId = UUID.randomUUID().toString();
                saveResult(matchResultDto, searchable -> searchable.addMatchId(matchId));
            }
        }
        matchStatsService.invalidateStatsCache();
    }

    protected void saveResult(MatchResultDto matchResultDto, UnaryOperator<MatchResultSearchable> matchId) {
        for (MatchResultDto.Player player: matchResultDto.players()) {
            searchProvider.index(matchId.apply(new MatchResultSearchable(matchResultDto, player)));
            playerLookUpService.updatePlayerLookUp(player.uId(), player.playerName());
        }
    }

    @GET
    @Path("top/npc-kills")
    public AggregationResult getNpcKills(@QueryParam("max") @DefaultValue("100") String maxResult,
                                         @QueryParam("tags") String tags) {
        return matchStatsService.getNpcKills(parseMaxResult(maxResult), parseTags(tags));
    }

    @GET
    @Path("top/player-kills")
    public AggregationResult getPlayerKills(@QueryParam("max") @DefaultValue("100") String maxResult,
                                            @QueryParam("tags") String tags) {
        return matchStatsService.getPlayerKills(parseMaxResult(maxResult), parseTags(tags));
    }

    @GET
    @Path("top/player-kd")
    public AggregationResult getPlayerKd(@QueryParam("max") @DefaultValue("100") String maxResult,
                                         @QueryParam("tags") String tags) {
        return matchStatsService.getPlayerKd(parseMaxResult(maxResult), parseTags(tags));
    }

    @GET
    @Path("top/win")
    public AggregationResult getWin(@QueryParam("max") @DefaultValue("100") String maxResult,
                                    @QueryParam("tags") String tags) {
        return matchStatsService.getWin(parseMaxResult(maxResult), parseTags(tags));
    }

    @GET
    @Path("top/win-ratio")
    public AggregationResult getWinRatio(@QueryParam("max") @DefaultValue("100") String maxResult,
                                         @QueryParam("tags") String tags) {
        return matchStatsService.getWinRatio(parseMaxResult(maxResult), parseTags(tags));
    }

    protected String[] parseTags(String queryTags) {
        if (StringUtils.isEmpty(queryTags)) {
            return new String[0];
        }
        return queryTags.split(",");
    }

    protected int parseMaxResult(String queryMaxResult) {
        try {
            return MathUtils.clamp(Integer.parseInt(queryMaxResult), 1, 10000) ;
        } catch (NumberFormatException ex) {
            return 100;
        }
    }
}