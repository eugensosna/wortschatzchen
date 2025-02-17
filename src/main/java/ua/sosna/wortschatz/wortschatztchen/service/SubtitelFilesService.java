package ua.sosna.wortschatz.wortschatztchen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import ua.sosna.wortschatz.wortschatztchen.domain.Language;
import ua.sosna.wortschatz.wortschatztchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatztchen.domain.TimestampRow;
import ua.sosna.wortschatz.wortschatztchen.domain.TranslatedWord;
import ua.sosna.wortschatz.wortschatztchen.repository.LanguageRepo;
import ua.sosna.wortschatz.wortschatztchen.repository.SubtitleFileRepo;
import ua.sosna.wortschatz.wortschatztchen.repository.TimestampRowRepo;
import ua.sosna.wortschatz.wortschatztchen.repository.TranslatedWordRepo;
import ua.sosna.wortschatz.wortschatztchen.storage.StorageService;
import ua.sosna.wortschatz.wortschatztchen.utils.NetflixSubtitle;

@Component
public class SubtitelFilesService {
	private SubtitleFileRepo subtitleRepo;
	private TimestampRowRepo timestampRepo;
	private StorageService storage;
	private final LanguageRepo languageRepo;
	private TranslateService translateService;
	public TranslateService getTranslateService() {
		return translateService;
	}

	public void setTranslateService(TranslateService translateService) {
		this.translateService = translateService;
	}

	public TranslatedWordRepo getTranslatedWordRepo() {
		return translatedWordRepo;
	}

	public void setTranslatedWordRepo(TranslatedWordRepo translatedWordRepo) {
		this.translatedWordRepo = translatedWordRepo;
	}

	private TranslatedWordRepo translatedWordRepo;

	public SubtitelFilesService(SubtitleFileRepo subtitleRepo, TimestampRowRepo timestampRepo, StorageService storage,
			LanguageRepo languageRepo, TranslateService translateService, TranslatedWordRepo translatedWordRepo) {
		super();
		this.translatedWordRepo = translatedWordRepo;
		this.setSubtitleRepo(subtitleRepo);
		this.setTimestampRepo(timestampRepo);
		this.setStorage(storage);
		this.languageRepo = languageRepo;
		this.translateService = translateService;
	}

	public void ParseFileToRows(Long subtitleFileId) throws Throwable {
		var item = subtitleRepo.findById(subtitleFileId).orElseThrow(RuntimeException::new);
		ParseFileToRows(item);

	}

	public void ParseFileToRows(SubtitleFile subtitleFile) throws Throwable {

		if (subtitleFile.getFile() == null) {
			throw new Exception("files not found " + subtitleFile.getFile());
		}

		var resource = storage.loadAsResource(subtitleFile.getFile().getFileName());
//		resource.getInputStream()
		List<NetflixSubtitle> netflixRows = NetflixSubtitle.parseXML(resource.getInputStream());

		for (int i = 0; i < netflixRows.size(); i++) {
			NetflixSubtitle element = netflixRows.get(i);
			var rowDB = new TimestampRow();
			rowDB.setSubtitleFile(subtitleFile);
			rowDB.setStartTime(element.getBegin());
			rowDB.setEndTime(element.getEnd());
			rowDB.setText(element.getContent());

			timestampRepo.save(rowDB);

		}

	}

	public SubtitleFileRepo getSubtitleRepo() {
		return subtitleRepo;
	}

	public void setSubtitleRepo(SubtitleFileRepo subtitleRepo) {
		this.subtitleRepo = subtitleRepo;
	}

	public TimestampRowRepo getTimestampRepo() {
		return timestampRepo;
	}

	public void setTimestampRepo(TimestampRowRepo timestampRepo) {
		this.timestampRepo = timestampRepo;
	}

	public StorageService getStorage() {
		return storage;
	}

	public void setStorage(StorageService storage) {
		this.storage = storage;
	}

	public LanguageRepo getLanguageRepo() {
		return languageRepo;
	}

	public void translateRow(TimestampRow row, Language targetLang) {
		TranslatedWord translatedRow;

		var baseLang = row.getSubtitleFile().getBaseLang();
		translatedRow = new TranslatedWord();

		Optional<TranslatedWord> optionalRow = this.translatedWordRepo.findFirstByBaseLangAndTargetLangAndName(baseLang,
				targetLang, row.getText());

		if (optionalRow.isPresent()) {
			return;
		}
		String translatedText = translateService.translate(row.getText(), baseLang.getShortName(),
				targetLang.getShortName());
		translatedRow.setBaseLang(baseLang);
		translatedRow.setName(row.getText());
		translatedRow.setTargetLang(targetLang);
		translatedRow.setUuidClass(row.getUuid());
		translatedRow.setNameClass(row.getClass().toString());
		translatedRow.setTranslatedName(translatedText);

		translatedWordRepo.save(translatedRow);

	}

}
