# Hemmah Backend

Hemmah is an accessibility application that connects people with disabilities with volunteers for assistance, including remote visual help through video communication. This repository contains its Spring Boot backend: account management, help requests, chat, notifications, and video-call coordination. Video media is handled by Twilio and a separate client application.

## Backend capabilities

- **Accounts:** `DISABLED` and `VOLUNTEER` account types, RSA-signed JWT sign-in, account details, password changes, profile pictures, availability, and language preferences (`AR` or `EN`; the signup service defaults to Arabic). The signup endpoint currently has a JSON-binding defect described below.
- **Help requests:** create, list, edit, cancel, and complete requests with a date, location, and optional coordinates. Volunteers can browse requests that need help.
- **Chat:** volunteers can initiate a chat for a help request; messages are stored in MySQL, delivered through STOMP user destinations, and retrieved through a paginated REST endpoint.
- **Video coordination:** find active volunteers who share the requester's language, create a Twilio Video room, issue participant tokens, and record call acceptance.
- **Notifications:** persist a notification, broadcast it over STOMP, and send it through Firebase Cloud Messaging to the `notificationAll` topic.

These describe the implementation in the repository. External notification delivery and complete video calls require configured services and compatible clients; see [current limitations](#current-limitations).

## Technology

| Component | Version / use |
| --- | --- |
| Java | 17 |
| Spring Boot | 3.0.0; Web, Data JPA, Security, validation, and WebSocket |
| MySQL | 8 in the supplied Compose file; Hibernate persistence |
| Maven | Wrapper pins 3.8.6 |
| Authentication | Spring Security JWT resource server, RSA signing, BCrypt passwords |
| Messaging | STOMP / SockJS with Spring's in-process simple broker; STOMP WebJar 2.3.4 |
| Twilio Java SDK | 9.2.2; Video rooms and access tokens |
| Firebase Admin SDK | 9.1.1; Cloud Messaging |
| OpenAPI | springdoc 2.0.4; JSON specification and Swagger UI |
| Container / pipeline | Amazon Corretto 17 Docker image and a Jenkins pipeline |

The Maven file also includes AMQP and Thymeleaf dependencies. There is no implemented RabbitMQ producer/consumer workflow; the STOMP broker does not require RabbitMQ.

## Local setup

### Prerequisites

- JDK **17** with `JAVA_HOME` set appropriately. Use the project's target JDK for the older dependency versions in this repository.
- Git and internet access for Maven dependencies. A separate Maven installation is optional.
- MySQL 8 and a disposable development database. Docker is optional for running MySQL or the backend.
- OpenSSL for generating a local RSA key pair.
- Your own Firebase service-account JSON and Firebase database URL. The service-account file is read during startup, even if you only use REST endpoints.
- A Twilio account SID, API key SID, and API key secret for video operations. Placeholder values do not enable Twilio calls.

### 1. Clone and prepare MySQL

```sh
git clone https://github.com/Hemmah-App/hemmah_backend.git
cd hemmah_backend
```

For an isolated local MySQL instance:

```sh
docker network create hemmah-local
docker run -d --name hemmah-mysql --network hemmah-local \
  -p 127.0.0.1:3306:3306 \
  -e MYSQL_DATABASE=hemmah_dev \
  -e MYSQL_USER=hemmah \
  -e MYSQL_PASSWORD=change-me-local \
  -e MYSQL_ROOT_PASSWORD=change-me-root \
  -v hemmah-mysql-data:/var/lib/mysql \
  mysql:8
```

Wait until `docker logs hemmah-mysql` reports that MySQL is ready for connections. If using an existing server, create `hemmah_dev` and a user with schema-creation and data-access privileges instead. Adjust the port and JDBC URL if port 3306 is occupied.

### 2. Keep configuration outside the checkout

The committed profiles, keys, service-account file, and Jenkins/Compose values are historical configuration. Supply your own values rather than reusing them. The following directory is a sibling of the repository, so credentials are not added to Git:

```sh
mkdir -p ../hemmah-local
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 \
  -out ../hemmah-local/private.pem
openssl pkey -in ../hemmah-local/private.pem -pubout \
  -out ../hemmah-local/public.pem
chmod 600 ../hemmah-local/private.pem
```

Place your Firebase JSON at `../hemmah-local/firebase-service-account.json` and restrict its permissions. Create `../hemmah-local/application-local.yml` with:

```yaml
server:
  port: 8080
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hemmah_dev?allowPublicKeyRetrieval=true&useSSL=false
    username: hemmah
    password: ${HEMMAH_DB_PASSWORD:change-me-local}
  jpa:
    hibernate:
      ddl-auto: update
rsa:
  private-key: file:${HEMMAH_CONFIG_DIR:../hemmah-local}/private.pem
  public-key: file:${HEMMAH_CONFIG_DIR:../hemmah-local}/public.pem
firebase:
  service-account-file: file:${HEMMAH_CONFIG_DIR:../hemmah-local}/firebase-service-account.json
  database-url: https://YOUR_PROJECT_ID-default-rtdb.firebaseio.com
twilio:
  ACCOUNT_SID: ${TWILIO_ACCOUNT_SID}
  API_SID: ${TWILIO_API_SID}
  API_SECRET: ${TWILIO_API_SECRET}
```

Replace the Firebase URL with your project's value and set the Twilio variables in your shell:

```sh
export TWILIO_ACCOUNT_SID='AC_REPLACE_WITH_YOUR_ACCOUNT_SID'
export TWILIO_API_SID='SK_REPLACE_WITH_YOUR_API_KEY_SID'
export TWILIO_API_SECRET='REPLACE_WITH_YOUR_API_KEY_SECRET'
```

The `local` profile avoids the committed `dev` database credentials. The example deliberately sets the standard `spring.jpa.hibernate.ddl-auto` property: the repository has no versioned database migrations, so `update` is suitable only for this disposable development setup. The `prod` profile instead reads `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD`; it does not provide external-service credentials by itself.

### 3. Build and run

From the repository root:

```sh
sh ./mvnw clean verify
java -jar target/hemah-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=local \
  --spring.config.additional-location=file:../hemmah-local/
```

Alternatively, run directly with Maven:

```sh
sh ./mvnw spring-boot:run \
  -Dspring-boot.run.arguments="--spring.profiles.active=local --spring.config.additional-location=file:../hemmah-local/"
```

Use `mvnw.cmd` on Windows. The Unix wrapper is not executable in the repository, so the commands above invoke it with `sh`.

- Swagger UI: <http://localhost:8080/docs>
- OpenAPI JSON: <http://localhost:8080/api-docs>
- WebSocket / SockJS endpoint: `/ws`

`clean verify` compiles and packages the application, but does not establish functional test coverage: `UserServiceTest` is disabled, and `HemahApplicationTests` contains no test methods. A successful build does not validate MySQL, Firebase delivery, or Twilio calls.

## API examples

Check startup with the public specification and help-request feed:

```sh
BASE_URL=http://localhost:8080
curl -sS "$BASE_URL/api-docs"
curl -sS "$BASE_URL/v1/help-request/feed"
```

The following signup payload matches the controller's request model, including its exact `userName` field. **On the current code, this request returns HTTP 400 with a `NewUserModel` type-definition error.** It documents the intended API contract; the JSON-binding defect must be fixed before using signup to create accounts. There is no automatic account seeding.

```sh
curl -sS "$BASE_URL/v1/auth/signup" \
  -H 'Content-Type: application/json' \
  -d '{"userName":"demo_user_01","password":"Example@123","firstName":"Demo","lastName":"User","email":"demo@example.com","phoneNumber":"01000000000","address":"Demo location","userType":"DISABLED"}'
```

The controllers return a JWT in `data.token` on successful signup or `POST /v1/auth/signin` (body: `email` and `password`). With an existing local account, or after correcting signup, use that token for account and help-request operations:

```sh
TOKEN='REPLACE_WITH_DATA_TOKEN'
curl -sS "$BASE_URL/v1/auth/@me" -H "Authorization: Bearer $TOKEN"
curl -sS "$BASE_URL/v1/help-request" \
  -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Assistance at an appointment","description":"Help finding the entrance","date":"2030-01-15","location":"Demo meeting point","latitude":30.0,"longitude":31.0}'
curl -sS "$BASE_URL/v1/help-request/feed"
```

Creating a request requires the `DISABLED` role. Editing and cancelling use `PATCH` and `DELETE /v1/help-request?requestId=<id>`; completing uses `PATCH /v1/help-request/complete?requestId=<id>`. Cancellation changes the stored status rather than deleting the record. Chat history is available through `GET /v1/chat` and `GET /v1/chat/{chatId}/messages?page=0&size=10` for a signed-in participant.

## STOMP workflow

Connect to `/ws` using a STOMP client. Supply the **raw JWT**, without the HTTP `Bearer ` prefix, as the STOMP `CONNECT` header named `Authorization`. Client sends use the `/app` prefix:

| Send destination | Payload / purpose | Receive destination |
| --- | --- | --- |
| `/app/messages/send` | `{"helpRequestId":1,"content":"I can help"}` starts a chat as a volunteer; use `chatId` for an existing chat | `/user/messages/receive/{chatId}` for the other participant |
| `/app/help_call/ask` | No payload; request a call as a `DISABLED` account | `/user/help_call/ask` for the requester; `/user/help_call/answer` for available volunteers |
| `/app/help_call/answer` | `{"roomName":"ROOM_NAME_FROM_EVENT"}` records acceptance as a volunteer | See the video limitation below |

Video events include `roomName` and `roomToken`; when no matching volunteers are active, the requester receives a message instead. Broadcast notifications use `/notification`. Firebase clients must subscribe to `notificationAll` to receive the corresponding push notifications.

## Docker

The Dockerfile packages an **already-built** JAR; it does not run Maven. Build first, then use the same external configuration and MySQL network created above:

```sh
sh ./mvnw clean verify
docker build -t hemmah-backend:local .
docker run --rm --name hemmah-backend --network hemmah-local \
  -p 127.0.0.1:5151:8080 \
  -v "$(cd ../hemmah-local && pwd):/run/hemmah:ro" \
  -e HEMMAH_CONFIG_DIR=/run/hemmah \
  -e TWILIO_ACCOUNT_SID -e TWILIO_API_SID -e TWILIO_API_SECRET \
  -e SPRING_DATASOURCE_URL='jdbc:mysql://hemmah-mysql:3306/hemmah_dev?allowPublicKeyRetrieval=true&useSSL=false' \
  hemmah-backend:local \
  --spring.profiles.active=local \
  --spring.config.additional-location=file:/run/hemmah/
```

For a custom database password, also pass `-e HEMMAH_DB_PASSWORD`. In this container example, use <http://localhost:5151/docs> and set `BASE_URL=http://localhost:5151` for the REST examples. `localhost` inside the application container is not the MySQL container; the JDBC hostname must be `hemmah-mysql`.

The supplied `docker-compose.yml` describes a different deployment: backend on host port 5151, MySQL on host port 3003, the `prod` profile, and a database bind mount at `/home/ubuntu/hemmah_backend/mysql-docker-volume`. It uses fixed credentials and a misspelled JDBC option (`creatDatabaseIfNotExist`); MySQL's `MYSQL_DATABASE` setting creates its database. Adapt its credentials, mount, and external configuration before using it. The commands above provide a local alternative without modifying that deployment file.

`Jenkinsfile` checks out `master`, runs `mvn clean` / `mvn install`, logs into Docker, and restarts Compose. It requires a configured Jenkins agent with Maven, Docker/Compose, sudo permissions, and a `jenkins-git-token` credential. Its embedded Docker login values must be replaced with managed credentials before use. There are no GitHub Actions workflows in this repository.

## Current limitations

- `POST /v1/auth/signup` currently fails JSON deserialization with HTTP 400 (`NewUserModel` type-definition error). Its Lombok builder constructor needs Jackson-compatible deserialization configuration or a suitable constructor before the registration workflow can be used.
- Treat this as a development/reference backend. HTTP security currently permits all requests globally, while selected methods enforce roles; administrative and notification endpoints have commented-out role checks. WebSocket authorization is also permissive.
- Credential material is committed, and the Twilio initialization code logs its configured credentials. Replace and rotate exposed credentials and remove credential logging before deploying with real service accounts. Configuration overrides do not remove the historical files from Git or from the packaged JAR.
- The video acceptance handler currently sets the Twilio room to `COMPLETED`. Room creation and token generation are implemented, but this behavior needs correction and end-to-end verification before relying on an ongoing video session.
- The repository does not demonstrate production operation, completed external integrations, or comprehensive automated tests. Firebase and Twilio functionality must be verified with your own service configuration and clients.
