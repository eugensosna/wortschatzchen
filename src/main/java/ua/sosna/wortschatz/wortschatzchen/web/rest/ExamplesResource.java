package ua.sosna.wortschatz.wortschatzchen.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.sosna.wortschatz.wortschatzchen.domain.Example;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;
import ua.sosna.wortschatz.wortschatzchen.dto.ExampleDTO;
import ua.sosna.wortschatz.wortschatzchen.service.ExampleService;
import ua.sosna.wortschatz.wortschatzchen.service.WordService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/examples")
@CrossOrigin
public class ExamplesResource {
	private static final String ENTITY_NAME = "Example";
	private static final Logger LOG = LoggerFactory.getLogger(ExamplesResource.class);
	private WordService wordService;

	private ExampleService itemsService;

	public ExamplesResource(ExampleService itemsService, WordService wordService) {
		super();
		this.itemsService = itemsService;
		this.wordService = wordService;
	}

	@GetMapping("/")
	public List<ExampleDTO> getAllItems() {
		return itemsService.findAll().stream()
				.map(itemsService::toDto)
				.toList();
	}

	@GetMapping("/by-word")
	private ResponseEntity<List<ExampleDTO>> getAllByWord(@RequestParam(value = "wordId", required = false) Long wordId,
			@RequestParam(value = "wordUUID", required = false) String wordUUIdInString) {
		if (wordId == null && wordUUIdInString == null) {
			return ResponseEntity.badRequest().build();
		}
		Word word = null;
		if (wordId != null) {
			try {
				word = wordService.findById(wordId).get();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
				return ResponseEntity.notFound().build();
			}
		}
		if (wordUUIdInString != null) {
			try {
				word = wordService.findByUuid(wordUUIdInString);
			} catch (NotFoundException e) {
				e.printStackTrace();
				return ResponseEntity.notFound().build();
			}
		}
		return ResponseEntity.ok(itemsService.findAllByWord(word).stream()
				.map(itemsService::toDto)
				.toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ExampleDTO> getExampleById(@PathVariable(value = "id", required = true) Long id) {
		Optional<Example> item = itemsService.findById(id);
		if (item.isPresent()) {
			return ResponseEntity.ok(itemsService.toDto(item.get()));
		} else {
			return  ResponseEntity.notFound().build();
		}
		
	}

	@GetMapping("/uuid/{uuid}")
	public ResponseEntity<ExampleDTO> getExampleByUuid(@PathVariable(value = "uuid", required = true) String uuid)
			throws NotFoundException {
		Example item = itemsService.findByUuid(uuid);
		return item != null ? ResponseEntity.ok(itemsService.toDto(item))
				: ResponseEntity.notFound().build();
	}

	@PostMapping("/")
	public ResponseEntity<ExampleDTO> createExample(@RequestBody ExampleDTO item) {
		var result = itemsService.create(item);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ExampleDTO> updateExample(@PathVariable(value = "id") Long id,
			@RequestBody ExampleDTO newItem) {
		if (newItem.getId() == null) {
			return ResponseEntity.badRequest().build();
		}
		Example result = itemsService.update(newItem.getId(), itemsService.toEntity(newItem));
		return ResponseEntity.ok(itemsService.entityToDTO(result));
	}

	@PutMapping("/uuid/{uuid}")
	public ResponseEntity<ExampleDTO> updateExampleByUUID(@PathVariable(value = "uuid") String uuidInString,
			@RequestBody ExampleDTO newItem) {

		if (newItem.getId() == null) {
			return ResponseEntity.badRequest().build();
		}

		Example result = itemsService.update(uuidInString, itemsService.toEntity(newItem));
		return ResponseEntity.ok(
				itemsService.entityToDTO(result));
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

	private <R> R toEntity(Example example1) {
		return null;
	}

}
