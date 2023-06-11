import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "xyz.xenondevs.nova"
version = "0.2.5-RC.1"

val mojangMapped = project.hasProperty("mojang-mapped")

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.nova)
    alias(libs.plugins.stringremapper)
    alias(libs.plugins.specialsource)
}

repositories {
    mavenCentral()
    maven("https://repo.xenondevs.xyz/releases")
    mavenLocal { content { includeGroup("org.spigotmc") } }
}

dependencies {
    implementation(libs.nova)
    implementation("xyz.xenondevs:simple-upgrades:1.0-SNAPSHOT")
}

addon {
    id.set(project.name)
    name.set(project.name.capitalized())
    version.set(project.version.toString())
    novaVersion.set(libs.versions.nova)
    main.set("xyz.xenondevs.nova.logistics.Logistics")
    depend.add("simple_upgrades")
    authors.set(listOf("StudioCode", "ByteZ", "Javahase"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    
    register<Copy>("addonJar") {
        group = "build"
        dependsOn("addon", if (mojangMapped) "jar" else "remapObfToSpigot")
        
        from(File(File(project.buildDir, "libs"), "${project.name}-${project.version}.jar"))
        into((project.findProperty("outDir") as? String)?.let(::File) ?: project.buildDir)
        rename { it.replace(project.name, addon.get().addonName.get()) }
    }
}

spigotRemap {
    spigotVersion.set(libs.versions.spigot.get().substringBefore('-'))
    sourceJarTask.set(tasks.jar)
}

remapStrings {
    remapGoal.set(if (mojangMapped) "mojang" else "spigot")
    spigotVersion.set(libs.versions.spigot.get())
}

generateWailaTextures {
    filter.set { !it.name.contains(Regex("\\d")) }
}