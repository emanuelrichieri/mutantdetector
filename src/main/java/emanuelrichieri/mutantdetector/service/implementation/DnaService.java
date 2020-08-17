package emanuelrichieri.mutantdetector.service.implementation;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import emanuelrichieri.mutantdetector.domain.entities.Dna;
import emanuelrichieri.mutantdetector.domain.entities.Dna.DnaClassification;
import emanuelrichieri.mutantdetector.domain.repository.IDnaRepository;
import emanuelrichieri.mutantdetector.io.DnaDTO;
import emanuelrichieri.mutantdetector.io.DnaStatsDTO;
import emanuelrichieri.mutantdetector.service.IDnaService;
import emanuelrichieri.mutantdetector.service.IMutantDetectorService;
import emanuelrichieri.mutantdetector.util.exception.InvalidDnaException;
import emanuelrichieri.mutantdetector.util.exception.RepositoryException;

@Service("dnaService")
public class DnaService implements IDnaService {

	@Autowired
	private IDnaRepository repository;
	
	@Autowired
	private IMutantDetectorService mutantDetectorService;
	
	private Logger logger = LoggerFactory.getLogger("DnaService");
	
	@Override
	public DnaStatsDTO getStats() throws RepositoryException {
		try {
			List<Dna> humanDna = this.repository.findByClassification(DnaClassification.HUMAN);
			List<Dna> mutantDna = this.repository.findByClassification(DnaClassification.MUTANT);
			Long countHumanDna = humanDna.stream().count();
			Long countMutantDna = mutantDna.stream().count();
			Double ratio =  countMutantDna / countHumanDna * 1.0;
			
			return new DnaStatsDTO(countHumanDna, countMutantDna, ratio);			
		} catch (Exception ex) {
			String msg = "Error getting DNA list";
			logger.error(msg, ex);
			throw new RepositoryException(msg, ex);
		}
	}

	@Override
	public void save(Dna dna) throws RepositoryException {
		try {
			this.repository.save(dna);			
		} catch (Exception ex) {
			String msg = "Error saving DNA. ";
			logger.error(msg, ex);
			throw new RepositoryException(msg, ex);
		}
	}

	@Override
	public Boolean isMutant(DnaDTO dnaDTO) throws InvalidDnaException, RepositoryException {
		Boolean isMutant = this.mutantDetectorService.isMutant(dnaDTO.getDna());
		DnaClassification classification = isMutant ? DnaClassification.MUTANT : DnaClassification.HUMAN;
		
		Dna dna = this.repository.findByDnaSequence(dnaDTO.getDna());
		if (Objects.isNull(dna)) {
			dna = new Dna(dnaDTO.getDna(), classification);
		} else {
			dna.setClassification(classification);
		}
		this.save(dna);
		return isMutant;
	}

}
