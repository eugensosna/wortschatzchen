package ua.sosna.wortschatz.wortschatzchen.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Lob;
import ua.sosna.wortschatz.wortschatzchen.domain.Example;
import ua.sosna.wortschatz.wortschatzchen.domain.Language;

public class WordDTO implements Serializable {

	@Lob
	private String baseForm;

	private Language baseLanguage;

	private Set<ExampleDTO> examples = new HashSet<>();

	private Set<String> examplesUUID = new HashSet<>();

	private Long id;

	@Lob
	private String important;

	private String kindOfWord;

	private String languageUUID;
	
	@Lob
	private String mean;

	@Lob
	private String name;
	
	private String uuid;
	
	private int version;
	

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof WordDTO)) {
			return false;
		}

		WordDTO wordDTO = (WordDTO) o;
		if (this.id == null) {
			return false;
		}
		return Objects.equals(this.id, wordDTO.id);
	}

	public String getBaseForm() {
		return baseForm;
	}

	public Language getBaseLanguage() {
		return baseLanguage;
	}

	public Set<ExampleDTO> getExamples() {
		return examples;
	}

	public Set<String> getExamplesUUID() {
		return examplesUUID;
	}

	public Long getId() {
		return id;
	}

	public String getImportant() {
		return important;
	}

	public String getKindOfWord() {
		return kindOfWord;
	}

	public String getLanguageUUID() {
		return languageUUID;
	}

	public String getMean() {
		return mean;
	}

	public String getName() {
		return name;
	}

	public String getUuid() {
		return uuid;
	}

	public int getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.uuid);
	}

	public void setBaseForm(String baseForm) {
		this.baseForm = baseForm;
	}

	public void setBaseLanguage(Language language) {
		this.baseLanguage = language;
	}

	public void setExamples(Set<ExampleDTO> examples) {
		this.examples = examples;
	}

	public void setExamplesUUID(Set<String> examplesUUID) {
		this.examplesUUID = examplesUUID;
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

	public void setLanguageUUID(String languageUUID) {
		this.languageUUID = languageUUID;
	}

	public void setMean(String mean) {
		this.mean = mean;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "WordDTO{" + "id=" + getId() + ", uuid='" + getUuid() + "'" + ", name='" + getName() + "'"
				+ ", important='" + getImportant() + "'" + ", mean='" + getMean() + "'" + ", baseForm='" + getBaseForm()
				+ "'" + ", kindOfWord='" + getKindOfWord() + "'" + ", language=" + getBaseLanguage() + "}";
	}
	
}
