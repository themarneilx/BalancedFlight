#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/../.."
test_dir=$(mktemp -d)
trap 'rm -rf "$test_dir"' EXIT
jdk_bin=${JAVA_HOME:+$JAVA_HOME/bin/}

mapfile -t fixtures < <(find tests/offline/java -name '*.java' -print)
"${jdk_bin}javac" --release 21 -d "$test_dir" "${fixtures[@]}" \
    src/main/java/com/vice/balancedflight/content/flightAnchor/FlightController.java \
    src/main/java/com/vice/balancedflight/foundation/compat/ExternalMods.java \
    src/main/java/com/vice/balancedflight/foundation/compat/SableCompat.java

"${jdk_bin}java" -cp "$test_dir" FlightRegressionChecks
"${jdk_bin}java" -Dtest.sable=true -cp "$test_dir" FlightRegressionChecks
"${jdk_bin}java" -Dtest.sable=true -Dtest.sable.missing=true -cp "$test_dir" FlightRegressionChecks
"${jdk_bin}java" -Dtest.sable=true -Dtest.sable.serviceFailure=true -cp "$test_dir" FlightRegressionChecks
