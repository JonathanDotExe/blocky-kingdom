package at.jojokobi.blockykingdom.players.quests;

import java.util.Random;

public class SimpleQuestGenerator implements QuestGenetator {
	
	private QuestFunction function;
	private int minAmount = 1;
	private int maxAmount = 1;
	private int amountMultiplier = 1;
	private int basicReward = 100;
	private int basicExperience = 50;
	private int basicSkillPoints = 2;
	private boolean mutable = true;

	public SimpleQuestGenerator(QuestFunction function) {
		super();
		this.function = function;
	}

	@Override
	public IQuest generate(Random random, long endTimestamp) {
		int amount = random.nextInt(maxAmount - minAmount + 1) + minAmount;
		
		return function.create(amount * amountMultiplier, basicReward * amount, basicExperience * amount, basicSkillPoints * amount, endTimestamp);
	}

	public int getMinAmount() {
		return minAmount;
	}

	public SimpleQuestGenerator setMinAmount(int minAmount) {
		if (!mutable) {
			throw new IllegalArgumentException("This instance is not mutable!");
		}
		this.minAmount = minAmount;
		return this;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public SimpleQuestGenerator setMaxAmount(int maxAmount) {
		if (!mutable) {
			throw new IllegalArgumentException("This instance is not mutable!");
		}
		this.maxAmount = maxAmount;
		return this;
	}

	public int getAmountMultiplier() {
		return amountMultiplier;
	}

	public SimpleQuestGenerator setAmountMultiplier(int amountMultiplier) {
		if (!mutable) {
			throw new IllegalArgumentException("This instance is not mutable!");
		}
		this.amountMultiplier = amountMultiplier;
		return this;
	}

	public int getBasicReward() {
		return basicReward;
	}

	public SimpleQuestGenerator setBasicReward(int basicReward) {
		if (!mutable) {
			throw new IllegalArgumentException("This instance is not mutable!");
		}
		this.basicReward = basicReward;
		return this;
	}
	
	public SimpleQuestGenerator setBasicExperience(int basicExperience) {
		if (!mutable) {
			throw new IllegalArgumentException("This instance is not mutable!");
		}
		this.basicExperience = basicExperience;
		return this;
	}

	public SimpleQuestGenerator setBasicSkillPoints(int basicSkillPoints) {
		if (!mutable) {
			throw new IllegalArgumentException("This instance is not mutable!");
		}
		this.basicSkillPoints = basicSkillPoints;
		return this;
	}

	public int getBasicExperience() {
		return basicExperience;
	}

	public int getBasicSkillPoints() {
		return basicSkillPoints;
	}

	public boolean isMutable() {
		return mutable;
	}

	public SimpleQuestGenerator makeImutable() {
		this.mutable = false;
		return this;
	}

}
