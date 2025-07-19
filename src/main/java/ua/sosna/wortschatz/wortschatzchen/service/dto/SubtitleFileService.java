package ua.sosna.wortschatz.wortschatzchen.service.dto;

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

import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatzchen.domain.TimestampRow;
import ua.sosna.wortschatz.wortschatzchen.repository.SubtitleFileRepo;
import ua.sosna.wortschatz.wortschatzchen.service.SubtitelFilesParseService;
import ua.sosna.wortschatz.wortschatzchen.storage.StorageService;
import ua.sosna.wortschatz.wortschatzchen.web.rest.SubtitleFileResource;

@Service
public class SubtitleFileService {
	private static final Logger LOG = LoggerFactory.getLogger(SubtitleFileService.class);

	private SubtitleFileRepo subtitleFileRepository;
	private StorageService storageService;
	private SubtitelFilesParseService subtitleParseService;

	public SubtitleFileService(SubtitleFileRepo subtitleFileRepository, StorageService storageService, SubtitelFilesParseService subtitleParseService) {
		super();
		this.subtitleFileRepository = subtitleFileRepository;
		this.storageService = storageService;
		this.subtitleParseService = subtitleParseService;
	}

	public List<SubtitleFile> findAll() {
		return subtitleFileRepository.findAll();
	}

	public Optional<SubtitleFile> findById(Long id) {
		return subtitleFileRepository.findById(id);
	}

	public SubtitleFile findByUuid(UUID uuid) throws NotFoundException {
		return subtitleFileRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException());
	}

	public SubtitleFile findByUuid(String uuid) throws NotFoundException {
		var uuidTrue = UUID.fromString(uuid);

		return subtitleFileRepository.findByUuid(uuidTrue).orElseThrow(() -> new NotFoundException());
	}

	public SubtitleFile save(SubtitleFile subtitleFile) {
		return subtitleFileRepository.save(subtitleFile);
	}

	public void deleteById(Long id) {
		subtitleFileRepository.deleteById(id);
	}

	public void deleteByUUID(String uuidInString) throws NotFoundException {
		UUID uuid = UUID.fromString(uuidInString);
		deleteByUUID(uuid);
	}

	public void deleteByUUID(UUID uuid) throws NotFoundException {
		SubtitleFile item = subtitleFileRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException());
		subtitleFileRepository.deleteById(item.getId());
	}

	public SubtitleFile create(SubtitleFile subtitleFile) {
		return save(subtitleFile);
	}

	public SubtitleFile update(Long idToUpdate, SubtitleFile subtitleFile) throws ResponseStatusException {
		if (subtitleFile.getId() != null & subtitleFile.getId() > 0 & subtitleFile.getId() != idToUpdate) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"id in url " + idToUpdate + " not equal to object " + subtitleFile.getId());

		}
		SubtitleFile item = subtitleFileRepository.findById(idToUpdate)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"subtitle not found  for id:" + idToUpdate.toString()));
		return update(item, subtitleFile);

	}

	public SubtitleFile update(String uuidInStringToUpdate, SubtitleFile subtitleFile) {
		UUID idToUpdate = UUID.fromString(uuidInStringToUpdate);
		if (subtitleFile.getUuid() != null & subtitleFile.getUuid() != idToUpdate) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "id in url " + idToUpdate.toString()
					+ " not equal to object: " + subtitleFile.getUuid().toString());
		}
		var item = subtitleFileRepository.findByUuid(idToUpdate)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"subtitleFile not found  for uuid:" + idToUpdate.toString()));

		return update(item, subtitleFile);

	}

	public SubtitleFile update(SubtitleFile item, SubtitleFile subtitleFile) throws ResponseStatusException {

		if (subtitleFile.getBaseLang() != null) {
			item.setBaseLang(subtitleFile.getBaseLang());
		}
		if (subtitleFile.getName() != null) {
			item.setName(subtitleFile.getName());
		}
		if (subtitleFile.getUploadDate() != null) {
			item.setUploadDate(subtitleFile.getUploadDate());
		}
		if (subtitleFile.getFile() != null) {
			item.setFile(subtitleFile.getFile());
		}
		if (subtitleFile.getTimestampRows() != null) {
			item.setTimestampRows(subtitleFile.getTimestampRows());
		}
		return subtitleFileRepository.save(item);

	}

	public SubtitleFile uploadFile(MultipartFile file, SubtitleFile item) {
		LOG.debug("upload {1}", file.getName());
		return subtitleFileRepository.findById(item.getId()).get();

	}

	public void ParseRowsFromFile(Long id) throws Throwable {
		SubtitleFile item = subtitleFileRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"subtitle not found  for id:" + id.toString()));
		
		if (item.getFile()!= null) {
			this.subtitleParseService.ParseFileToRows(item);
		}
		// TODO Auto-generated method stub
		
	}
	public void deleteAllTimestampRows(Long id) {
		
		SubtitleFile item = subtitleFileRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"subtitle not found  for id:" + id.toString()));
		
		var items = this.subtitleParseService.getTimestampRepo().findAllBySubtitleFile(item);
		for (Iterator iterator = items.iterator(); iterator.hasNext();) {
			
			TimestampRow timestampRow = (TimestampRow) iterator.next();
			this.subtitleParseService.getTimestampRepo().deleteById(timestampRow.getId());
		}	
	}
	
	
}