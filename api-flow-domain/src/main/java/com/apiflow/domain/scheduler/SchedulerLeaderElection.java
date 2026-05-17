package com.apiflow.domain.scheduler;

import com.apiflow.api.cache.CacheGateway;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SchedulerLeaderElection {

    private static final String LEADER_KEY = "scheduler:leader";
    private static final long LEASE_DURATION_SECONDS = 30;

    private final CacheGateway cacheGateway;

    private final String instanceId;

    private volatile boolean leader;

    public SchedulerLeaderElection(CacheGateway cacheGateway) {
        this.cacheGateway = cacheGateway;
        this.instanceId = UUID.randomUUID().toString().substring(0, 8);
    }

    public boolean isLeader() {
        return leader;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void tryAcquireLeadership() {
        try {
            Boolean acquired = cacheGateway.setIfAbsent(LEADER_KEY, instanceId, LEASE_DURATION_SECONDS, TimeUnit.SECONDS);
            if (acquired != null && acquired) {
                if (!leader) {
                    leader = true;
                    log.info("Node [{}] acquired scheduler leadership", instanceId);
                }
            }
        } catch (Exception e) {
            log.error("Failed to try acquire leadership", e);
        }
    }

    public boolean renewLeadership() {
        if (!leader) {
            return false;
        }
        try {
            Object current = cacheGateway.get(LEADER_KEY);
            if (instanceId.equals(current)) {
                cacheGateway.expire(LEADER_KEY, LEASE_DURATION_SECONDS, TimeUnit.SECONDS);
                return true;
            } else {
                leader = false;
                log.warn("Node [{}] lost scheduler leadership (key held by another node)", instanceId);
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to renew leadership", e);
            leader = false;
            return false;
        }
    }

    public void relinquishLeadership() {
        if (leader) {
            try {
                Object current = cacheGateway.get(LEADER_KEY);
                if (instanceId.equals(current)) {
                    cacheGateway.delete(LEADER_KEY);
                }
            } catch (Exception e) {
                log.error("Failed to relinquish leadership", e);
            }
            leader = false;
            log.info("Node [{}] relinquished scheduler leadership", instanceId);
        }
    }
}