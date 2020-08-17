package emanuelrichieri.mutantdetector.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import emanuelrichieri.mutantdetector.service.IMutantDetectorService;
import emanuelrichieri.mutantdetector.util.exception.InvalidDnaException;
import emanuelrichieri.mutantdetector.util.suffixtree.GeneralizedSuffixTree;

@Service("mutantDetectorService")
public class MutantDetectorService implements IMutantDetectorService {

	private Logger logger = LoggerFactory.getLogger("MutantDetectorService");
	
	public final static String ALLOWED_CHARACTERS = "ACGT";
	
	public final static Integer VALID_SEQUENCE_LENGTH = 4;
	
	public final static Integer MATCHING_SEQUENCES_NEEDED = 2;
	
    public static final String[] MUTANT_DNA_SEQUENCES = {"AAAA", "CCCC", "GGGG", "TTTT"};
	
	@Override
	public Boolean isMutant(String[] dna) throws InvalidDnaException {
		try {
			return this.isMutant(getSequences(dna));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}
	
	/**
	 * Uses {@link GeneralizedSuffixTree} to analyze if the given 
	 * list of sequences belongs to a mutant, searching for at least MATCHING_SEQUENCES_NEEDED
	 * matches for the MUTANT_DNA_SEQUENCES.
	 * @param sequences
	 * @return <code>true</code> if the list of sequences belongs to a mutant, <code>false</code> otherwise.
	 */
	private Boolean isMutant(List<String> sequences) throws InvalidDnaException {
		int matchesCount = 0;
		
		GeneralizedSuffixTree suffixTree = new GeneralizedSuffixTree();
		int i = 0;
		for (String sequence : sequences) {
			suffixTree.insert(sequence, i);
			i++;
		}
		for(String mutantSequence : MUTANT_DNA_SEQUENCES) {
			matchesCount += suffixTree.search(mutantSequence, MATCHING_SEQUENCES_NEEDED).size();
			if (matchesCount >= MATCHING_SEQUENCES_NEEDED) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get string sequences (vertical, horizontal, oblique) from DNA. 
	 * @param dna
	 * @throws InvalidDnaException if DNA sequence size is invalid or contains not allowed characters.
	 */
	private List<String> getSequences(String[] dna) throws InvalidDnaException {
		if (Objects.isNull(dna)) {
			throw new InvalidDnaException("Dna cannot be null.");
		}
		if (dna.length < VALID_SEQUENCE_LENGTH) {
			throw new InvalidDnaException("Invalid DNA size. Must be at least " + VALID_SEQUENCE_LENGTH + "x" + VALID_SEQUENCE_LENGTH);
		}
		
		List<String> sequences = new ArrayList<String>();
        
        Pattern pattern = Pattern.compile("[^" + ALLOWED_CHARACTERS + "]");
        
        for(int row = 0; row < dna.length; row++) {
            StringBuffer columnSequence = new StringBuffer(dna.length);
            StringBuffer ltrDiagSequence1 = new StringBuffer(dna.length);
            StringBuffer ltrDiagSequence2 = new StringBuffer(dna.length);
            		
            for (int column = 0; column < dna.length; column++) {
                columnSequence.append(dna[column].charAt(row));
                
                if (row <= dna.length - VALID_SEQUENCE_LENGTH && column < dna.length - row) {
            		ltrDiagSequence1.append(dna[column].charAt(column+row));
                    if(row > 0) {
                        ltrDiagSequence2.append(dna[row + column].charAt(column));
                    }
                }
            }
            String rowSequence = dna[row];
        	if (rowSequence.length() != dna.length) {
        		throw new InvalidDnaException("Invalid DNA. Sequence matrix must be NxN");
        	}
        	if (pattern.matcher(rowSequence).find()) {
        		throw new InvalidDnaException("Invalid character in the given sequence ");
        	}
            
            sequences.add(rowSequence);
            sequences.add(columnSequence.toString());
            if (ltrDiagSequence1.length() > 0) {
            	sequences.add(ltrDiagSequence1.toString());
            }
            if (ltrDiagSequence2.length() > 0) {
            	sequences.add(ltrDiagSequence2.toString());
            }
        }
        
        return sequences;
    }

}
