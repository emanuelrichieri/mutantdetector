package emanuelrichieri.mutantdetector.service;

import emanuelrichieri.mutantdetector.util.exception.InvalidDnaException;

public interface IMutantDetectorService {
	
	public Boolean isMutant(String[] dna) throws InvalidDnaException;

}
