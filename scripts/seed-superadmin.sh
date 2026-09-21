#!/usr/bin/env bash
# Seeds the very first ADMS account (which the backend automatically makes
# SUPERADMIN, since /api/users requires no auth while the users table is
# empty). Refuses to run if any user already exists.
#
# Usage:
#   ./scripts/seed-superadmin.sh [firstName] [lastName] [email] [password]
#
# Or via environment variables:
#   ADMS_API_URL=http://localhost:8081 \
#   SEED_FIRST_NAME=Joe SEED_LAST_NAME=Lebonheur \
#   SEED_EMAIL=joe@example.com SEED_PASSWORD='S0meStrongPass!' \
#   ./scripts/seed-superadmin.sh
#
# Positional args win over env vars; both are optional, but you should
# always pass your own email/password rather than relying on the defaults.

set -euo pipefail

API_URL="${ADMS_API_URL:-http://localhost:8081}"
FIRST_NAME="${1:-${SEED_FIRST_NAME:-Admin}}"
LAST_NAME="${2:-${SEED_LAST_NAME:-User}}"
EMAIL="${3:-${SEED_EMAIL:-admin@example.com}}"
PASSWORD="${4:-${SEED_PASSWORD:-ChangeMe123!}}"

if [ "$EMAIL" = "admin@example.com" ] || [ "$PASSWORD" = "ChangeMe123!" ]; then
  echo "WARNING: using a placeholder email/password. Pass your own:"
  echo "  ./scripts/seed-superadmin.sh \"First\" \"Last\" you@example.com 'YourPassword'"
  echo
fi

RESPONSE_FILE=$(mktemp)
trap 'rm -f "$RESPONSE_FILE"' EXIT

echo "Checking for existing users at $API_URL ..."
if ! curl -sf "$API_URL/api/users" -o "$RESPONSE_FILE"; then
  echo "Could not reach $API_URL/api/users - is the backend running?"
  exit 1
fi

TRIMMED=$(tr -d '[:space:]' < "$RESPONSE_FILE")
if [ "$TRIMMED" != "[]" ]; then
  echo "Users already exist in the database - bootstrap only works for the"
  echo "very first account. Use the Users page (logged in as a Superadmin)"
  echo "or a Bearer-token curl call to add more accounts instead."
  exit 1
fi

echo "No users found. Creating the first account as SUPERADMIN ..."
HTTP_STATUS=$(curl -s -o "$RESPONSE_FILE" -w "%{http_code}" -X POST "$API_URL/api/users" \
  -H "Content-Type: application/json" \
  -d "{\"firstName\":\"$FIRST_NAME\",\"lastName\":\"$LAST_NAME\",\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}")

if [ "$HTTP_STATUS" = "201" ]; then
  echo "Superadmin account created:"
  cat "$RESPONSE_FILE"
  echo
  echo
  echo "Log in with: email=$EMAIL  password=$PASSWORD"
else
  echo "Failed to create account (HTTP $HTTP_STATUS):"
  cat "$RESPONSE_FILE"
  exit 1
fi
