/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

package dev.tablight.test.dummies;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import dev.tablight.dataaddon.storeload.StoreLoadLookup;

public class DataAddonDummyLookup implements StoreLoadLookup<DataAddonDummy, DataAddonDummyLookup.NativeDummy> {

	private final Collection<NativeDummy> nativeDummiesContainer = Lists.newArrayList(
			new NativeDummy("native1")
	); 
	
	@Override
	public Supplier<Collection<DataAddonDummy>> lookup() {
		return () -> nativeDummiesContainer.stream().map(nativeD -> {
			var dummy = new DataAddonDummy();
			dummy.setSomeNativeStringData(nativeD.getSomeNativeString());
			return dummy;
		}).toList();
	}

	@Override
	public Stream<NativeDummy> getNatives() {
		return nativeDummiesContainer.stream();
	}
	
	public static class NativeDummy {
		private final String someNativeString;
		public NativeDummy(String nativeString) {
			this.someNativeString = nativeString;
		} 

		public String getSomeNativeString() {
			return someNativeString;
		}
	}
}
