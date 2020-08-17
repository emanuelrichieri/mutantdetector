package emanuelrichieri.mutantdetector.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import emanuelrichieri.mutantdetector.MockData;
import emanuelrichieri.mutantdetector.service.implementation.MutantDetectorService;
import emanuelrichieri.mutantdetector.util.exception.InvalidDnaException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MutantDetectorServiceTest {

	@InjectMocks
	MutantDetectorService service;
	
	@Test
	void isMutant() throws InvalidDnaException {
		Boolean mutantSequenceResult = this.service.isMutant(MockData.MUTANT_DNA_SEQUENCE);
		Boolean mutantSequence2Result = this.service.isMutant(MockData.MUTANT_DNA_SEQUENCE_ALL_EQUAL);
		Boolean humanSequenceResult = this.service.isMutant(MockData.HUMAN_DNA_SEQUENCE);
		
		assertThat(mutantSequenceResult).isEqualTo(true);
		assertThat(mutantSequence2Result).isEqualTo(true);
		assertThat(humanSequenceResult).isEqualTo(false);
	}
	
	@Test
	void invalidDna() throws InvalidDnaException {
		assertThatThrownBy(() -> this.service.isMutant(null)).isInstanceOf(InvalidDnaException.class);
	}
	

	
	/**
	 * Test at least 4x4 DNA constraint
	 */
	@Test
	void invalidDnaSizeException() {
		assertThatThrownBy(() -> service.isMutant(new String[] {"AAA", "AAA", "AAA"})).isInstanceOf(InvalidDnaException.class);
	}
	
	/**
	 * Test NxN constraint
	 */
	@Test
	void invalidDnaSequenceLengthException() {
		assertThatThrownBy(() -> service.isMutant(new String[] {"AA", "AAA", "AAA"})).isInstanceOf(InvalidDnaException.class);		
	}
	
	/**
	 * Test NxN constraint
	 */
	@Test
	void invalidCharactersException() {
		assertThatThrownBy(() -> service.isMutant(new String[] {"ABA", "ASA", "AAA"})).isInstanceOf(InvalidDnaException.class);		
	}
}
