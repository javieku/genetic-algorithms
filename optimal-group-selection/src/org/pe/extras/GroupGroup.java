package org.pe.extras;

import java.util.ArrayList;
import java.util.Collections;

public class GroupGroup {
	
	private ArrayList<Group> array;
	private Group group;
	
	public GroupGroup() {
		array = new ArrayList<Group>();
		group = null;
	}
	
	public void addToGroup(Double d) {
		group.add(d);
	}
	
	public void newGroup() {
		if (group != null)
			array.add(group);
		group = new Group();
	}
	
	public void addLastGroup() {
		if (group != null)
			array.add(group);
	}
	
	public void sort() {
		Collections.sort(array);
	}
	
	public void sortGroup() {
		group.sort();
	}
	
	public ArrayList<Group> getArray() {
		return array;
	}
}
