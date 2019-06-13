package org.kie.kogito.examples.protostream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.infinispan.protostream.MessageMarshaller;
import org.kie.kogito.examples.audit.NodeInstanceLog;
import org.kie.kogito.examples.audit.ProcessInstanceLog;

public class ProcessInstanceLogMarshaller implements MessageMarshaller<ProcessInstanceLog> {

    @Override
    public ProcessInstanceLog readFrom(ProtoStreamReader reader) throws IOException {
        Integer status = reader.readInt("status");
        Long processInstanceId = reader.readLong("processInstanceId");
        String processId = reader.readString("processId");
        Date start = reader.readDate("start");
        Date end = reader.readDate("end");
        List<NodeInstanceLog> nodes = reader.readCollection("nodes", new ArrayList<>(), NodeInstanceLog.class);
        ProcessInstanceLog log = new ProcessInstanceLog();
        log.setStatus(status);
        log.setProcessInstanceId(processInstanceId);
        log.setProcessId(processId);
        log.setStart(start);
        log.setEnd(end);
        log.setNodes(nodes);
        return log;
    }

    @Override
    public void writeTo(ProtoStreamWriter writer, ProcessInstanceLog log) throws IOException {
        writer.writeInt("status", log.getStatus());
        writer.writeLong("processInstanceId", log.getProcessInstanceId());
        writer.writeString("processId", log.getProcessId());
        writer.writeDate("start", log.getStart());
        writer.writeDate("end", log.getEnd());
        writer.writeCollection("nodes", log.getNodes(), NodeInstanceLog.class);
    }

    @Override
    public Class<? extends ProcessInstanceLog> getJavaClass() {
        return ProcessInstanceLog.class;
    }

    @Override
    public String getTypeName() {
        return "quickstart.ProcessInstanceLog";
    }
}
