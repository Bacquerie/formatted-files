package org.bff.formatted.model.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to represent a field in a formatted flat tex.
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.FIELD)
public @interface FormattedField
{
	/**
	 * Character used as padding when in text form.
	 */
	char filler () default ' ';

	/**
	 * If <code>true</code>, blank fields will be treated as <code>null</code>
	 */
	boolean optional () default false;

	/**
	 * Position that this field holds in the text lines. It must be greater than 0.
	 */
	int position ();

	/**
	 * Indicates how this field's text is aligned.
	 */
	boolean rightAligned () default false;

	/**
	 * Number of characters this field uses when in text form.
	 */
	int size () default 0;
}
