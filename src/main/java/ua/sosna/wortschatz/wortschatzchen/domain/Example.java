package ua.sosna.wortschatz.wortschatzchen.domain;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Mean.
 */
@Entity
@Table(name = "examples", uniqueConstraints = { @UniqueConstraint(columnNames = { "uuid" }) },

		indexes = { @Index(columnList = "uuid"), @Index(columnList = "base_word_id"), @Index(columnList = "base_lang_id") })
@EntityListeners(AuditingEntityListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Example implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "base_lang_id", nullable = true)
	@JsonIgnoreProperties(value = { "subtitleFiles", "words", "synonyms",
			"example", "examples" }, allowSetters = true)
	private Language baseLang;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false )
	@JsonIgnoreProperties(value = { "subtitleFiles", "words", "word", "synonyms",
			"example", "examples" }, allowSetters = true)
	private Word baseWord;

	@Column(nullable = false, updatable = false)
	private Instant created = Instant.now();

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "examples_id_seq")
    @SequenceGenerator(name = "examples_id_seq", sequenceName = "examples_id_seq", allocationSize = 1) // <--- CHANGE THIS TO 
	//@SequenceGenerator(name = "examples_id_seq")
	private Long id;

	@Column(name = "name", columnDefinition = "text")
	private String name;

	@Column(name = "order")
	@OrderBy("order DESC")
	private Integer order = 0;

	private Instant updated = Instant.now();
	@Column(name = "uuid", unique = true, nullable = false, updatable = false)
	protected UUID uuid = UUID.randomUUID();
	@Version
	private int version;

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

	@PrePersist
	public void GenerateUuid() {
		if (uuid == null) {
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

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

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

	@Override
	public int hashCode() {
		if (getUuid() != null) {
			return getUuid().hashCode();
		}
		return super.hashCode();
	}

	public Example id(Long id) {
		this.setId(id);
		return this;
	}

	public Example name(String name) {
		this.setName(name);
		return this;
	}

	public Example order(Integer order) {
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

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "Example{" + "id=" + getId() + ", uuid='" + getUuid() + "'" + ", name='" + getName() + "'" + ", order="
				+ getOrder() + "}";
	}

}
