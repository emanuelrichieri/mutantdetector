package emanuelrichieri.mutantdetector.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import emanuelrichieri.mutantdetector.MockData;
import emanuelrichieri.mutantdetector.domain.entities.Dna.DnaClassification;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DnaTest {
	
	@Test
	void getters() {
		Dna dna = MockData.HUMAN_DNA;
		Random rand = new Random();
		dna.setId(rand.nextLong());
		assertThat(dna.getDnaSequence()).isEqualTo(MockData.HUMAN_DNA_SEQUENCE);
		assertThat(dna.getClassification()).isEqualTo(DnaClassification.HUMAN);
		assertThat(dna.getId()).isNotNull();
		assertThat(dna.getId()).isInstanceOf(Long.class);
	}
	
	@Test
	void setters() {
		Dna dna = new Dna();
		dna.setDnaSequence(MockData.HUMAN_DNA_SEQUENCE);		
		assertThat(dna.getDnaSequence()).isEqualTo(MockData.HUMAN_DNA_SEQUENCE);
	}
	
	@Test
	void dnaClassificationValues() {
		DnaClassification humanClassification = DnaClassification.valueOf("HUMAN");
		DnaClassification mutantClassification = DnaClassification.valueOf("MUTANT");
		
		assertThat(humanClassification).isEqualTo(DnaClassification.HUMAN);
		assertThat(mutantClassification).isEqualTo(DnaClassification.MUTANT);
	}

}
