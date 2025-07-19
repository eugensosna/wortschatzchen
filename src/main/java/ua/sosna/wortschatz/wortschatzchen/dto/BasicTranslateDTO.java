package ua.sosna.wortschatz.wortschatzchen.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BasicTranslateDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
	private String from;
	private String to;
	private String text = "";
	private String uuidClass;


	public BasicTranslateDTO() {
		super();
		this.text = "";
	}

	public BasicTranslateDTO(String from, String to, String text, String uuidClass) {
		super();
		this.from = from;
		this.to = to;
		this.text = text;
		this.uuidClass = uuidClass;
	}

	public String getUuidClass() {
		return uuidClass;
	}

	public void setUuidClass(String uuidClass) {
		this.uuidClass = uuidClass;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getText() {
		return text;
	}

	public void setText(String data) {
		this.text = data;
	}

}
