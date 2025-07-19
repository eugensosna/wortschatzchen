package ua.sosna.wortschatz.wortschatzchen.dto;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import ua.sosna.wortschatz.wortschatzchen.domain.FileEntity;
import ua.sosna.wortschatz.wortschatzchen.domain.Language;
import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatzchen.repository.FileRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.SubtitleFileRepo;
import ua.sosna.wortschatz.wortschatzchen.service.SubtitelFilesParseService;
import ua.sosna.wortschatz.wortschatzchen.storage.StorageService;

@Component
public class SubtitleFilesDto implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private SubtitleFileRepo repo;
	private StorageService storageService;
	private FileRepo fileRepo;
	private SubtitelFilesParseService subtitelFilesService;

	private Long id;

	private String name;

	private MultipartFile file;
	private FileEntity fileDB;
	private Long baseLang;

	public SubtitleFileRepo getRepo() {
		return repo;
	}

	public void setRepo(SubtitleFileRepo repo) {
		this.repo = repo;
	}

	public FileRepo getFileRepo() {
		return fileRepo;
	}

	public void setFileRepo(FileRepo fileRepo) {
		this.fileRepo = fileRepo;
	}

	public StorageService getStorageService() {
		return storageService;
	}

	public void setStorageService(StorageService storageService) {
		this.storageService = storageService;
	}

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

	public Long getBaseLang() {
		return baseLang;
	}

	public void setBaseLang(Long baseLang) {
		this.baseLang = baseLang;
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

	/**
	 * @param fileDB
	 */
	public void setFileDB(FileEntity fileDB) {
		this.fileDB = fileDB;
	}

	public FileEntity getFileDB() {
		return fileDB;
	}

	public void save() throws IOException, NoSuchAlgorithmException, RuntimeException {
		SubtitleFile item = new SubtitleFile();
		FileEntity fileDBO;
		if (id != null) {
			item = repo.findById(id).orElseThrow(RuntimeException::new);
		}
		fileDBO = item.getFile();
		if (fileDBO == null) {
			fileDBO = new FileEntity();
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
			fileDBO.setFileName(destanationFile.getFileName().toString());
			fileDBO.setExtension(storageService.getExtension(file.getOriginalFilename()));
			fileDBO.setContentType(fileDBO.getContentType());
			fileDBO.setOriginalFilename(file.getOriginalFilename());
			fileDBO.setSize(file.getSize());

			fileDBO.setContentType(file.getContentType());
			fileDBO.setName(fileDBO.getOriginalFilename());
			fileDBO.setSha256(sha);
			fileDBO = fileRepo.save(fileDBO);

		}
		if (fileDBO.getId() > 0) {
			item.setFile(fileDBO);
		}
		Language language = getSubtitelFilesService().getLanguageRepo().findById(baseLang).get();
		if (language != null) {
			item.setBaseLang(language);

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
		result.setFileDB(item.getFile());

		return result;

	}

	public SubtitelFilesParseService getSubtitelFilesService() {
		return subtitelFilesService;
	}

	public void setSubtitelFilesService(SubtitelFilesParseService subtitelFilesService) {
		this.subtitelFilesService = subtitelFilesService;
	}

}
