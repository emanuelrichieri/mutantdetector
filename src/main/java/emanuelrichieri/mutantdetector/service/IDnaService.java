package emanuelrichieri.mutantdetector.service;

import emanuelrichieri.mutantdetector.domain.entities.Dna;
import emanuelrichieri.mutantdetector.io.DnaDTO;
import emanuelrichieri.mutantdetector.io.DnaStatsDTO;
import emanuelrichieri.mutantdetector.util.exception.InvalidDnaException;
import emanuelrichieri.mutantdetector.util.exception.RepositoryException;

public interface IDnaService {
	
	public DnaStatsDTO getStats() throws RepositoryException;

	public void save(Dna dna) throws RepositoryException;
	
	public Boolean isMutant(DnaDTO dnaDTO) throws InvalidDnaException, RepositoryException;
}
