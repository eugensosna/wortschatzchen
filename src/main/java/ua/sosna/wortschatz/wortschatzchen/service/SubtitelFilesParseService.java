package ua.sosna.wortschatz.wortschatzchen.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import ua.sosna.wortschatz.wortschatzchen.domain.Language;
import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatzchen.domain.TimestampRow;
import ua.sosna.wortschatz.wortschatzchen.domain.TranslatedWord;
import ua.sosna.wortschatz.wortschatzchen.repository.LanguageRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.SubtitleFileRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.TimestampRowRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.TranslatedWordRepo;
import ua.sosna.wortschatz.wortschatzchen.storage.StorageService;
import ua.sosna.wortschatz.wortschatzchen.utils.NetflixSubtitle;

@Component
public class SubtitelFilesParseService {
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

	public SubtitelFilesParseService(SubtitleFileRepo subtitleRepo, TimestampRowRepo timestampRepo, StorageService storage,
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
		// resource.getInputStream()
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
		Language basLangUnproxy = (Language) Hibernate.unproxy(baseLang);
		translatedRow = new TranslatedWord();

		Optional<TranslatedWord> optionalRow = this.translatedWordRepo.findFirstByBaseLangAndTargetLangAndName(baseLang,
				targetLang, row.getText());

		if (optionalRow.isPresent()) {
			return;
		}

		translatedRow.setBaseLang(basLangUnproxy);
		translatedRow.setName(row.getText());
		translatedRow.setTargetLang(targetLang);
		translatedRow.setUuidClass(row.getUuid());
		translatedRow.setNameClass(row.getClass().toString());

		String translatedText;
		try {
			translatedText = translateService.translate(row.getText(), basLangUnproxy.getShortName(),
					targetLang.getShortName());
			translatedRow.setTranslatedName(translatedText);
			translatedWordRepo.save(translatedRow);

		} catch (IOException e) {
			e.printStackTrace();
		}




	}

}
