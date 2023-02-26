package com.neo.r2.gs.impl.persistence;

import com.neo.util.framework.api.persistence.search.AbstractSearchable;
import com.neo.util.framework.api.persistence.search.IndexPeriod;
import com.neo.util.framework.api.persistence.search.Searchable;
import jakarta.enterprise.context.Dependent;

import java.io.Serializable;
import java.util.Date;

@Dependent
public class RequestSearchable extends AbstractSearchable implements Searchable {

    protected Date timestamp;
    protected String requestId;
    protected String owner;
    protected String remoteAddress;
    protected String context;
    protected String status;
    protected String error;
    protected long processTime;
    protected String agent;

    public RequestSearchable(Date timestamp, String requestId, String owner, String remoteAddress, String context, String status, String error, long processTime, String agent) {
        this.timestamp = timestamp;
        this.requestId = requestId;
        this.owner = owner;
        this.remoteAddress = remoteAddress;
        this.context = context;
        this.status = status;
        this.error = error;
        this.processTime = processTime;
        this.agent = agent;
    }

    protected RequestSearchable() {}
    @Override
    public IndexPeriod getIndexPeriod() {
        return IndexPeriod.DAILY;
    }

    @Override
    public String getIndexName() {
        return "log-request";
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getOwner() {
        return owner;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public String getContext() {
        return context;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public long getProcessTime() {
        return processTime;
    }

    public String getAgent() {
        return agent;
    }
}
