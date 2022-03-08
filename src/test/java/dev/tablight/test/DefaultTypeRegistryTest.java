/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package dev.tablight.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.tablight.dataaddon.DataAddonBootstrap;
import dev.tablight.dataaddon.annotation.group.GroupContainer;
import dev.tablight.dataaddon.typeregistry.DefaultTypeRegistry;
import dev.tablight.test.dummies.DataAddonDummy;
import dev.tablight.test.registries.DummyTypeRegistry;

class DefaultTypeRegistryTest {
	DataAddonBootstrap dataAddonBootstrap = new DataAddonBootstrap();
	DefaultTypeRegistry registry;
	
	@BeforeEach
	void before() {
		dataAddonBootstrap.setContainer(new GroupContainer());
		dataAddonBootstrap.bootstrapRegistries("dev.tablight.test.registries");
		dataAddonBootstrap.bootstrapDataAddons("dev.tablight.test.dummies");
		registry = dataAddonBootstrap.getRegistry(DummyTypeRegistry.class);
	}

	@AfterEach
	void after() {
		registry.clearRegistry();
		dataAddonBootstrap.getContainer().clearAll();
		registry = null;
	}

	@Test
	void checkIsRegistered() {
		assertTrue(registry.isRegistered(DataAddonDummy.class));
	}

	@Test
	void checkNewInstanceClass() {
		assertNotNull(registry.newInstance(DataAddonDummy.class));
	}

	@Test
	void checkNewInstanceID() {
		assertNotNull(registry.newInstance("dummy"));
	}
	
	@Test
	void checkGetIdentifier() {
		assertEquals("dummy", registry.getIdentifier(DataAddonDummy.class));
	}
	
	@Test
	void checkGetClass() {
		assertEquals(DataAddonDummy.class, registry.getRegistrableType("dummy"));
	}
}
