#!/usr/bin/env bash
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_DIR"

DOMAIN_NAME="${GLASSFISH_DOMAIN:-domain1}"
HTTP_PORT="${HTTP_PORT:-8080}"
POLL_INTERVAL_SECONDS="${POLL_INTERVAL_SECONDS:-2}"
APP_NAME="${APP_NAME:-forum}"
DB_HOST="${DB_HOST:-db}"
DB_NAME="${DB_NAME:-forum_db}"
DB_USER="${DB_USER:-forumuser}"
DB_PASSWORD="${DB_PASSWORD:-forumpass}"
GLASSFISH_HOME="${GLASSFISH_HOME:-/opt/glassfish7/glassfish}"
ASADMIN_BIN="${ASADMIN_BIN:-$GLASSFISH_HOME/bin/asadmin}"
APP_URL="http://localhost:${HTTP_PORT}/"
READY_CHECK_URL="${READY_CHECK_URL:-${APP_URL}home}"
READY_TIMEOUT_SECONDS="${READY_TIMEOUT_SECONDS:-60}"
JDBC_POOL_NAME="${JDBC_POOL_NAME:-forumPool}"
JDBC_RESOURCE_NAME="${JDBC_RESOURCE_NAME:-jdbc/forumDS}"

log() {
  printf '[glassfish-docker-dev] %s\n' "$*"
}

watch_signature() {
  find pom.xml src/main/java src/main/resources src/main/webapp -type f \( \
    -name 'pom.xml' -o \
    -name '*.java' -o \
    -name '*.properties' -o \
    -name '*.html' -o \
    -name '*.jsp' -o \
    -name '*.xhtml' -o \
    -name '*.xml' -o \
    -name '*.css' -o \
    -name '*.js' \
  \) -print0 2>/dev/null \
    | xargs -0 -r stat -c '%n:%Y' 2>/dev/null \
    | LC_ALL=C sort \
    | sha1sum \
    | awk '{print $1}'
}

ensure_domain_running() {
  if "$ASADMIN_BIN" list-domains 2>/dev/null | grep -Eq "^${DOMAIN_NAME}[[:space:]]+running$"; then
    log "GlassFish domain '$DOMAIN_NAME' already running."
  else
    log "Starting GlassFish domain '$DOMAIN_NAME'..."
    "$ASADMIN_BIN" start-domain "$DOMAIN_NAME"
  fi
}

ensure_jdbc_resources() {
  if "$ASADMIN_BIN" list-jdbc-connection-pools 2>/dev/null | awk '{print $1}' | grep -Fxq "$JDBC_POOL_NAME"; then
    log "JDBC pool '$JDBC_POOL_NAME' already exists."
  else
    log "Creating JDBC pool '$JDBC_POOL_NAME'..."
    "$ASADMIN_BIN" create-jdbc-connection-pool \
      --restype javax.sql.DataSource \
      --datasourceclassname org.postgresql.ds.PGSimpleDataSource \
      --property "user=${DB_USER}:password=${DB_PASSWORD}:databaseName=${DB_NAME}:serverName=${DB_HOST}:portNumber=5432" \
      "$JDBC_POOL_NAME"
  fi

  if "$ASADMIN_BIN" list-jdbc-resources 2>/dev/null | awk '{print $1}' | grep -Fxq "$JDBC_RESOURCE_NAME"; then
    log "JDBC resource '$JDBC_RESOURCE_NAME' already exists."
  else
    log "Creating JDBC resource '$JDBC_RESOURCE_NAME'..."
    "$ASADMIN_BIN" create-jdbc-resource --connectionpoolid "$JDBC_POOL_NAME" "$JDBC_RESOURCE_NAME"
  fi
}

build_and_deploy() {
  local war_file

  log "Building WAR..."
  ./mvnw -DskipTests clean package

  war_file="$(find target -maxdepth 1 -type f -name '*.war' ! -name 'original-*.war' | head -n 1)"
  if [[ -z "$war_file" ]]; then
    log "No WAR file found in target/."
    return 1
  fi

  if "$ASADMIN_BIN" list-applications 2>/dev/null | awk '{print $1}' | grep -Fxq "$APP_NAME"; then
    log "Redeploying application '$APP_NAME' at context root / ..."
  else
    log "Deploying application '$APP_NAME' at context root / ..."
  fi

  "$ASADMIN_BIN" deploy \
    --force=true \
    --name "$APP_NAME" \
    --contextroot / \
    "$war_file"

  log "Deployed $(basename "$war_file") as '$APP_NAME' on $APP_URL"
}

wait_for_app_ready() {
  local deadline
  local body_file

  deadline=$((SECONDS + READY_TIMEOUT_SECONDS))
  body_file="$(mktemp)"
  trap 'rm -f "$body_file"' RETURN

  while (( SECONDS < deadline )); do
    if curl -fsS "$READY_CHECK_URL" >"$body_file" 2>/dev/null; then
      if ! grep -q "Your server is now running" "$body_file"; then
        log "READY_URL $APP_URL"
        return 0
      fi
    fi
    sleep 1
  done

  log "Timed out waiting for app readiness at $READY_CHECK_URL"
  return 1
}

trap 'log "Watcher stopped."; exit 0' INT TERM

ensure_domain_running
ensure_jdbc_resources

last_signature=""
while true; do
  current_signature="$(watch_signature)"
  if [[ "$current_signature" != "$last_signature" ]]; then
    last_signature="$current_signature"
    if ! build_and_deploy; then
      log "Build/deploy failed. Fix issues and save a file to retry."
    elif ! wait_for_app_ready; then
      log "Deploy completed, but the application did not become ready in time."
    fi
  fi
  sleep "$POLL_INTERVAL_SECONDS"
done
