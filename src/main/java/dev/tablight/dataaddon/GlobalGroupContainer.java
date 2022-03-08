/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.tablight.dataaddon;

import dev.tablight.dataaddon.annotation.group.GroupContainer;

/**
 * Global group container for integration of multiple projects which use this library.
 */
public final class GlobalGroupContainer extends GroupContainer {
	private static GlobalGroupContainer instance;
	public static GlobalGroupContainer getInstance() {
		if (instance == null) instance = new GlobalGroupContainer();
		return instance;
	}
	
	private GlobalGroupContainer() {}
}
