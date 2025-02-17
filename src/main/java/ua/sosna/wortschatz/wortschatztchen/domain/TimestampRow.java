package ua.sosna.wortschatz.wortschatztchen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TimestampRow.
 */
@Entity
@Table(name = "timestamp_row")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimestampRow implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
	@SequenceGenerator(name = "sequenceGenerator")
	@Column(name = "id")
	private Long id;

	@Column(name = "uuid")
	private UUID uuid;

	@Column(name = "start_time")
	private String startTime;

	@Column(name = "text")
	private String text;

	@Column(name = "end_time")
	private String endTime;

	@Column(name = "time_in")
	private Long timeIn;

	@Column(name = "time_out")
	private Long timeOut;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties(value = { "timestampRows", "language" }, allowSetters = true)
	private SubtitleFile subtitleFile;

	// jhipster-needle-entity-add-field - JHipster will add fields here

	public Long getId() {
		return this.id;
	}

	public TimestampRow id(Long id) {
		this.setId(id);
		return this;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public TimestampRow startTime(String startTime) {
		this.setStartTime(startTime);
		return this;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getText() {
		return this.text;
	}

	public TimestampRow text(String text) {
		this.setText(text);
		return this;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public TimestampRow endTime(String endTime) {
		this.setEndTime(endTime);
		return this;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Long getTimeIn() {
		return this.timeIn;
	}

	public TimestampRow timeIn(Long timeIn) {
		this.setTimeIn(timeIn);
		return this;
	}

	public void setTimeIn(Long timeIn) {
		this.timeIn = timeIn;
	}

	public Long getTimeOut() {
		return this.timeOut;
	}

	public TimestampRow timeOut(Long timeOut) {
		this.setTimeOut(timeOut);
		return this;
	}

	public void setTimeOut(Long timeOut) {
		this.timeOut = timeOut;
	}

	public SubtitleFile getSubtitleFile() {
		return this.subtitleFile;
	}

	public void setSubtitleFile(SubtitleFile subtitleFile) {
		this.subtitleFile = subtitleFile;
	}

	public TimestampRow subtitleFile(SubtitleFile subtitleFile) {
		this.setSubtitleFile(subtitleFile);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof TimestampRow)) {
			return false;
		}
		return getId() != null && getId().equals(((TimestampRow) o).getId());
	}

	@Override
	public int hashCode() {
		// see
		// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	// prettier-ignore
	@Override
	public String toString() {
		return "TimestampRow{" + "id=" + getId() + ", startTime='" + getStartTime() + "'" + ", text1='" + getText()
				+ "'" + ", endTime='" + getEndTime() + "'" + ", timeIn=" + getTimeIn() + ", timeOut=" + getTimeOut()
				+ "}";
	}
    @PrePersist
    public void PreSave() {
    	if (this.uuid==null) {
    		this.uuid = UUID.randomUUID();
    	}
    }
}
