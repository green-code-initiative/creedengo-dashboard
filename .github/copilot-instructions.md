# Creedengo Dashboard — Agent Instructions

## Project Overview

**Creedengo Dashboard** is a Green IT / sustainable code dashboard (Green Code Initiative).
It displays the sustainability impact of un-remediated SonarQube issues and helps prioritize fixes.
The same Vue 3 app can be embedded in SonarQube, VSCode, Confluence, or any other host.

---

## Architecture

Monorepo managed with **pnpm workspaces** and **Turborepo**. Three workspace scopes:

| Folder     | Contents                                                                                          |
| ---------- | ------------------------------------------------------------------------------------------------- |
| `apps/`    | Deployable applications (Vue app, SonarQube plugin, Confluence plugin, VSCode extension)          |
| `shared/`  | Internal libraries (`@creedengo/core-services`, `@creedengo/sonar-services`, `@creedengo/vue-ui`) |
| `configs/` | Shared configs (ESLint, Playwright, Vitest)                                                       |

Key files:

- [turbo.json](../turbo.json) — task graph and caching
- [pnpm-workspace.yaml](../pnpm-workspace.yaml) — workspace definition
- [apps/confluence-plugin/pom.xml](../apps/confluence-plugin/pom.xml) — Atlassian OSGi bundle
- [apps/sonar-plugin/pom.xml](../apps/sonar-plugin/pom.xml) — SonarQube plugin

---

## Build & Test

### JavaScript / TypeScript (all packages)

```sh
pnpm install          # install dependencies (pnpm only — enforced via preinstall)
turbo build           # build all packages in topological order
turbo dev             # dev mode with hot-reload
turbo test:unit       # Vitest unit tests across all packages
turbo test:e2e        # Playwright E2E tests (requires prior build)
turbo lint            # ESLint across all packages
pnpm ci               # lint + test:unit + test:e2e (full CI)
```

Node.js `22.17.1` is required (`.nvmrc`). Use `pnpm`, never `npm` or `yarn`.

### Confluence plugin (Atlassian SDK + Maven)

```sh
atlas-run             # Start Confluence locally with the plugin loaded
atlas-debug           # Same in debug mode (port 5005)
atlas-mvn package     # Quick reload (hot-swap without restart)
mvn -B verify         # Full build + tests
mvn -B test           # Tests only
```

See VS Code tasks in `.vscode/` for `atlas-run`, `atlas-debug`, `quick-reload`.

### SonarQube plugin (Maven + embedded frontend)

```sh
mvn -B verify         # Full build: installs Node locally via frontend-maven-plugin, bundles Vue app, then packages JAR
mvn -B test           # Tests only
```

---

## Java / Atlassian Plugin Development

### Confluence plugin specifics

- **Packaging**: `atlassian-plugin` (OSGi bundle via AMPS `9.1.1`)
- **Java version**: source/target `1.8`
- **Target**: Confluence `8.5.4`
- **DI**: `atlassian-spring-scanner 2.2.4` — use `@Scanned` on implementations, `@ComponentImport` to import Atlassian components
- **Tests**: JUnit `4.10` + `atlassian-plugins-osgi-testrunner`
- **OSGi export**: only `org.greencodeinitiative.creedengo.confluence.plugin.api`

Package structure under `org.greencodeinitiative.creedengo.confluence.plugin`:

```
api/        ← public OSGi interfaces
impl/       ← Spring-scanned implementations
macro/      ← Confluence macros (Dashboard, EcoScore, PriorityRulesMacro)
service/    ← business logic (ScoreService, PriorityRulesService)
sonar/      ← SonarQube HTTP client and API sub-calls
  api/      ← per-endpoint API classes (issues, measures, branches, rules…)
```

- All macros extend `BaseMacro` and use `MacroHelper` for common rendering logic.
- `SonarClient` is the HTTP entry point; domain-specific calls are in `sonar/api/`.
- GPL-3 license headers are **mandatory** on every `.java` file (enforced by `license-maven-plugin` in the sonar-plugin; apply the same convention in the confluence-plugin).

### SonarQube plugin specifics

- **Packaging**: `sonar-plugin` (fat JAR via `maven-shade-plugin`)
- **Java**: compile `11`, JDK min `17`
- **SonarQube API**: `9.14.0.375`
- **Tests**: JUnit Jupiter `5.10.3` + Mockito `5.12` + AssertJ `3.26`
- **Coverage**: JaCoCo `0.8.11`
- Plugin entry point: `org.greencodeinitiative.creedengo.dashboard.DashboardPlugin`

---

## Frontend Conventions

- **Vue 3** (Composition API + `<script setup>`)
- **Vite 7** as bundler; build output is fixed to `dist/view.js` + `dist/view.css`
- `@` alias resolves to `./src` in `apps/vue-app`
- API layer is split: `@creedengo/core-services` (provider-agnostic) → `@creedengo/sonar-services` (SonarQube-specific)
- Mocking via **MSW** (`public/mockServiceWorker.js`)
- Unit tests: Vitest + `@vue/test-utils` + jsdom
- E2E tests: Playwright (config in `configs/playwright-config`)

---

## Code Style

- See [CODE_STYLE.md](../CODE_STYLE.md) and [CONTRIBUTING.md](../CONTRIBUTING.md)
- Commit messages follow **Conventional Commits** (`commitlint.config.js`)
- Shared ESLint config: `@creedengo/eslint-config` (`configs/eslint-config/`)
- Changesets (`pnpm changeset`) for versioning JS packages

---

## Integration Points

- **SonarQube/SonarCloud** REST API consumed by `sonar/` Java layer and `@creedengo/sonar-services`
- The Vue app is embedded at runtime: served as static assets inside the SonarQube JAR or Confluence OBR, injected as a webview in VSCode
- `frontend-maven-plugin` in `apps/sonar-plugin/pom.xml` bridges the JS and Java builds
