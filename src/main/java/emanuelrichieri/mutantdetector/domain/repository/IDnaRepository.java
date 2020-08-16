package emanuelrichieri.mutantdetector.domain.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import emanuelrichieri.mutantdetector.domain.entities.Dna;
import emanuelrichieri.mutantdetector.domain.entities.Dna.DnaClassification;

@Repository("dnaRepository")
public interface IDnaRepository extends CrudRepository<Dna, Long>  {

	public Dna findByDnaSequence(String[] dnaSequence);
	
	public List<Dna> findByClassification(DnaClassification classification);
}
