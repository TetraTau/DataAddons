/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package dev.tablight.test;

import dev.tablight.common.base.dataaddon.DataAddonBootstrap;
import dev.tablight.common.base.dataaddon.typeregistry.TypeRegistry;
import dev.tablight.common.base.dataaddon.annotation.group.GroupContainer;
import dev.tablight.common.base.dataaddon.holder.ConcurrentTypeHolder;
import dev.tablight.test.registries.DummyHolder;
import dev.tablight.test.registries.DummyTypeRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dev.tablight.test.dummies.DataAddonDummy;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentTypeHolderTest {
	DataAddonBootstrap dataAddonBootstrap = new DataAddonBootstrap();
	ConcurrentTypeHolder holder;
	TypeRegistry typeRegistry;

	@BeforeEach
	void before() {
		dataAddonBootstrap.setContainer(new GroupContainer());
		dataAddonBootstrap.bootstrapRegistries("dev.tablight.test.registries");
		dataAddonBootstrap.bootstrapDataAddons("dev.tablight.test.dummies");
		typeRegistry = dataAddonBootstrap.getRegistry(DummyTypeRegistry.class);
		holder = dataAddonBootstrap.getRegistry(DummyHolder.class);
	}

	@AfterEach
	void after() {
		dataAddonBootstrap.getContainer().clearAll();
	}

	@Test
	void checkHold() {
		assertDoesNotThrow(() -> holder.hold(new DataAddonDummy()));
	}

	@Test
	void checkHoldEquality() {
		var dummy = new DataAddonDummy();
		holder.hold(dummy);
		assertEquals(dummy, holder.getHeld(DataAddonDummy.class).toArray()[0]);
	}

	@Test
	void checkGetType() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		assertIterableEquals(List.of(dummy), holder.getHeld(DataAddonDummy.class));
	}

	@Test
	void checkGetID() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		assertIterableEquals(List.of(dummy), holder.getHeld("dummy"));
	}

	@Test
	void checkContains() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		assertTrue(holder.containsInstance(dummy));
	}

	@Test
	void checkRelease() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(DataAddonDummy.class);
		holder.release(dummy);
		assertFalse(holder.containsInstance(dummy));
		assertTrue(holder.containsInstance(dummy1));
	}

	@Test
	void checkReleaseClass() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(DataAddonDummy.class);
		holder.release(DataAddonDummy.class);
		assertFalse(holder.containsInstance(dummy));
		assertFalse(holder.containsInstance(dummy1));
	}

	@Test
	void checkReleaseID() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(DataAddonDummy.class);
		holder.release("dummy");
		assertFalse(holder.containsInstance(dummy));
		assertFalse(holder.containsInstance(dummy1));
	}

	@Test
	void checkEventHandlerType() {
		holder.handle(((event, sequence, endOfBatch) -> {
			assertEquals(DataAddonDummy.class, event.getRegistrableType());
		}));
		holder.hold(new DataAddonDummy());
	}

	@Test
	void checkEventHandlerCollection() {
		var dummy = new DataAddonDummy();
		holder.handle(((event, sequence, endOfBatch) -> {
			assertIterableEquals(List.of(dummy), event.getRegistrables());
		}));
		holder.hold(dummy);
	}
}
