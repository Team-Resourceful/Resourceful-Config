import com.teamresourceful.publishing.GitHubPom
import com.teamresourceful.publishing.javaPublishing
import com.teamresourceful.utils.Platform
import com.teamresourceful.utils.getPlatform

plugins {
    java
    id("maven-publish")
    alias(libs.plugins.resourceful.loom)
    alias(libs.plugins.resourceful.gradle)
}

subprojects {
    apply(plugin = "maven-publish")

    version = rootProject.libs.versions.mod.version.get()

    val platform = getPlatform()

    dependencies {
        if (platform == Platform.FABRIC) {
            "modApi"(rootProject.libs.modmenu)
        }
    }

    javaPublishing {
        artifactId = "${rootProject.name}-${platform.name}-${rootProject.libs.versions.minecraft.get()}".lowercase()

        pom = GitHubPom(
            "ResourcefulConfig",
            "Crossplatform config library for Team Resourceful mods and more.",
            "MIT",
            "https://github.com/Team-Resourceful/Resourceful-Config"
        )

        repo = "https://maven.teamresourceful.com/repository/maven-releases/"
    }
}


resourcefulGradle {
    templates {
        register("readme") {
            source = file("templates/README.md.template")
            injectedValues = mapOf(
                "version" to libs.versions.mod.version.get(),
                "minecraft" to libs.versions.minecraft.get(),
            )
        }
        register("discord") {
            source = file("templates/embed.json.template")
            injectedValues = mapOf(
                "version" to libs.versions.mod.version.get(),
                "minecraft" to libs.versions.minecraft.get(),
                "neoforge" to libs.versions.neoforge.get(),
                "fabric" to libs.versions.fabric.api.get(),
                "fabric_link" to System.getenv("FABRIC_RELEASE_URL"),
                "neoforge_link" to System.getenv("FORGE_RELEASE_URL"),
            )
        }
    }
}
