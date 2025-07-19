package ua.sosna.wortschatz.wortschatzchen.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.sosna.wortschatz.wortschatzchen.domain.Word;

public interface WordRepo extends JpaRepository<Word, Long> {

	Optional<Word> findByUuid(UUID uuid);

	Optional<Word> findOneByUuid(UUID fromString);

	Optional<Word> findByName(String name);
	

}
