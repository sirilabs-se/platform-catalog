# platform-catalog

A Gradle Version Catalog, published as its own artifact, containing no
code — only aligned dependency versions and library coordinates. Every
other platform artifact and every consuming application imports this
catalog rather than pinning versions independently. This is what
prevents silent version skew across applications that share no code.

> Enterprise Application Platform Strategy v7, Section 4.

This is the **first artifact in the build order** (Strategy v7
dependency graph, Section 11) — nothing else in the platform can be
built against real coordinates until this exists and is published.

## What's in here

| Alias group | Contents |
|---|---|
| Toolchain | Java, Gradle |
| Framework | Spring Boot (BOM), Micrometer |
| Test frameworks | JUnit 5, AssertJ, Mockito, ArchUnit |
| Code quality | Spotless, Google Java Format |
| Compatibility checking | Japicmp |

The full, current list of versions and aliases lives in
[`gradle/libs.versions.toml`](gradle/libs.versions.toml) — that file is
the single source of truth. If you find a raw version string for any of
these libraries anywhere else in the platform's repos, that's a bug:
point it back at this catalog instead.

## Using this catalog in a consuming project

Add the catalog as a dependency in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven {
            name = "platformRegistry"
            url = uri("https://<internal-artifact-repository>/maven")
        }
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from("com.labstrash.platform:platform-catalog:1.0.0")
        }
    }
}
```

Then reference aliases as usual in any module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    testImplementation(libs.bundles.unit.test)
    testImplementation(libs.archunit.junit5)
}

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spotless)
}
```

## Publishing

```bash
./gradlew publish
```

Publishes the `version-catalog` component to the internal artifact
repository (configured via the `PLATFORM_REGISTRY_URL`,
`PLATFORM_REGISTRY_USERNAME`, and `PLATFORM_REGISTRY_PASSWORD`
environment variables, or the equivalent Gradle properties).

To inspect what would be published without publishing:

```bash
./gradlew printCatalogSummary
```

## Versioning

This artifact is semantically versioned on its own. A version bump here
is a **deliberate, reviewed action, not automatic** — see
[`CHANGELOG.md`](CHANGELOG.md) for the history of what changed and why.

Automated dependency-update PRs are opened by [Renovate](renovate.json),
grouped by category (Spring Boot, test frameworks, code quality,
toolchain), but nothing auto-merges. Every version bump still needs a
human to verify compatibility across `platform-build`, `platform-core`,
`platform-testing`, and `application-template` before merging, and to
add the corresponding `CHANGELOG.md` entry.

## Explicitly out of scope

A Maven BOM equivalent is **not** built here. See
[`NOTES.md`](NOTES.md) for the rationale and the trigger condition for
revisiting that decision.

## Repo structure

```
platform-catalog/
├── gradle/
│   └── libs.versions.toml   # the source of truth
├── build.gradle.kts         # publishes the catalog as a version-catalog component
├── settings.gradle.kts
├── renovate.json            # automated, grouped dependency-update PRs
├── CHANGELOG.md
└── NOTES.md                 # records the deferred Maven BOM decision
```
