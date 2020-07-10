package org.bff.formatted.model.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Labels a <code>java.util.Date</code> with a given format.
 */
@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.FIELD)
public @interface FormattedDate
{
	/**
	 * Main date format when in text form.
	 **/
	String value () default "yyyy-MM-dd";
}
