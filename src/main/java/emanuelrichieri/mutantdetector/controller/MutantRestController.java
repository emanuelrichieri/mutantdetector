package emanuelrichieri.mutantdetector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import emanuelrichieri.mutantdetector.domain.entities.Dna.DnaClassification;
import emanuelrichieri.mutantdetector.io.DnaDTO;
import emanuelrichieri.mutantdetector.io.ResponseDTO;
import emanuelrichieri.mutantdetector.io.ResponseEntityBuilder;
import emanuelrichieri.mutantdetector.service.IDnaService;
import emanuelrichieri.mutantdetector.util.exception.InvalidDnaException;
import emanuelrichieri.mutantdetector.util.exception.RepositoryException;

@RestController
@RequestMapping("")
public class MutantRestController {
	
	@Autowired
	private IDnaService service;
	
	@PostMapping("/mutant")
	public ResponseEntity<ResponseDTO> isMutant(@RequestBody DnaDTO dnaDTO) {
		try {
			Boolean isMutant = this.service.isMutant(dnaDTO);
			if (isMutant) {
				return ResponseDTO.ok(DnaClassification.MUTANT.name(), dnaDTO).build();
			} 
			return ResponseDTO.forbidden(DnaClassification.HUMAN.name(), dnaDTO).build();
		} catch (InvalidDnaException ex) {
			return ResponseDTO.badRequest(ex).build();
		} catch (RepositoryException ex) {
			return ResponseDTO.internalServerError(ex.getMessage(), ex.getException()).build();
		} 
	}
	
	@GetMapping("/stats")
	public ResponseEntity<?> getStats() {
		try {
			return ResponseEntityBuilder.ok(this.service.getStats());
		} catch (Exception ex) {
			return ResponseDTO.internalServerError(ex).build();
		}
	}

}
