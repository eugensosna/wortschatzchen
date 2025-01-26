package ua.sosna.wortschatz.wortschatztchen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Synonyms.
 */
@Entity
@Table(name = "synonyms")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@SuppressWarnings("common-java:DuplicatedBlocks")
public class Synonyms implements Serializable {

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

    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn()
    @JsonIgnoreProperties(value = { "subtitleFiles", "words", "synonyms" }, allowSetters = true)
    private Language baseLang;

    @ManyToOne(fetch = FetchType.LAZY )
    @JsonIgnoreProperties(value = { "synonyms", "baseLang" }, allowSetters = true)
    private Word baseWord;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Synonyms id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Synonyms uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public Synonyms name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Language getBaseLang() {
        return this.baseLang;
    }

    public void setBaseLang(Language language) {
        this.baseLang = language;
    }

    public Synonyms baseLang(Language language) {
        this.setBaseLang(language);
        return this;
    }

    public Word getBaseWord() {
        return this.baseWord;
    }

    public void setBaseWord(Word word) {
        this.baseWord = word;
    }

    public Synonyms baseWord(Word word) {
        this.setBaseWord(word);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Synonyms)) {
            return false;
        }
        return getId() != null && getId().equals(((Synonyms) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Synonyms{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
