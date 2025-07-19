package ua.sosna.wortschatz.wortschatzchen.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
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
public class TimestampRow  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timestamp_row_id_seq")
	// The initial value is to account for data.sql demo data ids
	@SequenceGenerator(name = "timestamp_row_id_seq")
    @SequenceGenerator(name = "timestamp_row_id_seq", sequenceName = "timestamp_row_id_seq", allocationSize = 1) // <--- CHANGE THIS TO 
	protected Long id;
	@Column(nullable = false, updatable = false)
	private Instant created = Instant.now();

	@Column(name = "end_time")
	private String endTime;

	@Column(name = "start_time")
	private String startTime;


	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties(value = { "timestampRows", "language" }, allowSetters = true)
	private SubtitleFile subtitleFile;

	@Column(name = "text")
	private String text;

	@Column(name = "time_in")
	private Long timeIn;

	@Column(name = "time_out")
	private Long timeOut;

	private Instant updated = Instant.now();

	@Column(name = "uuid", unique = true, nullable = false, updatable = false)
	protected UUID uuid = UUID.randomUUID();

	// jhipster-needle-entity-add-field - JHipster will add fields here

	@Version
	private int version;

	public TimestampRow endTime(String endTime) {
		this.setEndTime(endTime);
		return this;
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

	public Instant getCreated() {
		return created;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public Long getId() {
		return this.id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public SubtitleFile getSubtitleFile() {
		return this.subtitleFile;
	}

	public String getText() {
		return this.text;
	}

	public Long getTimeIn() {
		return this.timeIn;
	}

	public Long getTimeOut() {
		return this.timeOut;
	}

	public Instant getUpdated() {
		return updated;
	}

	public UUID getUuid() {
		return uuid;
	}

	public int getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		// see
		// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
		return getClass().hashCode();
	}

	public TimestampRow id(Long id) {
		this.setId(id);
		return this;
	}

	@PrePersist
    public void PreSave() {
    	if (this.uuid==null) {
    		this.uuid = UUID.randomUUID();
    	}
    }

	public void setCreated(Instant created) {
		this.created = created;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setSubtitleFile(SubtitleFile subtitleFile) {
		this.subtitleFile = subtitleFile;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setTimeIn(Long timeIn) {
		this.timeIn = timeIn;
	}

	public void setTimeOut(Long timeOut) {
		this.timeOut = timeOut;
	}

	public void setUpdated(Instant updated) {
		this.updated = updated;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public TimestampRow startTime(String startTime) {
		this.setStartTime(startTime);
		return this;
	}

	public TimestampRow subtitleFile(SubtitleFile subtitleFile) {
		this.setSubtitleFile(subtitleFile);
		return this;
	}

	// jhipster-needle-entity-add-getters-setters - JHipster will add getters and
	// setters here


	public TimestampRow text(String text) {
		this.setText(text);
		return this;
	}

	public TimestampRow timeIn(Long timeIn) {
		this.setTimeIn(timeIn);
		return this;
	}

	public TimestampRow timeOut(Long timeOut) {
		this.setTimeOut(timeOut);
		return this;
	}
    // prettier-ignore
	@Override
	public String toString() {
		return "TimestampRow{" + "id=" + getId() + ", startTime='" + getStartTime() + "'" + ", text1='" + getText()
				+ "'" + ", endTime='" + getEndTime() + "'" + ", timeIn=" + getTimeIn() + ", timeOut=" + getTimeOut()
				+ "}";
	}
}
