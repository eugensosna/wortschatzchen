package ua.sosna.wortschatz.wortschatzchen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Word.
 */
@Entity
@Table(name = "word")
@JsonIgnoreProperties(value = { "word", "language", "synonyms", "mean" }, allowSetters = true)
public class Word implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	@Column(name = "base_form")
	private String baseForm;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "base_lang_id")
	// @JsonIgnoreProperties(value = { "subtitleFiles", "words", "synonyms" },
	// allowSetters = true)
	private Language baseLang;

	@Column(nullable = false, updatable = false)
	private Instant created = Instant.now();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "baseWord")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "baseLang", "baseWord" }, allowSetters = true)
	private Set<Example> examples = new HashSet<>();

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wods_id_seq")
    @SequenceGenerator(name = "wods_id_seq", sequenceName = "wods_id_seq", allocationSize = 1) // <--- CHANGE THIS TO 

	protected Long id;

	@Column(name = "important", nullable = true)
	private String important;

	@Column(name = "kind_of_word", nullable = true)
	private String kindOfWord;

	@Column(name = "main_mean", nullable = true)
	private String mainMean;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "baseWord")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "baseLang", "baseWord" }, allowSetters = true)
	private Set<Means> means = new HashSet<>();

	@Column(name = "name")
	private String name;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "baseWord")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@JsonIgnoreProperties(value = { "baseLang", "baseWord" }, allowSetters = true)
	private Set<Synonyms> synonyms = new HashSet<>();

	private Instant updated = Instant.now();

	@Column(name = "uuid", unique = true, nullable = false, updatable = false)
	protected UUID uuid = UUID.randomUUID();

	@Version
	private int version;

	public Word addSynonyms(Synonyms synonyms) {
		this.synonyms.add(synonyms);
		synonyms.setBaseWord(this);
		return this;
	}

	public Word baseForm(String baseForm) {
		this.setBaseForm(baseForm);
		return this;
	}

	public Word baseLang(Language language) {
		this.setBaseLang(language);
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Word)) {
			return false;
		}
		return getUuid() != null && getUuid().equals(((Word) o).getUuid());
	}

	public String getBaseForm() {
		return this.baseForm;
	}

	public Language getBaseLang() {
		return this.baseLang;
	}

	public Instant getCreated() {
		return created;
	}

	public Set<Example> getExamples() {
		return examples;
	}

	public Long getId() {
		return this.id;
	}

	public String getImportant() {
		return this.important;
	}

	public String getKindOfWord() {
		return this.kindOfWord;
	}

	public String getMainMean() {
		return mainMean;
	}

	public String getMean() {
		return this.mainMean;
	}

	public Set<Means> getMeans() {
		return means;
	}

	public String getName() {
		return this.name;
	}

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Set<Synonyms> getSynonyms() {

		return this.synonyms;
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

	@Override
	public int hashCode() {
		if (getUuid() != null) {
			return getUuid().hashCode();
		}
		return super.hashCode();
	}

	public Word id(Long id) {
		this.setId(id);
		return this;
	}

	public Word important(String important) {
		this.setImportant(important);
		return this;
	}

	public Word kindOfWord(String kindOfWord) {
		this.setKindOfWord(kindOfWord);
		return this;
	}

	public Word mean(String mean) {
		this.setMean(mean);
		return this;
	}

	public Word name(String name) {
		this.setName(name);
		return this;
	}

	public Word removeSynonyms(Synonyms synonyms) {
		this.synonyms.remove(synonyms);
		synonyms.setBaseWord(null);
		return this;
	}

	public void setBaseForm(String baseForm) {
		this.baseForm = baseForm;
	}

	public void setBaseLang(Language language) {
		this.baseLang = language;
	}

	public void setCreated(Instant created) {
		this.created = created;
	}

	public void setExamples(Set<Example> examples) {
		this.examples = examples;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setImportant(String important) {
		this.important = important;
	}

	public void setKindOfWord(String kindOfWord) {
		this.kindOfWord = kindOfWord;
	}

	public void setMainMean(String mainMean) {
		this.mainMean = mainMean;
	}

	public void setMean(String mean) {
		this.mainMean = mean;
	}

	public void setMeans(Set<Means> means) {
		this.means = means;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSynonyms(Set<Synonyms> synonyms) {
		if (this.synonyms != null) {
			this.synonyms.forEach(i -> i.setBaseWord(null));
		}
		if (synonyms != null) {
			synonyms.forEach(i -> i.setBaseWord(this));
		}
		this.synonyms = synonyms;
	}

	public void setUpdated(Instant updated) {
		this.updated = updated;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	public Word synonyms(Set<Synonyms> synonyms) {
		this.setSynonyms(synonyms);
		return this;
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "Word{" + "id=" + getId() + ", uuid='" + getUuid() + "'" + ", name='" + getName() + "'" + ", important='"
				+ getImportant() + "'" + ", mean='" + getMean() + "'" + ", baseForm='" + getBaseForm() + "'"
				+ ", kindOfWord='" + getKindOfWord() + "'" + "}";
	}
}
