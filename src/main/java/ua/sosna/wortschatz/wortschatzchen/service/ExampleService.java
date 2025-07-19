package ua.sosna.wortschatz.wortschatzchen.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.Hibernate;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ua.sosna.wortschatz.wortschatzchen.domain.Example;
import ua.sosna.wortschatz.wortschatzchen.domain.Language;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;
import ua.sosna.wortschatz.wortschatzchen.dto.ExampleDTO;
import ua.sosna.wortschatz.wortschatzchen.dto.WordDTO;
import ua.sosna.wortschatz.wortschatzchen.repository.ExamplesRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.LanguageRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.WordRepo;

@Service
public class ExampleService {

	private ExamplesRepo exampleRepo;
	private WordRepo wordRepository;
	private final LanguageRepo languageRepository;

	public ExampleService(ExamplesRepo exampleRepo, WordRepo wordRepository, LanguageRepo languageRepository) {
		this.languageRepository = languageRepository;
		this.exampleRepo = exampleRepo;
		this.wordRepository = wordRepository;

	}

	public ExampleDTO toDto(Example itemDB) {
		if (itemDB == null) {
			return null;
		}
		ExampleDTO itemDTO = new ExampleDTO();
		itemDTO.setId(itemDB.getId());
		itemDTO.setUUID(itemDB.getUuid().toString());
		itemDTO.setName(itemDB.getName());

		itemDTO.setBaseLanguage(itemDB.getBaseLang());
		if (itemDB.getBaseLang() != null) {
			itemDTO.setBaseLanguageUUID(itemDB.getBaseLang().getUuid().toString());
		}

		if (itemDB.getBaseWord() != null) {
			itemDTO.setBaseWord(itemDB.getBaseWord());
			itemDTO.setBaseWordUUID(itemDB.getBaseWord().getUuid().toString());
		}

		return itemDTO;
	}

	public Example toEntity(ExampleDTO itemDTO) {
		Example item;
		item = new Example();
		if (itemDTO == null) {
			return null;
		}
		if (itemDTO.getId() != null) {
			var exampleItem = findById(itemDTO.getId()).get();
			if (exampleItem == null && itemDTO.getUUID() != null) {
				try {
					item = findByUuid(itemDTO.getUUID());
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					item = new Example();
				}
			}
		} else {
			
		}

		item.setId(itemDTO.getId());
		item.setUuid(UUID.fromString(itemDTO.getUUID()));

		if (itemDTO.getBaseLanguageUUID() != null) {
			Optional<Language> language = languageRepository
					.findOneByUuid(UUID.fromString(itemDTO.getBaseLanguageUUID()));
			if (language.isPresent()) {
				item.setBaseLang(language.get());
			}
		} else if (itemDTO.getBaseLanguage() != null) {
			//
			item.setBaseLang(itemDTO.getBaseLanguage());
		}
		if (itemDTO.getBaseWordUUID() != null) {
			Optional<Word> word = wordRepository.findOneByUuid(UUID.fromString(itemDTO.getBaseWordUUID()));
			if (word.isPresent()) {
				item.setBaseWord(word.get());
			}

		} else if (itemDTO.getBaseWord() != null) {
			//
			item.setBaseWord(itemDTO.getBaseWord());
		}
		// word.setUuid(example.getUuid());
		item.setName(itemDTO.getName());

		item.setOrder(itemDTO.getOrder());

		return item;
	}

	public List<ua.sosna.wortschatz.wortschatzchen.domain.Example> findAll() {
		return exampleRepo.findAll();
	}

	public Optional<Example> findById(Long id) {
		return exampleRepo.findById(id);
	}

	public Example findByUuid(UUID uuid) throws NotFoundException {
		return exampleRepo.findByUuid(uuid).orElseThrow(() -> new NotFoundException());
	}

	public Example findByUuid(String uuid) throws NotFoundException {
		var uuidTrue = UUID.fromString(uuid);

		return exampleRepo.findByUuid(uuidTrue).orElseThrow(() -> new NotFoundException());
	}

	public Example save(Example newItem) {
		return exampleRepo.save(newItem);
	}

	public void deleteById(Long id) {
		exampleRepo.deleteById(id);
	}

	public void deleteByUUID(String uuidInString) throws NotFoundException {
		UUID uuid = UUID.fromString(uuidInString);
		deleteByUUID(uuid);
	}

	public void deleteByUUID(UUID uuid) throws NotFoundException {
		Example item = exampleRepo.findByUuid(uuid).orElseThrow(() -> new NotFoundException());
		exampleRepo.deleteById(item.getId());
	}

	public ExampleDTO create(ExampleDTO item) {
		Example newItem = save(toEntity(item));

		return toDto(newItem);
	}

	public Example update(Long idToUpdate, Example example) throws ResponseStatusException {
		if (example.getId() != null & example.getId() > 0 & example.getId() != idToUpdate) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"id in url " + idToUpdate + " not equal to object " + example.getId());

		}
		Example item = exampleRepo.findById(idToUpdate)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"subtitle not found  for id:" + idToUpdate.toString()));
		return update(item, example);

	}

	public Example update(String uuidInStringToUpdate, Example example) {
		UUID idToUpdate = UUID.fromString(uuidInStringToUpdate);
		if (example.getUuid() != null & example.getUuid() != idToUpdate) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"id in url " + idToUpdate.toString() + " not equal to object: " + example.getUuid().toString());
		}
		var item = exampleRepo.findByUuid(idToUpdate)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"subtitleFile not found  for uuid:" + idToUpdate.toString()));

		return update(item, example);

	}

	public Example update(Example item, Example example) throws ResponseStatusException {

		if (example.getBaseLang() != null) {
			item.setBaseLang(example.getBaseLang());
		}
		if (example.getName() != null) {
			item.setName(example.getName());
		}
		if (example.getOrder() != null) {
			item.setOrder(example.getOrder());
		}
		if (example.getBaseWord() != null) {
			item.setBaseWord(example.getBaseWord());
		}
		return exampleRepo.save(item);

	}

	public List<Example> findAllByWord(Word word) {
		if (word == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Word cannot be null");
		}
		if (word.getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Word ID cannot be null");
		}

		return exampleRepo.findAllByBaseWord(word);
	}

	public ExampleDTO entityToDTO(Example item) {

		return toDto(item);

	}
}
