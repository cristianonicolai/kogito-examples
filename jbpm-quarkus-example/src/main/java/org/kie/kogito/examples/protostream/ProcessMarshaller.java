package org.kie.kogito.examples.protostream;

import java.io.IOException;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.examples.demo.Order;

public class ProcessMarshaller implements MessageMarshaller<Order> {

    @Override
    public Order readFrom(ProtoStreamReader reader) throws IOException {
        String number = reader.readString("orderNumber");
        boolean shipped = reader.readBoolean("shipped");
        double total = reader.readDouble("total");
        Order order = new Order();
        order.setOrderNumber(number);
        order.setShipped(shipped);
        order.setTotal(total);
        return order;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, Order order) throws IOException {
        writer.writeString("orderNumber", order.getOrderNumber());
        writer.writeBoolean("shipped", order.getShipped());
        writer.writeDouble("total", order.getTotal());
    }

    @Override
    public Class<? extends Order> getJavaClass() {
        return Order.class;
    }

    @Override
    public String getTypeName() {
        return "quickstart.Order";
    }
}
