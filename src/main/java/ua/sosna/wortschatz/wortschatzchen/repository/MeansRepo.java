package ua.sosna.wortschatz.wortschatzchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ua.sosna.wortschatz.wortschatzchen.domain.Means;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;

public interface MeansRepo extends JpaRepository<Means, Long> {
	List<Means> findAllByBaseWord(Word baseWord);

	Optional<Means> findByUuid(UUID uuid);

	List<Means> findAllByUuid(UUID wordUUID);

}
