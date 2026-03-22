## Contribution

### Requirements

- Java 21
- Git

### Development Build

Build the project:

```bash
./gradlew build
```

Build the distributable plugin:

```bash
./gradlew shadowJar
```

Run the tests:

```bash
./gradlew test
```

Print the version used by CI and doc publishing:

```bash
./gradlew printVersion
```

### Project Structure

- `src/main/java`: plugin source code
- `src/main/resources`: Bukkit descriptor and default configuration
- `src/test/java`: unit and integration-style tests
- `docs/`: MkDocs documentation published through GitHub Pages
- `.github/workflows`: CI and docs workflows

### Documentation Stack

The documentation uses:

- `mkdocs`
- Material for MkDocs
- `stellionix-mkdocs` from `Stellionix/docs-common`

Deployments are handled by `.github/workflows/docs.yml`.

### Contribution Notes

- Keep command behavior aligned with `plugin.yml` and documentation.
- Add or update tests for behavior changes.
- Update `README.md` and `docs/` for user-facing changes.
- Prefer small, reviewable changesets when changing gameplay behavior.
