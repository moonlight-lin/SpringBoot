package com.example.demo.aop;

import com.example.demo.annotations.Audit;
import io.micrometer.core.instrument.Metrics;
import net.logstash.logback.marker.LogstashMarker;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.wildfly.common.annotation.NotNull;

import javax.servlet.http.HttpServletRequest;

import static net.logstash.logback.marker.Markers.append;

@Aspect
@Component
@Order(1)
public class AuditAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpServletRequest request;

    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Pointcut("@annotation(com.example.demo.annotations.Audit)")
    public void pcAudit() {
    }

    @Before(value = "pcAudit()")
    public void beforeAudit(JoinPoint point) {
        threadLocal.set(System.currentTimeMillis());
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String auditName = getAnnotationName(point);
        logger.info(getMarker(method), "receive " + method + " request on uri " + uri + " to " + auditName);
    }

    @AfterReturning(value = "pcAudit()")
    public void afterAuditReturning(JoinPoint point) {
        String auditName = getAnnotationName(point);
        Metrics.counter("request_success_counter", "demo", auditName).increment();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        long interval = System.currentTimeMillis() - threadLocal.get();
        logger.info(getMarker(method),
                "after " + method + " request on uri " + uri + " return, consume " + interval + "ms");
    }

    @AfterThrowing(value = "pcAudit()", throwing = "ex")
    public void afterAuditThrowing(JoinPoint point, Exception ex) {
        String auditName = getAnnotationName(point);
        Metrics.counter("request_fail_counter", "demo", auditName).increment();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        long interval = System.currentTimeMillis() - threadLocal.get();
        logger.info(getMarker(method), "after " + method + " request on uri " + uri + ", consume "
                + interval + "ms, throw " + ex.getMessage());
    }

    private String getAnnotationName(@NotNull JoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Audit audit = methodSignature.getMethod().getAnnotation(Audit.class);
        return audit.value();
    }

    private LogstashMarker getMarker(String action) {
        // marker 字段只会在 logback-spring.xml 中使用 LogstashEncoder 的 appender 会使用到，会打出来
        // 在其他 appender 中也会打 log，但不会带上 marker 字段
        return append("type", "audit").and(append("action", action));
    }
}
