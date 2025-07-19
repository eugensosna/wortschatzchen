package ua.sosna.wortschatz.wortschatzchen.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ua.sosna.wortschatz.wortschatzchen.dto.BasicTranslateDTO;

@Component
public class LocalTranslateImpl implements TranslateService {
	
	private static final String APPLICATION_JSON = "application/json";
	@Value("${ua.sosna.local.api}")
	private String LOCAL_API ;
	OkHttpClient client = new OkHttpClient();


	@Override
	public String translate(String text, String baseLang, String targetLang) throws IOException {
		String result = "";
		// Convert the object to JSON
				BasicTranslateDTO translation = new BasicTranslateDTO(baseLang, targetLang, text,null);

				
				// Convert the object to JSON
				Gson gson = new Gson();

				String json = gson.toJson(translation);
				
				RequestBody body = RequestBody.create(json, MediaType.get(APPLICATION_JSON + "; charset=utf-8"));
				Request request = new Request.Builder().url(LOCAL_API).post(body)
						.addHeader("accept", APPLICATION_JSON).addHeader("content-type", APPLICATION_JSON)
						.build();
				
	
				// Send the request and get the response
				try (Response response = client.newCall(request).execute()) {
					if (!response.isSuccessful())
						throw new IOException("Unexpected code " + response);

					// Print the response
					result = response.body().string();
				}

		return result;
	}

}
