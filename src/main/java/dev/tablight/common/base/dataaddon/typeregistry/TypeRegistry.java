/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.tablight.common.base.dataaddon.typeregistry;

import dev.tablight.common.base.dataaddon.holder.TypeHolder;
import dev.tablight.common.base.dataaddon.annotation.DataAddon;

import java.util.Collection;

/**
 * Registry containing Unique DataAddon types where you can register and instantiate "custom" types.
 */
public abstract class TypeRegistry {
	/**
	 * Registers your types for further handling, storing and loading.
	 * @param registrableType non-abstract "custom" type implementing {@link DataAddon} interface.
	 */
	public abstract void register(Class<?> registrableType);

	/**
	 * Checks if your {@link DataAddon} implementation already registered.
	 * @param registrableType non-abstract "custom" type implementing {@link DataAddon} interface.
	 * @return true if 
	 */
	public abstract boolean isRegistered(Class<?> registrableType);

	/**
	 * Instantiates registered class and adding it into all added holders.
	 * @param registrableType registered non-abstract "custom" type implementing {@link DataAddon} interface.
	 * @param <T> your exact type.
	 * @return Instance added into all holders added to this {@link TypeRegistry}.
	 */
	public abstract <T> T newInstance(Class<T> registrableType);

	/**
	 * Same as {@link #newInstance(Class)} but with unique identifier specified in {@link DataAddon#identifier()}
	 * @param identifier Unique identifier.
	 * @param <T> your exact type.
	 * @return Instance added into all holders added to this {@link TypeRegistry}.
	 */
	public abstract <T> T newInstance(String identifier);

	/**
	 * Obtains unique identifier by its class. 
	 * @param registrableType registered non-abstract "custom" type implementing {@link DataAddon} interface.
	 * @return unique identifier.
	 */
	public abstract String getIdentifier(Class<?> registrableType);

	/**
	 * Obtains {@link DataAddon} class by its unique id.
	 * @param identifier unique id.
	 * @return {@link DataAddon} class.
	 */
	public abstract Class<?> getRegistrableType(String identifier);

	/**
	 * Adds {@link TypeHolder} to this {@link TypeRegistry} for handling.
	 * @param registrableHolder {@link TypeHolder} you prefer to add to this {@link TypeRegistry}
	 */
	public abstract void addRegistrableHolder(TypeHolder registrableHolder);

	/**
	 * Obtains RegistrableHolders added to this {@link TypeRegistry}.
	 * @return All added RegistrableHolders.
	 */
	public abstract Collection<TypeHolder> getRegistrableHolders();
	
	/**
	 * clear everything in this {@link TypeRegistry}
	 */
	public abstract void clearRegistry(); 
}
