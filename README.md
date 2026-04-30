# p4pa-iam-sync

This application belong to the **batch** tier of the **Piattaforma Unitaria** product.

See [PU Microservice Architecture](https://raw.githubusercontent.com/pagopa/p4pa-doc/refs/heads/main/reference/technical-docs/Architettura_microservizi.pdf) for more details.

See [p4pa-doc](https://github.com/pagopa/p4pa-doc) for further documentation.

## 🧱 Role

* To receive organization onboarding events on the product
* To receive user events on the product

## 🌐 AsyncAPIs
See [AsyncAPI](asyncapi/generated.asyncapi.json), exposed through the following path:
* `/springwolf/asyncapi-ui.html`

## 🔎 Monitoring
See available actuator endpoints through the following path:
* `/actuator`

### 📌 Relevant endpoints
* Health (provide an accessToken to see details): `/actuator/health`
  * Liveness: `/actuator/health/liveness`
  * Readiness: `/actuator/health/readiness`
* Metrics: `/actuator/metrics`
  * Prometheus: `/actuator/prometheus`

Further endpoints are exposed through the JMX console.

## ✏️ Logging
See [log configured pattern](/src/main/resources/logback-spring.xml).

## 🔗 Dependencies

### 🗄️ Resources
* Kafka

### 🧩 Microservices
* [p4pa-auth](https://github.com/pagopa/p4pa-auth):
  * To obtain a technical access token (used on WF to call inner microservices);
  * To handle organization updates;
  * To handle operators updates;
* [p4pa-organization](https://github.com/pagopa/p4pa-organization):
  * To handle organization creation/update.

## 🔧 Configuration

See [application.yml](src/main/resources/application.yml) for each configurable property.

### 📌 Relevant configurations

#### 🌐 Application Server
| ENV         | DESCRIPTION                       | DEFAULT |
|-------------|-----------------------------------|---------|
| SERVER_PORT | Application server listening port | 8080    |

#### ✏️ Logging
| ENV                                      | DESCRIPTION                                                                                                                                                                      | DEFAULT |
|------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------|
| LOG_LEVEL_ROOT                           | Base level                                                                                                                                                                       | INFO    |
| LOG_LEVEL_PAGOPA                         | Base level of custom classes                                                                                                                                                     | INFO    |
| LOG_LEVEL_SPRING                         | Level applied to Spring framework                                                                                                                                                | INFO    |
| LOG_LEVEL_SPRING_BOOT_AVAILABILITY       | To print availability events                                                                                                                                                     | DEBUG   |
| LOGGING_LEVEL_API_REQUEST_EXCEPTION      | Level applied to APIs exception                                                                                                                                                  | INFO    |
| LOG_LEVEL_PERFORMANCE_LOG                | Level applied to [PerformanceLog](https://pagopa.atlassian.net/wiki/spaces/SPAC/pages/1540096383/Logging#2.2.-Log-di-performance)                                                | INFO    |
| LOG_LEVEL_PERFORMANCE_LOG_API_REQUEST    | Level applied to [API Performance Log](https://pagopa.atlassian.net/wiki/spaces/SPAC/pages/1540096383/Logging#2.2.2.1.-Log-di-perfomance-per-le-API)                             | INFO    |
| LOG_LEVEL_PERFORMANCE_LOG_REST_INVOKE    | Level applied to [REST invoke Performance Log](https://pagopa.atlassian.net/wiki/spaces/SPAC/pages/1540096383/Logging#2.2.2.2.-Log-di-performance-per-i-servizi-REST-integrati)  | INFO    |
| LOG_LEVEL_PERFORMANCE_LOG_INCOMING_EVENT | Level applied to [Incoming event Performance Log](https://pagopa.atlassian.net/wiki/spaces/SPAC/pages/1540096383/Logging#2.2.2.3.-Log-di-performance-per-gli-eventi-in-ingresso) | INFO    |

#### 🔁 Integrations

##### 🔗 REST
| ENV                                               | DESCRIPTION                               | DEFAULT |
|---------------------------------------------------|-------------------------------------------|---------|
| DEFAULT_REST_CONNECTION_POOL_SIZE                 | Default connection pool size              | 10      |
| DEFAULT_REST_CONNECTION_POOL_SIZE_PER_ROUTE       | Default connection pool size per route    | 5       |
| DEFAULT_REST_CONNECTION_POOL_TIME_TO_LIVE_MINUTES | Default connection pool TTL (minutes)     | 10      |
| DEFAULT_REST_TIMEOUT_CONNECT_MILLIS               | Default connection timeout (milliseconds) | 120000  |
| DEFAULT_REST_TIMEOUT_READ_MILLIS                  | Default read timeout (milliseconds)       | 120000  |

##### 🧩 Microservices
| ENV                                | DESCRIPTION                                    | DEFAULT |
|------------------------------------|------------------------------------------------|---------|
| AUTH_BASE_URL                      | Auth microservice URL                          |         |
| AUTH_MAX_ATTEMPTS                  | Auth API max attempts                          | 3       |
| AUTH_WAIT_TIME_MILLIS              | Auth retry waiting time (milliseconds)         | 500     |
| AUTH_PRINT_BODY_WHEN_ERROR         | To print body when an error occurs             | true    |
| ORGANIZATION_BASE_URL              | Organization microservice URL                  |         |
| ORGANIZATION_MAX_ATTEMPTS          | Organization API max attempts                  | 3       |
| ORGANIZATION_WAIT_TIME_MILLIS      | Organization retry waiting time (milliseconds) | 500     |
| ORGANIZATION_PRINT_BODY_WHEN_ERROR | To print body when an error occurs             | true    |

##### 🌀 KAFKA
| ENV                                   | DESCRIPTION                                                                                        | DEFAULT  |
|---------------------------------------|----------------------------------------------------------------------------------------------------|----------|
| KAFKA_CONFIG_HEARTBEAT_INTERVAL_MS    | Hearth beat interval (milliseconds)                                                                | 3000     |
| KAFKA_CONFIG_SESSION_TIMEOUT_MS       | Session timeout (milliseconds)                                                                     | 30000    |
| KAFKA_CONFIG_REQUEST_TIMEOUT_MS       | Request timeout (milliseconds)                                                                     | 60000    |
| KAFKA_CONFIG_METADATA_MAX_AGE         | Metadata max age (milliseconds)                                                                    | 180000   |
| KAFKA_CONFIG_SASL_MECHANISM           | SASL mechanism                                                                                     | PLAIN    |
| KAFKA_CONFIG_SECURITY_PROTOCOL        | Security protocol                                                                                  | SASL_SSL |
| KAFKA_CONFIG_MAX_REQUEST_SIZE         | Max request size                                                                                   | 1000000  |
| KAFKA_IAM_ORGANIZATIONS_BINDER_BROKER | Comma separated list of brokers to which the Kafka binder connects to retrieve organization events |          |
| KAFKA_IAM_USERS_BINDER_BROKER         | Comma separated list of brokers to which the Kafka binder connects to retrieve users events        |          |

###### 📥 KAFKA CONSUMERS
| ENV                                                       | DESCRIPTION                                                                                    | DEFAULT                                            |
|-----------------------------------------------------------|------------------------------------------------------------------------------------------------|----------------------------------------------------|
| KAFKA_CONSUMER_CONFIG_AUTO_COMMIT                         | True if the acknowledgement of the message is implicit if there are not errors                 | true                                               |
| KAFKA_CONSUMER_CONFIG_CONNECTIONS_MAX_IDLE_MS             | Maximum lifetime for idle connections (milliseconds)                                           | 180000                                             |
| KAFKA_CONFIG_MAX_POLL_INTERVAL_TIMEOUT_MS                 | Maximum interval between polls declared toward the broker (milliseconds)                       | 300000                                             |
| KAFKA_CONSUMER_CONFIG_MAX_POLL_SIZE                       | Maximum number of messages fetch for each poll                                                 | 500                                                |
| KAFKA_CONSUMER_CONFIG_CONNECTION_TIMEOUT_MS               | Initial timeout configured for the connection process (milliseconds)                           | 100000                                             |
| KAFKA_CONSUMER_CONFIG_CONNECTION_TIMEOUT_MAX_MS           | Maximum timeout configured when connection attempts repeatedly fail (milliseconds)             | 200000                                             |
| KAFKA_CONSUMER_CONFIG_STANDARD_HEADERS                    | If ask for contextual metadata headers when reading messages                                   | both                                               |
| KAFKA_CONSUMER_CONFIG_START_OFFSET                        | Where the consumer should begins consuming messages from a topic's partition (earliest/latest) | earliest                                           |
| KAFKA_IAM_ORGANIZATIONS_TOPIC                             | Topic where to read organizations event                                                        | sc-contracts                                       |
| KAFKA_IAM_ORGANIZATIONS_CONSUMER_SASL_JAAS_CONFIG         | JAAS Config string used to perform authentication                                              |                                                    |
| KAFKA_IAM_ORGANIZATIONS_GROUP_ID                          | Consumer group id                                                                              | piattaforma-unitaria-consumer                      |
| KAFKA_IAM_ORGANIZATIONS_CONSUMER_ENABLED                  | If the consumer should read messages                                                           | true                                               |
| KAFKA_IAM_ORGANIZATIONS_AUTO_COMMIT                       | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_AUTO_COMMIT}               |
| KAFKA_IAM_ORGANIZATIONS_REQUEST_CONNECTIONS_MAX_IDLE_MS   | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_CONNECTIONS_MAX_IDLE_MS}   |
| KAFKA_IAM_ORGANIZATIONS_INTERVAL_TIMEOUT_MS               | See default config description                                                                 | ${KAFKA_CONFIG_MAX_POLL_INTERVAL_TIMEOUT_MS}       |
| KAFKA_IAM_ORGANIZATIONS_MAX_POLL_SIZE                     | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_MAX_POLL_SIZE}             |
| KAFKA_IAM_ORGANIZATIONS_REQUEST_CONNECTION_TIMEOUT_MAX_MS | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_CONNECTION_TIMEOUT_MAX_MS} |
| KAFKA_IAM_ORGANIZATIONS_REQUEST_CONNECTION_TIMEOUT_MS     | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_CONNECTION_TIMEOUT_MS}     |
| KAFKA_IAM_ORGANIZATIONS_STANDARD_HEADERS                  | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_STANDARD_HEADERS}          |
| KAFKA_IAM_ORGANIZATIONS_REQUEST_START_OFFSET              | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_START_OFFSET}              |
| KAFKA_IAM_USERS_TOPIC                                     | Topic where to read users event                                                                | sc-users                                           |
| KAFKA_IAM_USERS_CONSUMER_SASL_JAAS_CONFIG                 | JAAS Config string used to perform authentication                                              |                                                    |
| KAFKA_IAM_USERS_GROUP_ID                                  | Consumer group id                                                                              | piattaforma-unitaria-consumer                      |
| KAFKA_IAM_USERS_CONSUMER_ENABLED                          | If the consumer should read messages                                                           | true                                               |
| KAFKA_IAM_USERS_AUTO_COMMIT                               | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_AUTO_COMMIT}               |
| KAFKA_IAM_USERS_REQUEST_CONNECTIONS_MAX_IDLE_MS           | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_CONNECTIONS_MAX_IDLE_MS}   |
| KAFKA_IAM_USERS_INTERVAL_TIMEOUT_MS                       | See default config description                                                                 | ${KAFKA_CONFIG_MAX_POLL_INTERVAL_TIMEOUT_MS}       |
| KAFKA_IAM_USERS_MAX_POLL_SIZE                             | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_MAX_POLL_SIZE}             |
| KAFKA_IAM_USERS_REQUEST_CONNECTION_TIMEOUT_MAX_MS         | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_CONNECTION_TIMEOUT_MAX_MS} |
| KAFKA_IAM_USERS_REQUEST_CONNECTION_TIMEOUT_MS             | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_CONNECTION_TIMEOUT_MS}     |
| KAFKA_IAM_USERS_STANDARD_HEADERS                          | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_STANDARD_HEADERS}          |
| KAFKA_IAM_USERS_REQUEST_START_OFFSET                      | See default config description                                                                 | ${KAFKA_CONSUMER_CONFIG_START_OFFSET}              |

#### 🔑 keys
| ENV                                  | DESCRIPTION                                                                              | DEFAULT |
|--------------------------------------|------------------------------------------------------------------------------------------|---------|
| JWT_TOKEN_PUBLIC_KEY                 | p4pa-auth JWT public key                                                                 |         |
| AUTH_CLIENT_SECRET                   | client_secret used on M2M authentication to get a technical access token                 |         |

## 🛠️ Getting Started

### 📝 Prerequisites

Ensure the following tools are installed on your machine:

1. **Java 21+**
2. **Gradle** (or use the Gradle wrapper included in the repository)
3. **Docker** (to build and run on an isolated environment, optional)
4. **GITHUB_TOKEN environment variable**

### 🔐 Write Locks

```sh
./gradlew dependencies --write-locks
```

### ⚙️ Build

```sh
./gradlew clean build
```

### 🧪 Test

#### 📌 JUnit
```sh
./gradlew test
```

### 🚀 Run local

```sh
./gradlew bootRun
```

### 🐳 Build & run through Docker
```sh
docker build -t <APP_NAME> .
docker run --env-file <ENV_FILE> <APP_NAME>
```

### ⚖️ Generate dependencies licenses
```sh
./gradlew generateLicenseReport
```
