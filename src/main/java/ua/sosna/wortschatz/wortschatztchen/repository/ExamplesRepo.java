package ua.sosna.wortschatz.wortschatztchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import ua.sosna.wortschatz.wortschatztchen.domain.Example;
import ua.sosna.wortschatz.wortschatztchen.domain.Word;

public interface ExamplesRepo extends JpaRepository<Example, Long> {
	List<Example> findAllByBaseWord(Word baseWord);

}
