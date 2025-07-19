package ua.sosna.wortschatz.wortschatzchen.web;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.sosna.wortschatz.wortschatzchen.domain.Language;
import ua.sosna.wortschatz.wortschatzchen.repository.LanguageRepo;
import ua.sosna.wortschatz.wortschatzchen.utils.EditMode;

@Controller
@RequestMapping("/languages")
public class LanguageMvc {

	private final LanguageRepo repo;

	public LanguageMvc(LanguageRepo repo) {
		super();
		this.repo = repo;
	}

	@GetMapping({ "/", "" })
	public String showLanguagesList(Model model) {
		List<Language> list = repo.findAll();
		model.addAttribute("languages", list);
		return "Languages/index";

	}

	@GetMapping({ "/create" })
	public String createLanguage(Model model) {
		Language item = new Language();
		model.addAttribute("editMode", EditMode.CREATE);
		model.addAttribute("item", item);
		return "Languages/edit";

	}

	@GetMapping({ "/edit" })
	public String createLanguage(@RequestParam(required = false) Long id, Model model) throws Exception {
		Language item = new Language();
		if (id != null) {
			Optional<Language> language = repo.findById(id);
			if (language.isEmpty()) {
				throw new Exception("not found " + id);
			} else {
				item = language.get();
			}
		} else {

			
		}

		model.addAttribute("editMode", EditMode.CREATE);
		model.addAttribute("item", item);
		return "Languages/edit";

	}

	@PostMapping("/save")
	public String saveUpdateLanguage(@ModelAttribute Language item, Model model) {
		var savedItem = repo.save(item);
		var editMode = model.getAttribute("editMode");
		// if(editMode == EditMode.CREATE) {
		model.addAttribute("currentID", savedItem.getId());
		return "redirect:/languages";

	}
	@GetMapping("/delete")
	public String deleteLanguage(@RequestParam(required = true) Long id,Model model) {
		//Long id = Long.parseUnsignedLong(idString);
		repo.deleteById(id);

		return "redirect:/languages";

	}

}
