package ua.sosna.wortschatz.wortschatztchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import ua.sosna.wortschatz.wortschatztchen.domain.Means;
import ua.sosna.wortschatz.wortschatztchen.domain.Word;

public interface MeansRepo extends JpaRepository<Means, Long> {
	List<Means> findAllByBaseWord(Word baseWord);

}
