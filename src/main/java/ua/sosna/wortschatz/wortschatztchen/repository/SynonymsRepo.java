package ua.sosna.wortschatz.wortschatztchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import ua.sosna.wortschatz.wortschatztchen.domain.Synonyms;
import ua.sosna.wortschatz.wortschatztchen.domain.Word;

public interface SynonymsRepo extends JpaRepository<Synonyms, Long> {
	List<Synonyms> findAllByBaseWord(Word baseWord);

}
