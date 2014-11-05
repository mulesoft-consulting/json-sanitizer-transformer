/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.jsonsanitizer;

import org.mule.api.annotations.Category;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Transformer;

import com.google.json.JsonSanitizer;

/**
 * 
 * 
 * @author MuleSoft, Inc.
 */
@Module(name = "jsonsanitizer", schemaVersion = "1.0", friendlyName = "JSON Sanitizer", description="Sanitizes JSON input to help prevent bad side affects")
@Category(name = "org.mule.tooling.category.transformers", description = "Transformers")
public class JsonSanitizerConnector {
	/**
	 * Sanitize JSON
	 * <p/>
	 * {@sample.xml ../../../doc/jsonsanitizer-transformer.xml.sample
	 * jsonsanitizer:sanitize}
	 * 
	 * @param input
	 *            the json string input for this transformer
	 * @return sanitized json string
	 */
	@Transformer(sourceTypes = { String.class })
	public static String sanitize(final String input) {
		if("{NullPayload}".equals(input)){
			return JsonSanitizer.sanitize(null);
		}
		return JsonSanitizer.sanitize(input);
	}
}