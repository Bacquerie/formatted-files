package org.bff.formatted;

import org.bff.files.formatted.DelimiterSeparatedReader;
import org.bff.formatted.model.annotations.FormattedField;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class TestBatchReader
{
	public static void main (String [] args) throws Exception
	{
		DelimiterSeparatedReader
			.builder (Person.class, "Luis,alejandro@gmail.com,Alejandro@otro@c")
			.build ()
			.read ()
			.forEach (System.out::println);
	}
}

@AllArgsConstructor
@Builder
@Data
class Person
{
	@FormattedField (order = 1)
	public String name;
	
	@FormattedField (order = 2)
	public String email;
	
	public Person () { }
}