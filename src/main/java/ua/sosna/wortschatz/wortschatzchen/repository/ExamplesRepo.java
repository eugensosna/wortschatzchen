package ua.sosna.wortschatz.wortschatzchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ua.sosna.wortschatz.wortschatzchen.domain.Example;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;

public interface ExamplesRepo extends JpaRepository<Example, Long> {
	List<Example> findAllByBaseWord(Word baseWord);
	Optional<Example> findByUuid(UUID uuidFromString);

}
