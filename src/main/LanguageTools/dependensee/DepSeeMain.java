package main.LanguageTools.dependensee;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DepSeeMain {
	
//	private static TreebankLanguagePack tlp = new PennTreebankLanguagePack();
	private static TreebankLanguagePack tlp = new ChineseTreebankLanguagePack();
	
	private static GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();

	public static void main(String[] args) throws Exception {
		if (args.length == 2)
			writeImage(args[0], args[1]);
		else if ((args.length == 3) && ("-t".equalsIgnoreCase(args[0])))
			writeFromTextFile(args[1], args[2]);
		else
			printHelp();
	}

	private static void printHelp() throws Exception {
		System.out
				.println("Usage: com.chaoticity.dependensee.Main <sentence> <image file>");
		System.out
				.println("Usage: com.chaoticity.dependensee.Main -t <input file> <image file>");
	}

	private static Graph getGraph(Tree tree) throws Exception {
		ArrayList words = tree.taggedYield();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
		Collection<TypedDependency> tdl = gs.typedDependencies();
		Graph g = new Graph(words);
		for (TypedDependency td : tdl)
			g.addEdge(td.gov().index() - 1, td.dep().index() - 1, td.reln()
					.toString());
		try {
			g.setRoot(((TypedDependency) GrammaticalStructure.getRoots(tdl)
					.iterator().next()).gov().toString());
		} catch (Exception ex) {
			if (g.nodes.size() > 0) {
				g.setRoot(((Node) g.nodes.get(Integer.valueOf(0))).label);
			}
		}
		return g;
	}

	public static Graph getGraph(String sentence) throws Exception {
		LexicalizedParser lp = LexicalizedParser.loadModel(
				"edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz",
				new String[0]);
		lp.setOptionFlags(new String[] { "-maxLength", "500",
				"-retainTmpSubcategories" });
		TokenizerFactory tokenizerFactory = PTBTokenizer.factory(
				new CoreLabelTokenFactory(), "");

		List wordList = tokenizerFactory.getTokenizer(
				new StringReader(sentence)).tokenize();
		Tree tree = lp.apply(wordList);
		GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
		Collection tdl = gs.typedDependencies();
		return getGraph(tree, tdl);
	}

	public static Graph getGraph(String sentence, LexicalizedParser lp)
			throws Exception {
		TokenizerFactory tokenizerFactory = PTBTokenizer.factory(
				new CoreLabelTokenFactory(), "");

		List wordList = tokenizerFactory.getTokenizer(
				new StringReader(sentence)).tokenize();
		Tree tree = lp.apply(wordList);
		GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
		Collection tdl = gs.typedDependencies();
		return getGraph(tree, tdl);
	}

	private static Graph getGraph(Tree tree, Collection<TypedDependency> tdl)
			throws Exception {
		ArrayList words = tree.taggedYield();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(tree);
		Graph g = new Graph(words);
		for (TypedDependency td : tdl)
			g.addEdge(td.gov().index() - 1, td.dep().index() - 1, td.reln()
					.toString());
		try {
			g.setRoot(((TypedDependency) GrammaticalStructure.getRoots(tdl)
					.iterator().next()).gov().toString());
		} catch (Exception ex) {
			if (g.nodes.size() > 0) {
				g.setRoot(((Node) g.nodes.get(Integer.valueOf(0))).label);
			}
		}

		return g;
	}

	private static int getNextHeight(Graph graph, Edge n) {
		int height = 3;
		boolean isFree = false;
		while (!(isFree)) {
			boolean overlapped = false;
			for (Edge e : graph.edges) {
				if (!(e.visible))
					continue;
				if (n == e) {
					continue;
				}
				int eFirst = (e.sourceIndex < e.targetIndex) ? e.sourceIndex
						: e.targetIndex;
				int eSecond = (e.sourceIndex < e.targetIndex) ? e.targetIndex
						: e.sourceIndex;
				int nFirst = (n.sourceIndex < n.targetIndex) ? n.sourceIndex
						: n.targetIndex;
				int nSecond = (n.sourceIndex < n.targetIndex) ? n.targetIndex
						: n.sourceIndex;
				if ((e.height == height)
						&& ((((nFirst > eFirst) && (nFirst < eSecond))
								|| ((nSecond > eFirst) && (nSecond < eSecond))
								|| ((eSecond > nFirst) && (eSecond < nSecond))
								|| ((eSecond > nFirst) && (eSecond < nSecond))
								|| (n.targetIndex == eFirst) || (n.targetIndex == eSecond)))) {
					overlapped = true;
				}
			}

			if (!(overlapped))
				isFree = true;
			else {
				++height;
			}
		}

		return height;
	}

	public static void writeImage(String sentence, String outFile)
			throws Exception {
		writeImage(sentence, outFile, 1);
	}

	public static void writeImage(String sentence, String outFile, int scale)
			throws Exception {
		LexicalizedParser lp = null;
		try {
			lp = LexicalizedParser.loadModel(
					"edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz",
					new String[0]);
		} catch (Exception e) {
			System.err
					.println("Could not load file englishPCFG.ser.gz. Try placing this file in the same directory as Dependencee.jar");
			return;
		}

		lp.setOptionFlags(new String[] { "-maxLength", "500",
				"-retainTmpSubcategories" });
		TokenizerFactory tokenizerFactory = PTBTokenizer.factory(
				new CoreLabelTokenFactory(), "");

		List wordList = tokenizerFactory.getTokenizer(
				new StringReader(sentence)).tokenize();
		Tree tree = lp.apply(wordList);
		writeImage(tree, outFile, scale);
	}

	public static void writeImage(String sentence, String outFile,
			LexicalizedParser lp) throws Exception {
		Tree parse;
		try {
			TokenizerFactory tokenizerFactory = PTBTokenizer.factory(
					new CoreLabelTokenFactory(), "");

			List wordList = tokenizerFactory.getTokenizer(
					new StringReader(sentence)).tokenize();
			parse = lp.apply(wordList);
		} catch (Exception e) {
			throw e;
		}
		writeImage(parse, outFile);
	}

	public static void writeImage(Tree tree, String outFile) throws Exception {
		writeImage(tree, outFile, 1);
	}

	public static void writeImage(Tree tree, Collection<TypedDependency> tdl,
			String outFile) throws Exception {
		Graph g = getGraph(tree, tdl);
		BufferedImage image = createTextImage(g, 1);
		ImageIO.write(image, "png", new File(outFile));
	}

	public static void writeImage(Tree tree, Collection<TypedDependency> tdl,
			String outFile, int scale) throws Exception {
		Graph g = getGraph(tree, tdl);
		BufferedImage image = createTextImage(g, scale);
		ImageIO.write(image, "png", new File(outFile));
	}

	public static void writeImage(Tree tree, String outFile, int scale)
			throws Exception {
		Graph g = getGraph(tree);
		BufferedImage image = createTextImage(g, scale);
		ImageIO.write(image, "png", new File(outFile));
	}

	private static BufferedImage createTextImage(Graph graph, int scale)
			throws Exception {
//		Font wordFont = new Font("Arial", 0, 12 * scale);
		Font wordFont = new Font("MS Song", 0, 12 * scale);//modity by hesz
		FontRenderContext frc = new FontRenderContext(null, true, false);

		int spaceHeight = 20 * scale;
		int spaceWidth = 20 * scale;
		double totalWidth = spaceWidth;

		for (Integer i : graph.nodes.keySet()) {
			Node node = (Node) graph.nodes.get(i);
			TextLayout layout = new TextLayout(node.toString(), wordFont, frc);
			Rectangle2D bounds = layout.getBounds();
			node.position.setRect(totalWidth, 0.0D, bounds.getWidth(),
					bounds.getHeight());
			totalWidth += node.position.getWidth() + spaceWidth;
		}
		int imageWidth = (int) Math.ceil(totalWidth);
		int imageHeight = spaceHeight * (6 * scale + graph.nodes.size());
		int baseline = imageHeight - (30 * scale);

		BufferedImage image = new BufferedImage(imageWidth, imageHeight, 1);
		Graphics2D g = image.createGraphics();
		g.setBackground(Color.white);
		g.clearRect(0, 0, imageWidth, imageHeight);
		g.setColor(Color.black);
		g.setFont(wordFont);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_OFF);

		for (Integer i : graph.nodes.keySet()) {
			Node node = (Node) graph.nodes.get(i);
			node.position.setRect(node.position.getX(), baseline - spaceHeight,
					node.position.getWidth(), node.position.getHeight());
			g.drawString(node.toString(), (int) node.position.getX(),
					(int) node.position.getY());
		}

		Font posFont = new Font("Arial", 0, 8 * scale);
//		Font posFont = new Font("MS Song", 0, 12 * scale);//modity by hesz
		g.setColor(Color.darkGray);
		g.setFont(posFont);
		for (Integer i : graph.nodes.keySet()) {
			Node node = (Node) graph.nodes.get(i);
			node.position.setRect(node.position.getX(),
					baseline - (10 * scale), node.position.getWidth(),
					node.position.getHeight());
			g.drawString(node.pos, (int) node.position.getX(),
					(int) node.position.getY());
		}
		g.setColor(Color.black);

		int lineDistance = 5 * scale;
		int arrowBase = 2 * scale;
		int maxHeight = 0;
		for (Integer i : graph.nodes.keySet()) {
			Node node = (Node) graph.nodes.get(i);
			int spacer = (int) node.position.getWidth() / 2
					- (node.outEdges.size() / 2 * lineDistance);
			for (Edge e : node.outEdges) {
				int height = getNextHeight(graph, e);
				if (height > maxHeight) {
					maxHeight = height;
				}
				e.height = height;
				int targetSpacer = (int) e.target.position.getWidth() / 2
						- ((e.target.outEdges.size() + 2) / 2 * lineDistance);

				g.drawLine((int) e.source.position.getX() + spacer, baseline
						- (height * spaceHeight),
						(int) e.target.position.getX() + targetSpacer, baseline
								- (height * spaceHeight));

				g.drawLine((int) e.source.position.getX() + spacer, baseline
						- (height * spaceHeight),
						(int) e.source.position.getX() + spacer, baseline
								- (spaceHeight * 2));

				g.drawLine((int) e.target.position.getX() + targetSpacer,
						baseline - (height * spaceHeight),
						(int) e.target.position.getX() + targetSpacer, baseline
								- (spaceHeight * 2));

				g.drawLine((int) e.target.position.getX() - arrowBase
						+ targetSpacer, baseline - (spaceHeight * 2)
						- (4 * scale), (int) e.target.position.getX()
						+ targetSpacer, baseline - (spaceHeight * 2));

				g.drawLine((int) e.target.position.getX() + arrowBase
						+ targetSpacer, baseline - (spaceHeight * 2)
						- (4 * scale), (int) e.target.position.getX()
						+ targetSpacer, baseline - (spaceHeight * 2));

				e.visible = true;
				spacer += lineDistance;
			}
		}
		int spacer;
		Font relFont = new Font("Arial", 0, 10 * scale);
//		Font relFont = new Font("MS Song", 0, 12 * scale);//modity by hesz
		g.setColor(Color.blue);
		g.setFont(relFont);

		for (Integer i : graph.nodes.keySet()) {
			Node node = (Node) graph.nodes.get(i);
			spacer = (int) node.position.getWidth() / 2
					- (node.outEdges.size() / 2 * lineDistance);
			for (Edge e : node.outEdges) {
				int targetSpacer = (int) e.target.position.getWidth() / 2
						- ((e.target.outEdges.size() + 2) / 2 * lineDistance);
				int x = (int) ((e.source.position.getX() < e.target.position
						.getX()) ? e.source.position.getX() + spacer
						: e.target.position.getX() + targetSpacer);

				TextLayout layout = new TextLayout(e.label, relFont, frc);
				Rectangle2D bounds = layout.getBounds();
				int clearWidth = (int) Math.ceil(bounds.getWidth());
				int clearHeight = (int) Math.ceil(bounds.getHeight()) + 2
						* scale;
				g.clearRect(x, baseline - (e.height * spaceHeight)
						- clearHeight - (2 * scale), clearWidth, clearHeight);

				g.drawString(e.label, x, baseline - (e.height * spaceHeight)
						- (3 * scale));

				spacer += lineDistance;
			}
		}
		g.dispose();
		int ystart = imageHeight - (spaceHeight * (maxHeight + 3 * scale));
		return image.getSubimage(0, ystart, imageWidth, imageHeight - ystart);
	}

	public static void writeFromTextFile(String infile, String outfile)
			throws Exception {
		Graph g = new Graph();
		BufferedReader input = new BufferedReader(new FileReader(infile));
		String line = null;
		while ((line = input.readLine()) != null) {
			if ("".equals(line)) {
				continue;
			}
			int relEnd = line.indexOf("(");
			int secondWordStart = line.indexOf(", ", relEnd + 1);
			String rel = line.substring(0, relEnd);
			String gov = line.substring(relEnd + 1, secondWordStart);
			String dep = line.substring(secondWordStart + 2, line.length() - 1);
			Node govNode = g.addNode(gov, "");
			Node depNode = g.addNode(dep, "");
			g.addEdge(govNode, depNode, rel);
		}

		BufferedImage image = createTextImage(g, 1);
		ImageIO.write(image, "png", new File(outfile));
	}
}
