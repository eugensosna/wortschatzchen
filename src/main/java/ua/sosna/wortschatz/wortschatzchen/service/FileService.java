package ua.sosna.wortschatz.wortschatzchen.service;

import java.io.IOException;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatzchen.repository.FileRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.SubtitleFileRepo;
import ua.sosna.wortschatz.wortschatzchen.storage.FileSystemStorageService;
import ua.sosna.wortschatz.wortschatzchen.storage.StorageService;
import ua.sosna.wortschatz.wortschatzchen.web.rest.SubtitleFileResource;

@Service
public class FileService {

	private final FileSystemStorageService fileSystemStorageService;
	private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

	private SubtitleFileRepo subtitleFileRepo;
	private StorageService storageService;
	private FileRepo fileRepository;

	public FileService(SubtitleFileRepo subtitleFileRepo, StorageService storageService, FileRepo fileRepository,
			FileSystemStorageService fileSystemStorageService) {
		super();
		this.subtitleFileRepo = subtitleFileRepo;
		this.storageService = storageService;
		this.fileRepository = fileRepository;
		this.fileSystemStorageService = fileSystemStorageService;
	}

	public List<FileEntity> findAll() {
		return fileRepository.findAll();
	}

	public Optional<FileEntity> findById(Long id) {
		return fileRepository.findById(id);
	}

	public FileEntity findByUuid(String uuid) throws NotFoundException {
		var uuidTrue = UUID.fromString(uuid);

		return findByUuid(uuidTrue);
	}

	public FileEntity findByUuid(UUID uuid) throws NotFoundException {
		// var uuidTrue = UUID.fromString(uuid);

		return fileRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException());
	}

	public FileEntity save(FileEntity subtitleFile) {
		return fileRepository.save(subtitleFile);
	}
	
	public void deleteItem(FileEntity item) {
		var refSubTitleFile = subtitleFileRepo.findByFile(item);
		if (refSubTitleFile.isPresent()) {
			SubtitleFile itemSubtitle = refSubTitleFile.get();
			itemSubtitle.setFile(null);
			subtitleFileRepo.save(itemSubtitle);
		}
		fileRepository.deleteById(item.getId());
	}

	public void deleteById(Long id) {
		var itemOptional = fileRepository.findById(id);
		if (itemOptional.isPresent()){
			deleteItem(itemOptional.get());
		}
		//fileRepository.deleteById(id);
	}

	public void deleteByUUID(String uuidInString) throws NotFoundException {
		UUID uuid = UUID.fromString(uuidInString);
		deleteByUUID(uuid);
	}

	public void deleteByUUID(UUID uuid) throws NotFoundException {
		FileEntity item = fileRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException());
		deleteItem(item);
		//fileRepository.deleteById(item.getId());
	}

	public FileEntity create(FileEntity subtitleFile) {
		return save(subtitleFile);
	}

	public FileEntity update(Long idToUpdate, FileEntity newItemData) throws ResponseStatusException {
		if (newItemData.getId() != null & newItemData.getId() > 0 & newItemData.getId() != idToUpdate) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"id in url " + idToUpdate + " not equal to object " + newItemData.getId());

		}
		FileEntity item = fileRepository.findById(idToUpdate)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"subtitle not found  for id:" + idToUpdate.toString()));
		return update(item, newItemData);

	}

	public FileEntity update(String uuidInStringToUpdate, FileEntity item) {
		UUID idToUpdate = UUID.fromString(uuidInStringToUpdate);
		if (item.getUuid() != null & item.getUuid() != idToUpdate) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"id in url " + idToUpdate.toString() + " not equal to object: " + item.getUuid().toString());
		}
		var itemEntity = fileRepository.findByUuid(idToUpdate)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"subtitleFile not found  for uuid:" + idToUpdate.toString()));

		return update(item, itemEntity);

	}

	public FileEntity update(FileEntity item, FileEntity newItemData) throws ResponseStatusException {

		if (newItemData.getOriginalFilename() != null) {
			item.setOriginalFilename(newItemData.getOriginalFilename());
		}
		if (newItemData.getName() != null) {
			item.setName(newItemData.getName());
		}
		if (newItemData.getFileName() != null) {
			item.setFileName(newItemData.getFileName());
		}
		if (newItemData.getExtension() != null) {
			item.setExtension(newItemData.getExtension());
		}

		return fileRepository.save(item);

	}

	public FileEntity uploadFile(MultipartFile file, SubtitleFile item) {
		LOG.debug("upload {1}", file.getName());

		FileEntity fileEntity = item.getFile();
		if (fileEntity == null) {
			fileEntity = new FileEntity();
		}

		if (file != null && file.getSize() > 0) {
			Path destanationFile = storageService.store(file);

			String sha = destanationFile.getFileName().toString();
			try {
				var mSha = MessageDigest.getInstance("SHA-256");

				var di = new DigestInputStream(file.getInputStream(), mSha);
				MessageDigest str = di.getMessageDigest();
				sha = str.toString();

			} catch (NoSuchAlgorithmException | IOException | RuntimeException e) {
				e.printStackTrace();
				// throw new NoSuchAlgorithmException(e);
			}
			fileEntity.setFileName(destanationFile.getFileName().toString());
			fileEntity.setExtension(storageService.getExtension(file.getOriginalFilename()));
			fileEntity.setContentType(fileEntity.getContentType());
			fileEntity.setOriginalFilename(file.getOriginalFilename());
			fileEntity.setSize(file.getSize());

			fileEntity.setContentType(file.getContentType());
			fileEntity.setName(fileEntity.getOriginalFilename());
			fileEntity.setSha256(sha);
			var newFileEntity = fileRepository.save(fileEntity);
			item.setFile(newFileEntity);
			var newSubtitleFile = subtitleFileRepo.save(item);
		}
		return fileEntity;
	}
}