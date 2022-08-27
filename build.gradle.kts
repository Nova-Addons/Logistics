group = "xyz.xenondevs.nova"
version = project.properties["version"] as String

val mojangMapped = System.getProperty("mojang-mapped") != null

plugins {
    kotlin("jvm") version "1.7.10"
    id("xyz.xenondevs.specialsource-gradle-plugin") version "1.0.0"
    id("xyz.xenondevs.string-remapper-gradle-plugin") version "1.0.0"
    id("xyz.xenondevs.nova.nova-gradle-plugin") version "0.11-SNAPSHOT"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.xenondevs.xyz/releases")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly(deps.nova)
    compileOnly(variantOf(deps.spigot) { classifier("remapped-mojang") })
}

tasks {
    register<Copy>("remappedJar") {
        group = "build"
        dependsOn(if (mojangMapped) "jar" else "remapObfToSpigot")
    
        from(File(File(project.buildDir, "libs"), "${project.name}-${project.version}.jar"))
        into(System.getProperty("outDir")?.let(::File) ?: project.buildDir)
    }
    
    withType<ProcessResources> {
        filesMatching(listOf("addon.yml")) {
            expand(project.properties + mapOf("novaVersion" to deps.versions.nova.get()))
        }
    }
}

spigotRemap {
    spigotVersion.set(deps.versions.spigot.get().substringBefore('-'))
    sourceJarTask.set(tasks.jar)
    spigotJarClassifier.set("")
}

remapStrings {
    remapGoal.set(if (mojangMapped) "mojang" else "spigot")
    spigotVersion.set(deps.versions.spigot.get())
    classes.set(emptyList())
}

generateWailaTextures {
    novaVersion.set(deps.versions.nova)
    filter.set { !it.name.contains(Regex("\\d")) }
}