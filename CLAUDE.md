# learn-java (Task Tracker)

Practice project: in-memory task list served over the JDK's built-in `com.sun.net.httpserver` (no frameworks). Global rules: `~/.claude/CLAUDE.md`.

## Commands (all run in this repo)
- Test: `mvn -B test` (30 JUnit 5 tests: domain 14, repository 5, service 11)
- Build jar: `mvn -B clean package` -> `target/task-list-app-1.0-SNAPSHOT.jar`
- Run: `java -jar target/task-list-app-1.0-SNAPSHOT.jar` (port 8080, hard-coded in `TaskTrackerEntryPoint`)
- HTTP smoke (needs a fresh server, curl): `scripts/smoke.sh [base-url]`
- Docker: `Dockerfile` copies the built jar, so run `mvn clean package` first.

## Layout
`domain/Task` -> `repository/TaskRepository` (HashMap) -> `service/TaskService` -> `server/TaskHandler` (all routing, one context `/`) -> `TaskHttpServer`.

## Conventions / don't
- Storage is in-memory only; nothing persists. JDBC, JSON, frontend in `TODO.txt` are plans, not features.
- `TaskHandler` has no unit tests; `scripts/smoke.sh` is its only automated coverage.
- Branch + PR only; never push to `main`. Keep `TODO.txt` as the owner's roadmap; don't rewrite it.
