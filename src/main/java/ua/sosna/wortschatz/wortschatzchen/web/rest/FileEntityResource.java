package ua.sosna.wortschatz.wortschatzchen.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ua.sosna.wortschatz.wortschatzchen.domain.FileEntity;
import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;
import ua.sosna.wortschatz.wortschatzchen.service.FileService;
import ua.sosna.wortschatz.wortschatzchen.service.dto.SubtitleFileService;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@CrossOrigin
public class FileEntityResource {
	private static final String ENTITY_NAME = "Example";
	private static final Logger LOG = LoggerFactory.getLogger(FileEntityResource.class);
	private SubtitleFileService subtitelFilesService;

	private FileService itemsService;

	public FileEntityResource(FileService itemsService, SubtitleFileService subtitelFilesService) {
		super();
		this.itemsService = itemsService;
		this.subtitelFilesService = subtitelFilesService;
	}

	@GetMapping("/")
	public List <FileEntity> getAllItems() {
		return itemsService.findAll();
	}
	@GetMapping("/{id}")
	public ResponseEntity <FileEntity> getItemById(@PathVariable(value = "id", required = true) Long id) {
		Optional <FileEntity> item = itemsService.findById(id);
		return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping("/uuid/{uuid}")
	public ResponseEntity <FileEntity> getItemByUuid(@PathVariable(value = "uuid", required = true) String uuid)
			throws NotFoundException {
		FileEntity item = itemsService.findByUuid(uuid);
		return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
	}

	@PostMapping("/")
	public ResponseEntity <FileEntity> createNewItem(@RequestBody FileEntity item) {
		FileEntity result = itemsService.create(item);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/{id}")
	public ResponseEntity <FileEntity> updateItem(@PathVariable(value = "id") Long id,
			@RequestBody FileEntity newItem) {
		if (newItem.getId() == null) {
			return ResponseEntity.badRequest().build();
		}
		FileEntity result = itemsService.update(newItem.getId(), newItem);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/uuid/{uuid}")
	public ResponseEntity <FileEntity> updateItemByUUID(@PathVariable(value = "uuid") String uuidInString,
			@RequestBody FileEntity newItem) {

		if (newItem.getId() == null) {
			return ResponseEntity.badRequest().build();
		}

		FileEntity result = itemsService.update(uuidInString, newItem);
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteItem(@PathVariable(value = "id", required = true) Long id) {
		itemsService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/uuid/{uuid}")
	public ResponseEntity<Void> deleteItemByUuuid(@PathVariable(value = "uuid", required = true) String id)
			throws NotFoundException {

		itemsService.deleteByUUID(id);
		return ResponseEntity.noContent().build();
	}


	@PostMapping("/upload/subtitle/{id}")
	public ResponseEntity<FileEntity> uploadFile(@PathVariable(value = "id", required = true) Long id,
			@RequestParam("file") MultipartFile file){
				Optional<SubtitleFile> item = subtitelFilesService.findById(id);
				if (item.isEmpty()) {
					return ResponseEntity.notFound().build();
				}
				SubtitleFile subtitleFile = item.get();
				var result =itemsService.uploadFile(file, subtitleFile); 
				return ResponseEntity.ok(result);
			}

}
