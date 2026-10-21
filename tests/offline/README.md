# Offline Regression Checks

Run with a Java 21 JDK:

```bash
JAVA_HOME=/path/to/jdk-21 bash tests/offline/run.sh
```

These checks compile the production `FlightController`, `SableCompat`, and
`ExternalMods` against small fixtures. They exercise flight permission ownership,
slow falling (including server/client expiry after 200 ticks), level isolation, and sub-level coordinate conversion without
downloading Gradle dependencies or starting Minecraft. The fixtures are confined
to the temporary test classpath and are never packaged in the mod.

They do not replace a Gradle build or an in-game client/server test. Verify the
Flight Anchor Ponder scene and anchors on moving Sable structures in game as well.
