/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.tablight.dataaddon.annotation.group;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.tablight.dataaddon.DataAddonBootstrap;
import dev.tablight.dataaddon.typeregistry.TypeRegistry;

/**
 * Mark your class extending {@link TypeRegistry} to use it with other infrastructure
 * @see DataAddonBootstrap
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Registry {
	/**
	 * @return Unique group tag, needs to mark relation between all other infrastructure.
	 */
	String value();
}
