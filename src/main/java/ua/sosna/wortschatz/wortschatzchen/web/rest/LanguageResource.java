package ua.sosna.wortschatz.wortschatzchen.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.tomcat.util.http.HeaderUtil;
import org.apache.tomcat.util.http.ResponseUtil;
import org.slf4j.ILoggerFactory;
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
import ua.sosna.wortschatz.wortschatzchen.repository.LanguageRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/languages")
public class LanguageResource {
	private static final Logger LOG = LoggerFactory.getLogger(LanguageResource.class);

	private static final String ENTITY_NAME = "language";

	private final LanguageRepo languageRepository;

	public LanguageResource(LanguageRepo languageRepository) {
		this.languageRepository = languageRepository;
	}

	/**
	 * {@code POST  /languages} : Create a new language.
	 *
	 * @param language the language to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new language, or with status {@code 400 (Bad Request)} if
	 *         the language has already an ID.
	 * @throws URISyntaxException 
	 * @throws Exception
	 */
	@PostMapping(value ="/", consumes = "application/json")
	public ResponseEntity<Language> createLanguage(@RequestBody Language language) throws URISyntaxException{
		LOG.debug("REST request to save Language : {}", language);
		if (language.getId() != null) {
			if (language.getId() > 0) {
				return  (ResponseEntity<Language>) ResponseEntity.unprocessableEntity(); 
				//throw new Exception("A new language cannot already have an ID :idexists");
//				return ResponseEntity.unprocessableEntity();
			}
//	           return ResponseEntity.unprocessableEntity("A new language cannot already have an ID :idexists");
		}
		language = languageRepository.save(language);
		return ResponseEntity.created(new URI("/api/languages/" + language.getId()))
				// .headers(HeaderUtil.createEntityCreationAlert(, true, ENTITY_NAME,
				// language.getId().toString()))
				.body(language);
	}

	/**
	 * {@code PUT  /languages/:id} : Updates an existing language.
	 *
	 * @param id       the id of the language to save.
	 * @param language the language to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated language, or with status {@code 400 (Bad Request)} if the
	 *         language is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the language couldn't be
	 *         updated.
	 * @throws Exception
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Language> updateLanguage(@PathVariable(value = "id", required = true) final Long id,
			@RequestBody Language language) throws Exception {
		LOG.debug("REST request to update Language : {}, {}", id, language);
		if (language.getId() == null) {
			throw new Exception("Invalid id idnull");
		}
		if (!Objects.equals(id, language.getId())) {
			throw new Exception("Invalid ID idinvalid");
		}

		if (!languageRepository.existsById(id)) {
			throw new Exception("Entity not found idnotfound");
		}

		language = languageRepository.save(language);
		return ResponseEntity.ok()
				// .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true,
				// ENTITY_NAME, language.getId().toString()))
				.body(language);
	}

	/**
	 * {@code PATCH  /languages/:id} : Partial updates given fields of an existing
	 * language, field will ignore if it is null
	 *
	 * @param id       the id of the language to save.
	 * @param language the language to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated language, or with status {@code 400 (Bad Request)} if the
	 *         language is not valid, or with status {@code 404 (Not Found)} if the
	 *         language is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the language couldn't be
	 *         updated.
	 * @throws Exception
	 */
	@PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
	public ResponseEntity<Language> partialUpdateLanguage(@PathVariable(required = false) final Long id,
			@RequestBody Language language) throws Exception {
		LOG.debug("REST request to partial update Language partially : {}, {}", id, language);
		if (language.getId() == null) {
			throw new Exception("Invalid ididnull");
		}
		if (!Objects.equals(id, language.getId())) {
			throw new Exception("Invalid ID idinvalid");
		}

		if (!languageRepository.existsById(id)) {
			throw new Exception("Entity not found idnotfound");
		}

		Optional<Language> result = languageRepository.findById(language.getId()).map(existingLanguage -> {
			if (language.getName() != null) {
				existingLanguage.setName(language.getName());
			}
			if (language.getShortName() != null) {
				existingLanguage.setShortName(language.getShortName());
			}
			if (language.getUuid() != null) {
				existingLanguage.setUuid(language.getUuid());
			}

			return existingLanguage;
		}).map(languageRepository::save);

		if (result.isPresent()) {
			return ResponseEntity.ok(result.get());
		} else {
			// item = new Language();
			throw new Exception("not found " + id);
		}

		// HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,
		// language.getId().toString())

	}

	/**
	 * {@code GET  /languages} : get all the languages.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of languages in body.
	 */
	@GetMapping("/")
	public List<Language> getAllLanguages() {
		LOG.debug("REST request to get all Languages");
		return languageRepository.findAll();
	}

//	    @GetMapping("/{wordUUID}")
//	    public List<Language> getAllMeansByWord(@PathVariable UUID wordUUID) {
//	        LOG.debug("REST request to get all Means by {}", wordUUID);
//	        
//	        return languageRepository.findAllByUuid(wordUUID);
//	    }

	/**
	 * {@code GET  /languages/:id} : get the "id" language.
	 *
	 * @param id the id of the language to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the language, or with status {@code 404 (Not Found)}.
	 * @throws Exception
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Language> getLanguage(@PathVariable(value = "id", required = false) final Long id)
			throws Exception {
		LOG.debug("REST request to get Language : {}", id);
		Optional<Language> language = languageRepository.findById(id);
		if (language.isPresent()) {
			return ResponseEntity.ok(language.get());
		} else {
			// item = new Language();
			throw new Exception("not found " + id);
		}

	}
	
	@GetMapping("/by-shortName")
	public ResponseEntity<Language> getLanguageByShortName(@RequestParam(name = "shortName", required = false, defaultValue = "") String shortName)
			throws Exception {
		LOG.debug("REST request to get Language with shortName: {}", shortName);
		Optional<Language> language = languageRepository.findOneByShortName(shortName);
		if (language.isPresent()) {
			return ResponseEntity.ok(language.get());
		} else {
			// item = new Language();
			throw new Exception("not found " + shortName);
		}

	}

	/**
	 * {@code GET  /languages/:uuid} : get the "uuid" language.
	 *
	 * @param id the id of the language to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the language, or with status {@code 404 (Not Found)}.
	 * @throws Exception
	 */
	@GetMapping("/by-uuid")
	public ResponseEntity<Language> getLanguageByUUID(
			@RequestParam(value = "uuid", required = true) String uuidString) {
		LOG.debug("REST request to get Language by uuid : {}", uuidString);
		UUID uuid = UUID.fromString(uuidString);
		Optional<Language> language = languageRepository.findByUuid(uuid);
		if (language.isPresent()) {
			return ResponseEntity.ok(language.get());
		} else {
			LOG.error("not found " + uuidString);
			// item = new Language();
			return ResponseEntity.notFound().build();
		}

	}

	/**
	 * {@code DELETE  /languages/:id} : delete the "id" language.
	 *
	 * @param id the id of the language to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteLanguage(@PathVariable(value = "id", required = true) final Long id) {
		LOG.debug("REST request to delete Language : {}", id);
		languageRepository.deleteById(id);
		return ResponseEntity.noContent()
				// .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true,
				// ENTITY_NAME, id.toString()))
				.build();
	}

}
