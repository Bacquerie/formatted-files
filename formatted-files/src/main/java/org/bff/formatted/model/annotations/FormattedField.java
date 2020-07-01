package org.bff.formatted.model.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.FIELD)
public @interface FormattedField
{
	public static enum Alignment { LEFT, RIGHT };
	
	char filler () default ' ';
	
	String name () default "";
	
	int order ();
	
	Alignment textAlignment () default Alignment.LEFT;
	
	int width () default 0;
}
