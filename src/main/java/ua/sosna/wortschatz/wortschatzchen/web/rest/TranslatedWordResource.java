package ua.sosna.wortschatz.wortschatzchen.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ua.sosna.wortschatz.wortschatzchen.domain.Example;
import ua.sosna.wortschatz.wortschatzchen.domain.FileEntity;
import ua.sosna.wortschatz.wortschatzchen.domain.Language;
import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatzchen.domain.TranslatedWord;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;
import ua.sosna.wortschatz.wortschatzchen.dto.BasicTranslateDTO;
import ua.sosna.wortschatz.wortschatzchen.service.FileService;
import ua.sosna.wortschatz.wortschatzchen.service.dto.SubtitleFileService;
import ua.sosna.wortschatz.wortschatzchen.service.dto.TranslatedWordService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/translatedwords")
@CrossOrigin
public class TranslatedWordResource {
	private static final String ENTITY_NAME = "TranslatedWords";
	private static final Logger LOG = LoggerFactory.getLogger(TranslatedWordResource.class);
	// private SubtitleFileService subtitelFilesService;

	private TranslatedWordService itemsService;

	public TranslatedWordResource(TranslatedWordService itemsService) {
		super();
		this.itemsService = itemsService;
	}

	@GetMapping("/")
	public List<TranslatedWord> getAllItems() {
		return itemsService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<TranslatedWord> getItemById(@PathVariable(value = "id", required = true) Long id) {
		Optional<TranslatedWord> item = itemsService.findById(id);
		return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	
	@GetMapping("/uuid/{uuid}")
	public ResponseEntity<TranslatedWord> getItemByUuid(@PathVariable(value = "uuid", required = true) String uuid)
			throws NotFoundException {
		TranslatedWord item = itemsService.findByUuid(uuid);
		return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
	}

	@PostMapping("/")
	public ResponseEntity<TranslatedWord> createNewItem(@RequestBody TranslatedWord item) {
		item.setId(null);
		TranslatedWord result = itemsService.create(item);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TranslatedWord> updateItem(@PathVariable(value = "id") Long id,
			@RequestBody TranslatedWord newItem) {
		if (newItem.getId() == null) {
			return ResponseEntity.badRequest().build();
		}
		TranslatedWord result = itemsService.update(newItem.getId(), newItem);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/uuid/{uuid}")
	public ResponseEntity<TranslatedWord> updateExampleByUUID(@PathVariable(value = "uuid") String uuidInString,
			@RequestBody TranslatedWord newItem) {

		if (newItem.getId() == null) {
			return ResponseEntity.badRequest().build();
		}

		TranslatedWord result = itemsService.update(uuidInString, newItem);
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteExample(@PathVariable(value = "id", required = true) Long id) {
		itemsService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/uuid/{uuid}")
	public ResponseEntity<Void> deleteExampleByUuuid(@PathVariable(value = "uuid", required = true) String id)
			throws NotFoundException {

		itemsService.deleteByUUID(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/translate")
	public String translateString(@RequestParam("from") String from, @RequestParam("to") String toLanguage,
			@RequestParam("text") String text) throws NotFoundException {
		String result = "";

		result = itemsService.Translate(from, toLanguage, text);
		return result;
	}

	@GetMapping("/by-uuidClass/")
	public ResponseEntity<List<TranslatedWord>> getItemsByClassUuid(
			@RequestParam(name = "uuidClass", defaultValue = "", required = true) String uuidClass,
			@RequestParam(name = "targetLang", defaultValue = "", required = false) String targetLang)
			throws NotFoundException {
		return ResponseEntity.ok(itemsService.getAllByUuidClass(uuidClass, targetLang));

	}
	
	

	@PostMapping("/by-uuidClass/translate")
	public ResponseEntity <List<TranslatedWord>> translateByUuidClass(
			@RequestParam(name = "uuidClass", defaultValue = "", required = true) String uuidClass,
			@RequestBody BasicTranslateDTO body) throws NotFoundException {
		String result = "";
		result = itemsService.TranslateByUuidClass(uuidClass, body);
		StringBuilder stringBuilder = null;
		List<TranslatedWord> resultItems = itemsService.getAllByUuidClass(uuidClass, body.getTo());
		
		
		return ResponseEntity.ok(resultItems);
	}

	@GetMapping("/by-name/")
	public ResponseEntity<List<TranslatedWord>> getItemsByName(
			@RequestParam(name = "name", defaultValue = "", required = true) String name,
			@RequestParam(name = "targetLang", defaultValue = "", required = false) String targetLang)
			throws NotFoundException {
		return ResponseEntity.ok(itemsService.getTranslationsByText(name));

	}

	@PostMapping("/by-name/translate")
	public String translateByUuidClass(@RequestBody BasicTranslateDTO body) throws NotFoundException {
		String result = "";
		result = itemsService.TranslateByName(body);
		return result;
	}

}
