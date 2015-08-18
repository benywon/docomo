package main.LanguageTools.dependensee;

import java.io.Serializable;

public class Edge implements Serializable {
	public Node source;
	public Node target;
	public String label;
	public int sourceIndex;
	public int targetIndex;
	public boolean visible;
	public int height;

	public Edge() {
		this.visible = false;
	}

	public String toString() {
		return this.label + "[" + this.sourceIndex + "->" + this.targetIndex
				+ "]";
	}
}