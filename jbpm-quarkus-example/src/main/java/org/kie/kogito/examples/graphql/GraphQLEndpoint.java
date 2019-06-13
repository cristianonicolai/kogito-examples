package org.kie.kogito.examples.graphql;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.annotation.WebServlet;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;
import io.leangen.graphql.GraphQLSchemaGenerator;
import org.kie.kogito.examples.OrderStoreService;

@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

    public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
        return new GraphQLSchemaGenerator()
                .withOperationsFromSingletons(CDI.current().select(OrderStoreService.class).get()) //register the beans
                .generate();
    }
}
