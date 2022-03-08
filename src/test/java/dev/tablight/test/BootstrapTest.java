/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package dev.tablight.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.tablight.dataaddon.DataAddonBootstrap;
import dev.tablight.dataaddon.annotation.group.GroupContainer;
import dev.tablight.test.registries.DummyHolder;
import dev.tablight.test.registries.DummyTypeRegistry;
import dev.tablight.test.registries.subpkg.DummyController;

class BootstrapTest {
	DataAddonBootstrap bootstrap = new DataAddonBootstrap();

	@BeforeEach
	void before() {
		bootstrap.setContainer(new GroupContainer());
		bootstrap.bootstrapRegistries("dev.tablight.test.registries");
		bootstrap.bootstrapDataAddons("dev.tablight.test.dummies");
	}

	@AfterEach
	void after() {
		bootstrap = null;
	}

	@Test
	void checkTypeRegistryInstance() {
		assertNotNull(bootstrap.getRegistry(DummyTypeRegistry.class));
	}

	@Test
	void checkTypeHolderInstance() {
		assertNotNull(bootstrap.getRegistry(DummyHolder.class));
	}

	@Test
	void checkStoreLoadControllerInstance() {
		assertNotNull(bootstrap.getRegistry(DummyController.class));
	}
}
