# learn-java: Task Tracker

A small Java 21 REST-style task list (create, rename, complete, delete) with in-memory storage, built on the JDK's own HTTP server with no frameworks. Roadmap: [TODO.txt](TODO.txt).

```
curl / Postman --HTTP--> TaskHttpServer :8080 --> TaskHandler --> TaskService --> TaskRepository (in-memory map)
```

Not local/contained: nothing. There is no database or external service; all tasks vanish when the process stops.

## Prerequisites
- JDK 21+ and Maven 3.9+ (verified with Maven 3.9.11 on JDK 27, `release 21`).
- Optional: Docker (for the container), curl (for the smoke test).

## Run it
1. `mvn clean package` (runs the 30 unit tests, builds `target/task-list-app-1.0-SNAPSHOT.jar`).
2. `java -jar target/task-list-app-1.0-SNAPSHOT.jar` (prints `Server started on port 8080`).
3. `curl -X POST -d 'buy milk' localhost:8080/tasks` then `curl localhost:8080/tasks`.

Docker instead of step 2: `docker build -t task-tracker . && docker run --rm -p 8080:8080 task-tracker`.

If port 8080 is taken the server prints a stack trace and exits; the port is hard-coded in [TaskTrackerEntryPoint.java](src/main/java/com/mab/tasktracker/TaskTrackerEntryPoint.java), so change it there (or map another host port with Docker: `-p 9090:8080`).

## What it does
| Request | Result |
|---|---|
| `POST /tasks` (title as plain-text body) | 201 and the task; 400 if the title is blank |
| `GET /tasks` | 200, plain-text list (or `No tasks found.`) |
| `PUT /tasks/{id}` (new title as body) | 200; 404 unknown id; 400 blank title |
| `PATCH /tasks/{id}/title` (new title as body) | same as PUT |
| `PATCH /tasks/{id}/complete` / `/reopen` | 200 with task; 404 unknown id |
| `PATCH /tasks/{id}/<anything else>` | 400, task untouched |
| `DELETE /tasks/{id}` | 200; 404 unknown id |
| `HEAD /tasks`, `HEAD /tasks/{id}` | 200 (404 for unknown id), no body |
| `OPTIONS /tasks`, `OPTIONS /tasks/{id}` | 200 with an `Allow` header |
| Other method on `/tasks` | 405; any other path 404 |

All routing is in [TaskHandler.java](src/main/java/com/mab/tasktracker/server/TaskHandler.java). No authentication; bodies and responses are plain text, not JSON.

## Tests
- `mvn test`: 30 JUnit 5 tests for [Task](src/test/java/com/mab/tasktracker/domain/TaskTest.java), [TaskRepository](src/test/java/com/mab/tasktracker/repository/TaskRepositoryTest.java), [TaskService](src/test/java/com/mab/tasktracker/service/TaskServiceTest.java). No external services.
- [scripts/smoke.sh](scripts/smoke.sh): 18 curl checks against a running, freshly started server (`scripts/smoke.sh http://localhost:8080`). It is the only automated coverage of the HTTP layer.
- CI: [ci.yml](.github/workflows/ci.yml) runs both plus a gitleaks scan.

## Layout
| Path | Role |
|---|---|
| [src/main/java/com/mab/tasktracker](src/main/java/com/mab/tasktracker) | `domain`, `repository`, `service`, `server` layers and the entry point |
| [src/test/java](src/test/java) | unit tests |
| [scripts/smoke.sh](scripts/smoke.sh) | HTTP smoke test |
| [Dockerfile](Dockerfile) | runs the prebuilt jar on Temurin 21 |
| [TODO.txt](TODO.txt) | owner's milestone log and plans (JDBC, JSON, frontend are not built) |

## Design notes
- JDK `HttpServer` instead of Spring: the point is to learn the layers and HTTP semantics directly.
- The Dockerfile copies a prebuilt jar rather than building in-image, so run Maven first.
