package com.partition.simulator;

import java.security.NoSuchAlgorithmException;
import java.util.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.partition.simulator.Utils.md5_32;

// TODO: range based hash partitioning
public class Simulator extends Application {
	private static List<Node> equalRangeNodes = new ArrayList<>();
	private static List<Node> skewedRangeNodes = new ArrayList<>();
	private static List<Node> equalHashNodes = new ArrayList<>();
	private static List<Node> skewedHashNodes = new ArrayList<>();
	private static int NUM_DOCS = 1000000;
	private static int NUM_NODES = 25;
	private static int CHAR_STEPS = 0;

	@Override
	public void start(Stage stage) throws Exception {
		Stage primaryStage = new Stage();

		primaryStage.setTitle("Number of docs per node");

		CategoryAxis xAxis    = new CategoryAxis();
		xAxis.setLabel("Nodes");

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Number of docs");

		BarChart barChart = new BarChart(xAxis, yAxis);

		XYChart.Series equalRangeSeries = new XYChart.Series();
		equalRangeSeries.setName("Equal prob range partitioning");
		XYChart.Series skewedRangeSeries = new XYChart.Series();
		skewedRangeSeries.setName("Skewed prob range partitioning");
		XYChart.Series equalHashSeries = new XYChart.Series();
		equalHashSeries.setName("Equal prob hash partitioning");
		XYChart.Series skewedHashSeries = new XYChart.Series();
		skewedHashSeries.setName("Skewed prob hash partitioning");

		for (Node n : equalRangeNodes) {
			equalRangeSeries.getData().add(new XYChart.Data(String.valueOf(n.getId()), n.getSize()));
		}

		for (Node n : skewedRangeNodes) {
			skewedRangeSeries.getData().add(new XYChart.Data(String.valueOf(n.getId()), n.getSize()));
		}

		int i = 0;
		for (Node n : equalHashNodes) {
			equalHashSeries.getData().add(new XYChart.Data(String.valueOf(i++), n.getSize()));
		}

		i = 0;
		for (Node n : skewedHashNodes) {
			skewedHashSeries.getData().add(new XYChart.Data(String.valueOf(i++), n.getSize()));
		}

		barChart.getData().add(equalRangeSeries);
		barChart.getData().add(skewedRangeSeries);
		barChart.getData().add(equalHashSeries);
		barChart.getData().add(skewedHashSeries);

		VBox vbox = new VBox(barChart);

		Scene scene = new Scene(vbox, 400, 200);

		primaryStage.setScene(scene);
		primaryStage.setHeight(300);
		primaryStage.setWidth(1200);

		primaryStage.show();
	}

	public static void main(String[] args) throws Exception {
        equalProbabilityRangeBasedSimulation();
        skewedProbabilityRangeBasedSimulation();
        skewedProbabilityHashBasedSimulation();
		equalProbabilityHashBasedSimulation();
        Application.launch();
    }

	private static void skewedProbabilityHashBasedSimulation() throws NoSuchAlgorithmException {
		// set up document generator
		ProbabilisticDocumentGenerator docGen = new ProbabilisticDocumentGenerator();
		docGen.add('a', 0.017);
		docGen.add('b', 0.044);
		docGen.add('c', 0.052);
		docGen.add('d', 0.032);
		docGen.add('e', 0.028);
		docGen.add('f', 0.04);
		docGen.add('g', 0.016);
		docGen.add('h', 0.042);
		docGen.add('i', 0.073);
		docGen.add('j', 0.0051);
		docGen.add('k', 0.0086);
		docGen.add('l', 0.024);
		docGen.add('m', 0.038);
		docGen.add('n', 0.023);
		docGen.add('o', 0.076);
		docGen.add('p', 0.043);
		docGen.add('q', 0.0022);
		docGen.add('r', 0.028);
		docGen.add('s', 0.067);
		docGen.add('t', 0.26); // adding 0.09 to balance out
		docGen.add('u', 0.012);
		docGen.add('v', 0.0082);
		docGen.add('w', 0.055);
		docGen.add('x', 0.00045);
		docGen.add('y', 0.0076);
		docGen.add('z', 0.00045);

		// generate sample input data
		int numDocs = NUM_DOCS;
		List<String> docs = new ArrayList<>(numDocs);
		for (int i = 0; i < numDocs; i++) {
			docs.add(docGen.getNext());
		}

		// generate range-based nodes that will store this data
		int numNodes = NUM_NODES;
		TreeMap<Integer, Node> nodeMap = new TreeMap<>();
		List<Node> nodes = skewedHashNodes;
		for (int i = 0; i <= numNodes; i++) {
			int nodeId = md5_32(String.valueOf(UUID.randomUUID()));
			Node newNode = new Node(nodeId, new SimpleHashPartitionResolver(nodeId));
			nodeMap.put(nodeId, newNode);
		}

		Node prevNode = nodeMap.get(nodeMap.lastKey());
		for (Integer id : nodeMap.keySet()) {
			System.out.println(String.format("Initializing hash based node with previous node id %d", prevNode == null ? -1 : prevNode.getId()));
			nodeMap.get(id).setPredecessor(prevNode);
			prevNode = nodeMap.get(id);
			nodes.add(prevNode);
		}

		for (Node n : nodes) {
			System.out.println("Node id: " + n.getId());
		}

		int docsAddedCount = 0;
		for (String c : docs) {
			boolean added = false;
			for (Node n : nodes) {
				if (n.ownsId(c)) {
//					System.out.println(String.format("%d owns %c", n.getId(), c));
					n.addData(c);
					docsAddedCount++;
					added = true;
				}
			}

			if (!added) {
				System.out.println("Not added: " + md5_32(c));
			}
		}

		System.out.println("Skewed prob hash docs added count: " + docsAddedCount);

//		Application.launch();
	}

    private static void equalProbabilityHashBasedSimulation() throws NoSuchAlgorithmException {
		// set up document generator
		ProbabilisticDocumentGenerator docGen = new ProbabilisticDocumentGenerator();
		double prob = 1.0 / 26.0;
		for (char c = 'a'; c <= 'z'; c++) {
			docGen.add(c, prob);
		}

		// generate sample input data
		int numDocs = NUM_DOCS;
		List<String> docs = new ArrayList<>(numDocs);
		for (int i = 0; i < numDocs; i++) {
			docs.add(docGen.getNext());
		}

		// generate range-based nodes that will store this data
		int numNodes = NUM_NODES;
		TreeMap<Integer, Node> nodeMap = new TreeMap<>();
		List<Node> nodes = equalHashNodes;
		for (int i = 0; i <= numNodes; i++) {
			int nodeId = md5_32(String.valueOf(UUID.randomUUID()));
			Node newNode = new Node(nodeId, new SimpleHashPartitionResolver(nodeId));
			nodeMap.put(nodeId, newNode);
		}

		Node prevNode = nodeMap.get(nodeMap.lastKey());
		for (Integer id : nodeMap.keySet()) {
			System.out.println(String.format("Initializing hash based node with previous node id %d", prevNode == null ? -1 : prevNode.getId()));
			nodeMap.get(id).setPredecessor(prevNode);
			prevNode = nodeMap.get(id);
			nodes.add(prevNode);
		}

		// assign data to nodes
		int docsAddedCount = 0;
		for (String c : docs) {
			for (Node n : nodes) {
				if (n.ownsId(c)) {
//					System.out.println(String.format("%d owns %c", n.getId(), c));
					n.addData(c);
					docsAddedCount++;
				}
			}
		}
		System.out.println("Equal prob hash docs added count: " + docsAddedCount);

//		Application.launch();
	}

    private static void equalProbabilityRangeBasedSimulation() {
		// set up document generator
		ProbabilisticDocumentGenerator docGen = new ProbabilisticDocumentGenerator();
		double prob = 1.0 / 26.0;
		for (char c = 'a'; c <= 'z'; c++) {
			docGen.add(c, prob);
		}

		// generate sample input data
		int numDocs = NUM_DOCS;
		List<String> docs = new ArrayList<>(numDocs);
		for (int i = 0; i < numDocs; i++) {
			docs.add(docGen.getNext());
		}

		// generate range-based nodes that will store this data
		int numNodes = NUM_NODES;
		List<Node> nodes = equalRangeNodes;
		int cur = 0;
		for (int i = 0; i <= numNodes; i++) {
			char rangeStart = (char) (cur + 'a');
			char rangeEnd = (char) (Math.min(rangeStart + CHAR_STEPS, 'z'));
			System.out.println(String.format("%d . Initializing node to handle range %c - %c", i, rangeStart, rangeEnd));
			nodes.add(
					new Node(i, new KeyRangePartitionResolver(rangeStart, rangeEnd))
			);
			cur += (1 + CHAR_STEPS);
		}

		// assign data to nodes
		int docsAddedCount = 0;
		for (String c : docs) {
			for (Node n : nodes) {
				if (n.ownsId(c)) {
					n.addData(c);
					docsAddedCount++;
				}
			}
		}

		System.out.println("Equal prob range docs added count: " + docsAddedCount);

//		Application.launch();
	}

	private static void skewedProbabilityRangeBasedSimulation() {
		// set up document generator
		ProbabilisticDocumentGenerator docGen = new ProbabilisticDocumentGenerator();

//		docGen.add('a', 0.70);
//		docGen.add('b', 0.10);
//		docGen.add('c', 0.10);
//		docGen.add('d', 0.10);

		docGen.add('a', 0.017);
		docGen.add('b', 0.044);
		docGen.add('c', 0.052);
		docGen.add('d', 0.032);
		docGen.add('e', 0.028);
		docGen.add('f', 0.04);
		docGen.add('g', 0.016);
		docGen.add('h', 0.042);
		docGen.add('i', 0.073);
		docGen.add('j', 0.0051);
		docGen.add('k', 0.0086);
		docGen.add('l', 0.024);
		docGen.add('m', 0.038);
		docGen.add('n', 0.023);
		docGen.add('o', 0.076);
		docGen.add('p', 0.043);
		docGen.add('q', 0.0022);
		docGen.add('r', 0.028);
		docGen.add('s', 0.067);
		docGen.add('t', 0.26); // adding 0.09 to balance out
		docGen.add('u', 0.012);
		docGen.add('v', 0.0082);
		docGen.add('w', 0.055);
		docGen.add('x', 0.00045);
		docGen.add('y', 0.0076);
		docGen.add('z', 0.00045);

		// generate sample input data
		int numDocs = NUM_DOCS;
		List<String> docs = new ArrayList<>(numDocs);
		for (int i = 0; i < numDocs; i++) {
			docs.add(docGen.getNext());
		}

		// generate range-based nodes that will store this data
		int numNodes = NUM_NODES;
		List<Node> nodes = skewedRangeNodes;
		int cur = 0;
		for (int i = 0; i <= numNodes; i++) {
			char rangeStart = (char) (cur + 'a');
			char rangeEnd = (char) (Math.min(rangeStart + CHAR_STEPS, 'z'));
			System.out.println(String.format("%d . Initializing node to handle range %c - %c", i, rangeStart, rangeEnd));
			nodes.add(
					new Node(i, new KeyRangePartitionResolver(rangeStart, rangeEnd))
			);
			cur += (1 + CHAR_STEPS);
		}

		// assign data to nodes
		int docsAddedCount = 0;
		for (String c : docs) {
			for (Node n : nodes) {
				if (n.ownsId(c)) {
					n.addData(c);
					docsAddedCount++;
				}
			}
		}

		System.out.println("Skewed prob range docs added count: " + docsAddedCount);

//		Application.launch();
	}
}
