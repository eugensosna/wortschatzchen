package ua.sosna.wortschatz.wortschatztchen.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class File extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 5120280978174795419L;

	@Column(name = "name")
	private String name;
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "original_file_name")
	private String originalFilename;
	
	@Column(name = "extension")
	private String extension;
	
	
	@Column(name = "content_type")
	private String contentType;
	
	@Column(name = "size")
	private Long size;
	
	@Column(name = "sha256")
	private String sha256;
	
	public File() {
		super();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getContentType() {
		return contentType;
	}


	public void setContentType(String contentType) {
		this.contentType = contentType;

	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}
	
	public void setOriginalFilename(String fileName) {
		this.originalFilename = fileName;
	}
	public String getOriginalFilename() {
		return originalFilename;
	}

	public String getExtension() {
		return extension;
	}

	/**
	 * @param String extension
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public String getSha256() {
		return extension;
	}

	/**
	 * @param String extension
	 */
	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}
	

}
