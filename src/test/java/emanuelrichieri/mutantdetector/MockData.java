package emanuelrichieri.mutantdetector;

import java.util.Arrays;
import java.util.List;

import emanuelrichieri.mutantdetector.domain.entities.Dna;
import emanuelrichieri.mutantdetector.domain.entities.Dna.DnaClassification;

public class MockData {
	
	public static final String[] HUMAN_DNA_SEQUENCE = {"ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"};
	public static final String[] MUTANT_DNA_SEQUENCE = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
	public static final String[] MUTANT_DNA_SEQUENCE_ALL_EQUAL = {"AAAA","CCCC","TTTT","GGGG"};
	
	public static final String HUMAN_DNA_SEQUENCE_STRING = "ATGCGA,CAGTGC,TTATTT,AGACGG,GCGTCA,TCACTG";
	
	public static final Dna HUMAN_DNA = new Dna(MockData.HUMAN_DNA_SEQUENCE, DnaClassification.HUMAN);	
	public static final Dna MUTANT_DNA = new Dna(MockData.MUTANT_DNA_SEQUENCE, DnaClassification.MUTANT);		

	
	public static final List<Dna> HUMAN_DNA_LIST = Arrays.asList(HUMAN_DNA, HUMAN_DNA, HUMAN_DNA);
	public static final List<Dna> MUTANT_DNA_LIST = Arrays.asList(MUTANT_DNA, MUTANT_DNA);
}
