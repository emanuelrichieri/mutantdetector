package emanuelrichieri.mutantdetector.service.implementation;

import java.util.Objects;

import org.springframework.stereotype.Service;

import emanuelrichieri.mutantdetector.service.IMutantDetectorService;
import emanuelrichieri.mutantdetector.util.Logger;
import emanuelrichieri.mutantdetector.util.exception.InvalidDnaException;

@Service("mutantDetectorService")
public class MutantDetectorService implements IMutantDetectorService {

	private Logger logger = Logger.getLogger("MutantDetectorService");
	
	@Override
	public Boolean isMutant(String[] dna) throws InvalidDnaException {
		try {
			if (Objects.isNull(dna)) {
				throw new InvalidDnaException("Dna cannot be null.");
			}
			return false;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

}
