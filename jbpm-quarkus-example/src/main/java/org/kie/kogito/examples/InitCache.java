package org.kie.kogito.examples;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.quarkus.runtime.StartupEvent;
import org.infinispan.client.hotrod.RemoteCacheManager;

@ApplicationScoped
public class InitCache {

    @Inject
    RemoteCacheManager cacheManager;

    protected void onStart(@Observes @Priority(value = 1) StartupEvent ev) {
        System.out.println("on start");
        initCache("orders");
        initCache("audit");
    }

    private void initCache(String cacheName) {
        cacheManager.administration().removeCache(cacheName);
        cacheManager.administration().getOrCreateCache(cacheName, "default");
    }
}
