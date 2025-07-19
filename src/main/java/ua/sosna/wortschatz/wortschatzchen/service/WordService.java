package ua.sosna.wortschatz.wortschatzchen.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Component;

import ua.sosna.wortschatz.wortschatzchen.domain.Example;
import ua.sosna.wortschatz.wortschatzchen.domain.Language;
import ua.sosna.wortschatz.wortschatzchen.domain.Synonyms;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;
import ua.sosna.wortschatz.wortschatzchen.dto.ExampleDTO;
import ua.sosna.wortschatz.wortschatzchen.dto.WordDTO;
import ua.sosna.wortschatz.wortschatzchen.repository.LanguageRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.SynonymsRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.WordRepo;

@Component
public class WordService {

	private static final Logger LOG = LoggerFactory.getLogger(WordService.class);

	private final WordRepo wordRepository;
	private final LanguageRepo languageRepository;
	private final SynonymsRepo synonymsRepo;
	private final ExampleService exampleService;

	public WordService(WordRepo wordRepository, LanguageRepo languageRepository, SynonymsRepo synonymsRepo, ExampleService exampleService) {
		this.wordRepository = wordRepository;
		this.languageRepository = languageRepository;
		this.synonymsRepo = synonymsRepo;
		this.exampleService = exampleService;
	}

	public WordDTO toDto(Word word) {
		if (word == null) {
			return null;
		}
		WordDTO wordDTO = new WordDTO();
		wordDTO.setId(word.getId());
		wordDTO.setUuid(word.getUuid().toString());
		wordDTO.setName(word.getName());
		wordDTO.setImportant(word.getImportant());
		wordDTO.setMean(word.getMean());
		wordDTO.setBaseForm(word.getBaseForm());
		wordDTO.setKindOfWord(word.getKindOfWord());
		wordDTO.setBaseLanguage(word.getBaseLang());
		if (word.getBaseLang() != null) {
			wordDTO.setLanguageUUID(word.getBaseLang().getUuid().toString());
		}
		if (!word.getSynonyms().isEmpty()) {
			Set<String> synonymsUUID = new HashSet<>();
			Set<Synonyms> synonyms = new HashSet<>();
			for (Synonyms synoymItem : word.getSynonyms()) {
				synonymsUUID.add(synoymItem.getUuid().toString());
				
				synonyms.add(synoymItem);

			}
		}
		if (!word.getExamples().isEmpty()) {
			Set<String> examplesUUID = new HashSet<String>();
			Set<ExampleDTO> examples = new HashSet<ExampleDTO>();
			for (Example example : word.getExamples()) {
				examplesUUID.add(example.getUuid().toString());
				examples.add(exampleService.toDto(example));
			}
			wordDTO.setExamples(examples);
			wordDTO.setExamplesUUID(examplesUUID);
		}
		
		return wordDTO;

	}

	public Word toEntity(WordDTO itemDTO) {
		Word entity = null;
		if (itemDTO == null) {
			return null;
		}
		if (itemDTO.getId() != null) {

			entity = wordRepository.findById(itemDTO.getId()).get();
			entity.setId(itemDTO.getId());
			entity.setUuid(UUID.fromString(itemDTO.getUuid()));
		}
		if (itemDTO.getUuid() != null) {
			entity = wordRepository.findByUuid(UUID.fromString(itemDTO.getUuid())).get();
		}

		if (entity == null) {
			entity = new Word();
			entity.setId(itemDTO.getId());
			entity.setUuid(UUID.fromString(itemDTO.getUuid()));
		}

		if (itemDTO.getLanguageUUID() != null) {
			Optional<Language> language = languageRepository.findOneByUuid(UUID.fromString(itemDTO.getLanguageUUID()));
			if (language.isPresent()) {
				itemDTO.setBaseLanguage(language.get());
			}
		}
		entity.setName(itemDTO.getName());
		entity.setImportant(itemDTO.getImportant());
		entity.setMean(itemDTO.getMean());
		entity.setBaseForm(itemDTO.getBaseForm());
		entity.setKindOfWord(itemDTO.getKindOfWord());
		entity.setBaseLang(itemDTO.getBaseLanguage());
		var examplesDTO = itemDTO.getExamples();
		var examplesUUID = itemDTO.getExamplesUUID();
		if (!examplesDTO.isEmpty()) {
			Set<Example> examples = new HashSet<Example>();
			for (ExampleDTO example : examplesDTO) {
				var exampleDB = exampleService.toEntity(example);
				examples.add(exampleDB);
				
			}
			entity.setExamples(examples);
		} else if (!examplesUUID.isEmpty()) {
			Set<Example> examples = new HashSet<Example>();
			for (String example : examplesUUID) {
				
				Example exampleDB;
				try {
					exampleDB = exampleService.findByUuid(example);
					examples.add(exampleDB);
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			entity.setExamples(examples);
		}
			
		
//    	word.setLanguageUUID(wordDTO.getLanguage().getUuid());
//    	word.setVersion(wordDTO.getVersion());
		return entity;
	}

	/**
	 * Save a word.
	 *
	 * @param wordDTO the entity to save.
	 * @return the persisted entity.
	 */
	public WordDTO save(WordDTO wordDTO) {
		LOG.debug("Request to save Word : {}", wordDTO);
		Word word = toEntity(wordDTO);
		word = wordRepository.save(word);
		return toDto(word);
	}

	/**
	 * Update a word.
	 *
	 * @param wordDTO the entity to save.
	 * @return the persisted entity.
	 */
	public WordDTO update(WordDTO wordDTO) {
		LOG.debug("Request to update Word : {}", wordDTO);
		Word word = toEntity(wordDTO);
		word = wordRepository.save(word);
		return toDto(word);
	}

	/**
	 * Partially update a word.
	 *
	 * @param wordDTO the entity to update partially.
	 * @return the persisted entity.
	 */
//	public Optional<WordDTO> partialUpdate(WordDTO wordDTO) {
//		LOG.debug("Request to partially update Word : {}", wordDTO);
//
//		return wordRepository.findById(wordDTO.getId()).map(existingWord -> {
//			partialUpdate(existingWord, wordDTO);
//
//			return existingWord;
//		}).map(save).map(toDto);
//	}

	/**
	 * Get all the words.
	 *
	 * @param pageable the pagination information.
	 * @return the list of entities.
	 */
//	@Transactional(readOnly = true)
//	public List<WordDTO> findAll(Pageable pageable) {
//		LOG.debug("Request to get all Words");
//		return wordRepository.findAll().map(wordMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
//	}
//
	/**
	 * Get one word by id.
	 *
	 * @param id the id of the entity.
	 * @return the entity.
	 */
//	@Transactional(readOnly = true)
//	public Optional<WordDTO> findOne(Long id) {
//		LOG.debug("Request to get Word : {}", id);
//		return wordRepository.findById(id).map(wordMapper::toDto);
//	}

	/**
	 * Delete the word by id.
	 *
	 * @param id the id of the entity.
	 */
	public void delete(Long id) {
		LOG.debug("Request to delete Word : {}", id);
		wordRepository.deleteById(id);
	}
	
	public Optional<Word> findById(Long id) {
		return this.wordRepository.findById(id);
	}

	public Word findByUuid(UUID uuid) throws NotFoundException {
		return wordRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException());
	}

	public Word findByUuid(String uuid) throws NotFoundException {
		var uuidTrue = UUID.fromString(uuid);

		return wordRepository.findByUuid(uuidTrue).orElseThrow(() -> new NotFoundException());
	}

}
