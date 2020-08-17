package emanuelrichieri.mutantdetector.io;

public class DnaStatsDTO {

	private Long count_mutant_dna;
	private Long count_human_dna;
	private Double ratio;
	
	public DnaStatsDTO() { }

	public DnaStatsDTO(Long countHumanDna, Long countMutantDna, Double ratio) {
		this.setCount_human_dna(countHumanDna);
		this.setCount_mutant_dna(countMutantDna);
		this.setRatio(ratio);
	}
	
	public Long getCount_mutant_dna() {
		return count_mutant_dna;
	}
	public Long getCount_human_dna() {
		return count_human_dna;
	}
	public Double getRatio() {
		return ratio;
	}
	public void setCount_mutant_dna(Long count_mutant_dna) {
		this.count_mutant_dna = count_mutant_dna;
	}
	public void setCount_human_dna(Long count_human_dna) {
		this.count_human_dna = count_human_dna;
	}
	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}
}
