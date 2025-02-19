/*
 * Knowage, Open Source Business Intelligence suite
 * Copyright (C) 2021 Engineering Ingegneria Informatica S.p.A.
 *
 * Knowage is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Knowage is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 */
package it.eng.knowage.knowageapi.utils;

public class ConfigSingleton {

	private static final String KNOWAGE_AUTHORIZATION_HEADER_NAME = "KNOWAGE_AUTHORIZATION_HEADER_NAME";

	private static final ConfigSingleton INSTANCE = new ConfigSingleton();

	private static String _rootPath = null;

	private static final String AF_ROOT_PATH = "";

	private String authorizationHeaderName;

	private ConfigSingleton() {

	}

	public String getAuthorizationHeaderName() {
		if (authorizationHeaderName == null) {
			if (System.getenv().containsKey(KNOWAGE_AUTHORIZATION_HEADER_NAME)) {
				authorizationHeaderName = System.getenv(KNOWAGE_AUTHORIZATION_HEADER_NAME);
			} else {
				authorizationHeaderName = "X-Kn-Authorization";
			}
		}
		return authorizationHeaderName;
	}

	public static ConfigSingleton getInstance() {
		return INSTANCE;
	}

}
