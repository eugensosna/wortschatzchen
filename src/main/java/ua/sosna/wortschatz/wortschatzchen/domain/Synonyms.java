package ua.sosna.wortschatz.wortschatzchen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
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

    @Serial
    private static final long serialVersionUID = 1L;
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn()
    @JsonIgnoreProperties(value = { "subtitleFiles", "words", "synonyms" }, allowSetters = true)
    private Language baseLang;

	@ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "base_word_id")
    //@JsonIgnoreProperties(value = { "baseLang" }, allowSetters = true)
    private Word baseWord;

	@Column(nullable = false, updatable = false)
	private Instant created = Instant.now();

	@Id
	@SequenceGenerator(name ="synonyms_id_seq", sequenceName =  "synonyms_id_seq", allocationSize = 1)
	// The initial value is to account for data.sql demo data ids
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="synonyms_id_seq")
	protected Long id;

    @Column(name = "name")
    private String name;

    private Instant updated = Instant.now();

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
	protected UUID uuid = UUID.randomUUID();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    @Version
	private int version;

    public Synonyms baseLang(Language language) {
        this.setBaseLang(language);
        return this;
    }

	public Synonyms baseWord(Word word) {
        this.setBaseWord(word);
        return this;
    }

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

	public Language getBaseLang() {
        return this.baseLang;
    }

	public Word getBaseWord() {
        return this.baseWord;
    }

	public Instant getCreated() {
		return created;
	}

	public Long getId() {
        return this.id;
    }

	public String getName() {
        return this.name;
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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    public Synonyms id(Long id) {
        this.setId(id);
        return this;
    }

    public Synonyms name(String name) {
        this.setName(name);
        return this;
    }

    @PrePersist
    public void PreSave() {
    	if (this.uuid==null) {
    		this.uuid = UUID.randomUUID();
    	}
    }

    public void setBaseLang(Language language) {
        this.baseLang = language;
    }

    public void setBaseWord(Word word) {
        this.baseWord = word;
    }

    public void setCreated(Instant created) {
		this.created = created;
	}

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setUpdated(Instant updated) {
		this.updated = updated;
	}

    public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

    public void setVersion(int version) {
		this.version = version;
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
