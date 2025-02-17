package ua.sosna.wortschatz.wortschatztchen.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.sosna.wortschatz.wortschatztchen.domain.Language;
import ua.sosna.wortschatz.wortschatztchen.domain.TranslatedWord;

public interface TranslatedWordRepo  extends JpaRepository<TranslatedWord, Long>{
	
	Optional<TranslatedWord> findFirstByBaseLangAndTargetLangAndName(Language baseLang, Language  tarLanguage, String name);

	Optional<TranslatedWord> findFirstByUuidClass(UUID uuid);
	List<TranslatedWord> findByUuidClass(UUID uuid);

}
