package ua.sosna.wortschatz.wortschatztchen.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Mean.
 */
@Entity
@Table(name = "examples")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@SuppressWarnings("common-java:DuplicatedBlocks")
public class Example implements Serializable {
    private static final long serialVersionUID = 1L;


//    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "order")
    private Integer order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn()
    @JsonIgnoreProperties(value = { "subtitleFiles", "words", "synonyms", "example"}, allowSetters = true)
    private Language baseLang;
    
    @ManyToOne(fetch = FetchType.LAZY )
    @JsonIgnoreProperties(value = { "synonyms", "baseLang", "xample" }, allowSetters = true)
    private Word baseWord;


    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Language getBaseLang() {
		return baseLang;
	}

	public void setBaseLang(Language baseLang) {
		this.baseLang = baseLang;
	}

	public Word getBaseWord() {
		return baseWord;
	}

	public void setBaseWord(Word baseWord) {
		this.baseWord = baseWord;
	}

	public Long getId() {
        return this.id;
    }

    public Example id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Example uuid(UUID uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public Example name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return this.order;
    }

    public Example order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Example)) {
            return false;
        }
        return getId() != null && getId().equals(((Example) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mean{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", name='" + getName() + "'" +
            ", order=" + getOrder() +
            "}";
    }
    @PrePersist
    public void  GenerateUuid() {
    	if(uuid==null) {
    		uuid = UUID.randomUUID();
    	}
    }
}
