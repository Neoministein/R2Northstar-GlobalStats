package com.neo.r2.gs.impl;

import com.neo.r2.gs.api.scheduler.AbstractScheduler;
import com.neo.util.framework.api.janitor.JanitorService;
import io.helidon.microprofile.scheduling.Scheduled;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JanitorExecution extends AbstractScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JanitorExecution.class);

    @Inject
    protected JanitorService janitorService;

    @Override
    protected void scheduledAction() {
        janitorService.executeAll();
    }

    @Scheduled(value = "0 0 * * *")
    public void monitorSchedule() {
        super.runSchedule();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }


}