package org.kie.kogito.examples.audit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.infinispan.client.runtime.Remote;
import org.infinispan.client.hotrod.RemoteCache;
import org.jbpm.workflow.instance.impl.NodeInstanceImpl;
import org.kie.api.event.process.DefaultProcessEventListener;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEvent;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.kogito.examples.demo.Order;

@ApplicationScoped
public class AuditProcessEventListener extends DefaultProcessEventListener {

    @Inject
    AuditEventBuilder builder;

    @Inject
    @Remote("orders")
    RemoteCache<String, Order> orderRemoteCache;

    @Inject
    @Remote("audit")
    RemoteCache<Long, OrderProcessLog> auditCache;

    @Override
    public void beforeProcessStarted(ProcessStartedEvent event) {
        System.out.println("\nbeforeProcessStarted event = " + event);
        final ProcessInstance processInstance = event.getProcessInstance();
        final OrderProcessLog log = getOrderProcessLog(event, (ProcessInstanceLog) builder.buildEvent(event));
        auditCache.putAsync(processInstance.getId(), log);
    }

    @Override
    public void afterProcessStarted(ProcessStartedEvent event) {
        System.out.println("\nafterProcessStarted event = " + event);
        final ProcessInstance processInstance = event.getProcessInstance();
        final Order order = (Order) processInstance.getVariables().get("order");
        orderRemoteCache.put(order.getOrderNumber(), order);

        updateOrderProcessLog(event);
    }

    private OrderProcessLog getOrderProcessLog(ProcessEvent event, ProcessInstanceLog process) {
        final ProcessInstance processInstance = event.getProcessInstance();

        final OrderProcessLog log = new OrderProcessLog();
        log.setProcess(process);
        log.setApprover((String) processInstance.getVariables().get("approver"));
        final Order order = (Order) processInstance.getVariables().get("order");
        log.setOrder(order);
        return log;
    }

    @Override
    public void afterProcessCompleted(ProcessCompletedEvent event) {
        System.out.println("\nafterProcessCompleted event = " + event);
        final Order order = (Order) event.getProcessInstance().getVariables().get("order");
        final ProcessInstance processInstance = event.getProcessInstance();

        orderRemoteCache.replaceAsync(order.getOrderNumber(), order);

        ProcessInstanceLog pInstanceLog = auditCache.get(processInstance.getId()).getProcess();
        updateOrderProcessLog(event, (ProcessInstanceLog) builder.buildEvent(event, pInstanceLog));
    }

    @Override
    public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
        NodeInstanceLog log = (NodeInstanceLog) builder.buildEvent(event);
        addNodeInstanceLog(event.getProcessInstance().getId(), log);
        ((NodeInstanceImpl) event.getNodeInstance()).getMetaData().put("NodeInstanceLog", log);
    }

    @Override
    public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
        NodeInstanceLog log = (NodeInstanceLog) ((NodeInstanceImpl) event.getNodeInstance()).getMetaData().get("NodeInstanceLog");
        builder.buildEvent(event, log);
    }

    @Override
    public void beforeNodeLeft(ProcessNodeLeftEvent event) {
        Long pId = event.getProcessInstance().getId();
        OrderProcessLog orderProcessLog = auditCache.get(pId);
        if (orderProcessLog != null) {
            String nodeId = Long.toString(event.getNodeInstance().getNodeId());
            orderProcessLog.getProcess().getNodes().stream().filter(node -> node.getNodeInstanceId().equals(nodeId)).forEach(node -> builder.buildEvent(event, node));
            System.out.println("Override = " + orderProcessLog);
            auditCache.replaceAsync(pId, orderProcessLog);
        }
    }

    private void addNodeInstanceLog(Long pId, NodeInstanceLog log) {
        final OrderProcessLog orderProcessLog = auditCache.get(pId);
        if (orderProcessLog != null) {
            orderProcessLog.getProcess().getNodes().add(log);
            System.out.println("add node instance log = " + log);
            auditCache.replaceAsync(pId, orderProcessLog);
        }
    }

    @Override
    public void afterVariableChanged(ProcessVariableChangedEvent event) {
        System.out.println("afterVariableChanged event = " + event);
        updateOrderProcessLog(event);
    }

    private void updateOrderProcessLog(ProcessEvent event, ProcessInstanceLog pInstanceLog) {
        final ProcessInstance processInstance = event.getProcessInstance();
        final OrderProcessLog log = getOrderProcessLog(event, pInstanceLog);
        System.out.println("updateOrderProcessLog log = " + log);
        auditCache.replaceAsync(processInstance.getId(), log);
    }

    private void updateOrderProcessLog(ProcessEvent event) {
        final ProcessInstance processInstance = event.getProcessInstance();
        final OrderProcessLog orderProcessLog = auditCache.get(processInstance.getId());
        if (orderProcessLog == null) {
            return;
        }
        ProcessInstanceLog pInstanceLog = orderProcessLog.getProcess();
        updateOrderProcessLog(event, pInstanceLog);
    }
}
