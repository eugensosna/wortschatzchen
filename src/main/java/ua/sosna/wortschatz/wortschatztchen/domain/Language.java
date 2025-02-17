package ua.sosna.wortschatz.wortschatztchen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Language.
 */
@Entity
@Table(name = "language")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@SuppressWarnings("common-java:DuplicatedBlocks")
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "uuid")
    private UUID uuid;

	
	  @OneToMany(fetch = FetchType.LAZY, mappedBy = "baseLang")
	  
	  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	  
	  @JsonIgnoreProperties(value = { "timestampRows", "baseLang" }, allowSetters =
	  true) private Set<SubtitleFile> subtitleFiles = new HashSet<>();
	 

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "baseLang")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "synonyms", "baseLang" }, allowSetters = true)
    private Set<Word> words = new HashSet<>();

	
	  @OneToMany(fetch = FetchType.LAZY, mappedBy = "baseLang")
	  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	  @JsonIgnoreProperties(value = { "baseLang", "baseWord" }, allowSetters =
	  true) private Set<Synonyms> synonyms = new HashSet<>();
	 
    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Language id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Language name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return this.shortName;
    }

    public Language shortName(String shortName) {
        this.setShortName(shortName);
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Language uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Set<SubtitleFile> getSubtitleFiles() {
        return this.subtitleFiles;
    }

    public void setSubtitleFiles(Set<SubtitleFile> subtitleFiles) {
        if (this.subtitleFiles != null) {
            this.subtitleFiles.forEach(i -> i.setBaseLang(null));
        }
        if (subtitleFiles != null) {
            subtitleFiles.forEach(i -> i.setBaseLang(this));
        }
        this.subtitleFiles = subtitleFiles;
    }

    public Language subtitleFiles(Set<SubtitleFile> subtitleFiles) {
        this.setSubtitleFiles(subtitleFiles);
        return this;
    }

    public Language addSubtitleFile(SubtitleFile subtitleFile) {
        this.subtitleFiles.add(subtitleFile);
        subtitleFile.setBaseLang(this);
        return this;
    }

    public Language removeSubtitleFile(SubtitleFile subtitleFile) {
        this.subtitleFiles.remove(subtitleFile);
        subtitleFile.setBaseLang(null);
        return this;
    }

    public Set<Word> getWords() {
        return this.words;
    }

    public void setWords(Set<Word> words) {
        if (this.words != null) {
            this.words.forEach(i -> i.setBaseLang(null));
        }
        if (words != null) {
            words.forEach(i -> i.setBaseLang(this));
        }
        this.words = words;
    }

    public Language words(Set<Word> words) {
        this.setWords(words);
        return this;
    }

    public Language addWord(Word word) {
        this.words.add(word);
        word.setBaseLang(this);
        return this;
    }

    public Language removeWord(Word word) {
        this.words.remove(word);
        word.setBaseLang(null);
        return this;
    }

    public Set<Synonyms> getSynonyms() {
        return this.synonyms;
    }

    public void setSynonyms(Set<Synonyms> synonyms) {
        if (this.synonyms != null) {
            this.synonyms.forEach(i -> i.setBaseLang(null));
        }
        if (synonyms != null) {
            synonyms.forEach(i -> i.setBaseLang(this));
        }
        this.synonyms = synonyms;
    }

    public Language synonyms(Set<Synonyms> synonyms) {
        this.setSynonyms(synonyms);
        return this;
    }

    public Language addSynonyms(Synonyms synonyms) {
        this.synonyms.add(synonyms);
        synonyms.setBaseLang(this);
        return this;
    }

    public Language removeSynonyms(Synonyms synonyms) {
        this.synonyms.remove(synonyms);
        synonyms.setBaseLang(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @PrePersist
    public void preSave() {
    	if(uuid==null) {
    		uuid = UUID.randomUUID();
    	}
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Language)) {
            return false;
        }
        return getId() != null && getId().equals(((Language) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Language{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", shortName='" + getShortName() + "'" +
            ", uuid='" + getUuid() + "'" +
            "}";
    }
}
