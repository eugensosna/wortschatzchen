package ua.sosna.wortschatz.wortschatzchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ua.sosna.wortschatz.wortschatzchen.domain.Synonyms;
import ua.sosna.wortschatz.wortschatzchen.domain.TimestampRow;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;

public interface SynonymsRepo extends JpaRepository<Synonyms, Long> {
	List<Synonyms> findAllByBaseWord(Word baseWord);
	Optional<Synonyms> findByUuid(UUID uuid);

}
