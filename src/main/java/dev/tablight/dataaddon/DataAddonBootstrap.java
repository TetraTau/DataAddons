/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.tablight.dataaddon;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.reflect.ClassPath;

import dev.tablight.dataaddon.annotation.AnnotationUtil;
import dev.tablight.dataaddon.annotation.DataAddon;
import dev.tablight.dataaddon.annotation.group.Controller;
import dev.tablight.dataaddon.annotation.group.GroupContainer;
import dev.tablight.dataaddon.annotation.group.Holder;
import dev.tablight.dataaddon.annotation.group.Registry;
import dev.tablight.dataaddon.holder.TypeHolder;
import dev.tablight.dataaddon.storeload.StoreLoadController;
import dev.tablight.dataaddon.typeregistry.TypeRegistry;

public final class DataAddonBootstrap {
	private GroupContainer container;

	public void setContainer(GroupContainer container) {
		this.container = container;
	}

	public GroupContainer getContainer() {
		return container;
	}

	/**
	 * Configures {@link TypeRegistry}, {@link TypeHolder}, {@link StoreLoadController} and other infrastructure in the package.
	 * @param packageName package containing registries.
	 */
	public void bootstrapRegistries(String packageName) {
		try {
			List<? extends Class<?>> classesInPackage = ClassPath.from(ClassLoader.getSystemClassLoader())
					.getAllClasses()
					.stream()
					.filter(clazz -> clazz.getPackageName().startsWith(packageName))
					.map(ClassPath.ClassInfo::load)
					.toList();


			classesInPackage.stream()
					.filter(clazz -> clazz.isAnnotationPresent(Registry.class))
					.forEach(clazz -> container.registerTypeRegistry(clazz.asSubclass(TypeRegistry.class)));

			classesInPackage.stream()
					.filter(clazz -> clazz.isAnnotationPresent(Holder.class))
					.forEach(clazz -> container.registerHolder(clazz.asSubclass(TypeHolder.class)));

			classesInPackage.stream()
					.filter(clazz -> clazz.isAnnotationPresent(Controller.class))
					.forEach(clazz -> container.registerController(clazz.asSubclass(StoreLoadController.class)));
			
			classesInPackage.stream().forEach(clazz -> {
				if (clazz.isAnnotationPresent(Registry.class)) 
					AnnotationUtil.connectGroupsInRepoByTag(clazz.getDeclaredAnnotation(Registry.class).value(), container);
				else if (clazz.isAnnotationPresent(Holder.class))
					AnnotationUtil.connectGroupsInRepoByTag(clazz.getDeclaredAnnotation(Holder.class).value(), container);
				else if (clazz.isAnnotationPresent(Controller.class))
					AnnotationUtil.connectGroupsInRepoByTag(clazz.getDeclaredAnnotation(Controller.class).value(), container);
			});
		} catch (IOException e) {
			throw new RegistryException("Something went wrong while bootstrapping Registries.");
		}
	}

	/**
	 * Configures {@link DataAddon} implementation and connects it with already existing infrastructure
	 * configured with {@link #bootstrapRegistries(String)} and contained in {@link #container}
	 * @param packageName package containing DataAddons.
	 */
	public void bootstrapDataAddons(String packageName) {
		bootstrapDataAddons(packageName, clazz -> {});
	}

	/**
	 * Configures {@link DataAddon} implementation and connects it with already existing infrastructure with specified processor
	 * configured with {@link #bootstrapRegistries(String)} and contained in {@link #container}
	 * @param packageName package containing DataAddons.
	 * @param processor processor running at configuration of each class.
	 */
	public void bootstrapDataAddons(String packageName, Consumer<? super Class<?>> processor) {
		try {
			List<? extends Class<?>> implClasses = ClassPath.from(ClassLoader.getSystemClassLoader())
					.getAllClasses()
					.stream()
					.filter(clazz -> clazz.getPackageName().startsWith(packageName))
					.map(ClassPath.ClassInfo::load)
					.filter(clazz -> clazz.isAnnotationPresent(DataAddon.class))
					.toList();

			implClasses.forEach(container::registerImplementation);
			implClasses.forEach(clazz -> {
				processor.accept(clazz);
				AnnotationUtil.connectImplByTag(clazz.getAnnotation(DataAddon.class).groupTag(), container);
			});
		} catch (IOException e) {
			throw new RegistryException("Something went wrong while bootstrapping Registries.");
		}
	}

	/**
	 * @return registry instance contained in {@link #container} by its class.
	 */
	public <T> T getRegistry(Class<T> clazz) {
		return (T) container.data.get(clazz);
	}

	public <T> DataAddon getDataAddonInfo(Class<T> clazz) {
		if (!container.implementations.values().contains(clazz)) throw new RegistryException("This DataAddon doesn't registered in this bootstrap!");
		return clazz.getAnnotation(DataAddon.class);
	}
}
