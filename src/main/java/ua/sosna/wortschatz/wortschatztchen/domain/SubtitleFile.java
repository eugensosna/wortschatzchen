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

	@NonNull
    @Column(name = "filename", nullable = false)
    private String filename;

    @NonNull
    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    
    @Column(name = "extension", nullable = false)
    private String extension;

    
    @Column(name = "size_in_bytes", nullable = false)
    private Integer sizeInBytes;

   
    @Column(name = "sha_256", nullable = false)
    private String sha256;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    
    @Column(name = "upload_date", nullable = false)
    private Instant uploadDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subtitleFile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subtitleFile" }, allowSetters = true)
    private Set<TimestampRow> timestampRows = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subtitleFiles", "words", "synonyms" }, allowSetters = true)
    private Language language;

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

    public String getFilename() {
        return this.filename;
    }

    public SubtitleFile filename(String filename) {
        this.setFilename(filename);
        return this;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getOriginalFilename() {
        return this.originalFilename;
    }

    public SubtitleFile originalFilename(String originalFilename) {
        this.setOriginalFilename(originalFilename);
        return this;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getExtension() {
        return this.extension;
    }

    public SubtitleFile extension(String extension) {
        this.setExtension(extension);
        return this;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Integer getSizeInBytes() {
        return this.sizeInBytes;
    }

    public SubtitleFile sizeInBytes(Integer sizeInBytes) {
        this.setSizeInBytes(sizeInBytes);
        return this;
    }

    public void setSizeInBytes(Integer sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public String getSha256() {
        return this.sha256;
    }

    public SubtitleFile sha256(String sha256) {
        this.setSha256(sha256);
        return this;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getContentType() {
        return this.contentType;
    }

    public SubtitleFile contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubtitleFile{" +
            "id=" + getId() +
            ", filename='" + getFilename() + "'" +
            ", originalFilename='" + getOriginalFilename() + "'" +
            ", extension='" + getExtension() + "'" +
            ", sizeInBytes=" + getSizeInBytes() +
            ", sha256='" + getSha256() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", uploadDate='" + getUploadDate() + "'" +
            "}";
    }
    
    @PrePersist
    public void PreSave() {
    	if (this.uploadDate==null) {
    		this.uploadDate = Instant.now();
    	}
    }
}
