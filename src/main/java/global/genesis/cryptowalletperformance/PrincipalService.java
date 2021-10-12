package global.genesis.cryptowalletperformance;

import java.io.IOException;
import java.lang.Thread.State;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import global.genesis.cryptowalletperformance.models.Input;
import global.genesis.cryptowalletperformance.models.Output;
import global.genesis.cryptowalletperformance.models.coincap.Assets;
import global.genesis.cryptowalletperformance.services.InputService;
import global.genesis.cryptowalletperformance.services.SearchAsset;

public class PrincipalService {

	public static void runPrincipal() throws IOException, InterruptedException {

		List<Input> inputs = InputService.convertInput();
		Map<String, Assets> map = Collections.synchronizedMap(new HashMap<String, Assets>());
		List<Thread> threads = new ArrayList<Thread>();

		for (Input input : inputs) {
			Thread procura = new Thread(new SearchAsset(map, input.getSymbol()));
			if (threads.size() < 3) {
				System.out.println("Submitted request ASSET_" + input.getSymbol() + " at " + LocalTime.now());
				procura.start();
				threads.add(procura);
			} else {
				int i = contRunning(threads);
				while (i == 3) {
					Thread.sleep(10000);
					i = contRunning(threads);
				}
				System.out.println("Submitted request ASSET_" + input.getSymbol() + " at " + LocalTime.now());
				procura.start();
				threads.add(procura);
			}
		}

		while (finished(threads).equals(false)) {
			Thread.sleep(1000);
		}
		Output obj = new Output(map, inputs);
		System.out.println(obj);

	}

	public static int contRunning(List<Thread> threads) {
		int i = 0;
		for (Thread thread : threads) {
			if (!thread.getState().equals(State.TERMINATED)) {
				i++;
			}
		}
		return i;
	}

	public static Boolean finished(List<Thread> threads) {
		int i = threads.size();

		for (Thread thread : threads) {
			if (thread.getState().equals(State.TERMINATED)) {
				i--;
			}
		}
		return i <= 0;
	}
}
