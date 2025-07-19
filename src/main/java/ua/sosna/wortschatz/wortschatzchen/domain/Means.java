package ua.sosna.wortschatz.wortschatzchen.domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Mean.
 */
@Entity
@Table(name = "mean")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@SuppressWarnings("common-java:DuplicatedBlocks")
public class Means implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@ManyToOne
    //@JoinColumn()
    @JsonIgnoreProperties(value = { "subtitleFiles", "words", "synonyms", "means"}, allowSetters = true)
    private Language baseLang;

	//    @ManyToOne(fetch = FetchType.LAZY )
    @ManyToOne
    @JsonIgnoreProperties(value = { "synonyms", "baseLang", "means" }, allowSetters = true)
    private Word baseWord;

	@Column(nullable = false, updatable = false)
	private Instant created = Instant.now();

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "means_seq_generator")

    @SequenceGenerator(name = "means_seq_generator", sequenceName = "means_id_seq", allocationSize = 1) // <--- CHANGE THIS TO 
	// The initial value is to account for data.sql demo data ids
	@SequenceGenerator(name = "means_id_seq")
	protected Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "order")
    private Integer order;
    
    private Instant updated = Instant.now();
    
@Column(name = "uuid", unique = true, nullable = false, updatable = false)
	protected UUID uuid = UUID.randomUUID();


    // jhipster-needle-entity-add-field - JHipster will add fields here

    @Version
	private int version;

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Means)) {
            return false;
        }
        return getId() != null && getId().equals(((Means) o).getId());
    }

	@PrePersist
    public void  GenerateUuid() {
    	if(uuid==null) {
    		uuid = UUID.randomUUID();
    	}
    }

	public Language getBaseLang() {
		return baseLang;
	}

	public Word getBaseWord() {
		return baseWord;
	}

	public Instant getCreated() {
		return created;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
        return this.name;
    }

	public Integer getOrder() {
        return this.order;
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

	public Means name(String name) {
        this.setName(name);
        return this;
    }

	public Means order(Integer order) {
        this.setOrder(order);
        return this;
    }

	public void setBaseLang(Language baseLang) {
		this.baseLang = baseLang;
	}
    public void setBaseWord(Word baseWord) {
		this.baseWord = baseWord;
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

    public void setOrder(Integer order) {
        this.order = order;
    }

    public void setUpdated(Instant updated) {
		this.updated = updated;
	}

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

   
    public void setVersion(int version) {
		this.version = version;
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
}
