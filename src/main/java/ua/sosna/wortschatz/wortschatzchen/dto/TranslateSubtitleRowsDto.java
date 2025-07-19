package ua.sosna.wortschatz.wortschatzchen.dto;

import java.util.List;

import org.hibernate.Hibernate;

import ua.sosna.wortschatz.wortschatzchen.domain.Language;
import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatzchen.domain.TimestampRow;
import ua.sosna.wortschatz.wortschatzchen.repository.LanguageRepo;
import ua.sosna.wortschatz.wortschatzchen.service.SubtitelFilesParseService;

public class TranslateSubtitleRowsDto {
	private Long baseLangId;
	private Long targetLangId;
	private SubtitleFile subtitleFile;

	/**
	 * @param languageRepo
	 * @param subtitelFilesService
	 */
	public TranslateSubtitleRowsDto() {
		super();
	}

	public Long getBaseLangId() {
		return baseLangId;
	}

	public void setBaseLangId(Long baseLangId) {
		this.baseLangId = baseLangId;
	}

	public Long getTargetLangId() {
		return targetLangId;
	}

	public void setTargetLangId(Long targetLangId) {
		this.targetLangId = targetLangId;
	}

	public SubtitleFile getSubtitleFile() {
		return subtitleFile;
	}

	public void setSubtitleFile(SubtitleFile subtitleFile) {
		this.subtitleFile = subtitleFile;
	}

	public void translateTimestampRow(SubtitelFilesParseService subtitelFilesService, TimestampRow row) {
		Language baseLang = (Language) Hibernate
				.unproxy(subtitelFilesService.getLanguageRepo().findById(baseLangId).get());
		List<Language> languages = subtitelFilesService.getLanguageRepo().findAll();
		for (int iLang = 0; iLang < languages.size(); iLang++) {
			var iterLang = (Language) Hibernate.unproxy(languages.get(iLang));
			if (iterLang.getShortName() == baseLang.getShortName()) {
				continue;
			} else {
				subtitelFilesService.translateRow(row, iterLang);
			}
		}

	}

	public void translateAllTimestampRows(SubtitelFilesParseService subtitelFilesService) {

		Language baseLang = subtitelFilesService.getLanguageRepo().findById(baseLangId).get();
		Language baseLangUnproxy = (Language) Hibernate.unproxy(baseLang);
		String shortName = baseLangUnproxy.getShortName();

		List<Language> languages = subtitelFilesService.getLanguageRepo().findAll();
		// Language targetLang =
		// subtitelFilesService.getLanguageRepo().findById(targetLangId).get();
		List<TimestampRow> timestamRows = subtitelFilesService.getTimestampRepo().findAllBySubtitleFile(subtitleFile);

		for (int i = 0; i < timestamRows.size(); i++) {
			var row = timestamRows.get(i);

			for (int iLang = 0; iLang < languages.size(); iLang++) {
				var iterLang = (Language) Hibernate.unproxy(languages.get(iLang));
				if (iterLang.getShortName() == shortName) {
					continue;
				} else {
					subtitelFilesService.translateRow(row, iterLang);
				}
			}
			// subtitelFilesService.translateRow(Element, targetLang);
		}
	}

}
