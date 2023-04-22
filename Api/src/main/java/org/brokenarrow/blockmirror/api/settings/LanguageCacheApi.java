package org.brokenarrow.blockmirror.api.settings;

import org.brokenarrow.blockmirror.api.builders.language.Language;

public interface LanguageCacheApi {

	/**
	 * Get all set language
	 *
	 * @return language class.
	 */
	Language getLanguage();
}
