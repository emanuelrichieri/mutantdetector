package emanuelrichieri.mutantdetector.service;

import emanuelrichieri.mutantdetector.util.exception.InvalidDnaException;
import emanuelrichieri.mutantdetector.util.exception.RepositoryException;

public interface IMutantDetectorService {
	

	/**
	 * Determines if a given DNA belongs to a mutant. 
	 * @param dna
	 * @return 
	 * 	- <code>true</code> if the given DNA contains more than one sequence of four equal letters,
	 * 	 obliquely, horizontally or vertically. <br>
	 *  - <code>false</code> otherwise.
	 * @throws InvalidDnaException
	 */
	public Boolean isMutant(String[] dna) throws InvalidDnaException;

}
