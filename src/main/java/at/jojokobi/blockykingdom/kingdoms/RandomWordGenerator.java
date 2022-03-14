package at.jojokobi.blockykingdom.kingdoms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomWordGenerator {
	
	private List<String> vocals = new ArrayList<>();
	private List<String> consonant = new ArrayList<>();
	
	public RandomWordGenerator(List<String> vocals, List<String> consonant) {
		super();
		this.vocals.addAll(vocals);
		this.consonant.addAll(consonant);
	}
	
	public RandomWordGenerator() {
		this(Arrays.asList("A", "E", "I", "O", "U"), Arrays.asList("B", "C", "D", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Sh", "R", "S", "T", "V", "W", "X", "Y", "Z"));
	}
	
	public String generateWord (Random random, int minSyllables, int maxSyllables) {
		StringBuilder word = new StringBuilder();
		int syllables = random.nextInt(maxSyllables - minSyllables + 1) + minSyllables;
		boolean toggle = random.nextBoolean();
		for (int i = 0; i < syllables; i++) {
			//Start
			if (i == 0) {
				if (toggle) {
					word.append(randomElement(random, vocals));
				}
				else  {
					word.append(randomElement(random, consonant));
				}
			}
			else {
				if (toggle) {
					word.append(randomElement(random, vocals).toLowerCase());
				}
				else  {
					word.append(randomElement(random, consonant).toLowerCase());
				}
				
//				switch (random.nextInt(2)) {
//				case 0:
//					word.append(randomElement(random, vocals).toLowerCase());
//					word.append(randomElement(random, consonant).toLowerCase());
//					break;
//				case 1:
//					word.append(randomElement(random, consonant).toLowerCase());
//					word.append(randomElement(random, vocals).toLowerCase());
//					break;
//				}
			}
			toggle = !toggle;
		}
		return word.toString();
	}
	
	protected String randomElement (Random random, List<String> list) {
		return list.get(random.nextInt(list.size()));
	}
	
}
