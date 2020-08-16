package emanuelrichieri.mutantdetector.domain.entities;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import emanuelrichieri.mutantdetector.util.converter.StringArrayConverter;

@Entity
@Table(indexes = {@Index(name = "dna_index", columnList = "dnaSequence", unique = true)})
public class Dna {
	
	public Dna() { } 
	
	public Dna(String[] dnaSequence, DnaClassification classification) {
		this.dnaSequence = dnaSequence;
		this.classification = classification;
	}
	
	public enum DnaClassification {
		HUMAN,
		MUTANT
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
	private Long id;
	
    
	@Convert(converter = StringArrayConverter.class)
	@Column(length = 8000)
	private String[] dnaSequence;
	
	@Enumerated(EnumType.STRING)
	private DnaClassification classification;

	public Long getId() {
		return id;
	}

	public String[] getDnaSequence() {
		return dnaSequence;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDnaSequence(String[] dnaSequence) {
		this.dnaSequence = dnaSequence;
	}

	public DnaClassification getClassification() {
		return classification;
	}

	public void setClassification(DnaClassification classification) {
		this.classification = classification;
	}
}
