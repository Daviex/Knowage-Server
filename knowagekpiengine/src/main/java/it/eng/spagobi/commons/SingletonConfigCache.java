/*
 * Knowage, Open Source Business Intelligence suite
 * Copyright (C) 2016 Engineering Ingegneria Informatica S.p.A.
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
package it.eng.spagobi.commons;

import org.apache.log4j.Logger;

import it.eng.spagobi.commons.bo.Config;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IConfigDAO;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

/**
 * This object is instantiated and used by SingletonConfig to read config parameters. SpagoBi project have its own iplementation. The engines have different
 * implementation.
 *
 * TODO refactor this class since it is not a real cache!!!!
 *
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SingletonConfigCache implements ISingletonConfigCache {

	private static Logger logger = Logger.getLogger(SingletonConfigCache.class);

	public SingletonConfigCache() {
	}

	/**
	 * TODO : Workaround for KNOWAGE-4784
	 */
	@Override
	public String get(String key) {
		String toReturn = null;
		try {
			IConfigDAO dao = null;
			dao = DAOFactory.getSbiConfigDAO();
			Config config = dao.loadConfigParametersByLabel(key);
			if (config != null) {
				toReturn = config.getValueCheck();
			}
		} catch (Exception e) {
			logger.error("Error while loading configuration with key [" + key + "]", e);
			throw new SpagoBIRuntimeException("Error while loading configuration with key [" + key + "]", e);
		}

		if (toReturn == null) {
			logger.info("The property '" + key + "' doens't have any value assigned, check SBI_CONFIG table");
			return null;
		}
		logger.debug("GET :" + key + "=" + toReturn);
		return toReturn;
	}
}
