package org.kie.kogito.examples;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.quarkus.infinispan.client.runtime.Remote;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.QueryBuilder;
import org.infinispan.query.dsl.QueryFactory;
import org.kie.kogito.examples.audit.OrderProcessLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/search")
public class OrderStoreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStoreService.class);

    @Inject
    @Remote("audit")
    RemoteCache<Integer, OrderProcessLog> auditCache;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderProcessLog> searchAll() {
        QueryFactory queryFactory = Search.getQueryFactory(auditCache);
        return queryFactory.from(OrderProcessLog.class).build().list();
    }

    @GET
    @Path("/{term}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderProcessLog> search(@PathParam("term") String term) {
        System.out.println("term = " + term);
        if (term == null || term.trim().isEmpty()) {
            return Collections.emptyList();
        }

        QueryFactory queryFactory = Search.getQueryFactory(auditCache);

        QueryBuilder qb = queryFactory.from(OrderProcessLog.class);
        qb.having("order.orderNumber").like("%" + term + "%");
        return qb.build().list();
    }
}
