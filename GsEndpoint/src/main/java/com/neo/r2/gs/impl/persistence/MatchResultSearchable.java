package com.neo.r2.gs.impl.persistence;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.neo.r2.gs.impl.rest.dto.inbound.MatchResultDto;
import com.neo.util.framework.api.persistence.search.AbstractSearchable;
import com.neo.util.framework.api.persistence.search.IndexPeriod;
import com.neo.util.framework.api.persistence.search.Searchable;
import com.neo.util.framework.api.persistence.search.SearchableIndex;

import java.time.Instant;

@SearchableIndex(indexName = MatchResultSearchable.INDEX_NAME, indexPeriod = IndexPeriod.MONTHLY)
public class MatchResultSearchable extends AbstractSearchable implements Searchable {

    public static final String INDEX_NAME = "match-result";

    protected Instant timestamp = Instant.now();

    protected String serverName;
    protected String matchId;
    protected String map;
    protected String gamemode;
    protected String[] tags;

    @JsonUnwrapped
    protected MatchResultDto.Player player;

    public MatchResultSearchable(MatchResultDto matchResultDto, MatchResultDto.Player player) {
        this.matchId = matchResultDto.matchId().orElse(null);
        this.map = matchResultDto.map();
        this.gamemode = matchResultDto.gamemode();
        this.serverName = matchResultDto.serverName();
        this.tags = matchResultDto.tags().map(t -> t.replaceAll("\\s+","").split(",")).orElse(null);
        this.player = player;
    }

    //Required by Jackson
    protected MatchResultSearchable() {}

    public MatchResultSearchable addMatchId(String matchId) {
        this.matchId = matchId;
        return this;
    }
}
