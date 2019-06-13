package org.kie.kogito.examples.protostream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.protostream.MessageMarshaller;

@ApplicationScoped
public class MarshallerConfiguration {

    @Produces
    MessageMarshaller orderMarshaller() {
        return new OrderMarshaller();
    }

    @Produces
    MessageMarshaller orderLogMarshaller() {
        return new OrderProcessLogMarshaller();
    }

    @Produces
    MessageMarshaller processInstanceLogMarshaller() {
        return new ProcessInstanceLogMarshaller();
    }

    @Produces
    MessageMarshaller nodeInstanceLogMarshaller() {
        return new NodeInstanceLogMarshaller();
    }
}
