LEARN-JAVA: TASK TRACKER

This is a small practice project in Java 21. It is a to-do list you talk to over HTTP: you can add a task, list tasks, rename one, mark it done or not done, and delete it. It uses only what comes with Java (no frameworks). Tasks are kept in memory only, so they disappear when you stop the program. There is no database and no outside service.

What you need: Java 21 or newer and Maven. Docker and curl are optional.

How to run it:
1. Run "mvn clean package". This runs the 30 unit tests and builds the jar file.
2. Run "java -jar target/task-list-app-1.0-SNAPSHOT.jar". It listens on port 8080.
3. In another terminal: curl -X POST -d 'buy milk' localhost:8080/tasks, then curl localhost:8080/tasks.
With Docker: "docker build -t task-tracker ." then "docker run --rm -p 8080:8080 task-tracker".
If port 8080 is busy the program prints an error and quits. The port is fixed in the entry-point file, so change it there, or tell Docker to use another outside port such as 9090.

What the server does: POST /tasks creates a task (400 if the title is blank). GET /tasks lists them. PUT /tasks/{id} or PATCH /tasks/{id}/title renames. PATCH /tasks/{id}/complete and /reopen change the done flag. DELETE /tasks/{id} removes one. HEAD and OPTIONS work on /tasks and /tasks/{id}. Unknown ids give 404, a wrong method on /tasks gives 405, a wrong path gives 404, and an unknown PATCH action gives 400 and leaves the task alone. Everything is plain text, not JSON, and there is no login.

Tests: "mvn test" runs 30 unit tests (14 for the task, 5 for the storage, 11 for the service). The file scripts/smoke.sh runs 18 curl checks against a freshly started server; it is the only automated test of the HTTP part. The GitHub workflow runs both and a secret scan.

Where things are: the Java code is in src/main/java/com/mab/tasktracker, split into domain, repository, service and server. Tests are in src/test/java. TODO.txt is the owner's roadmap; database storage, JSON, and a frontend listed there are plans and are not built.

Bug fixed during this clean-up: before, sending PATCH with an unknown action (for example /tasks/{id}/bogus) silently deleted the task, because the code fell through into the delete case. It now returns 400. The smoke test checks this.
