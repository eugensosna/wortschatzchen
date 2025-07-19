package ua.sosna.wortschatz.wortschatzchen.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ua.sosna.wortschatz.wortschatzchen.domain.Word;
import ua.sosna.wortschatz.wortschatzchen.repository.ExamplesRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.LanguageRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.WordRepo;

@Component
public class SenzeSeparate {
	
	private final WordRepo wordRepository;

	public SenzeSeparate(WordRepo wordRepository) {
		super();
		this.wordRepository = wordRepository;
	}

	public WordRepo getWordRepository() {
		return wordRepository;
	}
	
	public void save(List<String> words) {
		for (String element: words) {
			var wordO = this.wordRepository.findByName(element);
			if (wordO.isPresent()) {
				continue;
			}
			
		var word = new Word();
		word.setName(element);
		this.wordRepository.save(word);
		
	}
	
	
	
}
	
	public static List<String> splitSentenceIntoWords(String sentence) {
        // Check for null or empty sentence to avoid NullPointerExceptions or unnecessary processing
        if (sentence == null || sentence.trim().isEmpty()) {
            return Arrays.asList(); // Return an empty list
        }

        // Define a regex pattern to split by one or more whitespace characters.
        // This handles cases with multiple spaces between words.
        // \\s+ matches one or more whitespace characters (space, tab, newline, etc.)
        String[] wordsArray = sentence.split("\\s+");

        // Process each word to remove leading/trailing punctuation and convert to lowercase (optional, but good for consistency)
        // Using a stream for concise and functional processing
        return Arrays.stream(wordsArray)
                .map(word -> {
                    // Remove any characters that are not letters, numbers, or apostrophes from the beginning and end of the word.
                    // This helps clean up words with punctuation attached (e.g., "word." becomes "word").
                    // [^\\p{L}\\p{N}']+ matches one or more characters that are NOT (letters, numbers, or apostrophes)
                    // ^ means "not" inside character class []
                    // \\p{L} is a Unicode character property for any kind of letter from any language.
                    // \\p{N} is a Unicode character property for any kind of numeric character.
                    // ' is included to keep contractions like "don't" intact.
                    String cleanedWord = word.replaceAll("^[\\p{Punct}&&[^']]+|[\\p{Punct}&&[^']]+$", "");
                    // Optionally, convert to lowercase for case-insensitive word processing
                    return cleanedWord.toLowerCase();
                })
                .filter(word -> !word.isEmpty()) // Filter out any empty strings that might result from cleaning (e.g., if only punctuation was present)
                .collect(Collectors.toList()); // Collect the processed words into a List
    }

	
	

}
