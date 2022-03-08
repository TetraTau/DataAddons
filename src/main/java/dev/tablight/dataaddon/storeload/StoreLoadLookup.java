/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.tablight.dataaddon.storeload;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Class looking for native data and returns instantiated DataAddon classes added to a holder.
 * @param <T> DataAddon you want to lookup.
 * @param <N> Native type of DataAddon.
 */
public interface StoreLoadLookup<T, N> {
	/**
	 * @return lazy lookup obtaining instances of DataAddon instances
	 */
	Supplier<Collection<T>> lookup();

	/**
	 * @return Natives lazy stream matching DataAddon requirements.
	 */
	Stream<N> getNatives();
}
