package emanuelrichieri.mutantdetector.util.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringArrayConverter implements AttributeConverter<String[], String> {
	public static final String DELIMITER = ",";
	
	@Override
	public String convertToDatabaseColumn(String[] attribute) {
		return String.join(DELIMITER, attribute);
	}

	@Override
	public String[] convertToEntityAttribute(String dbData) {
		return dbData.split(DELIMITER);
	}
	
}
