package global.genesis.cryptowalletperformance.services;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import global.genesis.cryptowalletperformance.models.coincap.Assets;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CoincapService {

	public static String getIdBySymbol(String symbol) throws IOException, InterruptedException {
		Assets assetBySymbol = getAssetBySymbol(symbol);
		if (assetBySymbol == null) {
			return "NOTFOUND";
		}
		return assetBySymbol.getId();
	}

	private static String cleanJson(String json) {
		json = json.replace("{\"data\":", "");
		String[] split = json.split("]");
		return split[0] + "]";
	}

	public static Assets getAssetBySymbol(String symbol) throws IOException {
		symbol = symbol.toUpperCase();
		var response = get("https://api.coincap.io/v2/assets/?search=" + symbol);
		if (response == null) {
			return null;
		}
		String responseString = cleanJson(response.body().string());
		Gson gson = new Gson();
		List<Assets> assets = gson.fromJson(responseString, new TypeToken<List<Assets>>() {
		}.getType());
		for (Assets a : assets) {
			if (a.getSymbol().equals(symbol)) {
				return a;
			}
		}
		return null;
	}

	public static Response get(String path) {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url(path).method("GET", null).build();
		try {
			var response = client.newCall(request).execute();
			if (response.code() == 429) {
				Thread.sleep(2500);
				return get(path);
			}
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	// use to test with the same date
	public static Double getCurrentvalue(String symbol) throws IOException, InterruptedException {
		String id = getIdBySymbol(symbol);
		var response = get("https://api.coincap.io/v2/assets/" + id
				+ "/history?interval=d1&start=1617753600000&end=1617753601000");
		return getPriceUsd(response.body().string());
	}

	// use to test with the same date
	private static Double getPriceUsd(String json) {
		String[] split = json.split("\"priceUsd\":\"");
		split = split[1].split("\",");
		return Double.parseDouble(split[0]);
	}
}
