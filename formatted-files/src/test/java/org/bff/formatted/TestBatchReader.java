package org.bff.formatted;

import org.bff.files.formatted.FixedWidthWriter;
import org.bff.files.formatted.reader.DelimiterSeparatedReader;
import org.bff.files.formatted.reader.FixedWidthReader;
import org.bff.formatted.model.annotations.FormattedDate;
import org.bff.formatted.model.annotations.FormattedField;

import lombok.Data;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TestBatchReader
{
	public static void main (String [] args) throws Exception
	{
		List <Transaction> transactions = FixedWidthReader
			.builder (Transaction.class, new File ("/data/tmp/transfer/CDINFILE_fixed"))
			.batchSize (2)
			.preprocessor ((field, position) -> field.trim ())
			.limit (6)
			.build ()
			.readAsList ();

		transactions.forEach (System.out::println);
	}
}

@Data
class Transaction
{
	@FormattedField (order = 2, textAlignment = FormattedField.Alignment.RIGHT, filler = '0', width = 2)
	private Long id;

	@FormattedField (order = 1, width = 1)
	private String creditDebitIndicator;

	@FormattedField (order = 3, width = 12, textAlignment = FormattedField.Alignment.RIGHT, filler = '0')
	private BigInteger accountNumber;

	@FormattedField (order = 4, width = 12, filler = '*')
	private String accountHolder;

	@FormattedField (order = 5, width = 12, textAlignment = FormattedField.Alignment.RIGHT, filler = '0')
	private BigInteger reference;

	@FormattedDate ("yyyyMMdd")
	@FormattedField (order = 6, width = 8)
	private Date valueDate;

	@FormattedField (order = 7, width = 5, filler = '-', textAlignment = FormattedField.Alignment.RIGHT)
	private String currency;
}
