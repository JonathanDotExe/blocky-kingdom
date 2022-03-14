package at.jojokobi.blockykingdom.kingdoms.lore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import at.jojokobi.mcutil.TypedMap;

public class Lore implements ConfigurationSerializable{
	
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
			if (conn.getStartChapter() == start) {
				following.add(chapters.get(conn.getEndChapter()));
			}
		}
		return following;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("chapters", chapters);
		map.put("connections", connections);
		return map;
	}
	
	public static Lore deserialize (Map<String, Object> map) {
		TypedMap tMap = new TypedMap(map);
		Lore lore = new Lore();
		lore.chapters = tMap.getList("chapters", LoreChapter.class);
		for (ChapterConnection conn : tMap.getList("connections", ChapterConnection.class)) {
			lore.addChapterConnection(conn.getStartChapter(), conn.getEndChapter());
		}
		return lore;
	}

}
