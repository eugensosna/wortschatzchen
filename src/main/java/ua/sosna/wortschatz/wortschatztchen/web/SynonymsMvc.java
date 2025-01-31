package ua.sosna.wortschatz.wortschatztchen.web;

import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.sosna.wortschatz.wortschatztchen.domain.Synonyms;
import ua.sosna.wortschatz.wortschatztchen.domain.Word;
import ua.sosna.wortschatz.wortschatztchen.repository.SynonymsRepo;
import ua.sosna.wortschatz.wortschatztchen.repository.WordRepo;
import ua.sosna.wortschatz.wortschatztchen.utils.EditMode;

@Controller
@RequestMapping("/synonyms")
public class SynonymsMvc {

	private final SynonymsRepo repo;
	private final WordRepo repoWord;
	static private final String URL_SUFFIX = "Synonyms";

	public SynonymsMvc(SynonymsRepo repo, WordRepo repoWord) {
		super();
		this.repo = repo;
		this.repoWord = repoWord;
	}

	@GetMapping({ "/{wordId}/", "{wordId}" })
	public String showItemsList(@PathVariable("wordId") Long wordId, Model model) {
		List<Synonyms> list;
		Word baseWord = null;

		if (wordId != null) {
			baseWord = repoWord.findById(wordId).orElseThrow(RuntimeException::new);

			list = repo.findAllByBaseWord(baseWord);
		} else {
			list = repo.findAll();
		}
		model.addAttribute("baseWord", baseWord);
		model.addAttribute("items", list);
		return URL_SUFFIX + "/index";
	}

	@GetMapping({ "/{wordId}/create" })
	public String createItem(@PathVariable("wordId") Long wordId, Model model) {
		Synonyms item = new Synonyms();
		if (model.containsAttribute("baseWord")) {
			Word baseWord = (Word) model.getAttribute("baseWord");
			model.addAttribute("baseWord", baseWord);

			item.setBaseWord(baseWord);
		} else {
			Word baseWord = repoWord.findById(wordId).orElseThrow(RuntimeException::new);
			model.addAttribute("baseWord", baseWord);
		}

		model.addAttribute("editMode", EditMode.CREATE);
		model.addAttribute("item", item);
		return URL_SUFFIX + "/edit";

	}

	@GetMapping({ "/edit" })
	public String editItem(@RequestParam(name = "id", required = false) Long id,
			Model model) throws Exception {
		Synonyms item;
		
		item = repo.findById(id).orElseThrow(RuntimeException::new);
		
		Word baseWord = (Word) Hibernate.unproxy(item.getBaseWord());
		
		
		

		
		model.addAttribute("baseWord", baseWord);
		model.addAttribute("editMode", EditMode.UPDATE);
		model.addAttribute("item", item);
		return URL_SUFFIX + "/edit";

	}

	//@PostMapping("/save")
	@PostMapping("/{wordId}/save")
	public String save(@PathVariable("wordId") Long wordId, @ModelAttribute Synonyms item, Model model) {
		return saveUpdate(wordId, item, model);
	}
	
	
	@PostMapping("/save")
	public String save( @ModelAttribute Synonyms item, Model model) {
		return saveUpdate(null, item, model);
	}
	

	public String saveUpdate(Long wordId, @ModelAttribute Synonyms item, Model model) {

		Word baseWord;

		if (model.containsAttribute("baseWord")) {
			baseWord = (Word) model.getAttribute("baseWord");
			item.setBaseWord(baseWord);
			/*
			 * } else { if (!(item.getBaseWord() != null && item.getBaseWord().getId() ==
			 * wordId)) { baseWord =
			 * repoWord.findById(wordId).orElseThrow(RuntimeException::new);
			 * item.setBaseWord(baseWord);
			 * 
			 * }
			 */

		} else if (item.getId()==null && wordId>0) {
			baseWord =repoWord.findById(wordId).orElseThrow(RuntimeException::new);
			item.setBaseWord(baseWord);
			
			
		}
		else {
			var refreshedItem = repo.findById(item.getId()).orElseThrow(RuntimeException::new);
			baseWord = (Word) Hibernate.unproxy(refreshedItem.getBaseWord());
			item.setBaseWord(baseWord);
		}
		
		
		//model.addAttribute("baseWord", item.getBaseWord());

		var savedItem = repo.save(item);
		// if(editMode == EditMode.CREATE) {
		model.addAttribute("currentID", savedItem.getId());
		//return "redirect:/words";
		String redirect = "redirect:/synonyms/"+ baseWord.getId() ;
		System.out.println(redirect);
		return redirect;

	}

	
	
	
	@GetMapping("/delete")
	public String deleteItem(@RequestParam(name = "id", required = true) Long id, Model model) {
		// Long id = Long.parseUnsignedLong(idString);
		var itemToDelete = repo.findById(id).orElseThrow(RuntimeException::new);
		var baseWord = itemToDelete.getBaseWord();
		
		repo.deleteById(id);

		return "redirect:/synonyms/" + baseWord.getId();
		

	}

}
