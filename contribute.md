# Contribute

## Requirements
- Java 21
- Git

## Development Build
Build the plugin:

```bash
./gradlew build
```

Build the shaded jar:

```bash
./gradlew shadowJar
```

Run the test suite:

```bash
./gradlew test
```

## CI
GitHub Actions runs the build and test workflow on each push and pull request.

Workflow file:

- `.github/workflows/ci.yml`

## Project Structure
- `src/main/java`: plugin source code
- `src/main/resources`: Bukkit descriptors and default configuration
- `src/test/java`: unit and integration-style tests
- `build.gradle.kts`: Gradle Kotlin DSL build

## Main Technical Choices
- Gradle Kotlin DSL for build configuration
- ShadowJar for distributable plugin packaging
- SQLite for trigger persistence
- Per-command class structure under `me.crylonz.command`
- Dedicated action handlers under `me.crylonz.action`

## Contribution Notes
- Keep command logic in dedicated command classes
- Add or update tests for behavior changes
- Prefer updating `README.md` when user-facing behavior changes
- Keep `plugin.yml` permissions and command declarations aligned with the code
