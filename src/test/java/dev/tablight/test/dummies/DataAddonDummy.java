/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package dev.tablight.test.dummies;

import dev.tablight.dataaddon.annotation.DataAddon;
import dev.tablight.dataaddon.annotation.Load;
import dev.tablight.dataaddon.annotation.Store;

@DataAddon(
		identifier = "dummy",
		groupTag = "dummyGroup",
		nativeClass = DataAddonDummyLookup.NativeDummy.class,
		lookup = DataAddonDummyLookup.class
)
public class DataAddonDummy {
	private String someString;
	private String someNativeStringData;

	public String getSomeString() {
		return someString;
	}

	public String getSomeNativeStringData() {
		return someNativeStringData;
	}

	public void setSomeNativeStringData(String someNativeStringData) {
		this.someNativeStringData = someNativeStringData;
	}

	@Store
	public void store() {
		someString = "store";
	}

	@Load
	public void load() {
		someString = "load";
	}
}
