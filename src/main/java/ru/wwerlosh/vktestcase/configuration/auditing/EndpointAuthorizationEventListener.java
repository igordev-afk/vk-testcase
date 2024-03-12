package ru.wwerlosh.vktestcase.configuration.auditing;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.event.AuthorizationEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EndpointAuthorizationEventListener {

    private final AuditDAO auditDAO;

    private final HttpServletRequest request;

    public EndpointAuthorizationEventListener(AuditDAO auditDAO, HttpServletRequest request) {
        this.auditDAO = auditDAO;
        this.request = request;
    }

    @EventListener
    public void listen(AuthorizationEvent authorizationEvent) {
        AuthorizationDecision decision = authorizationEvent.getAuthorizationDecision();
        String name = authorizationEvent.getAuthentication().get().getName();
        Audit audit = new Audit();
        audit.setUsername(name);

        if (!request.getRequestURI().equals("/error")) {
            audit.setIp(request.getRemoteAddr());
            audit.setTimestamp(LocalDateTime.now());
            audit.setEndpoint(request.getRequestURI());
            audit.setMethod(request.getMethod());
            audit.setStatus(!decision.isGranted() ? "DENIED" : "GRANTED");
            auditDAO.save(audit);
            log.info("method: " + request.getMethod());
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            log.info("Username: " + name);
            log.info("Path: " + request.getRequestURI());
            log.info("Timestamp: " + LocalDateTime.now());
            log.info("Auth decision: " + decision.isGranted());
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }


}
