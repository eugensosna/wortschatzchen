package ua.sosna.wortschatz.wortschatzchen.web;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.sosna.wortschatz.wortschatzchen.domain.Example;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;
import ua.sosna.wortschatz.wortschatzchen.repository.ExamplesRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.WordRepo;
import ua.sosna.wortschatz.wortschatzchen.utils.EditMode;

@Controller
@RequestMapping("/examples")
public class ExamplesMvc {

	private static final String WORD_ID = "wordId";
	private final ExamplesRepo repo;
	private final WordRepo repoWord;
	static private final String URL_SUFFIX = "Examples";

	public ExamplesMvc(ExamplesRepo repo, WordRepo repoWord) {
		super();
		this.repo = repo;
		this.repoWord = repoWord;
	}

	@GetMapping({ "/{wordId}/", "{wordId}" })
	public String showItemsList(@PathVariable(WORD_ID) Long wordId, Model model) {
		List<Example> list;
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
	public String createItem(@PathVariable(WORD_ID) Long wordId, Model model) {
		Example item = new Example();
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
	public String editItem(@RequestParam(required = false) Long id,
			Model model) throws Exception {
		Example item;

		item = repo.findById(id).orElseThrow(RuntimeException::new);

		Word baseWord = (Word) Hibernate.unproxy(item.getBaseWord());

		model.addAttribute("baseWord", baseWord);
		model.addAttribute("editMode", EditMode.UPDATE);
		model.addAttribute("item", item);
		return URL_SUFFIX + "/edit";

	}

	// @PostMapping("/save")
	@PostMapping("/{wordId}/save")
	public String save(@PathVariable(WORD_ID) Long wordId, @ModelAttribute Example item, Model model) {
		return saveUpdate(wordId, item, model);
	}

	@PostMapping("/save")
	public String save(@ModelAttribute Example item, Model model) {
		return saveUpdate(null, item, model);
	}

	public String saveUpdate(Long wordId, @ModelAttribute Example item, Model model) {

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

		} else if (item.getId() == null && wordId > 0) {
			baseWord = repoWord.findById(wordId).orElseThrow(RuntimeException::new);
			item.setBaseWord(baseWord);

		} else {
			var refreshedItem = repo.findById(item.getId()).orElseThrow(RuntimeException::new);
			baseWord = (Word) Hibernate.unproxy(refreshedItem.getBaseWord());
			item.setBaseWord(baseWord);
		}

		// model.addAttribute("baseWord", item.getBaseWord());

		var savedItem = repo.save(item);
		// if(editMode == EditMode.CREATE) {
		model.addAttribute("currentID", savedItem.getId());
		// return "redirect:/words";
		String redirect = "redirect:/means/" + baseWord.getId();
		System.out.println(redirect);
		return redirect;

	}

	@GetMapping("/delete")
	public String deleteItem(@RequestParam(required = true) Long id, Model model) {
		// Long id = Long.parseUnsignedLong(idString);
		Example itemToDelete = repo.findById(id).orElseThrow(RuntimeException::new);
		var baseWord = itemToDelete.getBaseWord();

		repo.deleteById(id);

		return "redirect:/means/" + baseWord.getId();

	}

}
