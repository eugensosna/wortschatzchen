package ua.sosna.wortschatz.wortschatztchen.domain;

import java.io.Serializable;
import java.util.UUID;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Version;

@MappedSuperclass
public class AbstractEntity  implements Serializable{


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
	// The initial value is to account for data.sql demo data ids
	@SequenceGenerator(name = "idgenerator", initialValue = 1000)
	private Long id;
	
	 @Column(name = "uuid")
	 private UUID uuid;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Version
	private int version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		if (getUuid() != null) {
			return getUuid().hashCode();
		}
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractEntity)) {
			return false; // null or other class
		}
		AbstractEntity other = (AbstractEntity) obj;

		if (getUuid() != null) {
			return getUuid().equals(other.getUuid());
		}
		return super.equals(other);
	}
	@PrePersist
	void preSave () {
		if (getUuid() == null) {
			setUuid(UUID.randomUUID());
		}
		
		
		
	}

}
