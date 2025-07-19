package ua.sosna.wortschatz.wortschatzchen.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Version;

@Entity
public class TranslatedWord  {
	@Id
	@SequenceGenerator(name = "translated_word_id_seq", sequenceName = "translated_word_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "translated_word_id_seq")
	protected Long id;

	@Column(name = "uuid", unique = true, nullable = false, updatable = false)
	protected UUID uuid = UUID.randomUUID();

	public UUID getUuid() {
		return uuid;
	}

	@Column(nullable = false, updatable = false)
	private Instant created = Instant.now();

	private Instant updated = Instant.now();

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Version
	private int version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "base_lang_id")
	Language baseLang;

	@ManyToOne()
	@JoinColumn(name = "target_lang_id")
	Language targetLang;

	@Column(name = "name")
	String name;

	@Column(name = "translated_name")
	String translatedName;
	
	
	
	String nameClass;
	
	@Column(name = "uuid_class")
    private UUID uuidClass;

	public Language getBaseLang() {
		return baseLang;
	}

	public void setBaseLang(Language baseLang) {
		this.baseLang = baseLang;
	}

	public Language getTargetLang() {
		return targetLang;
	}

	public void setTargetLang(Language targetLang) {
		this.targetLang = targetLang;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTranslatedName() {
		return translatedName;
	}

	public void setTranslatedName(String translatedName) {
		this.translatedName = translatedName;
	}

	public String getNameClass() {
		return nameClass;
	}

	public void setNameClass(String nameClass) {
		this.nameClass = nameClass;
	}

	public UUID getUuidClass() {
		return uuidClass;
	}

	public void setUuidClass(UUID uUID) {
		this.uuidClass = uUID;
	}

	/**
	 * 
	 */
	public TranslatedWord() {
		super();
	}
	@PrePersist
	public void PreSave() {
			this.updated = Instant.now();
		
	}

}
