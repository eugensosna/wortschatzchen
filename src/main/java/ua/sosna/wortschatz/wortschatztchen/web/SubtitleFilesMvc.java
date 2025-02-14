package ua.sosna.wortschatz.wortschatztchen.web;

import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import ua.sosna.wortschatz.wortschatztchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatztchen.dto.SubtitleFilesDto;
import ua.sosna.wortschatz.wortschatztchen.repository.FileRepo;
import ua.sosna.wortschatz.wortschatztchen.repository.SubtitleFileRepo;
import ua.sosna.wortschatz.wortschatztchen.storage.StorageService;
import ua.sosna.wortschatz.wortschatztchen.utils.EditMode;

@Controller
@RequestMapping("/subtitlefiles")
public class SubtitleFilesMvc {

	private final SubtitleFileRepo repo;
	private final StorageService storageService;
	private FileRepo fileRepo;

	static private final String URL_SUFFIX = "subtitlefiles";

	public SubtitleFilesMvc(SubtitleFileRepo repo, StorageService storageService, FileRepo fileRepo) {
		super();
		this.repo = repo;
		this.storageService = storageService;
		this.fileRepo = fileRepo;
	}

	@GetMapping({ "/", "" })
	public String showItemsList(Model model) {
		List<SubtitleFile> list = repo.findAll();
		model.addAttribute("items", list);
		return URL_SUFFIX + "/index";

	}

	@GetMapping({ "/create" })
	public String createItem(Model model) {
		var item = new SubtitleFilesDto(repo, storageService, fileRepo);
		model.addAttribute("editMode", EditMode.CREATE);
		model.addAttribute("item", item);
		return URL_SUFFIX + "/edit";

	}

	@GetMapping("/file")
	@ResponseBody
	public ResponseEntity<Resource> serveFileById(@RequestParam(name = "id", required = true) Long id) {
		var item = this.fileRepo.findById(id).orElseThrow(RuntimeException::new);

		Resource file = this.storageService.loadAsResource(item.getFileName());

		if (file == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);

	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = this.storageService.loadAsResource(filename);

		if (file == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@GetMapping({ "/edit" })
	public String createItem(@RequestParam(name = "id", required = false) Long id, Model model) throws Exception {
		SubtitleFilesDto item;
		EditMode mode;
		if (id != null) {
			item = SubtitleFilesDto.read(id, this.repo);

			mode = EditMode.UPDATE;

		} else {
			item = new SubtitleFilesDto(repo, storageService, fileRepo);
			mode = EditMode.CREATE;

		}

		model.addAttribute("editMode", mode);
		model.addAttribute("item", item);
		return URL_SUFFIX + "/edit";

	}

	@PostMapping("/save")
	public String saveUpdateLanguage(@ModelAttribute SubtitleFilesDto item, Model model) {
		item.setRepo(this.repo);
		item.setStorageService(storageService);

		try {
			item.save();
		} catch (NoSuchAlgorithmException | IOException | RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("currentID", item.getId());
		return "redirect:/subtitlefiles";

	}

	@GetMapping("/delete")
	public String deleteLanguage(@RequestParam(name = "id", required = true) Long id, Model model) {
		// Long id = Long.parseUnsignedLong(idString);
		repo.deleteById(id);

		return "redirect:/subtitlefiles";

	}

}
