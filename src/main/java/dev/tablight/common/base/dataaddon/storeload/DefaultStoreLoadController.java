/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.tablight.common.base.dataaddon.storeload;

import dev.tablight.common.base.dataaddon.RegistryException;
import dev.tablight.common.base.dataaddon.annotation.AnnotationUtil;
import dev.tablight.common.base.dataaddon.annotation.DataAddon;
import dev.tablight.common.base.dataaddon.holder.TypeHolder;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultStoreLoadController extends StoreLoadController {
	protected final Collection<TypeHolder> holders = new ArrayList<>();

	@Override
	public void store(Class<?> registrableType) {
		holders.forEach(holder -> holder.getHeld(registrableType).forEach(AnnotationUtil::invokeStore));
	}

	@Override
	public void load(Class<?> registrableType) {
		holders.forEach(holder -> holder.getHeld(registrableType).forEach(AnnotationUtil::invokeLoad));
	}

	@Override
	public <T> void lookupAndLoad(Class<T> registrableType) {
		AnnotationUtil.checkAnnotation(registrableType);
		try {
			lookupAndLoad(registrableType.getAnnotation(DataAddon.class).lookup().getDeclaredConstructor().newInstance());
		} catch (ReflectiveOperationException e) {
			throw new RegistryException("Check if your lookup matches requirements", e);
		}
	}

	@Override
	public <T> void lookup(Class<T> registrableType) {
		AnnotationUtil.checkAnnotation(registrableType);
		try {
			lookup(registrableType.getAnnotation(DataAddon.class).lookup().getDeclaredConstructor().newInstance());
		} catch (ReflectiveOperationException e) {
			throw new RegistryException("Check if your lookup matches requirements", e);
		}
	}

	@Override
	public <T, N> void lookupAndLoad(StoreLoadLookup<T, N> lookup) {
		lookup.lookup().get().forEach(instantiated -> {
			holders.forEach(holder -> holder.hold(instantiated));
			AnnotationUtil.invokeLoad(instantiated);
		});
	}

	@Override
	public <T, N> void lookup(StoreLoadLookup<T, N> lookup) {
		lookup.lookup().get().forEach(registrable -> holders.forEach(holder -> holder.hold(registrable)));
	}

	@Override
	public void addRegistrableHolder(TypeHolder registrableHolder) {
		holders.add(registrableHolder);
	}

	@Override
	public Collection<TypeHolder> getRegistrableHolders() {
		return holders;
	}
}
