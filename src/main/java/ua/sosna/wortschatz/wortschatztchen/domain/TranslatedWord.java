package ua.sosna.wortschatz.wortschatztchen.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class TranslatedWord extends AbstractEntity {

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

}
