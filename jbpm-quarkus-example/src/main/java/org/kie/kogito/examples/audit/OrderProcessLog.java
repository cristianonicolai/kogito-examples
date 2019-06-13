package org.kie.kogito.examples.audit;

import org.kie.kogito.examples.demo.Order;

public class OrderProcessLog {

    private Order order;
    
    private String approver;
    
    private ProcessInstanceLog process;

    public OrderProcessLog() {
    }

    public OrderProcessLog(Order order, String approver, ProcessInstanceLog process) {
        this.order = order;
        this.approver = approver;
        this.process = process;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public ProcessInstanceLog getProcess() {
        return process;
    }

    public void setProcess(ProcessInstanceLog process) {
        this.process = process;
    }

    @Override
    public String toString() {
        return "OrderProcessLog{" +
                "order=" + order +
                ", approver='" + approver + '\'' +
                ", process=" + process +
                '}';
    }
}
