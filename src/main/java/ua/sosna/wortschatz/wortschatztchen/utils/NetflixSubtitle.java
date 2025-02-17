package ua.sosna.wortschatz.wortschatztchen.utils;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class NetflixSubtitle {
	private static final Logger LOGGER = LoggerFactory.getLogger(NetflixSubtitle.class);
	private String id;
	private String begin;
	private String end;
	private String region;
	private String content;

	// Getters and Setters

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static List<NetflixSubtitle> parseXML(InputStream inputStream) {
		List<NetflixSubtitle> subtitles = new ArrayList<>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputStream);

			NodeList pNodes = doc.getElementsByTagName("p");
			for (int i = 0; i < pNodes.getLength(); i++) {
				Element pElement = (Element) pNodes.item(i);

				NetflixSubtitle subtitle = new NetflixSubtitle();
				subtitle.setId(pElement.getAttribute("xml:id"));
				subtitle.setBegin(pElement.getAttribute("begin"));
				subtitle.setEnd(pElement.getAttribute("end"));
				subtitle.setRegion(pElement.getAttribute("region"));
				subtitle.setContent(pElement.getTextContent());

				subtitles.add(subtitle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return subtitles;
	}
}
