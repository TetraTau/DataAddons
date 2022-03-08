/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.tablight.dataaddon.holder.eventsourcing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import dev.tablight.dataaddon.RegistryException;
import dev.tablight.dataaddon.holder.HolderEvent;
import dev.tablight.dataaddon.holder.TypeHolder;
import dev.tablight.dataaddon.typeregistry.TypeRegistry;

public class ConcurrentEventTypeHolder extends TypeHolder {
	protected boolean running = false;
	protected Disruptor<HolderEvent> disruptor;
	protected final Collection<TypeRegistry> typeRegistries = new ArrayList<>();
	protected final Multimap<Class<?>, Object> instances =
			Multimaps.newMultimap(new ConcurrentHashMap<>(), ConcurrentHashMap::newKeySet);

	@Override
	public <T> void hold(T instance) {
		final Class<?> clazz = instance.getClass();
		checkRegistered(clazz);
		instances.put(clazz, instance);
		checkRunning();
		publishEvent(clazz);
	}

	@Override
	public void addTypeRegistry(TypeRegistry typeRegistry) {
		typeRegistries.add(typeRegistry);
	}

	@Override
	public <T> void release(T instance) {
		final Class<?> clazz = instance.getClass();
		checkRegistered(clazz);
		instances.remove(clazz, instance);
		checkRunning();
		publishEvent(clazz);
	}

	@Override
	public <T> void release(Class<T> registrableType) {
		checkRegistered(registrableType);
		instances.removeAll(registrableType);
		checkRunning();
		publishEvent(registrableType);
	}

	@Override
	public Collection<TypeRegistry> getTypeRegistries() {
		return typeRegistries;
	}

	@Override
	public void release(String identifier) {
		release(getClassByID(identifier));
	}

	@Override
	public void clearHeld() {
		instances.clear();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Collection<T> getHeld(Class<T> registrableType) {
		return (Collection<T>) instances.get(registrableType);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Collection<T> getHeld(String identifier) {
		return (Collection<T>) instances.get(getClassByID(identifier));
	}

	@Override
	public void handle(EventHandler<? super HolderEvent> handler) {
		checkDisruptorNull();
		disruptor.handleEventsWith(handler);
	}

	@Override
	public void forceStart() {
		running = true;
		checkDisruptorNull();
		disruptor.start();
	}

	@Override
	public <T> boolean containsInstance(T registrable) {
		return instances.containsValue(registrable);
	}

	// Implementation details methods, they're not specified in RegistrableHolder.
	
	public boolean isRunning() {
		return running;
	}
	
	public void shutdownDisruptor() {
		disruptor.shutdown();
	}

	public Multimap<Class<?>, Object> getInternalMap() {
		return instances;
	}
	
	// Private checking methods.

	private <T> void checkRegistered(Class<T> tClass) {
		if (typeRegistries.stream().noneMatch(typeRegistry -> typeRegistry.isRegistered(tClass)))
			throw new RegistryException(tClass, "Can't hold registrable because it isn't registered");
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> getClassByID(String id) {
		return (Class<T>) typeRegistries.stream()
				.map(typeRegistry -> typeRegistry.getRegistrableType(id)).findFirst().orElseThrow(() -> new RegistryException("There is no Registrables defined with given Id"));
	}

	private <T> void publishEvent(Class<T> tClass) {
		disruptor.publishEvent((event, sequence) -> {
			event.setRegistrables(instances.get(tClass));
			event.setRegistrableType(tClass);
		});
	}

	private void checkRunning() {
		if (!running) forceStart();
	}

	private void checkDisruptorNull() {
		if (disruptor == null) disruptor = new Disruptor<>(
				HolderEvent::new,
				1024,
				DaemonThreadFactory.INSTANCE,
				ProducerType.MULTI,
				new BusySpinWaitStrategy()
		);
	}
}
