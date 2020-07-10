package org.bff.formatted;

import org.bff.files.formatted.DelimiterSeparatedWriter;
import org.bff.files.formatted.reader.FixedWidthReader;
import org.bff.formatted.model.annotations.FormattedDate;
import org.bff.formatted.model.annotations.FormattedField;

import lombok.Data;

import java.io.File;
import java.math.BigInteger;
import java.util.Date;
import java.util.stream.Stream;

public class TestBatchReader
{
	public static void main (String [] args) throws Exception
	{
		Stream <Transaction> transactions =FixedWidthReader
			.builder (Transaction.class, new File ("/data/tmp/transfer/CDINFILE_fixed"))
			.build ()
			.read ();

		String out = DelimiterSeparatedWriter
			.builder (Transaction.class)
			.separator ('@')
			.build ()
			.writeToString (transactions);

		System.out.println (out);
	}
}

@Data
class Transaction
{
	public enum SupportedCurrencies
	{
		MXN, USD, EUR
	}

	@FormattedField (position = 2, rightAligned = true, filler = '0', size = 2)
	private Long id;

	@FormattedField (position = 1, size = 1)
	private String creditDebitIndicator;

	@FormattedField (position = 3, size = 12, rightAligned = true, filler = '0')
	private BigInteger accountNumber;

	@FormattedField (position = 4, size = 12, filler = '*', optional = true)
	private String accountHolder;

	@FormattedField (position = 5, size = 12, rightAligned = true, filler = '0')
	private BigInteger reference;

	@FormattedDate ("yyyyMMdd")
	@FormattedField (position = 6, size = 8)
	private Date valueDate;

	@FormattedField (position = 7, size = 5, filler = '-', rightAligned = true)
	private SupportedCurrencies currency;
}
