/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.tablight.dataaddon.holder;

import java.util.Collection;

import com.lmax.disruptor.EventHandler;

import dev.tablight.dataaddon.annotation.DataAddon;
import dev.tablight.dataaddon.typeregistry.TypeRegistry;

/**
 * Class "holding" instances of {@link TypeRegistry},
 * it is needed when you need to handle your "custom" types marked with {@link DataAddon}.
 */
public abstract class TypeHolder {

	/**
	 * "Holds" your {@link DataAddon} instance until {@link #release(Class)} or {@link #clearHeld()}.
	 * After that you can get collection of this type instances using {@link #getHeld(Class)}.
	 * @param instance Instance you want to hold.
	 * @param <T> Type of Instance you want to hold.
	 */
	public abstract <T> void hold(T instance);

	/**
	 * Removes your {@link DataAddon} from holding in this holder.
	 * @param instance Instance you want to release.
	 * @param <T> Type of instance you want to release.
	 */
	public abstract <T> void release(T instance);

	/**
	 * Removes your {@link DataAddon} type from holding in this holder.
	 * @param registrableType type of Instance you want to release.
	 * @param <T> Type of instance you want to release.
	 */
	public abstract <T> void release(Class<T> registrableType);

	/**
	 * Same as {@link #release(Class)} but with String identifier.
	 * @param identifier Identifier of instances you want to release.
	 */
	public abstract void release(String identifier);

	/**
	 * Checks if this holder contains the following instance.
	 * @param registrable {@link DataAddon} instance you want to check.
	 * @param <T> Exact type of {@link DataAddon}
	 * @return true if holder contains this instance.
	 */
	public abstract <T> boolean containsInstance(T registrable);

	/**
	 * Obtains all elements of type you want held by this holder.
	 * @param registrableType {@link DataAddon} Class of Instances you want to get.
	 * @param <T> Exact type of {@link DataAddon}.
	 * @return All instances held by this holder of your specific type.
	 */
	public abstract <T> Collection<T> getHeld(Class<T> registrableType);

	/**
	 * Same as {@link #getHeld(Class)} but get held instances by unique identifier.
	 * @param identifier unique identifier specified in {@link DataAddon#identifier()}
	 * @param <T> Exact type of {@link DataAddon}
	 * @return All instances held by this holder of your specific type.
	 */
	public abstract <T> Collection<T> getHeld(String identifier);

	/**
	 * Adds handler of Events happening after {@link #hold(Object)} and {@link #release(Class)} methods.
	 * 
	 * @implNote 1. Handlers run in their own threads, make sure you're handling everything thread-safely.
	 * 2. You need to add all your handlers before any action or {@link #forceStart()} method fired. 
	 * It works this way because we use "disruptor" event system for fastest ever events.
	 * @param handler handler you want to add.
	 */
	public abstract void handle(EventHandler<? super HolderEvent> handler);

	/**
	 * Starts handlers mechanism of this holder.
	 */
	public abstract void forceStart();

	/**
	 * Adds TypeRegistry checks to this holder, if you are making your implementations you need to specify TypeRegistries for your holder.
	 * @param typeRegistry {@link TypeRegistry} you want to check in this holder.
	 */
	public abstract void addTypeRegistry(TypeRegistry typeRegistry);

	/**
	 * Obtains TypeRegistries added in this {@link TypeHolder}
	 * @return All added TypeRegistries.
	 */
	public abstract Collection<TypeRegistry> getTypeRegistries();

	/**
	 * Clears all held instances from this holder.
	 */
	public abstract void clearHeld();
}
