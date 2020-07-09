package org.bff.files.formatted;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.bff.files.utils.FFUtils;

public class FixedWidthWriter <Type> extends FormattedWriter <Type>
{
	///region CONSTRUCTORS

	/**
	 * Creates a new writer to process fixed-width text files, and map its lines to the given
	 * <code>klass</code>.
	 */
	public FixedWidthWriter (final Class <Type> klass)
	{
		super (klass);
	}

	///endregion

	///region OVERRIDDEN METHODS

	@Override
	protected String mapItem (final Type item)
	{
		final StringBuilder fields = new StringBuilder ();

		formattedFields.forEach (field ->
		{
			try
			{
				Object fieldValue = FieldUtils.readField (field, item, true);

				fields.append (FFUtils.padAndTrimField (field, fieldValue));
			}

			catch (Exception e)
			{
				e.printStackTrace ();
			}
		});

		return fields.toString ();
	}

	///endregion
}
