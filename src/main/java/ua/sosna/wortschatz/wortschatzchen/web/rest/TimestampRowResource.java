package ua.sosna.wortschatz.wortschatzchen.web.rest;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ua.sosna.wortschatz.wortschatzchen.domain.FileEntity;
import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatzchen.domain.TimestampRow;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;
import ua.sosna.wortschatz.wortschatzchen.dto.ExampleDTO;
import ua.sosna.wortschatz.wortschatzchen.service.FileService;
import ua.sosna.wortschatz.wortschatzchen.service.dto.SubtitleFileService;
import ua.sosna.wortschatz.wortschatzchen.service.dto.TimestampRowService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/timestamprow")
@CrossOrigin
public class TimestampRowResource {
	private static final String ENTITY_NAME = "TimestampRow";
	private static final Logger LOG = LoggerFactory.getLogger(TimestampRowResource.class);
	private SubtitleFileService subtitelFilesService;

	private TimestampRowService itemsService;

	public TimestampRowResource(TimestampRowService itemsService, SubtitleFileService subtitelFilesService) {
		super();
		this.itemsService = itemsService;
		this.subtitelFilesService = subtitelFilesService;
	}

	@GetMapping("/")
	public List <TimestampRow> getAllItems() {
		return itemsService.findAll();
	}

	@GetMapping("/bay-main")
	private ResponseEntity<List<TimestampRow>> getAllByMain(@RequestParam(value = "mainId", required = false) Long mainId,
			@RequestParam(value = "mainUUID", required = false) String mainUUIDInString) throws NotFoundException {
		if (mainId == null && mainUUIDInString == null) {
			return ResponseEntity.badRequest().build();
		}
		SubtitleFile mainItem = null;
		if (mainId != null) {
			mainItem = subtitelFilesService.findById(mainId).get();
		}
		if (mainUUIDInString != null) {
			try {
				mainItem = subtitelFilesService.findByUuid(mainUUIDInString);
			} catch (NotFoundException e) {
				e.printStackTrace();
				return ResponseEntity.notFound().build();
			}
		}
		return ResponseEntity.ok(itemsService.findAllBySubtitleFile(mainItem));
	}
	@GetMapping("/by-main/page")
	private ResponseEntity<Page<TimestampRow>> getPageByMain(@RequestParam(value = "mainId", required = false) Long mainId,
			@RequestParam(value = "mainUUID", required = false) String mainUUIDInString, 
			Pageable pageable) throws NotFoundException {
		
		SubtitleFile mainItem = getSubtitleByIdOrUUID(mainId, mainUUIDInString);
		
		return ResponseEntity.ok(itemsService.findAllBySubtitleFilePageable(mainItem, pageable));
		
	}
	
	protected SubtitleFile getSubtitleByIdOrUUID(Long mainId, String mainUUIDInString) {
		SubtitleFile mainItem = null;
		if (mainId != null) {
			mainItem = subtitelFilesService.findById(mainId).get();
		}
		if (mainUUIDInString != null) {
			try {
				mainItem = subtitelFilesService.findByUuid(mainUUIDInString);
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
			
		}
		return mainItem;
	}
	

	@GetMapping("/{id}")
	public ResponseEntity <TimestampRow> getItemById(@PathVariable(value = "id", required = true) Long id) {
		Optional <TimestampRow> item = itemsService.findById(id);
		if (item.isPresent()) {
			var hibernateItem = item.get();
			 Hibernate.initialize(hibernateItem.getSubtitleFile());
			 return ResponseEntity.ok(hibernateItem);
			
		} else {
			return ResponseEntity.notFound().build();
		}
		
	}

	@GetMapping("/uuid/{uuid}")
	public ResponseEntity <TimestampRow> getItemByUuid(@PathVariable(value = "uuid", required = true) String uuid)
			throws NotFoundException {
		TimestampRow item = itemsService.findByUuid(uuid);
		return item != null ? ResponseEntity.ok(item) : ResponseEntity.notFound().build();
	}

	@PostMapping("/")
	public ResponseEntity <TimestampRow> createNewItem(@RequestBody TimestampRow item) {
		item.setId(null);
		TimestampRow result = itemsService.create(item);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/{id}")
	public ResponseEntity <TimestampRow> updateItem(@PathVariable(value = "id") Long id,
			@RequestBody TimestampRow newItem) {
		if (newItem.getId() == null) {
			return ResponseEntity.badRequest().build();
		}
		TimestampRow result = itemsService.update(newItem.getId(), newItem);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/uuid/{uuid}")
	public ResponseEntity <TimestampRow> updateItemByUUID(@PathVariable(value = "uuid") String uuidInString,
			@RequestBody TimestampRow newItem) {

		if (newItem.getId() == null) {
			return ResponseEntity.badRequest().build();
		}

		TimestampRow result = itemsService.update(uuidInString, newItem);
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
}
