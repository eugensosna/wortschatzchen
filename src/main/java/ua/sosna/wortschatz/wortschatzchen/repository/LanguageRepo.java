package ua.sosna.wortschatz.wortschatzchen.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.sosna.wortschatz.wortschatzchen.domain.Language;

public interface LanguageRepo extends JpaRepository<Language, Long> {

	List<Language> findAllByUuid(UUID wordUUID);

	Optional<Language> findByUuid(UUID uuid);

	Optional<Language> findOneByUuid(UUID fromString);

	Optional<Language>  findOneByShortName(String from);

}
