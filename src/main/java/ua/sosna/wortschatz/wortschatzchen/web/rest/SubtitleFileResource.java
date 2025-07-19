package ua.sosna.wortschatz.wortschatzchen.web.rest;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatzchen.service.dto.SubtitleFileService;

@RestController
@RequestMapping("/api/subtitle-files")
@CrossOrigin
public class SubtitleFileResource {
	private static final String ENTITY_NAME = "subtitleFile";
	private static final Logger LOG = LoggerFactory.getLogger(SubtitleFileResource.class);

	private SubtitleFileService itemsService;
	public SubtitleFileResource(SubtitleFileService itemsService) {
		super();
		this.itemsService = itemsService;
	}

	@GetMapping("/")
	public List<SubtitleFile> getAllSubtitleFiles() {
		return itemsService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<SubtitleFile> getSubtitleFileById(@PathVariable(value="id", required = true) Long id) {
		Optional<SubtitleFile> item = itemsService.findById(id);
		return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/by-uuid/{uuid}")
	public ResponseEntity<SubtitleFile> getSubtitleFileByUuid(@PathVariable(value="uuid", required = true) String uuid) throws NotFoundException {
		SubtitleFile item = itemsService.findByUuid(uuid);
		return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
	}

	@PostMapping("/")
	public ResponseEntity<SubtitleFile> createSubtitleFile(@RequestBody SubtitleFile item) {
		SubtitleFile result = itemsService.create(item);
		return ResponseEntity.ok(result);
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<SubtitleFile> updateSubtitleFile(@PathVariable(value = "id") Long id,
			@RequestBody SubtitleFile  newItem) {
		if (newItem.getId() == null) {
			return ResponseEntity.badRequest().build();
		}
		SubtitleFile result = itemsService.update(newItem.getId(),  newItem);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/by-uuid")
	public ResponseEntity<SubtitleFile> updateSubtitleFileByUUID(
			@RequestParam(value = "uuid", required = true) String uuidInString,
			@RequestBody SubtitleFile  newItem) {

		if (newItem.getId() == null) {
			return ResponseEntity.badRequest().build();
		}
		
		
		SubtitleFile result = itemsService.update(uuidInString,  newItem);
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSubtitleFile(@PathVariable(value="id", required = true) Long id) {
		itemsService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/by-uuid")
	public ResponseEntity<Void> deleteSubtitleFileByUuuid(@RequestParam(value = "uuid", required = true) String uuidInString) throws NotFoundException {

		itemsService.deleteByUUID(uuidInString);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/parserows/{id}")
	public String parseRowsFromFile(@PathVariable(value="id", required = true) Long id) throws Throwable {
		itemsService.ParseRowsFromFile(id);
		return "";
	}
	
	@DeleteMapping("/delete-all-rows/{id}")
	public String deleteAllRows(@PathVariable(value="id", required = true) Long id) {
		itemsService.deleteAllTimestampRows(id);
		return "ok";
	}

}
