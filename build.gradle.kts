import com.teamresourceful.publishing.GitHubPom
import com.teamresourceful.publishing.javaPublishing
import com.teamresourceful.utils.Platform
import com.teamresourceful.utils.getPlatform

plugins {
    java
    id("maven-publish")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.4.1"
    id("net.neoforged.moddev") version "2.0.147"
    alias(libs.plugins.resourceful.gradle)
    alias(libs.plugins.resourceful.minecraft) apply false
}

subprojects {
    apply(plugin = "maven-publish")

    version = rootProject.libs.versions.mod.version.get()

    val platform = getPlatform()

    when (platform) {
        Platform.COMMON -> apply(plugin = "com.teamresourceful.plugins.minecraft-platform-common")
        Platform.FABRIC -> apply(plugin = "com.teamresourceful.plugins.minecraft-platform-fabric")
        Platform.NEOFORGE -> apply(plugin = "com.teamresourceful.plugins.minecraft-platform-neoforge")
    }

    if (platform != Platform.COMMON) {
        tasks.withType<JavaCompile> {
            val serviceArgs = listOf(
                "-Xplugin:ServicePlugin",
                "--service-plugin-platform=$platform",
                "--service-plugin-platform-class=com.teamresourceful.resourcefulconfig.common.utils.Platform",
            )

            options.encoding = "UTF-8"
            options.compilerArgs.add(serviceArgs.joinToString(separator = " "))
        }
    }

    dependencies {
        if (platform != Platform.COMMON) {
            annotationProcessor(rootProject.libs.service.plugin)
        }
        if (platform == Platform.FABRIC) {
            compileOnly(rootProject.libs.modmenu)
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
