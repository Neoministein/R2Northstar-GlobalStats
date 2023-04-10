package com.neo.r2.gs.impl.persistence;

import com.neo.util.framework.api.persistence.search.AbstractSearchable;
import com.neo.util.framework.api.persistence.search.IndexPeriod;
import com.neo.util.framework.api.persistence.search.Searchable;
import jakarta.enterprise.context.Dependent;

import java.time.Instant;

@Dependent
public class PlayerUidSearchable extends AbstractSearchable implements Searchable {

    public static final String INDEX_NAME = "player-uid";
    public static final String U_ID = "uId";
    public static final String PLAYER_NAME = "playerName";

    protected Instant lastUpdate = Instant.now();

    protected String uId;
    protected String playerName;


    public PlayerUidSearchable(String  uId, String playerName) {
        this.uId = uId;
        this.playerName = playerName;
    }

    //Required by Jackson
    protected PlayerUidSearchable() {}

    @Override
    public String getBusinessId() {
        return uId;
    }

    @Override
    public String getIndexName() {
        return INDEX_NAME;
    }

    @Override
    public IndexPeriod getIndexPeriod() {
        return IndexPeriod.ALL;
    }
}
