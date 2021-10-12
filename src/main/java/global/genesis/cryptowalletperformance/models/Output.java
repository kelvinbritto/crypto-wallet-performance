package global.genesis.cryptowalletperformance.models;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import global.genesis.cryptowalletperformance.models.coincap.Assets;

public class Output {

	private Double total = 0d;

	private String bestAsset;
	private Double bestPerformance = 0d;

	private String worstAsset;
	private Double worstPerformance = 0d;

	public Output(Map<String, Assets> map, List<Input> inputs) {

		Double paid;
		for (Input i : inputs) {
			// calculate Total
			total += ((Double.parseDouble(map.get(i.getSymbol()).getPriceUsd())) * i.getQuantity());

			// best_asset
			paid = i.getPrice() * i.getQuantity();
			var priceNow = Double.parseDouble(map.get(i.getSymbol()).getPriceUsd());
			var o = (priceNow * i.getQuantity()) / paid;
			if (o > bestPerformance) {
				bestPerformance = o;
				bestAsset = i.getSymbol();
			}

			// worst_asset
			if (o < worstPerformance || worstPerformance.equals(0d)) {
				worstAsset = i.getSymbol();
				worstPerformance = o;
			}
		}
	}

	public String getTotal() {
		var total = new DecimalFormat(".##");
		total.setRoundingMode(RoundingMode.HALF_UP);
		return total.format(this.total);
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getBestAsset() {
		return bestAsset;
	}

	public void setBestAsset(String bestAsset) {
		this.bestAsset = bestAsset;
	}

	public String getBestPerformance() {
		var bestPerformance = new DecimalFormat(".##");
		bestPerformance.setRoundingMode(RoundingMode.HALF_UP);
		return bestPerformance.format(this.bestPerformance);
	}

	public void setBestPerformance(Double bestPerformance) {
		this.bestPerformance = bestPerformance;
	}

	public String getWorstAsset() {
		return worstAsset;
	}

	public void setWorstAsset(String worstAsset) {
		this.worstAsset = worstAsset;
	}

	public String getWorstPerformance() {
		var worstPerformance = new DecimalFormat("#.##");
		worstPerformance.setRoundingMode(RoundingMode.HALF_UP);
		return worstPerformance.format(this.worstPerformance);
	}

	public void setWorstPerformance(Double worstPerformance) {
		this.worstPerformance = worstPerformance;
	}

	@Override
	public String toString() {
		return "total=" + getTotal() + ", best_asset=" + bestAsset + ", best_performance=" + getBestPerformance()
				+ ", worst_asset=" + this.worstAsset + ", worst_performance=" + getWorstPerformance();
	}

}
