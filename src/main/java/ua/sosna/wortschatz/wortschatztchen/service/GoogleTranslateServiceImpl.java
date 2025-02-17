package ua.sosna.wortschatz.wortschatztchen.service;

import org.springframework.stereotype.Component;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import ua.sosna.wortschatz.wortschatztchen.repository.LanguageRepo;
@Component
public class GoogleTranslateServiceImpl implements TranslateService {
	 private final  Translate translate;
//	 private final LanguageRepo languageRepo;
	 

	
	
	public GoogleTranslateServiceImpl(){
		super();
		this.translate = TranslateOptions.getDefaultInstance().getService();
		
	}


	public String translate(String text, String baseLanguage, String targetLanguage) {
	        Translation translation = translate.translate(
	                text,
	                Translate.TranslateOption.sourceLanguage(baseLanguage),
	                Translate.TranslateOption.targetLanguage(targetLanguage)
	        );
	        return translation.getTranslatedText();
	    }
	

}
