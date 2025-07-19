package ua.sosna.wortschatz.wortschatzchen.service;

import java.io.IOException;

public interface TranslateService {
	
	public String translate(String text, String baseLang, String targetLang) throws IOException;

}
