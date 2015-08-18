package main.LanguageTools.dependensee;

import edu.stanford.nlp.util.Pair;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Node implements Serializable {
	private static final long serialVersionUID = -8871645177307943816L;
	public String label;
	public int idx;
	public String lex;
	public String pos;
	public List<Node> children;
	public List<Edge> outEdges;
	public int degree = 1;
	public Node parent;
	public Rectangle2D position = new Rectangle();

	public Node(String lex, int idx, String pos) {
		this.label = lex + "-" + idx;
		this.lex = lex;
		this.idx = idx;
		this.pos = pos;
		this.children = new ArrayList();
		this.outEdges = new ArrayList();
	}

	public Node(String label, String pos) {
		this.label = label;
		this.lex = label.substring(0, label.lastIndexOf("-"));
		this.idx = Integer
				.parseInt(label.substring(label.lastIndexOf("-") + 1));
		this.pos = pos;
		this.children = new ArrayList();
		this.outEdges = new ArrayList();
	}

	public void addChild(Node c) {
		for (Node node : this.children) {
			if (node.label.equalsIgnoreCase(c.label)) {
				return;
			}
		}
		this.children.add(c);
		this.degree += 1;
	}

	public String toString() {
		return this.lex;
	}

	public int getPathLength(Node n) {
		Queue q = new LinkedList();
		Set marked = new HashSet();
		q.add(new Pair(this, Integer.valueOf(0)));
		marked.add(this);
		Pair v;
		while (!(q.isEmpty())) {
			v = (Pair) q.remove();
			if (v.first == n) {
				return ((Integer) v.second).intValue();
			}
			if ((((Node) v.first).parent != null)
					&& (!(marked.contains(((Node) v.first).parent)))) {
				q.add(new Pair(((Node) v.first).parent, Integer
						.valueOf(((Integer) v.second).intValue() + 1)));
				marked.add(((Node) v.first).parent);
			}
			for (Node node : ((Node) v.first).children) {
				q.add(new Pair(node, Integer.valueOf(((Integer) v.second)
						.intValue() + 1)));
				marked.add(node);
			}
		}
		return 2147483647;
	}

	public String getRelationToParent() {
		String rel = null;
		if (this.parent == null) {
			return null;
		}
		for (Edge e : this.parent.outEdges) {
			if (e.target == this) {
				return e.label;
			}
		}
		return null;
	}

	public String DFS() {
		StringBuilder b = new StringBuilder();
		Set done = new HashSet();
		done.add(this);
		DFS(this, done, b);
		return b.toString();
	}

	private void DFS(Node node, Set<Node> done, StringBuilder b) {
		for (Edge e : node.outEdges) {
			if (((("amod".equalsIgnoreCase(e.label)) || ("advmod"
					.equalsIgnoreCase(e.label))))
					&& (!(done.contains(e.target)))) {
				DFS(e.target, done, b);
				done.add(e.target);
			}
		}

		b.append(" ").append(node.lex).append("/").append(node.pos);
	}
}