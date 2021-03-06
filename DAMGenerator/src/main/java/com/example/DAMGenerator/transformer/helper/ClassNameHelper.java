package com.example.DAMGenerator.transformer.helper;

import java.util.Arrays;
import java.util.List;

public final class ClassNameHelper {

	public static String toClassName(String tableName) {	
		
		StringBuilder builder = new StringBuilder();
		String newTableName = defineSingular(tableName);
		String parts[] = newTableName.split("_");

		List<String> listedParts = Arrays.asList(parts);
		listedParts.forEach(part -> {
			String capitalizedWord = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
			builder.append(capitalizedWord);
		});
		
		return builder.toString();
	}
	
	public static String toFieldName(String fieldName) {
		String camelCasePattern = "([a-z]+[A-Z]+\\w+)+";
		if(fieldName.matches(camelCasePattern))
			return fieldName;
		StringBuilder builder = new StringBuilder();
		String initialSplit[] = fieldName.split("_", 2);

		String firstWord = initialSplit[0];
		builder.append(firstWord.toLowerCase());

		if (initialSplit.length > 1) {
			String otherWords = initialSplit[1].toString();
			String parts[] = otherWords.split("_");
			List<String> listedParts = Arrays.asList(parts);
			listedParts.forEach(part -> {
				String capitalizedWord = part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase();
				builder.append(capitalizedWord);
			});
		}

		return builder.toString();
	}
	
	private static String defineSingular(final String plural) {
		if (plural.toLowerCase().endsWith("s")) {
			return plural.substring(0, plural.length() - 1);
		} else {
			return plural;
		}
	}
}
