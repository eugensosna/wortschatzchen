package ua.sosna.wortschatz.wortschatzchen.web.rest;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ua.sosna.wortschatz.wortschatzchen.domain.Language;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;
import ua.sosna.wortschatz.wortschatzchen.repository.WordRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/words")
public class WordResource {

	private static final Logger LOG = LoggerFactory.getLogger(WordResource.class);
	private static final String ENTITY_NAME = "wort";
	private final WordRepo repo;

	public WordResource(WordRepo repo) {
		super();
		this.repo = repo;
	}
	@GetMapping("/")
	public List<Word> getAll (){
		LOG.debug("get all words");
		return repo.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Word> getItem(@PathVariable(value="id", required = true) final Long id) throws Exception {
		LOG.debug("REST request to get Language : {}", id);
		Optional<Word> item = repo.findById(id);
		if (item.isPresent()) {
			return ResponseEntity.ok(item.get());
		} else {
			// item = new Language();
			throw new Exception("not found " + id);
		}

	}

	@PostMapping("/")
	public ResponseEntity<Word> createItem(@RequestBody Word item) throws Exception {
		LOG.debug("REST request to create Word : {}", item);
		if (item.getId() != null) {
			throw new Exception("A new language cannot already have an ID :idexists");
			// return ResponseEntity.unprocessableEntity("A new language cannot already have
			// an ID :idexists");
		}
		var newItem = repo.save(item);
		return ResponseEntity.created(new URI("/api/languages/" + newItem.getId()))
				// .headers(HeaderUtil.createEntityCreationAlert(, true, ENTITY_NAME,
				// item.getId().toString()))
				.body(newItem);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Word> updateItem(@PathVariable(value="id", required = true) final Long id,
			@RequestBody Word item) throws Exception {
		LOG.debug("REST request to update {} : {}, {}", ENTITY_NAME, id, item);
		if (item.getId() == null) {
			throw new Exception("Invalid id idnull");
		}
		if (!Objects.equals(id, item.getId())) {
			throw new Exception("Invalid ID idinvalid");
		}

		if (!repo.existsById(id)) {
			throw new Exception("Entity not found idnotfound");
		}

		var newItem = repo.save(item);
		return ResponseEntity.ok()
				// .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
				// ENTITY_NAME, item.getId().toString()))
				.body(newItem);
	}

	@PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
	public ResponseEntity<Word> partialUpdateItem(@PathVariable(required = true) final Long id,
			@RequestBody Word item) throws Exception {

		LOG.debug("REST request to partial update {} partially : {}, {}", ENTITY_NAME, id, item);

		// Validate input
		if (item.getId() == null) {
			throw new Exception("Invalid id: null");
		}
		if (!Objects.equals(id, item.getId())) {
			throw new Exception("Invalid ID: path variable does not match item ID");
		}

		// Find existing item
		Optional<Word> optionalItem = repo.findById(id);
		if (optionalItem.isEmpty()) {
			LOG.error("Item not found {} id {}", ENTITY_NAME, id);
			throw new RuntimeException("Item not found");
		}

		Word existingItem = optionalItem.get();

		// Comprehensively update all properties if not null
		if (item.getName() != null) {
			existingItem.setName(item.getName());
		}
		if (item.getBaseForm() != null) {
			existingItem.setBaseForm(item.getBaseForm());
		}
		if (item.getKindOfWord() != null) {
			existingItem.setKindOfWord(item.getKindOfWord());
		}
		if (item.getMainMean() != null) {
			existingItem.setMainMean(item.getMainMean());
		}
		if (item.getImportant() != null) {
			existingItem.setImportant(item.getImportant());
		}
		if (item.getBaseLang() != null) {
			existingItem.setBaseLang(item.getBaseLang());
		}
		if (item.getUuid() != null) {
			existingItem.setUuid(item.getUuid());
		}
		if (item.getMeans() != null) {
			existingItem.setMeans(item.getMeans());
		}
		if (item.getExamples() != null) {
			existingItem.setExamples(item.getExamples());
		}
		if (item.getSynonyms() != null) {
			existingItem.setSynonyms(item.getSynonyms());
		}

		// Save and return updated item
		Word result = repo.save(existingItem);
		return ResponseEntity.ok(result);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLanguage(@PathVariable(required = true) final Long id) {
		LOG.debug("REST request to delete {} : {}", ENTITY_NAME, id);
		repo.deleteById(id);
		return ResponseEntity.noContent()
				// .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true,
				// ENTITY_NAME, id.toString()))
				.build();
	}
	
	  @GetMapping("/uuid")
	    public ResponseEntity<Word> getItemByUUID(
	    		@RequestParam(value = "uuid", required =true) String uuidString) {
	    	LOG.debug("REST request to get Word by uuid : {}", uuidString);
	        UUID uuid = UUID.fromString(uuidString);
	        Optional<Word> item = repo.findByUuid(uuid);
	        if (item.isPresent()) {
	        	return ResponseEntity.ok(item.get());
	        } else {
	        	LOG.error("not found "+uuidString);
	        	//item = new Language();
	        	return ResponseEntity.notFound().build();      
	        }
	        
	    }

}
