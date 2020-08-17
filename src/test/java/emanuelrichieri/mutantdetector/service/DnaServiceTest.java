package emanuelrichieri.mutantdetector.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import emanuelrichieri.mutantdetector.MockData;
import emanuelrichieri.mutantdetector.domain.entities.Dna;
import emanuelrichieri.mutantdetector.domain.entities.Dna.DnaClassification;
import emanuelrichieri.mutantdetector.domain.repository.IDnaRepository;
import emanuelrichieri.mutantdetector.io.DnaDTO;
import emanuelrichieri.mutantdetector.io.DnaStatsDTO;
import emanuelrichieri.mutantdetector.service.implementation.DnaService;
import emanuelrichieri.mutantdetector.util.exception.InvalidDnaException;
import emanuelrichieri.mutantdetector.util.exception.RepositoryException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DnaServiceTest {

	@Mock
	private IDnaRepository dnaRepository;
	
	@Mock
	private IMutantDetectorService mutantDetectorService;
	
	@InjectMocks
	private DnaService dnaService;

	/**
	 * Test saving mutant Dna entity into database. 
	 * @throws InvalidDnaException
	 * @throws RepositoryException
	 */
	@Test
	void saveMutantDna() throws RepositoryException {
		final Dna mutantDna = MockData.MUTANT_DNA;		
		when(dnaRepository.findByDnaSequence(MockData.MUTANT_DNA_SEQUENCE)).thenReturn(null);
		when(dnaRepository.save(mutantDna)).thenAnswer(invocation -> invocation.getArgument(0));		
		dnaService.save(mutantDna);
		
		verify(dnaRepository).save(Mockito.any(Dna.class));
	}
	

	/**
	 * Test saving human Dna entity into database. 
	 * @throws InvalidDnaException
	 * @throws RepositoryException
	 */
	@Test
	void saveHumanDna() throws RepositoryException {		
		final Dna humanDna = MockData.HUMAN_DNA;		
		when(dnaRepository.findByDnaSequence(MockData.HUMAN_DNA_SEQUENCE)).thenReturn(null);
		when(dnaRepository.save(humanDna)).thenAnswer(invocation -> invocation.getArgument(0));		
		dnaService.save(humanDna);
		
		verify(dnaRepository).save(Mockito.any(Dna.class));
	}
	
	/**
	 * Test if saving null DNA throws a {@link RepositoryException}
	 */
	@Test
	void savingRepositoryException() {
		when(dnaRepository.save(null)).thenThrow(new IllegalArgumentException());		
		assertThatThrownBy(() -> dnaService.save(null)).hasMessage("Error saving DNA. ");
	}

	/**
	 * Test when dna belongs to a mutant and it is not recorded in the database. 
	 * @throws InvalidDnaException
	 * @throws RepositoryException
	 */
	@Test
	void isMutantNewDna() throws InvalidDnaException, RepositoryException {
		DnaDTO dnaDTO = new DnaDTO();
		dnaDTO.setDna(MockData.MUTANT_DNA_SEQUENCE);
		when(dnaRepository.findByDnaSequence(MockData.MUTANT_DNA_SEQUENCE)).thenReturn(null);
		when(dnaRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));
		when(mutantDetectorService.isMutant(Mockito.any())).thenReturn(true);
		
		Boolean result = dnaService.isMutant(dnaDTO);
		assertThat(result).isEqualTo(true);
	}

	/**
	 * Test when dna belongs to a mutant and it is already recorded in the database. 
	 * @throws InvalidDnaException
	 * @throws RepositoryException
	 */	
	@Test
	void isMutantExistingDna() throws InvalidDnaException, RepositoryException {
		DnaDTO dnaDTO = new DnaDTO();
		dnaDTO.setDna(MockData.MUTANT_DNA_SEQUENCE);
		when(dnaRepository.findByDnaSequence(MockData.MUTANT_DNA_SEQUENCE)).thenReturn(MockData.MUTANT_DNA);
		when(dnaRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));
		when(mutantDetectorService.isMutant(Mockito.any())).thenReturn(true);
		
		Boolean result = dnaService.isMutant(dnaDTO);
		assertThat(result).isEqualTo(true);
	}

	/**
	 * Test when dna belongs to a human and it is not recorded in the database. 
	 * @throws InvalidDnaException
	 * @throws RepositoryException
	 */
	@Test
	void isNotMutantNewDna() throws InvalidDnaException, RepositoryException {
		DnaDTO dnaDTO = new DnaDTO();
		dnaDTO.setDna(MockData.HUMAN_DNA_SEQUENCE);
		when(dnaRepository.findByDnaSequence(MockData.HUMAN_DNA_SEQUENCE)).thenReturn(null);
		when(dnaRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));		
		when(mutantDetectorService.isMutant(Mockito.any())).thenReturn(false);

		Boolean result = dnaService.isMutant(dnaDTO);
		assertThat(result).isEqualTo(false);
	}
	

	/**
	 * Test when dna belongs to a human and it is already recorded in the database. 
	 * @throws InvalidDnaException
	 * @throws RepositoryException
	 */
	@Test
	void isNotMutantExistingDna() throws InvalidDnaException, RepositoryException {
		DnaDTO dnaDTO = new DnaDTO();
		dnaDTO.setDna(MockData.HUMAN_DNA_SEQUENCE);
		when(dnaRepository.findByDnaSequence(MockData.HUMAN_DNA_SEQUENCE)).thenReturn(MockData.HUMAN_DNA);
		when(dnaRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));		
		when(mutantDetectorService.isMutant(Mockito.any())).thenReturn(false);

		Boolean result = dnaService.isMutant(dnaDTO);
		assertThat(result).isEqualTo(false);
	}
	
	@Test
	void getStats() throws RepositoryException {
		when(dnaRepository.findByClassification(DnaClassification.HUMAN)).thenReturn(MockData.HUMAN_DNA_LIST);
		when(dnaRepository.findByClassification(DnaClassification.MUTANT)).thenReturn(MockData.MUTANT_DNA_LIST);
		
		Integer countHumanDna = MockData.HUMAN_DNA_LIST.size();
		Integer countMutantDna = MockData.MUTANT_DNA_LIST.size();
		Double ratio =  countMutantDna / countHumanDna * 1.0;
		
		DnaStatsDTO result = dnaService.getStats();
		assertThat(result.getCount_human_dna()).isEqualTo(countHumanDna.longValue());
		assertThat(result.getCount_mutant_dna()).isEqualTo(countMutantDna.longValue());
		assertThat(result.getRatio()).isEqualTo(ratio);
	}
	
	@Test
	void getStatsException() {
		when(dnaRepository.findByClassification(DnaClassification.HUMAN)).thenThrow(new IllegalArgumentException());
		when(dnaRepository.findByClassification(DnaClassification.MUTANT)).thenThrow(new IllegalArgumentException());
		
		assertThatThrownBy(() -> dnaService.getStats()).isInstanceOf(RepositoryException.class);
	}
}
