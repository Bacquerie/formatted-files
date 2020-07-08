package org.bff.formatted;

import org.bff.files.formatted.DelimiterSeparatedReader;
import org.bff.formatted.model.annotations.FormattedDate;
import org.bff.formatted.model.annotations.FormattedField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.Date;

public class TestBatchReader
{
	public static void main (String [] args) throws Exception
	{
		DelimiterSeparatedReader
			.builder (Transaction.class, new File ("/data/tmp/transfer/CDINFILE"))
			.separator ('|')
			.build ()
			.read ()
			.forEach (System.out::println);
	}
}

@Data
class Transaction
{
	@FormattedField (order = 1)
	private String creditDebitIndicator;

	@FormattedField (order = 2)
	private Long id;

	@FormattedField (order = 3)
	private String accountNumber;

	@FormattedField (order = 4)
	private String accountHolder;

	@FormattedField (order = 5)
	private Integer reference;

	@FormattedDate ("yyyyMMdd")
	@FormattedField (order = 6)
	private Date valueDate;

	@FormattedField (order = 7)
	private String currency;
}
