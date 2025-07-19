package ua.sosna.wortschatz.wortschatzchen.service;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import okhttp3.*;
import ua.sosna.wortschatz.wortschatzchen.dto.BasicTranslateDTO;

//@Component
public class LingvanexTranslateServiceImpl implements TranslateService {

	private static final String A_DJZI1_WF4QI_LS6T_INNX_ECDW_K_SXWU_JIXT_RNQ_QNP_QPTD_B_OG18_TL_D7_YH_UJ_UQ_MWR_AE_E_SK_JFD_RN_OP_DR1L_EN2W_G = "a_Djzi1Wf4qiLs6tINNXEcdwKSxwuJixtRnqQNPQptdBOg18TlD7YhUjUqMWRAeESkJFDRnOpDr1lEn2wG";
	private static final String APPLICATION_JSON = "application/json";
	private static final String HTTPS_API_B2B_BACKENSTER_COM_B1_API_V3_TRANSLATE = "https://api-b2b.backenster.com/b1/api/v3/translate";

	@Override
	public String translate(String text, String baseLang, String targetLang) throws IOException {
		// Convert the object to JSON
		BasicTranslateDTO translation = new BasicTranslateDTO(baseLang, targetLang, text, null);

		OkHttpClient client = new OkHttpClient();
		// Convert the object to JSON
		Gson gson = new Gson();

		String json = gson.toJson(translation);

		RequestBody body = RequestBody.create(json, MediaType.get(APPLICATION_JSON + "; charset=utf-8"));
		Request request = new Request.Builder().url(HTTPS_API_B2B_BACKENSTER_COM_B1_API_V3_TRANSLATE).post(body)
				.addHeader("accept", APPLICATION_JSON).addHeader("content-type", APPLICATION_JSON)
				.addHeader("Authorization",
						A_DJZI1_WF4QI_LS6T_INNX_ECDW_K_SXWU_JIXT_RNQ_QNP_QPTD_B_OG18_TL_D7_YH_UJ_UQ_MWR_AE_E_SK_JFD_RN_OP_DR1L_EN2W_G)
				.build();

		// Send the request and get the response
		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful())
				throw new IOException("Unexpected code " + response);

			// Print the response
			return response.body().string();
		}
	}

}
