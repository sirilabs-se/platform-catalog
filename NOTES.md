# Notes

## Deferred: Maven BOM

**Decision:** `platform-catalog` publishes a Gradle `version-catalog`
component only. A Maven BOM (`pom` packaging with `<dependencyManagement>`)
is explicitly **not** built at this time.

**Rationale (Strategy v7, Section 4):** every current and planned consumer
of the platform (`platform-build`, `platform-core`, `platform-testing`,
`application-template`) is a Gradle project. A Maven BOM would duplicate
every version alias in a second format with no consumer to justify the
maintenance cost.

**Trigger to revisit:** the moment a real Maven-based consumer appears,
add a sibling publication (or a companion artifact) that generates a BOM
from the same `gradle/libs.versions.toml` source of truth, so the two
formats can't drift apart. Until then, this decision should not be
re-litigated.

## Version pinning discipline

All versions in `gradle/libs.versions.toml` should be treated as the
single point of truth. If you find a raw version string for any of these
libraries anywhere else in the platform's five repos, that's a bug —
point the offending line back at this catalog instead of adding a new
pin.

## Verifying versions before publishing

The alias versions in this initial `1.0.0` catalog were set to the latest
stable releases available at authoring time. Before tagging and
publishing, re-verify each version against its upstream release page —
especially `spring-boot`, `junit`, and `archunit`, which release on
independent cadences and may have moved since this file was written.
