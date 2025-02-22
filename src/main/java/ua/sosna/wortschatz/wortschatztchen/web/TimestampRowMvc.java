package ua.sosna.wortschatz.wortschatztchen.web;

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

import ua.sosna.wortschatz.wortschatztchen.domain.Example;
import ua.sosna.wortschatz.wortschatztchen.domain.Language;
import ua.sosna.wortschatz.wortschatztchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatztchen.domain.TimestampRow;
import ua.sosna.wortschatz.wortschatztchen.domain.TranslatedWord;
import ua.sosna.wortschatz.wortschatztchen.domain.Word;
import ua.sosna.wortschatz.wortschatztchen.dto.TranslateSubtitleRowsDto;
import ua.sosna.wortschatz.wortschatztchen.repository.ExamplesRepo;
import ua.sosna.wortschatz.wortschatztchen.repository.SubtitleFileRepo;
import ua.sosna.wortschatz.wortschatztchen.repository.TimestampRowRepo;
import ua.sosna.wortschatz.wortschatztchen.repository.WordRepo;
import ua.sosna.wortschatz.wortschatztchen.service.SubtitelFilesService;
import ua.sosna.wortschatz.wortschatztchen.utils.EditMode;

@Controller
@RequestMapping("/subtitlerows")
public class TimestampRowMvc {

	private final TimestampRowRepo repo;
	private final SubtitleFileRepo repoBase;
	static private final String URL_SUFFIX = "Subtitlerows";
	private SubtitelFilesService subtitleFilesService;

	public TimestampRowMvc(TimestampRowRepo repo, SubtitleFileRepo repoBase,
			SubtitelFilesService subtitleFilesService) {
		super();
		this.repo = repo;
		this.repoBase = repoBase;
		this.subtitleFilesService = subtitleFilesService;
	}

	@GetMapping({ "/{baseId}/", "{baseId}" })
	public String index(@PathVariable("baseId") Long baseId, Model model) {
		List<TimestampRow> list;
		SubtitleFile baseItem = null;

		if (baseId != null) {
			baseItem = repoBase.findById(baseId).orElseThrow(RuntimeException::new);

			list = repo.findAllBySubtitleFile(baseItem);
		} else {
			list = repo.findAll();
		}

		TranslateSubtitleRowsDto translateSubtitleRowsDto = new TranslateSubtitleRowsDto();
		translateSubtitleRowsDto.setBaseLangId(baseItem.getBaseLang().getId());
		translateSubtitleRowsDto.setSubtitleFile(baseItem);

		List<Language> languages = subtitleFilesService.getLanguageRepo().findAll();

		model.addAttribute("languages", languages);
		model.addAttribute("baseItem", baseItem);
		model.addAttribute("items", list);
		model.addAttribute("translateSubtitleRowsDto", translateSubtitleRowsDto);
		return URL_SUFFIX + "/index";
	}

	@GetMapping({ "/{baseId}/create" })
	public String createItem(@PathVariable("baseId") Long baseId, Model model) {
		TimestampRow item = new TimestampRow();
		if (model.containsAttribute("baseItem")) {
			SubtitleFile baseItem = (SubtitleFile) model.getAttribute("baseItem");
			model.addAttribute("baseItem", baseItem);

			item.setSubtitleFile(baseItem);
		} else {
			SubtitleFile baseItem = repoBase.findById(baseId).orElseThrow(RuntimeException::new);
			model.addAttribute("baseItem", baseItem);
		}

		model.addAttribute("editMode", EditMode.CREATE);
		model.addAttribute("item", item);
		return URL_SUFFIX + "/edit";

	}

	@GetMapping({ "/edit" })
	public String editItem(@RequestParam(name = "id", required = false) Long id, Model model) throws Exception {
		TimestampRow item;

		item = repo.findById(id).orElseThrow(RuntimeException::new);

		SubtitleFile baseItem = (SubtitleFile) Hibernate.unproxy(item.getSubtitleFile());
		List<TranslatedWord> translatedRows = subtitleFilesService.getTranslatedWordRepo().findByUuidClass(item.getUuid());

		model.addAttribute("baseItem", baseItem);
		model.addAttribute("editMode", EditMode.UPDATE);
		model.addAttribute("item", item);
		model.addAttribute("translatedRows", translatedRows);
		return URL_SUFFIX + "/edit";

	}

	// @PostMapping("/save")
	@PostMapping("/{baseId}/save")
	public String save(@PathVariable("baseId") Long baseId, @ModelAttribute TimestampRow item, Model model) {
		return saveUpdate(baseId, item, model);
	}

	@PostMapping("/save")
	public String save(@ModelAttribute TimestampRow item, Model model) {
		return saveUpdate(null, item, model);
	}

	public String saveUpdate(Long baseId, @ModelAttribute TimestampRow item, Model model) {

		SubtitleFile baseItem;

		if (model.containsAttribute("baseItem")) {
			baseItem = (SubtitleFile) model.getAttribute("baseItem");
			item.setSubtitleFile(baseItem);
			/*
			 * } else { if (!(item.getBaseWord() != null && item.getBaseWord().getId() ==
			 * baseId)) { baseItem =
			 * repoWord.findById(baseId).orElseThrow(RuntimeException::new);
			 * item.setBaseWord(baseItem);
			 * 
			 * }
			 */

		} else if (item.getId() == null && baseId > 0) {
			baseItem = repoBase.findById(baseId).orElseThrow(RuntimeException::new);
			item.setSubtitleFile(baseItem);

		} else if (item.getId() != null) {
			var refreshedItem = repo.findById(item.getId()).orElseThrow(RuntimeException::new);
			baseItem = (SubtitleFile) Hibernate.unproxy(refreshedItem.getSubtitleFile());
			item.setSubtitleFile(baseItem);
		}

		// model.addAttribute("baseItem", item.getBaseWord());

		var savedItem = repo.save(item);
		baseItem = (SubtitleFile) Hibernate.unproxy(savedItem.getSubtitleFile());
		// if(editMode == EditMode.CREATE) {
		model.addAttribute("currentID", savedItem.getId());
		// return "redirect:/words";
		String redirect = "redirect:/subtitlerows/" + baseItem.getId() + "/";
		System.out.println(redirect);
		return redirect;

	}

	@GetMapping("/delete")
	public String deleteItem(@RequestParam(name = "id", required = true) Long id, Model model) {
		// Long id = Long.parseUnsignedLong(idString);
		TimestampRow itemToDelete = repo.findById(id).orElseThrow(RuntimeException::new);
		var baseItem = (SubtitleFile) Hibernate.unproxy(itemToDelete.getSubtitleFile());

		repo.deleteById(id);

		return "redirect:/subtitlerows/" + baseItem.getId() + "/";

	}

	// @PostMapping("/save")
	@RequestMapping("/{baseId}/parsesubtitels")
	public String parseSubtitels(@PathVariable("baseId") Long baseId, Model model) throws Throwable {
		subtitleFilesService.ParseFileToRows(baseId);
		String redirect = "redirect:/subtitlerows/" + baseId + "/";
		return redirect;
		}
	@PostMapping("/{baseId}/translaterows")
	public String translateSubtitelsRows(@PathVariable("baseId") Long baseId,@ModelAttribute TranslateSubtitleRowsDto item, Model model) {
		item.setSubtitleFile(subtitleFilesService.getSubtitleRepo().findById(baseId).get());
		item.translateAllTimestampRows(subtitleFilesService);
		
		
		//Long baseId = item.getSubtitleFile().getId();
		//subtitleFilesService.ParseFileToRows(baseId);
		String redirect = "redirect:/subtitlerows/" + baseId + "/";
		return redirect;
	}

}
