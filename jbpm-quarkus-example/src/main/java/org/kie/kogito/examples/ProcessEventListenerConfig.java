package org.kie.kogito.examples;

import javax.enterprise.inject.spi.CDI;

import org.kie.kogito.examples.audit.AuditProcessEventListener;
import org.kie.kogito.process.impl.DefaultProcessEventListenerConfig;

public class ProcessEventListenerConfig extends DefaultProcessEventListenerConfig {

    public ProcessEventListenerConfig() {
        super(CDI.current().select(AuditProcessEventListener.class).get());
    }
}
