package ua.sosna.wortschatz.wortschatztchen.web;


import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.core.io.Resource;
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
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletResponse;
import ua.sosna.wortschatz.wortschatztchen.domain.File;
import ua.sosna.wortschatz.wortschatztchen.domain.Language;
import ua.sosna.wortschatz.wortschatztchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatztchen.dto.SubtitleFilesDto;
import ua.sosna.wortschatz.wortschatztchen.dto.UploadFileResponse;
import ua.sosna.wortschatz.wortschatztchen.repository.FileRepo;
import ua.sosna.wortschatz.wortschatztchen.repository.LanguageRepo;
import ua.sosna.wortschatz.wortschatztchen.repository.SubtitleFileRepo;
import ua.sosna.wortschatz.wortschatztchen.service.SubtitelFilesService;
import ua.sosna.wortschatz.wortschatztchen.storage.StorageService;
import ua.sosna.wortschatz.wortschatztchen.utils.EditMode;

@Controller
@RequestMapping("/subtitlefiles")
public class SubtitleFilesMvc {

	private final SubtitleFileRepo repo;
	private final StorageService storageService;
	private FileRepo fileRepo;
	private SubtitelFilesService subtitleFilesService;

	static private final String URL_SUFFIX = "subtitlefiles";

	public SubtitleFilesMvc(SubtitleFileRepo repo, StorageService storageService, FileRepo fileRepo, SubtitelFilesService subtitleFilesService) {
		super();
		this.repo = repo;
		this.storageService = storageService;
		this.subtitleFilesService= subtitleFilesService;
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
		List<Language> languages = subtitleFilesService.getLanguageRepo().findAll();
		model.addAttribute("editMode", EditMode.CREATE);
		model.addAttribute("item", item);
		model.addAttribute("languages", languages);
		
		
		return URL_SUFFIX + "/edit";

	}

	@GetMapping("/file")
	@ResponseBody
	public RedirectView serveFileById(@RequestParam(name = "id", required = true) Long id, HttpServletResponse response) throws IOException {
		File item = this.fileRepo.findById(id).orElseThrow(RuntimeException::new);

		Resource file = this.storageService.loadAsResource(item.getFileName());
		
		
		

		if (file == null)
			return null;
		var uploadFileResponse = new UploadFileResponse(item.getFileName(), item.getContentType(), item.getId(), file.getContentAsByteArray());
		
		 response.setContentType(uploadFileResponse.getContentType());
		 String content = "inline"+";filename="+uploadFileResponse.getFileName();
	     response.setHeader("Content-Disposition",content);
	  // Write the byte array to the response output stream
	      try (OutputStream out = response.getOutputStream()) {
	    	  out.write(uploadFileResponse.getData());
	    	  out.flush();
	      }
	     
	        response.flushBuffer();

		return null;

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
		List<Language> languages = subtitleFilesService.getLanguageRepo().findAll();

		model.addAttribute("editMode", mode);
		model.addAttribute("item", item);
		model.addAttribute("languages", languages);
		return URL_SUFFIX + "/edit";

	}
	
	
	@PostMapping("/save")
	public String saveUpdate(@ModelAttribute SubtitleFilesDto item, Model model) {
		item.setRepo(this.repo);
		item.setStorageService(storageService);
		item.setFileRepo(fileRepo);
		item.setSubtitelFilesService(subtitleFilesService);

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
