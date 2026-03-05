#!/usr/bin/env bash
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_DIR"

DOMAIN_NAME="${GLASSFISH_DOMAIN:-domain1}"
HTTP_PORT="${HTTP_PORT:-8080}"
POLL_INTERVAL_SECONDS="${POLL_INTERVAL_SECONDS:-2}"

log() {
  printf '[glassfish-local] %s\n' "$*"
}

ASADMIN_BIN="${ASADMIN_BIN:-$(command -v asadmin || true)}"
BREW_GF_PREFIX=""

if [[ -z "$ASADMIN_BIN" ]] && command -v brew >/dev/null 2>&1; then
  BREW_GF_PREFIX="$(brew --prefix glassfish 2>/dev/null || true)"
  if [[ -n "$BREW_GF_PREFIX" && -x "$BREW_GF_PREFIX/libexec/glassfish/bin/asadmin" ]]; then
    ASADMIN_BIN="$BREW_GF_PREFIX/libexec/glassfish/bin/asadmin"
  fi
fi

if [[ -z "$ASADMIN_BIN" ]]; then
  log "asadmin was not found. Install GlassFish with Homebrew (brew install glassfish) or set ASADMIN_BIN."
  exit 1
fi

GLASSFISH_HOME="${GLASSFISH_HOME:-}"
if [[ -z "$GLASSFISH_HOME" ]]; then
  if [[ -z "$BREW_GF_PREFIX" ]] && command -v brew >/dev/null 2>&1; then
    BREW_GF_PREFIX="$(brew --prefix glassfish 2>/dev/null || true)"
  fi
  if [[ -n "$BREW_GF_PREFIX" && -d "$BREW_GF_PREFIX/libexec/glassfish" ]]; then
    GLASSFISH_HOME="$BREW_GF_PREFIX/libexec/glassfish"
  else
    GLASSFISH_HOME="$(cd "$(dirname "$ASADMIN_BIN")/.." && pwd)"
  fi
fi

AUTODEPLOY_DIR="${AUTODEPLOY_DIR:-$GLASSFISH_HOME/domains/$DOMAIN_NAME/autodeploy}"
APP_URL="http://localhost:${HTTP_PORT}/"

watch_signature() {
  find src/main/java src/main/webapp -type f \( \
    -name '*.java' -o \
    -name '*.html' -o \
    -name '*.jsp' -o \
    -name '*.xhtml' -o \
    -name '*.xml' -o \
    -name '*.css' -o \
    -name '*.js' \
  \) -print0 2>/dev/null \
    | xargs -0 -I{} stat -f '%N:%m' "{}" 2>/dev/null \
    | LC_ALL=C sort \
    | shasum \
    | awk '{print $1}'
}

ensure_domain_running() {
  if ! "$ASADMIN_BIN" list-domains 2>/dev/null | awk '{print $1}' | grep -Fxq "$DOMAIN_NAME"; then
    log "Creating GlassFish domain '$DOMAIN_NAME'..."
    "$ASADMIN_BIN" create-domain --nopassword "$DOMAIN_NAME"
  fi

  if "$ASADMIN_BIN" list-domains 2>/dev/null | grep -Eq "^${DOMAIN_NAME}[[:space:]]+running$"; then
    log "GlassFish domain '$DOMAIN_NAME' already running."
  else
    log "Starting GlassFish domain '$DOMAIN_NAME'..."
    "$ASADMIN_BIN" start-domain "$DOMAIN_NAME"
  fi
}

build_and_deploy() {
  local war_file

  log "Building WAR..."
  ./mvnw -DskipTests package

  war_file="$(find target -maxdepth 1 -type f -name '*.war' ! -name 'original-*.war' | head -n 1)"
  if [[ -z "$war_file" ]]; then
    log "No WAR file found in target/."
    return 1
  fi

  mkdir -p "$AUTODEPLOY_DIR"
  cp "$war_file" "$AUTODEPLOY_DIR/app.war"
  log "Deployed $(basename "$war_file") to $AUTODEPLOY_DIR/app.war"
}

trap 'log "Watcher stopped."; exit 0' INT TERM

ensure_domain_running
log "APP_URL $APP_URL"

last_signature=""
while true; do
  current_signature="$(watch_signature)"
  if [[ "$current_signature" != "$last_signature" ]]; then
    last_signature="$current_signature"
    if ! build_and_deploy; then
      log "Build/deploy failed. Fix issues and save a file to retry."
    fi
  fi
  sleep "$POLL_INTERVAL_SECONDS"
done
