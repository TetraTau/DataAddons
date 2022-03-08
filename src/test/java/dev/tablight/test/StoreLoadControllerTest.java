/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package dev.tablight.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.tablight.dataaddon.DataAddonBootstrap;
import dev.tablight.dataaddon.annotation.group.GroupContainer;
import dev.tablight.dataaddon.holder.TypeHolder;
import dev.tablight.dataaddon.storeload.StoreLoadController;
import dev.tablight.dataaddon.typeregistry.TypeRegistry;
import dev.tablight.test.dummies.DataAddonDummy;
import dev.tablight.test.dummies.DataAddonDummyLookup;
import dev.tablight.test.registries.DummyHolder;
import dev.tablight.test.registries.DummyTypeRegistry;
import dev.tablight.test.registries.subpkg.DummyController;

class StoreLoadControllerTest {
	TypeRegistry typeRegistry;
	TypeHolder holder;
	StoreLoadController controller;

	@BeforeEach
	void before() {
		DataAddonBootstrap dataAddonBootstrap = new DataAddonBootstrap();

		dataAddonBootstrap.setContainer(new GroupContainer());
		dataAddonBootstrap.bootstrapRegistries("dev.tablight.test.registries");
		dataAddonBootstrap.bootstrapDataAddons("dev.tablight.test.dummies");

		typeRegistry = dataAddonBootstrap.getRegistry(DummyTypeRegistry.class);
		holder = dataAddonBootstrap.getRegistry(DummyHolder.class);
		controller = dataAddonBootstrap.getRegistry(DummyController.class);
	}

	@AfterEach
	void after() {
		holder.clearHeld();
		typeRegistry.clearRegistry();
		typeRegistry = null;
		holder = null;
		controller = null;
	}

	@Test
	void checkStore() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(DataAddonDummy.class);
		controller.store(DataAddonDummy.class);
		assertEquals("store", dummy.getSomeString());
		assertEquals("store", dummy1.getSomeString());
	}

	@Test
	void checkLoad() {
		var dummy = typeRegistry.newInstance(DataAddonDummy.class);
		var dummy1 = typeRegistry.newInstance(DataAddonDummy.class);
		controller.load(DataAddonDummy.class);
		assertEquals("load", dummy.getSomeString());
		assertEquals("load", dummy1.getSomeString());
	}

	@Test
	void checkLookup() {
		controller.lookup(new DataAddonDummyLookup());
		controller.load(DataAddonDummy.class);
		DataAddonDummy dummy = holder.getHeld(DataAddonDummy.class).stream().findFirst().get();
		assertEquals("native1", dummy.getSomeNativeStringData());
		assertEquals("load", dummy.getSomeString());
	}

	@Test
	void checkLookupAndLoad() {
		controller.lookupAndLoad(new DataAddonDummyLookup());
		DataAddonDummy dummy = holder.getHeld(DataAddonDummy.class).stream().findFirst().get();
		assertEquals("native1", dummy.getSomeNativeStringData());
		assertEquals("load", dummy.getSomeString());
	}

	@Test
	void checkLookupClass() {
		controller.lookup(DataAddonDummy.class);
		controller.load(DataAddonDummy.class);
		DataAddonDummy dummy = holder.getHeld(DataAddonDummy.class).stream().findFirst().get();
		assertEquals("native1", dummy.getSomeNativeStringData());
		assertEquals("load", dummy.getSomeString());
	}

	@Test
	void checkLookupAndLoadClass() {
		controller.lookupAndLoad(DataAddonDummy.class);
		DataAddonDummy dummy = holder.getHeld(DataAddonDummy.class).stream().findFirst().get();
		assertEquals("native1", dummy.getSomeNativeStringData());
		assertEquals("load", dummy.getSomeString());
	}
}
