### Config Files
C:/Users/Lin/Downloads/config-server/application-local.yml
```yaml
logging:
  level:
    root:
      DEBUG

spring:
  data:
    redis:
      host: localhost
      port: 6397
      password: pass
      open-ssl: false

feature:
  test:
    value: for-refresh
    userList: user-a,user-b
    featureList:
      - "feature-a"
      - feature-b
```
C:/Users/Lin/Downloads/config-server/application-preprod.yml
```yaml
logging:
  level:
    root:
      INFO

spring:
  data:
    redis:
      host: localhost
      port: 6397
      password: ENC(REDIS-PASS)
      open-ssl: true

feature:
  test:
    value: for-refresh
    userList: user-c,user-d
    featureList:
      - "feature-c"
      - feature-d
```

### API
GET http://localhost:8888/application/local
```
{
  "name": "application",
  "profiles": [
    "local"
  ],
  "label": null,
  "version": null,
  "state": null,
  "propertySources": [
    {
      "name": "file:/C:/Users/Lin/Downloads/config-server/application-local.yml",
      "source": {
        "logging.level.root": "DEBUG",
        "spring.data.redis.host": "localhost",
        "spring.data.redis.port": 6397,
        "spring.data.redis.password": "pass",
        "spring.data.redis.open-ssl": false,
        "feature.test.value": "for-refresh",
        "feature.test.userList": "user-a,user-b",
        "feature.test.featureList[0]": "feature-a",
        "feature.test.featureList[1]": "feature-b"
      }
    }
  ]
}
```
GET http://localhost:8888/application/preprod
```
{
  "name": "application",
  "profiles": [
    "preprod"
  ],
  "label": null,
  "version": null,
  "state": null,
  "propertySources": [
    {
      "name": "file:/C:/Users/Lin/Downloads/config-server/application-preprod.yml",
      "source": {
        "logging.level.root": "INFO",
        "spring.data.redis.host": "localhost",
        "spring.data.redis.port": 6397,
        "spring.data.redis.password": "ENC(REDIS-PASS)",
        "spring.data.redis.open-ssl": true,
        "feature.test.value": "for-refresh",
        "feature.test.userList": "user-c,user-d",
        "feature.test.featureList[0]": "feature-c",
        "feature.test.featureList[1]": "feature-d"
      }
    }
  ]
}
```
GET http://localhost:8888/application-local.json
```
{
  "logging": {
    "level": {
      "root": "DEBUG"
    }
  },
  "spring": {
    "data": {
      "redis": {
        "host": "localhost",
        "port": 6397,
        "password": "pass",
        "open-ssl": false
      }
    }
  },
  "feature": {
    "test": {
      "value": "for-refresh",
      "userList": "user-a,user-b",
      "featureList": [
        "feature-a",
        "feature-b"
      ]
    }
  }
}
```
GET http://localhost:8888/application-preprod.json
```
{
  "logging": {
    "level": {
      "root": "INFO"
    }
  },
  "spring": {
    "data": {
      "redis": {
        "host": "localhost",
        "port": 6397,
        "password": "ENC(REDIS-PASS)",
        "open-ssl": true
      }
    }
  },
  "feature": {
    "test": {
      "value": "for-refresh",
      "userList": "user-c,user-d",
      "featureList": [
        "feature-c",
        "feature-d"
      ]
    }
  }
}
```
