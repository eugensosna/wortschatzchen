package ua.sosna.wortschatz.wortschatzchen.web.rest;

import ua.sosna.wortschatz.wortschatzchen.domain.Means;
import ua.sosna.wortschatz.wortschatzchen.repository.MeansRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link ua.sosna.wortschatz.wortschatzchen.domain.Means}.
 */
@RestController
@RequestMapping("/api/means")
@Transactional
public class MeansResource {

    private final Logger log = LoggerFactory.getLogger(MeansResource.class);

    private final MeansRepo meansRepository;

    public MeansResource(MeansRepo meansRepository) {
        this.meansRepository = meansRepository;
    }

    /**
     * {@code POST  /means} : Create a new means.
     *
     * @param means the means to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new means, or with status {@code 400 (Bad Request)} if the means has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping
    public ResponseEntity<Means> createMeans(@RequestBody Means means) throws URISyntaxException {
        log.debug("REST request to save Means : {}", means);
        if (means.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A new means cannot already have an ID");
        }
        Means result = meansRepository.save(means);
        return ResponseEntity
            .created(new URI("/api/means/" + result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /means/:id} : Updates an existing means.
     *
     * @param id the id of the means to save.
     * @param means the means to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated means,
     * or with status {@code 400 (Bad Request)} if the means is not valid,
     * or with status {@code 500 (Internal Server Error)} if the means couldn't be updated.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Means> updateMeans(@PathVariable(value = "id", required = false) final Long id, @RequestBody Means means) {
        log.debug("REST request to update Means : {}, {}", id, means);
        if (means.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        if (!Objects.equals(id, means.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }

        if (!meansRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entity not found");
        }

        Means result = meansRepository.save(means);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PATCH  /means/:id} : Partial updates given fields of an existing means, field will ignore if it is null
     *
     * @param id the id of the means to save.
     * @param means the means to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated means,
     * or with status {@code 400 (Bad Request)} if the means is not valid,
     * or with status {@code 404 (Not Found)} if the means is not found,
     * or with status {@code 500 (Internal Server Error)} if the means couldn't be updated.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Means> partialUpdateMeans(@PathVariable(value = "id", required = false) final Long id, @RequestBody Means means) {
        log.debug("REST request to partially update Means : {}, {}", id, means);
        if (means.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid id");
        }
        if (!Objects.equals(id, means.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }

        if (!meansRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found");
        }

        Optional<Means> result = meansRepository.findById(means.getId())
            .map(existingMeans -> {
                if (means.getUuid() != null) {
                    existingMeans.setUuid(means.getUuid());
                }
                if (means.getName() != null) {
                    existingMeans.setName(means.getName());
                }
                if (means.getOrder() != null) {
                    existingMeans.setOrder(means.getOrder());
                }
                if (means.getBaseLang() != null) {
                    existingMeans.setBaseLang(means.getBaseLang());
                }
                if (means.getBaseWord() != null) {
                    existingMeans.setBaseWord(means.getBaseWord());
                }

                return existingMeans;
            })
            .map(meansRepository::save);

        return result.map(response -> ResponseEntity.ok().body(response))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update entity"));
    }

    /**
     * {@code GET  /means} : get all the means.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of means in body.
     */
    @GetMapping
    public List<Means> getAllMeans() {
        log.debug("REST request to get all Means");
        return meansRepository.findAll();
    }
    
    @GetMapping("/{wordUUID}")
    public List<Means> getAllMeansByWord(@PathVariable UUID wordUUID) {
        log.debug("REST request to get all Means by {}", wordUUID);
        
        return meansRepository.findAllByUuid(wordUUID);
    }

    /**
     * {@code GET  /means/:id} : get the "id" means.
     *
     * @param id the id of the means to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the means, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Means> getMeans(@PathVariable Long id) {
        log.debug("REST request to get Means : {}", id);
        Optional<Means> means = meansRepository.findById(id);
        return means.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * {@code GET  /means/uuid/:uuid} : get the means by uuid.
     *
     * @param uuid the uuid of the means to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the means, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<Means> getMeansByUuid(@PathVariable UUID uuid) {
        log.debug("REST request to get Means by uuid : {}", uuid);
        Optional<Means> means = meansRepository.findByUuid(uuid);
        return means.map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE  /means/:id} : delete the "id" means.
     *
     * @param id the id of the means to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeans(@PathVariable Long id) {
        log.debug("REST request to delete Means : {}", id);
        meansRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

