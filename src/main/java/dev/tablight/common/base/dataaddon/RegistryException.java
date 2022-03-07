/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.tablight.common.base.dataaddon;

public final class RegistryException extends RuntimeException {
	private static final String WENT_WRONG = "Something went wrong while registering type: ";
	private static final String PLS_CHECK = ", please check if your class matches Registrable requirements.";

	public RegistryException(Class<?> registrableType, String message, Throwable cause) {
		super(WENT_WRONG + registrableType + PLS_CHECK + " | " + message, cause);
	}

	public RegistryException(Class<?> registrableType, String message) {
		super(WENT_WRONG + registrableType + PLS_CHECK + " | " + message);
	}

	public RegistryException(Class<?> registrableType, Throwable cause) {
		super(WENT_WRONG + registrableType + PLS_CHECK, cause);
	}

	public RegistryException(Class<?> registrableType) {
		super(WENT_WRONG + registrableType + PLS_CHECK);
	}

	public RegistryException(String message, Throwable cause) {
		super(message, cause);
	}

	public RegistryException(String message) {
		super(message);
	}
}
