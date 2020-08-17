package emanuelrichieri.mutantdetector.util.converter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import emanuelrichieri.mutantdetector.MockData;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StringArrayConverterTest {

	@InjectMocks
	StringArrayConverter converter = new StringArrayConverter();
	
	@Test
	void convertToString() {
		String result = converter.convertToDatabaseColumn(MockData.HUMAN_DNA_SEQUENCE);
		assertThat(result).isEqualTo(MockData.HUMAN_DNA_SEQUENCE_STRING);
	}
	
	@Test
	void convertToArray() {
		String[] result = converter.convertToEntityAttribute(MockData.HUMAN_DNA_SEQUENCE_STRING);
		assertThat(result).isEqualTo(MockData.HUMAN_DNA_SEQUENCE);
	}
}
