import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

description = "Plugin for running DataAddons as paper plugin."

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}

bukkit {
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    name = "DataAddons"
    version = rootProject.version.toString()
    main = "io.xxr.dataaddon.DataAddonPlugin"
    apiVersion = "1.18"
    authors = listOf("Denery")

    commands {
        register("dataaddons") {
            description = ""
            usage = "\"/dataaddons version\" for version"
        }
    }
}
