package org.kie.kogito.examples.protostream;

import java.io.IOException;
import java.util.Date;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.examples.audit.NodeInstanceLog;

public class NodeInstanceLogMarshaller implements MessageMarshaller<NodeInstanceLog> {

    @Override
    public NodeInstanceLog readFrom(ProtoStreamReader reader) throws IOException {
        String nodeInstanceId = reader.readString("nodeInstanceId");
        Date enter = reader.readDate("enter");
        Date exit = reader.readDate("exit");
        String nodeName = reader.readString("nodeName");
        String nodeType = reader.readString("nodeType");
        NodeInstanceLog log = new NodeInstanceLog();
        log.setNodeInstanceId(nodeInstanceId);
        log.setNodeName(nodeName);
        log.setNodeType(nodeType);
        log.setEnter(enter);
        log.setExit(exit);
        return log;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, NodeInstanceLog log) throws IOException {
        writer.writeString("nodeInstanceId", log.getNodeInstanceId());
        writer.writeDate("enter", log.getEnter());
        writer.writeDate("exit", log.getExit());
        writer.writeString("nodeName", log.getNodeName());
        writer.writeString("nodeType", log.getNodeType());
    }

    @Override
    public Class<? extends NodeInstanceLog> getJavaClass() {
        return NodeInstanceLog.class;
    }

    @Override
    public String getTypeName() {
        return "quickstart.NodeInstanceLog";
    }
}
