package ua.sosna.wortschatz.wortschatzchen.service.dto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.Hibernate;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ua.sosna.wortschatz.wortschatzchen.domain.Example;
import ua.sosna.wortschatz.wortschatzchen.domain.Language;
import ua.sosna.wortschatz.wortschatzchen.domain.SubtitleFile;
import ua.sosna.wortschatz.wortschatzchen.domain.TimestampRow;
import ua.sosna.wortschatz.wortschatzchen.domain.Word;
import ua.sosna.wortschatz.wortschatzchen.dto.ExampleDTO;
import ua.sosna.wortschatz.wortschatzchen.repository.ExamplesRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.TimestampRowRepo;
import ua.sosna.wortschatz.wortschatzchen.repository.WordRepo;

@Service
public class TimestampRowService {

	private TimestampRowRepo baseRepository;
	private SubtitleFileService subtitleFileService;

	public TimestampRowService(TimestampRowRepo baseRepository, SubtitleFileService subtitleFileService) {
		super();
		this.baseRepository = baseRepository;
		this.subtitleFileService = subtitleFileService;
	}

	public List<TimestampRow> findAll() {
		return baseRepository.findAll();
	}

	public Optional<TimestampRow> findById(Long id) {
		return baseRepository.findById(id);
	}

	public TimestampRow findByUuid(UUID uuid) throws NotFoundException {
		return baseRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException());
	}

	public TimestampRow findByUuid(String uuid) throws NotFoundException {
		var uuidTrue = UUID.fromString(uuid);

		return baseRepository.findByUuid(uuidTrue).orElseThrow(() -> new NotFoundException());
	}

	public TimestampRow save(TimestampRow newItem) {
		return baseRepository.save(newItem);
	}

	public void deleteById(Long id) {
		baseRepository.deleteById(id);
	}

	public void deleteByUUID(String uuidInString) throws NotFoundException {
		UUID uuid = UUID.fromString(uuidInString);
		deleteByUUID(uuid);
	}

	public void deleteByUUID(UUID uuid) throws NotFoundException {
		TimestampRow item = baseRepository.findByUuid(uuid).orElseThrow(() -> new NotFoundException());
		baseRepository.deleteById(item.getId());
	}

	public TimestampRow create(TimestampRow item) {
		TimestampRow newItem = save(item);

		return newItem;
	}

	public TimestampRow update(Long idToUpdate, TimestampRow example) throws ResponseStatusException {
		if (example.getId() != null & example.getId() > 0 & example.getId() != idToUpdate) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"id in url " + idToUpdate + " not equal to object " + example.getId());

		}
		TimestampRow item = baseRepository.findById(idToUpdate)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"subtitle not found  for id:" + idToUpdate.toString()));
		return update(item, example);

	}

	public TimestampRow update(String uuidInStringToUpdate, TimestampRow example) {
		UUID idToUpdate = UUID.fromString(uuidInStringToUpdate);
		if (example.getUuid() != null & example.getUuid() != idToUpdate) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"id in url " + idToUpdate.toString() + " not equal to object: " + example.getUuid().toString());
		}
		var item = baseRepository.findByUuid(idToUpdate)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"subtitleFile not found  for uuid:" + idToUpdate.toString()));

		return update(item, example);

	}

	public TimestampRow update(TimestampRow item, TimestampRow newData) throws ResponseStatusException {

		if (newData.getEndTime() != null) {
			item.setEndTime(newData.getEndTime());
		}
		if (newData.getText() != null) {
			item.setText(newData.getText());
		}
		if (newData.getTimeIn() != null) {
			item.setTimeIn(newData.getTimeIn());
		}
		if (newData.getTimeOut() != null) {
			item.setTimeOut(newData.getTimeOut());
		}
		if (newData.getSubtitleFile() != null) {
			item.setSubtitleFile(newData.getSubtitleFile());
		}
		return baseRepository.save(item);

	}

	public List<TimestampRow> findAllSubtitleFile(SubtitleFile baseItem) {
		if (baseItem == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SubtitleFile cannot be null");
		}
		if (baseItem.getId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SubtitleFile ID cannot be null");
		}

		return baseRepository.findAllBySubtitleFile(baseItem);
	}

	public List<TimestampRow> findAllBySubtitleFile(SubtitleFile baseItem) {

		var items = baseRepository.findAllBySubtitleFile(baseItem);
		return items;
	}

	public Page<TimestampRow> findAllBySubtitleFilePageable(SubtitleFile mainItem, Pageable pageable) {
		// TODO Auto-generated method stub
		return baseRepository.findAllBySubtitleFile(mainItem, pageable);
	}

}
