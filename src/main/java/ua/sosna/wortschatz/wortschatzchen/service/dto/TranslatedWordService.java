package ua.sosna.wortschatz.wortschatzchen.service.dto;

import java.io.IOException;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import ua.sosna.wortschatz.wortschatzchen.domain.FileEntity;
import ua.sosna.wortschatz.wortschatzchen.domain.Language;
import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatzchen.domain.TranslatedWord;
import ua.sosna.wortschatz.wortschatzchen.dto.BasicTranslateDTO;
import ua.sosna.wortschatz.wortschatzchen.repository.FileRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.LanguageRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.SubtitleFileRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.TranslatedWordRepo;
import ua.sosna.wortschatz.wortschatzchen.service.TranslateService;
import ua.sosna.wortschatz.wortschatzchen.storage.FileSystemStorageService;
import ua.sosna.wortschatz.wortschatzchen.storage.StorageService;
import ua.sosna.wortschatz.wortschatzchen.web.rest.SubtitleFileResource;

@Service
public class TranslatedWordService {

	private final TranslateService translateService;
	private final TranslatedWordRepo baseRepositotory;

	private final LanguageRepo languageRepo;

	private static final Logger LOG = LoggerFactory.getLogger(TranslatedWordService.class);

	private SubtitleFileRepo subtitleFileRepo;
	private StorageService storageService;
	private FileRepo fileRepository;

	public TranslatedWordService(LanguageRepo languageRepo, TranslateService translateService,
			TranslatedWordRepo translatedWordRepo) {

		super();
		this.baseRepositotory = translatedWordRepo;
		this.translateService = translateService;
		this.languageRepo = languageRepo;
	}

	public List<TranslatedWord> findAll() {
		return baseRepositotory.findAll();
	}

	public TranslateService getTranslateService() {
		return this.translateService;
	}

	public Optional<TranslatedWord> findById(Long id) {
		return baseRepositotory.findById(id);
	}

	public TranslatedWord findByUuid(String uuid) throws NotFoundException {
		var uuidTrue = UUID.fromString(uuid);

		return findByUuid(uuidTrue);
	}

	public TranslatedWord findByUuid(UUID uuid) throws NotFoundException {
		// var uuidTrue = UUID.fromString(uuid);

		return baseRepositotory.findByUuid(uuid).orElseThrow(() -> new NotFoundException());
	}

	public TranslatedWord save(TranslatedWord item) {
		return baseRepositotory.save(item);
	}

	public void deleteById(Long id) {
		baseRepositotory.deleteById(id);
	}

	public void deleteByUUID(String uuidInString) throws NotFoundException {
		UUID uuid = UUID.fromString(uuidInString);
		deleteByUUID(uuid);
	}

	public void deleteByUUID(UUID uuid) throws NotFoundException {
		TranslatedWord item = baseRepositotory.findByUuid(uuid).orElseThrow(() -> new NotFoundException());
		baseRepositotory.deleteById(item.getId());
	}

	public TranslatedWord create(TranslatedWord item) {
		return save(item);
	}

	public TranslatedWord update(Long idToUpdate, TranslatedWord newItemData) throws ResponseStatusException {
		if (newItemData.getId() != null & newItemData.getId() > 0 & newItemData.getId() != idToUpdate) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"id in url " + idToUpdate + " not equal to object " + newItemData.getId());

		}
		TranslatedWord item = baseRepositotory.findById(idToUpdate)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"subtitle not found  for id:" + idToUpdate.toString()));
		return update(item, newItemData);

	}

	public TranslatedWord update(String uuidInStringToUpdate, TranslatedWord item) {
		UUID idToUpdate = UUID.fromString(uuidInStringToUpdate);
		if (item.getUuid() != null & item.getUuid() != idToUpdate) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"id in url " + idToUpdate.toString() + " not equal to object: " + item.getUuid().toString());
		}
		var itemEntity = baseRepositotory.findByUuid(idToUpdate)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"translatedWord not found  for uuid:" + idToUpdate.toString()));

		return update(item, itemEntity);

	}

	public TranslatedWord update(TranslatedWord item, TranslatedWord newItemData) throws ResponseStatusException {

		if (newItemData.getNameClass() != null) {
			item.setNameClass(newItemData.getNameClass());
		}
		if (newItemData.getName() != null) {
			item.setName(newItemData.getName());
		}
		if (newItemData.getTranslatedName() != null) {
			item.setTranslatedName(newItemData.getTranslatedName());
		}
		if (newItemData.getBaseLang() != null) {
			item.setBaseLang(newItemData.getBaseLang());
		}
		if (newItemData.getTargetLang() != null) {
			item.setTargetLang(newItemData.getTargetLang());
		}

		return baseRepositotory.save(item);

	}

	public String Translate(Language from, Language to, String text) {
		return Translate(from, to, text, true, UUID.randomUUID(), "");
	}

	public String Translate(String from, String to, String text) throws NotFoundException {

		Language fromEntity = languageRepo.findOneByShortName(from).orElseThrow(() -> new NotFoundException());
		Optional<Language> toEntity = languageRepo.findOneByShortName(to);
		String result = "";
		if (!toEntity.isPresent()) {
			List<Language> langItems = languageRepo.findAll();
			for (int i = 0; i < langItems.size(); i++) {
				Language array_element = langItems.get(i);
				if (array_element.getShortName().equals(from)) {
					continue;
				}
				result = Translate(fromEntity, array_element, text, true, UUID.randomUUID(), "");

			}
		} else {
			result = Translate(fromEntity, toEntity.get(), text, true, UUID.randomUUID(), "");
		}

		return result;
	}

	public String Translate(Language from, Language to, String text, boolean checkInBase, UUID uuidClass,
			String nameClass) {
		if (uuidClass == null) {
			uuidClass = UUID.randomUUID();
		}

		String result = "";
		TranslatedWord currentRow;

		if (checkInBase) {
			var item = baseRepositotory.findFirstByBaseLangAndTargetLangAndName(from, to, text);
			if (item.isPresent()) {
				currentRow = item.get();
				result = currentRow.getTranslatedName();
				if (!currentRow.getUuidClass().equals(uuidClass)) {
					var newItem = new TranslatedWord();
					newItem.setBaseLang(from);
					newItem.setTargetLang(to);
					newItem.setName(text);
					newItem.setTranslatedName(result);
					newItem.setUuidClass(uuidClass);
					
					newItem.setNameClass(nameClass);
					baseRepositotory.save(newItem);
					
				}
				return result;
			}
		}

		try {
			result = translateService.translate(text, from.getShortName(), to.getShortName());
			if (!result.isEmpty()) {
				var item = new TranslatedWord();
				item.setBaseLang(from);
				item.setTargetLang(to);
				item.setName(text);
				item.setTranslatedName(result);
				item.setUuidClass(uuidClass);
				item.setNameClass(nameClass);
				baseRepositotory.save(item);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public List<TranslatedWord> getAllByUuidClass(String uuidClass, String targetLang) {
		List<TranslatedWord> items = new ArrayList<TranslatedWord>();
		UUID uuid = UUID.fromString(uuidClass);
		if (targetLang.isEmpty() | targetLang == null) {
			return getAllByUuidClass(uuid, null);
		}

		var toLangEntity = languageRepo.findOneByShortName(targetLang);
		if (toLangEntity.isPresent()) {
			return getAllByUuidClass(uuid, toLangEntity.get());
		}
		return items;
	}

	public List<TranslatedWord> getAllByUuidClass(UUID uuidClass, Language targetLang) {
		List<TranslatedWord> items = new ArrayList<TranslatedWord>();

		if (targetLang != null) {
			items = baseRepositotory.findAllByUuidClassAndTargetLang(uuidClass, targetLang);
		} else {
			items = baseRepositotory.findAllByUuidClass(uuidClass);
		}
		return items;

	}

	public List<TranslatedWord> getTranslationsByText(String name) {
		var items = baseRepositotory.findAllByName(name);
		return items;
	}

	public String TranslateByUuidClass(String uuidInStringClass, BasicTranslateDTO body) throws NotFoundException {
		Language fromEntity = languageRepo.findOneByShortName(body.getFrom())
				.orElseThrow(() -> new NotFoundException());
		// Language toEntity =
		// languageRepo.findOneByShortName(body.getTo()).orElseThrow(() -> new
		// NotFoundException());

		String result = "";
		UUID uuidClass = UUID.fromString(uuidInStringClass);
		var toLangEntity = languageRepo.findOneByShortName(body.getTo());
		if (toLangEntity.isPresent()) {
			return Translate(fromEntity, toLangEntity.get(), body.getText(), true, uuidClass, uuidInStringClass);
			// return getAllByUuidClass(uuidInStringClass, toLangEntity.get());
		} else {
			var lanItems = languageRepo.findAll();
			for (Iterator iterator = lanItems.iterator(); iterator.hasNext();) {
				Language language = (Language) iterator.next();
				if (language.getShortName() != null & !language.getShortName().isEmpty()
						&& !language.equals(fromEntity)) {
					result = result
							+ Translate(fromEntity, language, body.getText(), true, uuidClass, uuidInStringClass);
				}

			}

		}
		return result;
		// return Translate(fromEntity, toEntity, body.getText(), true, uuidClass,
		// uuidInStringClass );
	}

	public String TranslateByName(BasicTranslateDTO body) throws NotFoundException {
		Language fromEntity = languageRepo.findOneByShortName(body.getFrom())
				.orElseThrow(() -> new NotFoundException());
		Language toEntity = languageRepo.findOneByShortName(body.getTo()).orElseThrow(() -> new NotFoundException());
		return Translate(fromEntity, toEntity, body.getText());
	}

}