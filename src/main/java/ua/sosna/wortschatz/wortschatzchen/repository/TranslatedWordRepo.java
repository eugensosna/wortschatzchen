package ua.sosna.wortschatz.wortschatzchen.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.sosna.wortschatz.wortschatzchen.domain.Language;
import ua.sosna.wortschatz.wortschatzchen.domain.TimestampRow;
import ua.sosna.wortschatz.wortschatzchen.domain.TranslatedWord;

public interface TranslatedWordRepo  extends JpaRepository<TranslatedWord, Long>{
	
	Optional<TranslatedWord> findFirstByBaseLangAndTargetLangAndName(Language baseLang, Language  tarLanguage, String name);

	Optional<TranslatedWord> findFirstByUuidClass(UUID uuid);
	List<TranslatedWord> findByUuidClass(UUID uuid);
	
	Optional<TranslatedWord> findByUuid(UUID uuid);

	List<TranslatedWord> findAllByNameAndTargetLang(String name, Language targetLang);
	List<TranslatedWord> findAllByName(String name);
	List<TranslatedWord> findAllByUuidClass(UUID uuidClass );
	List<TranslatedWord> findAllByUuidClassAndTargetLang(UUID uuidClass, Language targetLang );
	
	
	

}
