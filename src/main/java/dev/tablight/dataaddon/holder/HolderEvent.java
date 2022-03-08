/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.tablight.dataaddon.holder;

import java.util.Collection;

public final class HolderEvent {
	private Collection<Object> registrables;
	private Class<?> registrableType;

	public Collection<Object> getRegistrables() {
		return registrables;
	}

	public void setRegistrables(Collection<Object> registrables) {
		this.registrables = registrables;
	}

	public Class<?> getRegistrableType() {
		return registrableType;
	}

	public void setRegistrableType(Class<?> registrableType) {
		this.registrableType = registrableType;
	}
}
