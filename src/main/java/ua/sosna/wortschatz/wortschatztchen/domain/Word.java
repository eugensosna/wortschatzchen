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
 * A Word.
 */
@Entity
@Table(name = "word")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@SuppressWarnings("common-java:DuplicatedBlocks")
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "important")
    private String important;

    @Column(name = "main_mean")
    private String mainMean;

    @Column(name = "base_form")
    private String baseForm;

    @Column(name = "kind_of_word")
    private String kindOfWord;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "baseWord")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "baseLang", "baseWord" }, allowSetters = true)
    private Set<Synonyms> synonyms = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subtitleFiles", "words", "synonyms" }, allowSetters = true)
    private Language baseLang;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Word id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Word uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public Word name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImportant() {
        return this.important;
    }

    public Word important(String important) {
        this.setImportant(important);
        return this;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    public String getMean() {
        return this.mainMean;
    }

    public Word mean(String mean) {
        this.setMean(mean);
        return this;
    }

    public void setMean(String mean) {
        this.mainMean = mean;
    }

    public String getBaseForm() {
        return this.baseForm;
    }

    public Word baseForm(String baseForm) {
        this.setBaseForm(baseForm);
        return this;
    }

    public void setBaseForm(String baseForm) {
        this.baseForm = baseForm;
    }

    public String getKindOfWord() {
        return this.kindOfWord;
    }

    public Word kindOfWord(String kindOfWord) {
        this.setKindOfWord(kindOfWord);
        return this;
    }

    public void setKindOfWord(String kindOfWord) {
        this.kindOfWord = kindOfWord;
    }

    public Set<Synonyms> getSynonyms() {
        return this.synonyms;
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

    public Word synonyms(Set<Synonyms> synonyms) {
        this.setSynonyms(synonyms);
        return this;
    }

    public Word addSynonyms(Synonyms synonyms) {
        this.synonyms.add(synonyms);
        synonyms.setBaseWord(this);
        return this;
    }

    public Word removeSynonyms(Synonyms synonyms) {
        this.synonyms.remove(synonyms);
        synonyms.setBaseWord(null);
        return this;
    }

    public Language getBaseLang() {
        return this.baseLang;
    }

    public void setBaseLang(Language language) {
        this.baseLang = language;
    }

    public Word baseLang(Language language) {
        this.setBaseLang(language);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Word)) {
            return false;
        }
        return getId() != null && getId().equals(((Word) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Word{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", important='" + getImportant() + "'" +
            ", mean='" + getMean() + "'" +
            ", baseForm='" + getBaseForm() + "'" +
            ", kindOfWord='" + getKindOfWord() + "'" +
            "}";
    }
}
