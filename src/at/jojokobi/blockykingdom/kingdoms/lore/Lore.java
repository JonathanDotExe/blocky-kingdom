package at.jojokobi.blockykingdom.kingdoms.lore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lore {
	
	private List<LoreChapter> chapters = new ArrayList<>();
	private Set<ChapterConnection> connections = new HashSet<>();
	
	public void addChapter (LoreChapter chapter) {
		chapters.add(chapter);
	}
	
	public void addChapterConnection (int start, int end) {
		chapters.get(start);
		chapters.get(end);
		connections.add(new ChapterConnection(start, end));
	}
	
	public List<LoreChapter> getChapters() {
		return new ArrayList<>(chapters);
	}
	
	public List<LoreChapter> getFollowingChapters (int start) {
		List<LoreChapter> following = new ArrayList<>();
		for (ChapterConnection conn : connections) {
			
		}
	}

}
