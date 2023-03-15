package com.neo.r2.gs.impl.rest;

import com.neo.r2.gs.impl.CustomConstants;
import com.neo.r2.gs.impl.persistence.MatchResultSearchable;
import com.neo.r2.gs.impl.persistence.PlayerUidSearchable;
import com.neo.r2.gs.impl.rest.dto.outbound.ValueDto;
import com.neo.util.framework.api.persistence.aggregation.SimpleFieldAggregation;
import com.neo.util.framework.api.persistence.aggregation.TermAggregation;
import com.neo.util.framework.api.persistence.criteria.DateSearchCriteria;
import com.neo.util.framework.api.persistence.criteria.SearchCriteria;
import com.neo.util.framework.api.persistence.search.SearchProvider;
import com.neo.util.framework.api.persistence.search.SearchQuery;
import com.neo.util.framework.api.persistence.search.SearchResult;
import com.neo.util.framework.elastic.api.IndexNamingService;
import com.neo.util.framework.rest.api.cache.CacheControl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import java.util.Date;
import java.util.List;

@ApplicationScoped
@CacheControl(maxAge = 60)
@Path(DashboardResource.RESOURCE_LOCATION)
@Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
public class DashboardResource {

    public static final String RESOURCE_LOCATION = CustomConstants.URI_PREFIX + "/dashboard";

    @Inject
    protected SearchProvider searchProvider;

    protected String resultIndexName;

    @Inject
    public void init(IndexNamingService indexNamingService) {
        resultIndexName = indexNamingService.getIndexNamePrefixFromClass(MatchResultSearchable.class, true);
    }

    @GET
    @Path("/serverNames")
    public SearchResult getServerNames(@QueryParam("since") String since) {
        SearchQuery searchQuery = new SearchQuery();

        searchQuery.setFilters(parseSinceToCriteria(since));

        searchQuery.addAggregations(new TermAggregation("servername", "serverName", List.of(new SimpleFieldAggregation("count","timestamp", SimpleFieldAggregation.Type.COUNT))));

        return searchProvider.fetch(resultIndexName, searchQuery);
    }


    @GET
    @Path("/matchesPlayed")
    public ValueDto getMatchesPlayed(@QueryParam("since") String since) {
        SearchQuery searchQuery = new SearchQuery();

        searchQuery.setFilters(parseSinceToCriteria(since));

        searchQuery.addAggregations(new SimpleFieldAggregation("count", "matchId", SimpleFieldAggregation.Type.CARDINALITY));

        return new ValueDto(searchProvider.fetch(resultIndexName, searchQuery).getAggregations().get("count"));
    }

    @GET
    @Path("/uniquePlayers")
    public ValueDto getUniquePlayers(@QueryParam("since") String since) {
        List<SearchCriteria> searchCriteria = parseSinceToCriteria(since);
        return new ValueDto(searchProvider.count(PlayerUidSearchable.class, searchCriteria));
    }

    public List<SearchCriteria> parseSinceToCriteria(String since) {
        try {
            return List.of(new DateSearchCriteria("lastUpdate", new Date(Long.parseLong(since)), null));
        } catch (NumberFormatException ex) {
            return List.of();
        }
    }
}
