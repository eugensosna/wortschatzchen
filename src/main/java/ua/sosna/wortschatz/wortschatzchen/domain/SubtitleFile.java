package ua.sosna.wortschatz.wortschatzchen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.lang.NonNull;

/**
 * A SubtitleFile.
 */
@Entity
@Table(name = "subtitle_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
//@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubtitleFile implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties(value = { "subtitleFiles", "words", "synonyms" }, allowSetters = true)
	private Language baseLang;

	@Column(nullable = false, updatable = false)
	private Instant created = Instant.now();

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "file_id")
	private FileEntity file;

	@Id
	@SequenceGenerator(name = "subtitle_file_id_seq", sequenceName = "subtitle_file_id", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subtitle_file_id_seq")

	protected Long id;

	@Column(name = "name")
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "subtitleFile")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "subtitleFile" }, allowSetters = true)
	private Set<TimestampRow> timestampRows = new HashSet<>();

	private Instant updated = Instant.now();

	@Column(name = "upload_date", nullable = false)
	private Instant uploadDate = Instant.now();

	@Column(name = "uuid", unique = true, nullable = false, updatable = false)
	protected UUID uuid = UUID.randomUUID();

	@Version
	private int version;

	public SubtitleFile addTimestampRow(TimestampRow timestampRow) {
		this.timestampRows.add(timestampRow);
		timestampRow.setSubtitleFile(this);
		return this;
	}

	public SubtitleFile baseLang(Language baseLang) {
		this.setBaseLang(baseLang);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SubtitleFile)) {
			return false;
		}
		return getId() != null && getId().equals(((SubtitleFile) o).getId());
	}

	public Language getBaseLang() {
		return this.baseLang;
	}

	public FileEntity getFile() {
		return file;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Set<TimestampRow> getTimestampRows() {
		return this.timestampRows;
	}

	public Instant getUpdated() {
		return updated;
	}

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Instant getUploadDate() {
		return this.uploadDate;
	}

	public UUID getUuid() {
		return uuid;
	}

	public int getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		// see
		// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	public SubtitleFile id(Long id) {
		this.setId(id);
		return this;
	}

	public SubtitleFile name(String name) {
		this.setName(name);
		return this;
	}

	@PrePersist
	public void PreSave() {
		if (this.uploadDate == null) {
			this.uploadDate = Instant.now();
		}
	}

	public SubtitleFile removeTimestampRow(TimestampRow timestampRow) {
		this.timestampRows.remove(timestampRow);
		timestampRow.setSubtitleFile(null);
		return this;
	}

	public void setBaseLang(Language baseLang) {
		this.baseLang = baseLang;
	}

	public void setFile(FileEntity fileDB) {
		this.file = fileDB;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	public void setTimestampRows(Set<TimestampRow> timestampRows) {
		if (this.timestampRows != null) {
			this.timestampRows.forEach(i -> i.setSubtitleFile(null));
		}
		if (timestampRows != null) {
			timestampRows.forEach(i -> i.setSubtitleFile(this));
		}
		this.timestampRows = timestampRows;
	}

	public void setUploadDate(Instant uploadDate) {
		this.uploadDate = uploadDate;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public SubtitleFile timestampRows(Set<TimestampRow> timestampRows) {
		this.setTimestampRows(timestampRows);
		return this;
	}

	@Override
	public String toString() {
		return "SubtitleFile{" + "id=" + getId() + "name=" + getName() + ", uploadDate='" + getUploadDate() + "'" + "}";
	}

	public SubtitleFile uploadDate(Instant uploadDate) {
		this.setUploadDate(uploadDate);
		return this;
	}
}
