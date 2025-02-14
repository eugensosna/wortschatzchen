package ua.sosna.wortschatz.wortschatztchen.dto;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import ua.sosna.wortschatz.wortschatztchen.domain.File;
import ua.sosna.wortschatz.wortschatztchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatztchen.repository.FileRepo;
import ua.sosna.wortschatz.wortschatztchen.repository.SubtitleFileRepo;
import ua.sosna.wortschatz.wortschatztchen.storage.StorageService;

@Component
public class SubtitleFilesDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private SubtitleFileRepo repo;
	private StorageService storageService;
	private FileRepo fileRepo;

	public SubtitleFileRepo getRepo() {
		return repo;
	}

	public void setRepo(SubtitleFileRepo repo) {
		this.repo = repo;
	}

	public StorageService getStorageService() {
		return storageService;
	}

	public void setStorageService(StorageService storageService) {
		this.storageService = storageService;
	}

	private Long id;
	private String name;
	private MultipartFile file;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public SubtitleFilesDto(StorageService storageService, Long id, String name, MultipartFile file) {
		super();
		this.id = id;
		this.storageService = storageService;
		this.name = name;
		this.file = file;
	}

	public SubtitleFilesDto(StorageService storageService, SubtitleFileRepo repo, Long id, String name,
			MultipartFile file) {
		super();
		this.repo = repo;
		this.storageService = storageService;
		this.id = id;
		this.name = name;
		this.file = file;
	}

	public SubtitleFilesDto(SubtitleFileRepo repo, StorageService storageService, FileRepo fileRepo) {
		super();
		this.storageService = storageService;
		this.repo = repo;
		this.fileRepo = fileRepo;
	}

	public SubtitleFilesDto() {
		super();
	}

	public void save() throws IOException, NoSuchAlgorithmException, RuntimeException {
		SubtitleFile item = new SubtitleFile();
		if (id != null) {
			item = repo.findById(id).orElseThrow(RuntimeException::new);
		}

		if (file != null) {
			Path destanationFile = storageService.store(file);

			File fileDBO = new File();

			// file.setFileName(name);

			fileDBO.setFileName(destanationFile.getFileName().toString());
			fileDBO.setExtension(storageService.getExtension(file.getOriginalFilename()));
			fileDBO.setContentType(fileDBO.getContentType());
			fileDBO.setOriginalFilename(file.getOriginalFilename());
			fileDBO.setSize(file.getSize());
			String sha = destanationFile.getFileName().toString();
			try {
				var mSha = MessageDigest.getInstance("SHA-256");

				var di = new DigestInputStream(file.getInputStream(), mSha);
				MessageDigest str = di.getMessageDigest();
				sha = str.toString();

			} catch (NoSuchAlgorithmException | IOException | RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// throw new NoSuchAlgorithmException(e);
			}
			fileDBO.setSha256(sha);

		}
		item.setName(getName());
		var savedItem = repo.save(item);
		setFile(null);
		setId(savedItem.getId());
		setName(savedItem.getName());

	}

	public static SubtitleFilesDto read(Long id, SubtitleFileRepo repo) {
		var item = repo.findById(id).orElseThrow(RuntimeException::new);
		var result = new SubtitleFilesDto();
		result.setId(id);
		result.setName(item.getName());
		return result;

	}

}
