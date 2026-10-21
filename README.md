# Create: Balanced Flight

Create: Balanced Flight is a Create addon that adds progression-friendly flight tools for survival play.

The mod adds two main items:

- Flight Anchor: a kinetic block powered by Create rotation. When it receives enough speed, it enables flight in a configurable radius and renders a beacon-style beam.
- Ascended Flight Ring: a Curios-compatible ring item used by the flight system.

## Minecraft 1.21.1 NeoForge Port

This branch ports the mod to:

- Minecraft `1.21.1`
- NeoForge `21.1.228`
- Create `6.0.10`
- Flywheel `1.0.6`
- Ponder `1.0.82`
- Curios API `9.5.1+1.21.1`
- GeckoLib `4.8.4`

The port updates the project from the old Minecraft 1.20.1 Forge setup to NeoForge's 1.21.1 mod loading, event, data generation, network, config, Curios, and renderer APIs.

## Features

- Create-powered Flight Anchor block
- Flight radius controlled by the anchor system
- Beacon-style beam when the Flight Anchor is active
- Animated GeckoLib block and item rendering
- Create shaft connection support on the Flight Anchor's side faces
- Curios ring slot support for the Ascended Flight Ring
- Create-style tooltips, recipes, and Ponder integration
- Optional Sable compatibility for Flight Anchors on moving physics structures

## Sable Compatibility

Install [Sable](https://modrinth.com/mod/sable) `1.2.0` or newer alongside the mod
to enable Flight Anchors on Sable structures. Anchor range checks project the
player and anchor into world coordinates, so the flight area follows the structure.
Flight Anchors also work inside Sable plot grids. Without Sable, anchors retain
their normal Overworld behavior. Existing Elytra features remain available.

## Slow Falling Setting

Slow falling is enabled by default. Losing flight while airborne grants 10 seconds
of protection. The server applies and removes the effect so it expires normally
on the client, even when the player stays far from an anchor. This addresses the
effect getting stuck at `0:00`.

The setting in `config/balanced_flight.toml` is:

```toml
[Settings."Balancing Config"]
"Enable Slow Falling On Flight Loss" = true
```

Server owners can optionally set it to `false` in the server's copy of the config.
Slow falling from potions and other sources is preserved.

## Attribution

This repository is a fork and port of Create: Balanced Flight, originally created by Txni.

- Original GitHub repository: https://github.com/txnimc/BalancedFlight
- Original CurseForge page: https://www.curseforge.com/minecraft/mc-mods/create-balanced-flight

This fork is maintained at:

- https://github.com/themarneilx/BalancedFlight

Previous Create 6.0 update work was based on contributions by Sintinium:

- https://www.curseforge.com/members/sintinium/projects

This fork is republished under the license chosen by the original project.

Sable compatibility is adapted from the `master` branch of
[qwer854645/BalancedFlight_sable](https://github.com/qwer854645/BalancedFlight_sable/tree/master),
maintained by xs_267. See [NOTICE](NOTICE) and [LICENSE](LICENSE).

## Building

Use Java 21.

```bash
./gradlew build
```

The compiled jar is written to:

```text
build/libs/
```

## Regression Checks

With Java 21 configured, run `bash tests/offline/run.sh`. These checks exercise
flight and optional Sable compatibility against test fixtures without launching
Minecraft. See [tests/offline/README.md](tests/offline/README.md) for details.

## Development Notes

- The Flight Anchor uses GeckoLib for the animated block model and a Create-safe renderer for shaft partials.
- The Flight Anchor advertises shafts on its side faces and uses the matching side-axis rotation so it connects naturally with Create shafts.
- Curios data is provided under `data/balancedflight/curios` and item tags under `data/curios/tags/item`.
- Generated data is written under `src/generated/resources`.
