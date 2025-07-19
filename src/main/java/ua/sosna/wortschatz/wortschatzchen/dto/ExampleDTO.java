package ua.sosna.wortschatz.wortschatzchen.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ua.sosna.wortschatz.wortschatzchen.domain.Language;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;

public class ExampleDTO {
	private Language baseLanguage;
	private String baseLanguageUUID;
	@JsonIgnore
	private Word baseWord;
	private String baseWordUUID;
	private Long id;

	private String name;

	private int order;

	public ExampleDTO(Language baseLanguage, Word baseWord, Long id, String name, int order, String uUID) {
		super();
		this.baseLanguage = baseLanguage;
		this.baseWord = baseWord;
		this.id = id;
		this.name = name;
		this.order = order;
		UUID = uUID;
	}

	private String UUID;

	public ExampleDTO() {
		super();
	}

	public ExampleDTO(Long id, String name, int order, String baseWordUuid, String baseLanguageUUID, String uuid) {
		super();
		this.id = id;
		this.name = name;
		this.order = order;
		this.baseWordUUID = baseWordUuid;
		this.baseLanguageUUID =

		this.UUID = uuid;
	}

	public Language getBaseLanguage() {
		return baseLanguage;
	}

	public String getBaseLanguageUUID() {
		return baseLanguageUUID;
	}

	public Word getBaseWord() {
		return baseWord;
	}


	public String getBaseWordUUID() {
		return baseWordUUID;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getOrder() {
		return order;
	}


	public String getUUID() {
		return UUID;
	}

	public void setBaseLanguage(Language baseLanguage) {
		this.baseLanguage = baseLanguage;
	}

	public void setBaseLanguageUUID(String baseLanguageUUID) {
		this.baseLanguageUUID = baseLanguageUUID;
	}

	public void setBaseWord(Word baseWord) {
		this.baseWord = baseWord;
	}


	public void setBaseWordUUID(String baseWordUUID) {
		this.baseWordUUID = baseWordUUID;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public void setUUID(String uUID) {
		this.UUID = uUID;
	}

}
