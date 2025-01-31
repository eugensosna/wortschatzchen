package ua.sosna.wortschatz.wortschatztchen.web;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.sosna.wortschatz.wortschatztchen.domain.Word;
import ua.sosna.wortschatz.wortschatztchen.repository.WordRepo;
import ua.sosna.wortschatz.wortschatztchen.utils.EditMode;

@Controller
@RequestMapping("/words")
public class WordMvc {

	private final WordRepo repo;
	static private final String URL_SUFFIX = "Words"; 

	public WordMvc(WordRepo repo) {
		super();
		this.repo = repo;
	}

	@GetMapping({ "/", "" })
	public String showItemsList(Model model) {
		List<Word> list = repo.findAll();
		model.addAttribute("items", list);
		return URL_SUFFIX+"/index";

	}

	@GetMapping({ "/create" })
	public String createItem(Model model) {
		Word item = new Word();
		model.addAttribute("editMode", EditMode.CREATE);
		model.addAttribute("item", item);
		return URL_SUFFIX+"/edit";

	}

	@GetMapping({ "/edit" })
	public String editItem(@RequestParam(name = "id", required = false) Long id, Model model) throws Exception {
		Word item;
		if (id != null) {
			Optional<Word> itemO = repo.findById(id);
			if (itemO.isEmpty()) {
				throw new Exception("not found " + id);
			} else {
				item = itemO.get();
			}
		} else {
			item = new Word();

		}

		model.addAttribute("editMode", EditMode.CREATE);
		model.addAttribute("item", item);
		return URL_SUFFIX+"/edit";

	}

	@PostMapping("/save")
	public String saveUpdateLanguage(@ModelAttribute Word item, Model model) {
		var savedItem = repo.save(item);
		// if(editMode == EditMode.CREATE) {
		model.addAttribute("currentID", savedItem.getId());
		return "redirect:/words";

	}

	@GetMapping("/delete")
	public String deleteLanguage(@RequestParam(name = "id", required = true) Long id, Model model) {
		// Long id = Long.parseUnsignedLong(idString);
		repo.deleteById(id);

		return "redirect:/words";

	}

}
