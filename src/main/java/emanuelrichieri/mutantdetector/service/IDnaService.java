package emanuelrichieri.mutantdetector.service;

import emanuelrichieri.mutantdetector.domain.entities.Dna;
import emanuelrichieri.mutantdetector.io.DnaDTO;
import emanuelrichieri.mutantdetector.io.DnaStatsDTO;
import emanuelrichieri.mutantdetector.util.exception.InvalidDnaException;
import emanuelrichieri.mutantdetector.util.exception.RepositoryException;

public interface IDnaService {
	
	/***
	 * Get DNA checks statistics.
	 * @return {@link DnaDTO} which contains: count_mutant_dna, count_human_dna, ratio.
	 * @throws RepositoryException
	 */
	public DnaStatsDTO getStats() throws RepositoryException;

	/**
	 * Save a DNA entity.
	 * @param dna
	 * @throws RepositoryException
	 */
	public void save(Dna dna) throws RepositoryException;
	
	/**
	 * Determines if a given DNA belongs to a mutant. 
	 * @param dnaDTO
	 * @return 
	 * 	- <code>true</code> if the given DNA contains more than one sequence of four equal letters,
	 * 	 obliquely, horizontally or vertically. <br>
	 *  - <code>false</code> otherwise.
	 * @throws InvalidDnaException
	 * @throws RepositoryException
	 */
	public Boolean isMutant(DnaDTO dnaDTO) throws InvalidDnaException, RepositoryException;
}
