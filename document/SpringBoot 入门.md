**Ŀ¼**
- �ļ��ṹ
- pom.xml
- ������
- Controller
- Service
- �쳣����
- ����
- �Զ���ע���Լ� AOP
- ������
- ApplicationRunner
- ��ʱ����
- logback-spring.xml ������־
- Actuator
- Prometheus

**�ļ��ṹ**
![](https://img2020.cnblogs.com/blog/1926863/202011/1926863-20201128221400665-527883396.png)
<br>
**pom.xml**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.4.0</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>demo</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <logstash.logback.version>5.2</logstash.logback.version>
        <prometheus.simple.client.version>0.8.0</prometheus.simple.client.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-undertow</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
        </dependency>

        <dependency>
            <groupId>io.prometheus</groupId>
            <artifactId>simpleclient</artifactId>
            <version>${prometheus.simple.client.version}</version>
        </dependency>

        <dependency>
            <groupId>net.logstash.logback</groupId>
            <artifactId>logstash-logback-encoder</artifactId>
            <version>${logstash.logback.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```
��򵥵� spring boot �������ֻ���� spring-boot-starter
��Ϊ���������һ�� web �������Ըĳ����� spring-boot-starter-web
�� web ��Ĭ�Ϸ������� tomcat������ͨ�� exclusion ����ȥ����Ȼ������ undertow �滻
<br>
**������**
```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```
ͨ�� @SpringBootApplication ע������ spring boot ����
<br>
**Controller**
```java
package com.example.demo.controller;

import com.example.demo.annotations.Audit;
import com.example.demo.entity.User;
import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DemoController {
    @Autowired
    private DemoService demoService;

    @Audit("get_all_user_id")
    @GetMapping("/users-id")
    public List<String> getId() {
        return demoService.getUsersId();
    }

    @Audit("get_users")
    @GetMapping("/users")
    public List<User> getUsers(@RequestParam(value = "gender", required = false) String gender) {
        return demoService.getUsers(gender);
    }

    @Audit("create_user")
    @PostMapping("/users/{id}")
    public void createUser(@PathVariable("id") String id,
                           @RequestBody User user) {
        demoService.createUser(id, user);
    }

    @Audit("get_user")
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") String id) {
        return demoService.getUser(id);
    }

    @Audit("update_user")
    @PostMapping("/user/{id}")
    public void updateUser(@PathVariable("id") String id,
                           @RequestBody User user) {
        demoService.updateUser(id, user);
    }
}
```
����ʵ�� Rest �ӿڣ����������� URL �ӿڶ��� "/api/v1" ��ͷ
@PathVariable ������� URL ·����ı���
@RequestParam ������� URL ·�����ʺź���ı���
@RequestBody ����Ϣ����ı���

����
```
curl -X GET "http://localhost:9000/api/v1/user/1"
curl -X GET "http://localhost:9000/api/v1/users?gender=male"
curl -l -H "Content-type: application/json" -X POST -d '{"name":"han","gender":"male","age":35,"salary":20000}' "http://localhost:9000/api/v1/user/1"
```

�����ҵ�񽻸��� DemoService ��ʵ��
ע�� @Autowired �����Զ���ʼ���࣬��ʵ�ֵ�����

Controller ���Զ�������� body Я��������� User �࣬User ��ı���������� body ������һ��
```java
package com.example.demo.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String gender;
    private int age;
    private float salary;

    // ����յĹ��캯���Ǳ���ģ���Ȼ Controller �޷��� request �� body ȡ��
    public User() {
    }

    public User(String name, String gender, int age, float salary) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.salary = salary;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public int getAge() {
        return this.age;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }
    public float getSalary() {
        return this.salary;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getGender() {
        return this.gender;
    }
}
```
�⼸���ӿ�ʵ������ӡ��鿴�������û���Ϣ�Ĺ���
<br>
**Service**
```java
package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.DemoException;
import com.example.demo.properties.UserProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DemoService {
    @Autowired
    private UserProperties userProperties;

    private Map<String, User> users = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        users.put("1", new User("Lin", "male", 30, 20000));
        users.put("2", new User("Zhao", "female", 25, 10000));
    }

    public User getUser(String id) {
        if (! users.containsKey(id)) {
            throw new DemoException("Demo-40001", "User not exist");
        }
        return users.get(id);
    }

    public List<String> getUsersId() {
        return new ArrayList<>(users.keySet());
    }

    public List<User> getUsers(String gender) {
        if (gender != null) {
            if (!gender.equals("male") && !gender.equals("female") ) {
                throw new DemoException("Demo-40005", "Invalid gender");
            }

            return users.values().stream()
                    .filter(user -> user.getGender().equals(gender))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<> (users.values());
        }
    }

    public void createUser(String id, User user) {
        if (users.containsKey(id)) {
            throw new DemoException("Demo-40002", "User already exist");
        } else if (userProperties.getSize() <= users.size()) {
            throw new DemoException("Demo-40003", "User db is full");
        } else if (userProperties.getNameLength() <= user.getName().length()) {
            throw new DemoException("Demo-40004", "User name must <= " + userProperties.getNameLength());
        }

        users.put(id, user);
    }

    public void updateUser(String id, User user) {
        if (! users.containsKey(id)) {
            throw new DemoException("Demo-40001", "User not exist");
        } else if (userProperties.getNameLength() <= user.getName().length()) {
            throw new DemoException("Demo-40004", "User name must <= " + userProperties.getNameLength());
        }
        users.put(id, user);
    }

}
```
@PostConstruct ��ʾ������ע����ɺ���ã������ĳ�ʼ��˳���� Construct -> Autowired -> PostConstruct������ڹ��캯��ʹ���� @Autowired �ı������᲻��Ч������Ϊ����ִ�й��캯���ٳ�ʼ�� Autowired ������������������������Ҫ�� @PostConstruct������������ӿ����ڹ��캯��ִ�п��Բ��� @PostConstruct
<br>
**�쳣����**
```java
package com.example.demo.exception;

public class DemoException extends RuntimeException {
    private final String code;
    private final String message;

    public DemoException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
```
```java
package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.Serializable;

@ControllerAdvice
public class DemoExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private class Result implements Serializable {
        private String code;
        private String message;

        public String getCode() {
            return code;
        }
        public String getMessage() {
            return message;
        }

        private Result(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    @ExceptionHandler(DemoException.class)
    @ResponseBody
    public ResponseEntity<Result> handleDemoException(DemoException ex) {
        logger.error("Demo Exception", ex.getMessage(), ex);
        Result result = new Result(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Result> handleOtherError(Exception ex) {
        logger.error("Unknown Exception", ex.getMessage(), ex);
        Result result = new Result("Demo-50000", ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```
ͨ�� @ControllerAdvice �� ResponseEntityExceptionHandler ����ͳһ����������׳����쳣��@ExceptionHandler(DemoException.class) �� @ExceptionHandler(Exception.class) ��ʾ�����������ֱ����׳��� DemoException �� Exception����ͨ�� ResponseEntity ���ظ��ͻ���

DemoService ���׳��� DemoException �쳣�������쳣����������ͳһ����

����������һ���Ѿ����ڵ� user��HTTP ����ķ���������
```
{
    "code": "Demo-40002",
    "message": "User already exist"
}
```
���������� 400 Bad Request
<br>
**����**
```yaml
# application.yaml

# server:
#   port: 9000

logging:
  level:
    root: INFO
    com:
      example:
        demo: INFO

user:
  size: 10
  name-length: 10

# ��¶ Actuator �����нӿڣ���ʹ health �ӿ�չʾ������Ϣ
# http://localhost:9000/actuator
# http://localhost:9000/actuator/health
# http://localhost:9000/actuator/metrics
# http://localhost:9000/actuator/prometheus
# ��Ҫ�� pom.xml ��� actuator ��
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
```
spring boot Ĭ�� 8080 �˿ڣ�ͨ�� server.port ����ָ��Ϊ 9000

DemoService ��ȡ�� UserProperties ��������ڻ�ȡ application.yaml ��������
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
```
```java
package com.example.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "user")
@Validated
public class UserProperties {
    @NotNull
    private int size;

    @Min(5)
    @Max(20)
    private int nameLength = 10;

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

    public int getNameLength() {
        return nameLength;
    }
    public void setNameLength(int nameLength) {
        this.nameLength = nameLength;
    }
}
```
@EnableConfigurationProperties ��ʾ��ȡ�����ļ�
@ConfigurationProperties(prefix = "user") ��ʾ��ȡ user ������
@Validated��@NotNull��@Min��@Max ������֤���õ�ֵ
����������������ļ���һ�£������ӷ� - �ľ����շ��ʾ������

����ͳһ����Ĭ������
```java
package com.example.demo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

public class DemoEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        Map<String, Object> defaultMap = new HashMap<>();

        defaultMap.put("server.port", 9000);
        defaultMap.put("user.size", 100);
        defaultMap.put("user.name-length", 20);

        PropertySource<?> propertySource = new MapPropertySource("defaultProp", defaultMap);
        environment.getPropertySources().addLast(propertySource);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
```
������� application.yaml ûָ��ĳ��������� defaultMap ������Ӧ��������Ǿ�ʹ�� defaultMap ָ����ֵ

��Ҫ���� resources/META-INF/spring.factories �ļ�
```
org.springframework.boot.env.EnvironmentPostProcessor=com.example.demo.config.DemoEnvironmentPostProcessor
```
����������������
<br>
**�Զ���ע���Լ� AOP**
```java
package com.example.demo.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Audit {
    String value() default "";
}
```

�Զ���ע�Ⲣ�ӵ� Controller �������� REST �ӿ�
```java
    @Audit("get_all_user_id")
    @GetMapping("/users-id")
    public List<String> getId() {
        return demoService.getUsersId();
    }
```

Ȼ��Ҫʵ��һ�� AOP��Aspect Oriented Programming�� �����ע��������ش���
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
```
```java
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
        // marker �ֶ�ֻ���� logback-spring.xml ��ʹ�� LogstashEncoder �� appender ��ʹ�õ���������
        // ������ appender ��Ҳ��� log����������� marker �ֶ�
        return append("type", "audit").and(append("action", action));
    }
}
```
@Aspect ��ʾ��������ڽ��� AOP ����
@Order(1) ��ʾ���ȼ�����Ϊһ�������п��ܱ����ע����
@Pointcut("@annotation(com.example.demo.annotations.Audit)") ��ʾ���� Audit ��ǵĺ���
@Before(value = "pcAudit()") ��ʾ�ڱ���ǵĺ�������ǰִ��
@AfterReturning(value = "pcAudit()") ��ʾ�ڱ���ǵĺ������к�ִ��
@AfterThrowing(value = "pcAudit()", throwing = "ex") ��ʾ�ڱ���ǵĺ������쳣��ִ��

getAnnotationName ���ڻ�ȡ Audit ע���ֵ������ "get_all_user_id"

����ʵ������Ŀ�꺯��ִ��ǰ���ӡ��־�����㺯��ִ��ʱ�䣬���㺯��ִ�д������ȹ���
<br>
**������**
```java
package com.example.demo.config;

import com.example.demo.interceptor.UriInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigurerImpl implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UriInterceptor());
    }
}
```
ͨ���̳� WebMvcConfigurer ����������� UriInterceptor ���������û�����
������Ӷ��������ӵ�˳��ִ��
```
package com.example.demo.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UriInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String urlList[] = {
                "^/api/v1/users-id$",
                "^/api/v1/users(/[^//]*){0,1}$",
                "^/api/v1/user/[^//]+$"
        };

        String uri = request.getRequestURI();
        for (String urlPattern : urlList) {
            if (uri.matches(urlPattern)) {
                return true;
            }
        }
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.getWriter().write("<html><head><title>Error Page</title></head><body>Invalid Request</body></html>");
        return false;
    }
}
```
UriInterceptor ͨ���̳� HandlerInterceptorAdapter ������ preHandle ����ʵ��
preHandle �������û�����ִ��֮ǰ����

�����յ�������ȼ���ǲ��ǺϷ��� URL������Ǿͷ��� true����ʾִ����һ��������������ִ�� Controller��������ǺϷ��� URL���ͷ��������Զ���� 404 NOT FOUND ҳ�棨��������������᷵��Ĭ�ϵ� 404 ҳ�棩
<br>
**ApplicationRunner**
```
package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class InitService implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // TODO: init database
         logger.info("InitService : init database");
    }
}
```
��ʱ��Ҫ�ڳ�����������һЩ������������ ApplicationRunner ʵ��
<br>
**��ʱ����**
```java
package com.example.demo.service;

import com.example.demo.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.List;

@EnableScheduling
@Component
public class ScheduleService {
    @Autowired
    private DemoService demoService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //@Scheduled(cron = "0 0/2 * * * ?")
    @Scheduled(initialDelayString = "5000", fixedDelayString = "100000")
    public void saveUserToDB() {
        List<User> user = demoService.getUsers(null);
        // TODO: save user to database
        logger.info("schedule : save user list to database");
    }

    // �����̳߳�
    // ��֪��д��������û���ã�����д��һ��ר�ų�ʼ�����õ���ȽϺ�
    @Bean
    public TaskScheduler configTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        return scheduler;
    }
}
```
���������Ե��ã�Ҳ����ͨ�� cron ָ���̶�ʱ�����
<br>
**logback-spring.xml ������־**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!-- scan: �����ļ���������ı䣬���ᱻ���¼��أ�Ĭ��ֵΪ true -->
<!-- scanPeriod: ��������ļ��Ƿ����޸ĵ�ʱ������Ĭ�ϵ�λ�Ǻ��룬Ĭ�ϵ�ʱ����Ϊ 1 ���� -->
<!-- debug: ����Ϊ true ʱ������ӡ�� logback �ڲ���־��Ϣ��ʵʱ�鿴 logback ����״̬��Ĭ��ֵΪ false -->
<configuration  scan="true" scanPeriod="10 seconds"  debug="true">

    <!-- ����������������ͨ�� ${log.path} ���� -->
    <property name="log.path" value="./log" />

    <!-- ���������̨��
         name �������������֣������Ҫ��ӵ� <root>��
         class �Ǵ�ӡ��־���࣬ConsoleAppender �Ǵ򵽿���̨ -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <!-- filter ָ�����ڹ��˵��࣬�������Զ���ģ������ǹ��˴��ڵ��� info level ����־ -->
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>info</level>
        </filter>

        <encoder>
            <!-- �����־�ĸ�ʽ -->
            <Pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] [%-5level] [%logger{50}] - %msg%n</Pattern>
            <!-- �����ַ��� -->
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!-- ������ļ� -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- ·�����ļ��� -->
        <file>${log.path}/demo.log</file>

        <!-- ����־�ļ�ֻ��¼ info ����� -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>info</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>

        <!-- �����־�ĸ�ʽ -->
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] [%-5level] [%logger{50}] - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>

        <!-- ��־�Ĺ������ԣ������ڣ�����С��¼ -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- ��־�鵵 -->
            <fileNamePattern>${log.path}/save/demo-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!-- ��־�ļ��������� -->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
    </appender>

    <!-- ������ļ���ʹ�� LogstashEncoder ��� json ��ʽ����־ -->
    <appender name="FILE-JSON" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- ·�����ļ��� -->
        <file>${log.path}/demo-json.log</file>

        <!-- �Զ��� filter ֻ��� audit �ķ� ERROR ��־ -->
        <Filter class="com.example.demo.filter.FileJsonLogAuditFilter" />

        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <includeCallerData>true</includeCallerData>
            <customFields>{"group":"example", "service":"demo"}</customFields>
            <timestampPattern>yyyy-MM-dd HH:mm:ss.SSS'Z'</timestampPattern>
            <timeZone>UTC +0</timeZone>
            <fieldNames>
                <timestamp>timestamp</timestamp>
                <thread>thread</thread>
                <logger>logger</logger>
                <message>message</message>
                <level>level</level>
                <callerLine>line</callerLine>
                <!-- ���������Ϊ ignore �Ļ������� -->
                <version>[ignore]</version>
                <levelValue>[ignore]</levelValue>
                <callerClass>[ignore]</callerClass>
                <callerMethod>[ignore]</callerMethod>
                <callerFile>[ignore]</callerFile>
            </fieldNames>
        </encoder>

        <!-- ��־�Ĺ������ԣ������ڣ�����С��¼ -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- ��־�鵵 -->
            <fileNamePattern>${log.path}/save/demo-json-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>100MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>
            <!-- ��־�ļ��������� -->
            <maxHistory>15</maxHistory>
        </rollingPolicy>
    </appender>

    <root level="info" additivity="true">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
        <appender-ref ref="FILE-JSON" />
    </root>

</configuration>
```
SpringBoot ��Ĭ��ɨ�� classpath ����� logback.xml��logback-spring.xml �ļ�

������Զ����� appender��ÿ�� appender ������־���������ǵ� console �����ļ���ʹ��ʲô���� filter ���ˣ������ʽ��ô�����ȵ�

��������е� FILE-JSON appender ʹ���� net.logstash.logback.encoder.LogstashEncoder ������� JSON ��ʽ����־������ʹ�����Զ���� filter
```
        <dependency>
            <groupId>net.logstash.logback</groupId>
            <artifactId>logstash-logback-encoder</artifactId>
            <version>${logstash.logback.version}</version>
        </dependency>
```
```
package com.example.demo.filter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

import static net.logstash.logback.marker.Markers.append;

public class FileJsonLogAuditFilter extends Filter<Object> {

    @Override
    public FilterReply decide(Object eventObject) {
        LoggingEvent event = (LoggingEvent) eventObject;
        Level level = event.getLevel();
        Marker marker = event.getMarker();

        if (level != Level.ERROR) {
            if (marker != null && marker.contains(append("type", "audit"))) {
                return FilterReply.ACCEPT;
            } else {
                return FilterReply.DENY;
            }
        }
        return FilterReply.DENY;
    }
}
```
���Կ������ Filter ֻ����� ERROR ������ {"type": "audit"} ��� marker ����־���
```
import net.logstash.logback.marker.LogstashMarker;

    private LogstashMarker getMarker(String action) {
        // marker �ֶ�ֻ���� logback-spring.xml ��ʹ�� LogstashEncoder �� appender ��ʹ�õ���������
        // ������ appender ��Ҳ��� log����������� marker �ֶ�
        return append("type", "audit").and(append("action", action));
    }

logger.info(getMarker(method), "receive " + method + " request on uri " + uri + " to " + auditName);
```
���Կ��� Audit AOP �о�ʹ���� marker����Ҫ���ڱ�� log������������־�в����ӡ marker������ LogstashEncoder �� appender ���ӡ����

FILE-JSON appender �� log ����������
```
{"timestamp":"2020-11-24 13:13:43.259Z","message":"after GET request on uri /api/v1/users return, consume 3ms","logger":"com.example.demo.aop.AuditAspect","thread":"XNIO-1 task-1","level":"INFO","type":"audit","action":"GET","line":55,"group":"example","service":"demo"}
```

CONSOLE appender �� log ����������
```
2020-11-25 01:08:25.203 [main] [INFO ] [com.example.demo.service.InitService] - InitService : init database
```
��� appender ͬʱ������
<br>
**Actuator**
Spring Boot Actuator ģ���ṩ����������Ĺ��ܣ����罡����飬��ƣ�ָ���ռ���HTTP ���ٵ�
��Щ���ܶ�����ͨ�� HTTP �� JMX ����
```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
```

application.yaml ��Ҫ����
```yaml
# ��¶ Actuator �����нӿڣ���ʹ health �ӿ�չʾ������Ϣ
# http://localhost:9000/actuator
# http://localhost:9000/actuator/health
# http://localhost:9000/actuator/metrics
# http://localhost:9000/actuator/prometheus
# ��Ҫ�� pom.xml ��� actuator ��
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
```
http://localhost:9000/actuator ���Բ鿴����Щ Actuator �����ã����� health��metrics��beans ��

��һЩ����
http://localhost:9000/actuator/health �ķ�������
```
{
    "status": "UP",
    "components": {
        "diskSpace": {
            "status": "UP",
            "details": {
                "total": 175025696768,
                "free": 33702227968,
                "threshold": 10485760,
                "exists": true
            }
        },
        "ping": {
            "status": "UP"
        }
    }
}
```
http://localhost:9000/actuator/metrics �ķ�������
```
{
    "names": [
        "http.server.requests",
        "jvm.buffer.count",
        "jvm.buffer.memory.used",
        "jvm.buffer.total.capacity",
        "jvm.classes.loaded",
        "jvm.classes.unloaded",
        "jvm.gc.live.data.size",
        "jvm.gc.max.data.size",
        "jvm.gc.memory.allocated",
        "jvm.gc.memory.promoted",
        "jvm.memory.committed",
        "jvm.memory.max",
        "jvm.memory.used",
        "jvm.threads.daemon",
        "jvm.threads.live",
        "jvm.threads.peak",
        "jvm.threads.states",
        "logback.events",
        "process.cpu.usage",
        "process.start.time",
        "process.uptime",
        "request_success_counter",
        "system.cpu.count",
        "system.cpu.usage"
    ]
}
```
���Կ��������� AOP ����� request_success_counter ������Կ���
��һ���鿴 http://localhost:9000/actuator/metrics/request_success_counter
```
{
    "name": "request_success_counter",
    "description": null,
    "baseUnit": null,
    "measurements": [
        {
            "statistic": "COUNT",
            "value": 3.0
        }
    ],
    "availableTags": [
        {
            "tag": "demo",
            "values": [
                "get_all_user_id",
                "get_users"
            ]
        }
    ]
}
```
ͳ���˷��ʴ���
<br>
**Prometheus**
```
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
        </dependency>

        <dependency>
            <groupId>io.prometheus</groupId>
            <artifactId>simpleclient</artifactId>
            <version>${prometheus.simple.client.version}</version>
        </dependency>
```
�� AuditAspect ���У�����ʹ���� metrics ����ͳ��
������ metrics actuator ���Կ������� prometheus actuator Ҳ���Կ���
```
String auditName = getAnnotationName(point);

Metrics.counter("request_success_counter", "demo", auditName).increment();

Metrics.counter("request_fail_counter", "demo", auditName).increment();
```
http://localhost:9000/actuator/prometheus
```
# HELP process_cpu_usage The "recent cpu usage" for the Java Virtual Machine process
# TYPE process_cpu_usage gauge
process_cpu_usage 0.0
# HELP http_server_requests_seconds  
# TYPE http_server_requests_seconds summary
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/api/v1/users",} 1.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/api/v1/users",} 0.155734128
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/metrics",} 2.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/metrics",} 0.011018067
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",} 1.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",} 0.074643576
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/api/v1/users-id",} 2.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/api/v1/users-id",} 0.01569854
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator",} 1.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator",} 0.153424829
http_server_requests_seconds_count{exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/**",} 1.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/**",} 0.067295168
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/metrics/{requiredMetricName}",} 1.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/metrics/{requiredMetricName}",} 0.021771337
http_server_requests_seconds_count{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/beans",} 1.0
http_server_requests_seconds_sum{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/beans",} 0.097360332
# HELP http_server_requests_seconds_max  
# TYPE http_server_requests_seconds_max gauge
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/api/v1/users",} 0.0
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/metrics",} 0.0
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/health",} 0.0
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/api/v1/users-id",} 0.0
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator",} 0.0
http_server_requests_seconds_max{exception="None",method="GET",outcome="CLIENT_ERROR",status="404",uri="/**",} 0.0
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/metrics/{requiredMetricName}",} 0.0
http_server_requests_seconds_max{exception="None",method="GET",outcome="SUCCESS",status="200",uri="/actuator/beans",} 0.0
# HELP jvm_memory_used_bytes The amount of used memory
# TYPE jvm_memory_used_bytes gauge
jvm_memory_used_bytes{area="heap",id="PS Survivor Space",} 0.0
jvm_memory_used_bytes{area="heap",id="PS Old Gen",} 1.5820736E7
jvm_memory_used_bytes{area="heap",id="PS Eden Space",} 1.73317984E8
jvm_memory_used_bytes{area="nonheap",id="Metaspace",} 4.5230552E7
jvm_memory_used_bytes{area="nonheap",id="Code Cache",} 1.296704E7
jvm_memory_used_bytes{area="nonheap",id="Compressed Class Space",} 6097952.0
# HELP jvm_gc_live_data_size_bytes Size of long-lived heap memory pool after reclamation
# TYPE jvm_gc_live_data_size_bytes gauge
jvm_gc_live_data_size_bytes 0.0
# HELP jvm_gc_memory_promoted_bytes_total Count of positive increases in the size of the old generation memory pool before GC to after GC
# TYPE jvm_gc_memory_promoted_bytes_total counter
jvm_gc_memory_promoted_bytes_total 0.0
# HELP jvm_classes_unloaded_classes_total The total number of classes unloaded since the Java virtual machine has started execution
# TYPE jvm_classes_unloaded_classes_total counter
jvm_classes_unloaded_classes_total 0.0
# HELP jvm_threads_states_threads The current number of threads having NEW state
# TYPE jvm_threads_states_threads gauge
jvm_threads_states_threads{state="runnable",} 10.0
jvm_threads_states_threads{state="blocked",} 0.0
jvm_threads_states_threads{state="waiting",} 15.0
jvm_threads_states_threads{state="timed-waiting",} 2.0
jvm_threads_states_threads{state="new",} 0.0
jvm_threads_states_threads{state="terminated",} 0.0
# HELP jvm_buffer_memory_used_bytes An estimate of the memory that the Java virtual machine is using for this buffer pool
# TYPE jvm_buffer_memory_used_bytes gauge
jvm_buffer_memory_used_bytes{id="direct",} 118702.0
jvm_buffer_memory_used_bytes{id="mapped",} 0.0
# HELP jvm_buffer_count_buffers An estimate of the number of buffers in the pool
# TYPE jvm_buffer_count_buffers gauge
jvm_buffer_count_buffers{id="direct",} 10.0
jvm_buffer_count_buffers{id="mapped",} 0.0
# HELP jvm_gc_memory_allocated_bytes_total Incremented for an increase in the size of the (young) heap memory pool after one GC to before the next
# TYPE jvm_gc_memory_allocated_bytes_total counter
jvm_gc_memory_allocated_bytes_total 0.0
# HELP jvm_buffer_total_capacity_bytes An estimate of the total capacity of the buffers in this pool
# TYPE jvm_buffer_total_capacity_bytes gauge
jvm_buffer_total_capacity_bytes{id="direct",} 118702.0
jvm_buffer_total_capacity_bytes{id="mapped",} 0.0
# HELP logback_events_total Number of error level events that made it to the logs
# TYPE logback_events_total counter
logback_events_total{level="warn",} 0.0
logback_events_total{level="debug",} 0.0
logback_events_total{level="error",} 0.0
logback_events_total{level="trace",} 0.0
logback_events_total{level="info",} 25.0
# HELP request_success_counter_total  
# TYPE request_success_counter_total counter
request_success_counter_total{demo="get_all_user_id",} 2.0
request_success_counter_total{demo="get_users",} 1.0
# HELP system_cpu_count The number of processors available to the Java virtual machine
# TYPE system_cpu_count gauge
system_cpu_count 4.0
# HELP jvm_threads_daemon_threads The current number of live daemon threads
# TYPE jvm_threads_daemon_threads gauge
jvm_threads_daemon_threads 13.0
# HELP jvm_threads_peak_threads The peak live thread count since the Java virtual machine started or peak was reset
# TYPE jvm_threads_peak_threads gauge
jvm_threads_peak_threads 27.0
# HELP jvm_memory_committed_bytes The amount of memory in bytes that is committed for the Java virtual machine to use
# TYPE jvm_memory_committed_bytes gauge
jvm_memory_committed_bytes{area="heap",id="PS Survivor Space",} 1.1534336E7
jvm_memory_committed_bytes{area="heap",id="PS Old Gen",} 6.8681728E7
jvm_memory_committed_bytes{area="heap",id="PS Eden Space",} 2.03948032E8
jvm_memory_committed_bytes{area="nonheap",id="Metaspace",} 4.8324608E7
jvm_memory_committed_bytes{area="nonheap",id="Code Cache",} 1.4352384E7
jvm_memory_committed_bytes{area="nonheap",id="Compressed Class Space",} 6684672.0
# HELP process_uptime_seconds The uptime of the Java virtual machine
# TYPE process_uptime_seconds gauge
process_uptime_seconds 587.891
# HELP jvm_memory_max_bytes The maximum amount of memory in bytes that can be used for memory management
# TYPE jvm_memory_max_bytes gauge
jvm_memory_max_bytes{area="heap",id="PS Survivor Space",} 1.1534336E7
jvm_memory_max_bytes{area="heap",id="PS Old Gen",} 1.244659712E9
jvm_memory_max_bytes{area="heap",id="PS Eden Space",} 5.95591168E8
jvm_memory_max_bytes{area="nonheap",id="Metaspace",} -1.0
jvm_memory_max_bytes{area="nonheap",id="Code Cache",} 2.5165824E8
jvm_memory_max_bytes{area="nonheap",id="Compressed Class Space",} 1.073741824E9
# HELP jvm_classes_loaded_classes The number of classes that are currently loaded in the Java virtual machine
# TYPE jvm_classes_loaded_classes gauge
jvm_classes_loaded_classes 9090.0
# HELP jvm_threads_live_threads The current number of live threads including both daemon and non-daemon threads
# TYPE jvm_threads_live_threads gauge
jvm_threads_live_threads 27.0
# HELP jvm_gc_max_data_size_bytes Max size of long-lived heap memory pool
# TYPE jvm_gc_max_data_size_bytes gauge
jvm_gc_max_data_size_bytes 1.244659712E9
# HELP system_cpu_usage The "recent cpu usage" for the whole system
# TYPE system_cpu_usage gauge
system_cpu_usage 0.23906219894981506
# HELP process_start_time_seconds Start time of the process since unix epoch.
# TYPE process_start_time_seconds gauge
process_start_time_seconds 1.60623930251E9
```
���Կ����кܶ�ϵͳĬ�ϵ�ͳ�ƣ�Ҳ�������Զ����
```
# HELP request_success_counter_total  
# TYPE request_success_counter_total counter
request_success_counter_total{demo="get_all_user_id",} 2.0
request_success_counter_total{demo="get_users",} 1.0
```
<br>
<br>