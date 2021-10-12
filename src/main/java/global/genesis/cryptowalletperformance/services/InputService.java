package global.genesis.cryptowalletperformance.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import global.genesis.cryptowalletperformance.models.Input;

public class InputService {

	public static List<Input> convertInput() throws FileNotFoundException {
		List<Input> inputs = new ArrayList<Input>();
		Scanner scanner = new Scanner(new File("./src/main/resources/input-csv.csv"));
		scanner.nextLine();
		while (scanner.hasNextLine()) {
			Input input = new Input();
			String line = scanner.nextLine();
			Scanner snLine = new Scanner(line);
			snLine.useDelimiter(",");
			input.setSymbol(snLine.next());
			input.setQuantity(Double.parseDouble(snLine.next()));
			input.setPrice(Double.parseDouble(snLine.next()));
			snLine.close();
			inputs.add(input);
		}
		
		scanner.close();
		return inputs;
	}
}
