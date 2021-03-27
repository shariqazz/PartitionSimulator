package com.partition.simulator;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static com.partition.simulator.Utils.md5_32;

public class Simulator extends Application {
	private static List<Node> nodes;
	private static int NUM_DOCS = 10000;

	@Override
	public void start(Stage stage) throws Exception {
		Stage primaryStage = new Stage();

		primaryStage.setTitle("Number of docs per node");

		CategoryAxis xAxis    = new CategoryAxis();
		xAxis.setLabel("Nodes");

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Number of docs");

		BarChart barChart = new BarChart(xAxis, yAxis);

		XYChart.Series dataSeries1 = new XYChart.Series();
		dataSeries1.setName("Number of Docs");

		for (Node n : nodes) {
			dataSeries1.getData().add(new XYChart.Data(String.valueOf(n.getId()), n.getSize()));
		}

		barChart.getData().add(dataSeries1);

		VBox vbox = new VBox(barChart);

		Scene scene = new Scene(vbox, 400, 200);

		primaryStage.setScene(scene);
		primaryStage.setHeight(300);
		primaryStage.setWidth(1200);

		primaryStage.show();
	}

	public static void main(String[] args) throws Exception {
//        equalProbabilityRangeBasedSimulation();
//        skewedProbabilityRangeBasedSimulation();
//        equalProbabilityHashBasedSimulation();
//        skewedProbabilityHashBasedSimulation();
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
		List<Character> docs = new ArrayList<>(numDocs);
		for (int i = 0; i < numDocs; i++) {
			docs.add(docGen.getNext());
		}

		// generate range-based nodes that will store this data
		int numNodes = 12;
		nodes = new ArrayList<>(numNodes);
		Node prevNode = null;
		for (int i = 0; i <= numNodes; i++) {
			System.out.println(String.format("%d . Initializing hash based node with previous node id %d", i, prevNode == null ? -1 : prevNode.getId()));
			int nodeId = md5_32(String.valueOf(i));
			Node newNode = new Node(nodeId, new SimpleHashPartitionResolver(nodeId, prevNode));
			nodes.add(newNode);
			prevNode = newNode;
		}

		// assign data to nodes
		for (char c : docs) {
			for (Node n : nodes) {
				if (n.ownsId(c)) {
//					System.out.println(String.format("%d owns %c", n.getId(), c));
					n.addData(c);
				}
			}
		}

		Application.launch();
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
		List<Character> docs = new ArrayList<>(numDocs);
		for (int i = 0; i < numDocs; i++) {
			docs.add(docGen.getNext());
		}

		// generate range-based nodes that will store this data
		int numNodes = 12;
		nodes = new ArrayList<>(numNodes);
		Node prevNode = null;
		for (int i = 0; i <= numNodes; i++) {
			System.out.println(String.format("%d . Initializing hash based node with previous node id %d", i, prevNode == null ? -1 : prevNode.getId()));
			int nodeId = md5_32(String.valueOf(i));
			Node newNode = new Node(nodeId, new SimpleHashPartitionResolver(nodeId, prevNode));
			nodes.add(newNode);
			prevNode = newNode;
		}

		// assign data to nodes
		for (char c : docs) {
			for (Node n : nodes) {
				if (n.ownsId(c)) {
//					System.out.println(String.format("%d owns %c", n.getId(), c));
					n.addData(c);
				}
			}
		}

		Application.launch();
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
		List<Character> docs = new ArrayList<>(numDocs);
		for (int i = 0; i < numDocs; i++) {
			docs.add(docGen.getNext());
		}

		// generate range-based nodes that will store this data
		int numNodes = 12;
		nodes = new ArrayList<>(numNodes);
		int cur = 0;
		for (int i = 0; i <= numNodes; i++) {
			char rangeStart = (char) (cur + 'a');
			char rangeEnd = (char) (Math.min(rangeStart + 1, 'z'));
			System.out.println(String.format("%d . Initializing node to handle range %c - %c", i, rangeStart, rangeEnd));
			nodes.add(
					new Node(i, new KeyRangePartitionResolver(rangeStart, rangeEnd))
			);
			cur += 2;
		}

		// assign data to nodes
		for (char c : docs) {
			for (Node n : nodes) {
				if (n.ownsId(c)) {
					n.addData(c);
				}
			}
		}

		Application.launch();
	}

	private static void skewedProbabilityRangeBasedSimulation() {
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
		List<Character> docs = new ArrayList<>(numDocs);
		for (int i = 0; i < numDocs; i++) {
			docs.add(docGen.getNext());
		}

		// generate range-based nodes that will store this data
		int numNodes = 12;
		nodes = new ArrayList<>(numNodes);
		int cur = 0;
		for (int i = 0; i <= numNodes; i++) {
			char rangeStart = (char) (cur + 'a');
			char rangeEnd = (char) (Math.min(rangeStart + 1, 'z'));
			System.out.println(String.format("%d . Initializing node to handle range %c - %c", i, rangeStart, rangeEnd));
			nodes.add(
					new Node(i, new KeyRangePartitionResolver(rangeStart, rangeEnd))
			);
			cur += 2;
		}

		// assign data to nodes
		for (char c : docs) {
			for (Node n : nodes) {
				if (n.ownsId(c)) {
					n.addData(c);
				}
			}
		}

		System.out.println(nodes.toString());

		Application.launch();
	}
}
