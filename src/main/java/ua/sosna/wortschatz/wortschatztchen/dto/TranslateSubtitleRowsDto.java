package ua.sosna.wortschatz.wortschatztchen.dto;

import java.util.List;
import ua.sosna.wortschatz.wortschatztchen.domain.Language;
import ua.sosna.wortschatz.wortschatztchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatztchen.domain.TimestampRow;
import ua.sosna.wortschatz.wortschatztchen.repository.LanguageRepo;
import ua.sosna.wortschatz.wortschatztchen.service.SubtitelFilesService;

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

	public void translateAllTimestampRows(SubtitelFilesService subtitelFilesService) {

		Language baseLang = subtitelFilesService.getLanguageRepo().findById(baseLangId).get();
		Language targetLang = subtitelFilesService.getLanguageRepo().findById(targetLangId).get();
		List<TimestampRow> timestamRows = subtitelFilesService.getTimestampRepo()
				.findAllBySubtitleFile(subtitleFile);

		for (int i = 0; i < timestamRows.size(); i++) {
			var Element = timestamRows.get(i);
			subtitelFilesService.translateRow(Element, targetLang);
		}

	}

}
