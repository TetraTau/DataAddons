/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.tablight.dataaddon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dev.tablight.dataaddon.DataAddonBootstrap;
import dev.tablight.dataaddon.holder.TypeHolder;
import dev.tablight.dataaddon.storeload.StoreLoadController;
import dev.tablight.dataaddon.storeload.StoreLoadLookup;
import dev.tablight.dataaddon.typeregistry.TypeRegistry;

/**
 * Mark your class with this annotation if it represents object with additional data over some other object data
 * to connect and use it with {@link TypeRegistry}, {@link TypeHolder}, {@link StoreLoadController} and other infrastructure.
 * @see DataAddonBootstrap
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataAddon {
	/**
	 * @return Unique String identifier, used in {@link TypeRegistry} and {@link TypeHolder} 
	 */
	String identifier();

	/**
	 * @return Unique group tag, needs to mark relation between this DataAddon and {@link TypeRegistry}, {@link TypeHolder}, {@link StoreLoadController}.
	 */
	String groupTag();

	/**
	 * @return "Native" class which this DataAddon decorate with additional data.
	 */
	Class<?> nativeClass();

	/**
	 * @return "Lookup" which looks for "native" data, makes objects with this type and holds them in connected holders by group tag. 
	 */
	Class<? extends StoreLoadLookup<?, ?>> lookup();
}
