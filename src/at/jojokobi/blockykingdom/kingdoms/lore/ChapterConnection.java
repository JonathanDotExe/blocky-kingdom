package at.jojokobi.blockykingdom.kingdoms.lore;

public class ChapterConnection {
	
	private int startChapter;
	private int endChapter;
	
	public ChapterConnection(int startChapter, int endChapter) {
		super();
		this.startChapter = startChapter;
		this.endChapter = endChapter;
	}
	
	public int getStartChapter() {
		return startChapter;
	}
	
	public int getEndChapter() {
		return endChapter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + endChapter;
		result = prime * result + startChapter;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChapterConnection other = (ChapterConnection) obj;
		if (endChapter != other.endChapter)
			return false;
		if (startChapter != other.startChapter)
			return false;
		return true;
	}

}
