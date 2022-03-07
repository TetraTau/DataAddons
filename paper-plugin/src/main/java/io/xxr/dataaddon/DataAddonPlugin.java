/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package io.xxr.dataaddon;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataAddonPlugin extends JavaPlugin {
	private static final Logger loggerDA = LoggerFactory.getLogger("DataAddons");
	private static DataAddonPlugin instance;

	public static DataAddonPlugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		this.getCommand("dataaddons").setExecutor(new DataAddonsCommand());
		loggerDA.info("Data Addons loaded successfully!");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	private static final class DataAddonsCommand implements TabExecutor {
		@Override
		public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
			if (args.length == 1) {
				if (args[0].equals("version")) {
					sender.sendMessage("Data Addons version: " + DataAddonPlugin.instance.getDescription().getVersion());
					return true;
				}
			}
			return false;
		}

		@Override
		public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
			return List.of("version");
		}
	}
}
