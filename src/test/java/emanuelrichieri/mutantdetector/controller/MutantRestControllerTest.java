package emanuelrichieri.mutantdetector.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import emanuelrichieri.mutantdetector.MockData;
import emanuelrichieri.mutantdetector.controller.MutantRestController;
import emanuelrichieri.mutantdetector.io.DnaDTO;
import emanuelrichieri.mutantdetector.io.DnaStatsDTO;
import emanuelrichieri.mutantdetector.io.ResponseDTO;
import emanuelrichieri.mutantdetector.service.IDnaService;
import emanuelrichieri.mutantdetector.util.exception.InvalidDnaException;
import emanuelrichieri.mutantdetector.util.exception.RepositoryException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MutantRestControllerTest {

	@Mock
	IDnaService dnaService;
	
	@InjectMocks
	MutantRestController controller;
	
	@Test
	void isMutant() throws InvalidDnaException, RepositoryException {
		when(dnaService.isMutant(Mockito.any())).thenReturn(true);
		DnaDTO dnaDTO = new DnaDTO();
		dnaDTO.setDna(MockData.MUTANT_DNA_SEQUENCE);
		ResponseEntity<ResponseDTO> response = controller.isMutant(dnaDTO);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	void isNotMutant() throws InvalidDnaException, RepositoryException {
		when(dnaService.isMutant(Mockito.any())).thenReturn(false);
		DnaDTO dnaDTO = new DnaDTO();
		dnaDTO.setDna(MockData.HUMAN_DNA_SEQUENCE);
		ResponseEntity<ResponseDTO> response = controller.isMutant(dnaDTO);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
	
	@Test
	void isMutantInvalidDna() throws InvalidDnaException, RepositoryException {
		when(dnaService.isMutant(Mockito.any())).thenThrow(InvalidDnaException.class);
		ResponseEntity<ResponseDTO> response = controller.isMutant(Mockito.any());
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);		
	}
	
	@Test
	void isMutantInternalError() throws InvalidDnaException, RepositoryException {
		when(dnaService.isMutant(Mockito.any())).thenThrow(RepositoryException.class);
		ResponseEntity<ResponseDTO> response = controller.isMutant(Mockito.any());
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);		
	}
	
	@Test
	void getStats() throws RepositoryException {
		when(dnaService.getStats()).thenReturn(new DnaStatsDTO());
		ResponseEntity<?> response = controller.getStats();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	@Test
	void getStatsInternalError() throws RepositoryException {
		when(dnaService.getStats()).thenThrow(RepositoryException.class);
		ResponseEntity<?> response = controller.getStats();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);		
	}
	
}
