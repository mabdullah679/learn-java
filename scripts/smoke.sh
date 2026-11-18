#!/usr/bin/env bash
# HTTP smoke test against a running task-tracker server.
# Usage: scripts/smoke.sh [base-url]   (default http://localhost:8080)
# Needs: bash, curl. Expects an EMPTY server (fresh start; storage is in-memory).
set -u
B="${1:-http://localhost:8080}"
pass=0; fail=0
code() { curl -s -o /dev/null -w '%{http_code}' "$@"; }
check() { # name expected actual
  if [ "$2" = "$3" ]; then pass=$((pass+1)); echo "ok   $1"; else fail=$((fail+1)); echo "FAIL $1 (expected $2, got $3)"; fi
}

check "GET /tasks (empty) 200"            200 "$(code "$B/tasks")"
check "GET /wrong 404"                    404 "$(code "$B/wrong")"
check "DELETE /tasks 405"                 405 "$(code -X DELETE "$B/tasks")"
check "POST /tasks blank title 400"       400 "$(code -X POST -d '   ' "$B/tasks")"
check "POST /tasks valid 201"             201 "$(code -X POST -d 'buy milk' "$B/tasks")"

ID=$(curl -s "$B/tasks" | awk '/^ID:/{print $2; exit}')
check "created task is listed"            1 "$(curl -s "$B/tasks" | grep -c 'Title: buy milk')"
check "HEAD /tasks/{id} 200"              200 "$(code -I "$B/tasks/$ID")"
check "PATCH complete 200"                200 "$(code -X PATCH "$B/tasks/$ID/complete")"
check "task shows Completed: Yes"         1 "$(curl -s "$B/tasks" | grep -c 'Completed: Yes')"
check "PATCH reopen 200"                  200 "$(code -X PATCH "$B/tasks/$ID/reopen")"
check "PATCH title 200"                   200 "$(code -X PATCH -d 'buy oat milk' "$B/tasks/$ID/title")"
check "PUT /tasks/{id} 200"               200 "$(code -X PUT -d 'buy soy milk' "$B/tasks/$ID")"
check "PUT blank title 400"               400 "$(code -X PUT -d ' ' "$B/tasks/$ID")"
check "OPTIONS /tasks/{id} 200"           200 "$(code -X OPTIONS "$B/tasks/$ID")"

# Regression: an unknown PATCH action must NOT delete the task.
code -X PATCH "$B/tasks/$ID/bogus" >/dev/null
check "PATCH unknown action keeps task"   200 "$(code -I "$B/tasks/$ID")"

check "DELETE /tasks/{id} 200"            200 "$(code -X DELETE "$B/tasks/$ID")"
check "HEAD deleted task 404"             404 "$(code -I "$B/tasks/$ID")"
check "DELETE missing task 404"           404 "$(code -X DELETE "$B/tasks/$ID")"

echo "passed=$pass failed=$fail"
[ "$fail" -eq 0 ]
