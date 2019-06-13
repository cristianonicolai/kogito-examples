package org.kie.kogito.examples.protostream;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.examples.audit.OrderProcessLog;
import org.kie.kogito.examples.audit.ProcessInstanceLog;
import org.kie.kogito.examples.demo.Order;

public class OrderProcessLogMarshaller implements MessageMarshaller<OrderProcessLog> {

    @Override
    public OrderProcessLog readFrom(ProtoStreamReader reader) throws IOException {
        Order order = reader.readObject("order", Order.class);
        String approver = reader.readString("approver");
        ProcessInstanceLog log = reader.readObject("process", ProcessInstanceLog.class);
        return new OrderProcessLog(order, approver, log);
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, OrderProcessLog log) throws IOException {
        writer.writeObject("order", log.getOrder(), Order.class);
        writer.writeString("approver", log.getApprover());
        writer.writeObject("process", log.getProcess(), ProcessInstanceLog.class);
    }

    @Override
    public Class<? extends OrderProcessLog> getJavaClass() {
        return OrderProcessLog.class;
    }

    @Override
    public String getTypeName() {
        return "quickstart.OrderProcessLog";
    }
}
