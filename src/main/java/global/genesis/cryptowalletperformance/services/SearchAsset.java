package global.genesis.cryptowalletperformance.services;

import java.io.IOException;
import java.util.Map;

import global.genesis.cryptowalletperformance.models.coincap.Assets;

public class SearchAsset implements Runnable {

	private Assets asset;
	private String symbol;

	private Map<String, Assets> map;

	public SearchAsset(Map<String, Assets> map, String symbol) {
		this.symbol = symbol;
		this.map = map;
	}

	@Override
	public void run() {
		try {
			Assets assetBySymbol = CoincapService.getAssetBySymbol(symbol);
			asset = assetBySymbol;

			// use The same date
			// asset.setPriceUsd(CoincapService.getCurrentvalue(symbol).toString());

			map.put(asset.getSymbol(), asset);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
