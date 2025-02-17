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
import ua.sosna.wortschatz.wortschatztchen.domain.Language;
import ua.sosna.wortschatz.wortschatztchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatztchen.repository.FileRepo;
import ua.sosna.wortschatz.wortschatztchen.repository.SubtitleFileRepo;
import ua.sosna.wortschatz.wortschatztchen.service.SubtitelFilesService;
import ua.sosna.wortschatz.wortschatztchen.storage.StorageService;

@Component
public class SubtitleFilesDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private SubtitleFileRepo repo;
	private StorageService storageService;
	private FileRepo fileRepo;
	private SubtitelFilesService subtitelFilesService;

	private Long id;

	private String name;

	private MultipartFile file;
	private File fileDB;
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
	public void setFileDB(File fileDB) {
		this.fileDB = fileDB;
	}

	public File getFileDB() {
		return fileDB;
	}

	public void save() throws IOException, NoSuchAlgorithmException, RuntimeException {
		SubtitleFile item = new SubtitleFile();
		File fileDBO;
		if (id != null) {
			item = repo.findById(id).orElseThrow(RuntimeException::new);
		}
		fileDBO = item.getFile();
		if (fileDBO == null) {
			fileDBO = new File();
		}

		if (file != null && file.getSize()>0) {
			Path destanationFile = storageService.store(file);
			
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
		if (fileDBO.getId()>0) {
		item.setFile(fileDBO);
		}
		Language language = getSubtitelFilesService().getLanguageRepo().findById(baseLang).get();
		if (language!=null) {
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

	public SubtitelFilesService getSubtitelFilesService() {
		return subtitelFilesService;
	}

	public void setSubtitelFilesService(SubtitelFilesService subtitelFilesService) {
		this.subtitelFilesService = subtitelFilesService;
	}

}
