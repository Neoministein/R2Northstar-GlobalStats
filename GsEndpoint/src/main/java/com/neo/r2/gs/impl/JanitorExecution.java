package com.neo.r2.gs.impl;

import com.neo.r2.gs.api.scheduler.AbstractScheduler;
import com.neo.util.framework.elastic.impl.ElasticSearchRetentionJanitor;
import io.helidon.microprofile.scheduling.Scheduled;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class JanitorExecution extends AbstractScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JanitorExecution.class);

    @Inject
    protected ElasticSearchRetentionJanitor elasticSearchRetentionJanitor;

    @Override
    protected void scheduledAction() {
        elasticSearchRetentionJanitor.cleanup(LocalDate.now());
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