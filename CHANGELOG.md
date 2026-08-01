# Changelog

All notable version bumps to `platform-catalog` are documented here.
This artifact is semantically versioned on its own: a bump here can
affect every downstream consumer at once, so it's a deliberate,
reviewed action, not automatic.

## [1.0.0] - 2026-08-01

### Added

Initial publication of the platform version catalog. Establishes the
aligned versions for every shared dependency across the platform:

- **Toolchain:** Java 25, Gradle 9.6.0
- **Framework:** Spring Boot 4.1.0 (BOM), Micrometer 1.17.0
- **Test frameworks:** JUnit 6.0.3, AssertJ 3.27.7, Mockito 5.23.0,
  ArchUnit 1.4.1
- **Code quality:** Spotless 7.2.1, Google Java Format 1.28.0
- **Compatibility checking:** Japicmp 0.4.6

Published as a `version-catalog` component to the internal artifact
repository, consumable by `platform-build`, `platform-core`,
`platform-testing`, and `application-template`.

This is the first artifact in the build order — nothing else can be
built against real coordinates until this exists and is published.
