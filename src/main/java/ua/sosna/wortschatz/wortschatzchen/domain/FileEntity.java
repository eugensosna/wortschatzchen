package ua.sosna.wortschatz.wortschatzchen.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Version;

@Entity()
public class FileEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 5120280978174795419L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(name = "content_type")
	private String contentType;

	@Column(nullable = false, updatable = false)
	private Instant created = Instant.now();

	@Column(name = "extension")
	private String extension;

	@Column(name = "file_name")
	private String fileName;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_entity_id_seq")
    @SequenceGenerator(name = "file_entity_id_seq", sequenceName = "file_entity_id_seq", allocationSize = 1) // <--- CHANGE THIS TO 

	// The initial value is to account for data.sql demo data ids
	//@SequenceGenerator(name = "file_entity_id_seq")
	protected Long id;
	@Column(name = "name")
	private String name;

	@Column(name = "original_file_name")
	private String originalFilename;

	@Column(name = "sha256")
	private String sha256;

	@Column(name = "size")
	private Long size;

	private Instant updated = Instant.now();

	@Column(name = "uuid", unique = true, nullable = false, updatable = false)
	protected UUID uuid = UUID.randomUUID();

	@Version
	private int version;

	public FileEntity() {
		super();
	}

	public String getContentType() {
		return contentType;
	}

	public Instant getCreated() {
		return created;
	}

	public String getExtension() {
		return extension;
	}

	public String getFileName() {
		return fileName;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public String getSha256() {
		return extension;
	}

	public Long getSize() {
		return size;
	}

	public Instant getUpdated() {
		return updated;
	}

	public UUID getUuid() {
		return uuid;
	}

	public int getVersion() {
		return version;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;

	}

	public void setCreated(Instant created) {
		this.created = created;
	}

	/**
	 * @param String extension
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOriginalFilename(String fileName) {
		this.originalFilename = fileName;
	}

	/**
	 * @param String extension
	 */
	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public void setUpdated(Instant updated) {
		this.updated = updated;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
