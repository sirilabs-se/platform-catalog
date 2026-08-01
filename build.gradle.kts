import java.time.Instant

plugins {
    `version-catalog`
    `maven-publish`
}

group = "se.sirilabs.platform"
version = "1.0.0"

catalog {
    versionCatalog {
        from(files("gradle/libs.versions.toml"))
    }
}

publishing {
    publications {
        create<MavenPublication>("platformCatalog") {
            from(components["versionCatalog"])
            artifactId = "platform-catalog"

            pom {
                name.set("platform-catalog")
                description.set(
                    "Aligned dependency versions and library coordinates for the " +
                            "Enterprise Application Platform. Every other platform artifact " +
                            "and every consuming application imports this catalog rather " +
                            "than pinning versions independently."
                )
            }
        }
    }

    repositories {
        maven {
            name = "platformRegistry"
            // Internal artifact repository (Nexus/Artifactory/GitHub Packages).
            // Strategy v7, Section 16 — resolved identically by every consumer.
            url = uri(
                System.getenv("PLATFORM_REGISTRY_URL")
                    ?: providers.gradleProperty("platformRegistryUrl").getOrElse(
                        "https://example.invalid/replace-with-internal-registry"
                    )
            )
            credentials {
                username = System.getenv("PLATFORM_REGISTRY_USERNAME")
                    ?: providers.gradleProperty("platformRegistryUsername").getOrNull()
                password = System.getenv("PLATFORM_REGISTRY_PASSWORD")
                    ?: providers.gradleProperty("platformRegistryPassword").getOrNull()
            }
        }
    }
}

tasks.register("printCatalogSummary") {
    group = "verification"
    description = "Prints the version-catalog coordinates and the resolved alias count."
    val projectVersion = project.version
    val projectGroup = project.group
    doLast {
        val toml = file("gradle/libs.versions.toml").readText()
        val versionCount = Regex("""^\s*[\w-]+\s*=""", RegexOption.MULTILINE)
            .findAll(toml.substringAfter("[versions]").substringBefore("[libraries]"))
            .count()
        val libraryCount = Regex("""^\s*[\w-]+\s*=""", RegexOption.MULTILINE)
            .findAll(toml.substringAfter("[libraries]").substringBefore("[plugins]"))
            .count()
        println("platform-catalog $projectVersion")
        println("  group        = $projectGroup")
        println("  versions     = $versionCount")
        println("  libraries    = $libraryCount")
        println("  built        = ${Instant.now()}")
    }
}