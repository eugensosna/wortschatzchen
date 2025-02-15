package ua.sosna.wortschatz.wortschatztchen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.lang.NonNull;

/**
 * A SubtitleFile.
 */
@Entity
@Table(name = "subtitle_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubtitleFile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SubtitleFile name(String name) {
		this.setName(name);
		return this;
	}

	@Column(name = "upload_date", nullable = false)
	private Instant uploadDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "subtitleFile")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "subtitleFile" }, allowSetters = true)
	private Set<TimestampRow> timestampRows = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties(value = { "subtitleFiles", "words", "synonyms" }, allowSetters = true)
	private Language language;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_id")
	private File file;

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return this.id;
	}

	public SubtitleFile id(Long id) {
		this.setId(id);
		return this;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getUploadDate() {
		return this.uploadDate;
	}

	public SubtitleFile uploadDate(Instant uploadDate) {
		this.setUploadDate(uploadDate);
		return this;
	}

	public void setUploadDate(Instant uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Set<TimestampRow> getTimestampRows() {
		return this.timestampRows;
	}

	public void setTimestampRows(Set<TimestampRow> timestampRows) {
		if (this.timestampRows != null) {
			this.timestampRows.forEach(i -> i.setSubtitleFile(null));
		}
		if (timestampRows != null) {
			timestampRows.forEach(i -> i.setSubtitleFile(this));
		}
		this.timestampRows = timestampRows;
	}

	public SubtitleFile timestampRows(Set<TimestampRow> timestampRows) {
		this.setTimestampRows(timestampRows);
		return this;
	}

	public SubtitleFile addTimestampRow(TimestampRow timestampRow) {
		this.timestampRows.add(timestampRow);
		timestampRow.setSubtitleFile(this);
		return this;
	}

	public SubtitleFile removeTimestampRow(TimestampRow timestampRow) {
		this.timestampRows.remove(timestampRow);
		timestampRow.setSubtitleFile(null);
		return this;
	}

	public Language getLanguage() {
		return this.language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public SubtitleFile language(Language language) {
		this.setLanguage(language);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

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

	@Override
	public int hashCode() {
		// see
		// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}
	public void setFile(File fileDB) {
		this.file = fileDB;
	}

	public File getFile() {
		return file;
	}


	@Override
	public String toString() {
		return "SubtitleFile{" + "id=" + getId() + "name=" + getName() + ", uploadDate='" + getUploadDate() + "'" + "}";
	}

	@PrePersist
	public void PreSave() {
		if (this.uploadDate == null) {
			this.uploadDate = Instant.now();
		}
	}
}
