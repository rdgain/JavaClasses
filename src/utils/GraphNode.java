package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GraphNode {
    public int id;

//    public HashMap<GraphNode, Integer> connections;
    public HashMap<Integer, Integer> connections;

    public GraphNode(int id) {
        this.id = id;
        this.connections = new HashMap<>();
    }

    public void addConnection(int direction, int nodeId) {
//        connections.put(n, cost);
        connections.put(direction, nodeId);
    }

    @Override
    public String toString() {
//        Set<GraphNode> nodesConnected = connections.keySet();
//        HashSet<Integer> ids = new HashSet<>();
//        for (GraphNode node: nodesConnected) {
//            ids.add(node.id);
//        }

        return "GraphNode{" +
                "id=" + id +
                ", connections=" + connections.values().toString() +
                '}';
    }

    public static class Container {
        public HashMap<Integer, GraphNode> graph;
        public int width;
        public int height;
    }

    public static Container readMazeFromFile() {
        String path = "mazeGraph.txt";
        HashMap<Integer, GraphNode> graph = new HashMap<>();
        int width = 0, height = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String l = reader.readLine();
            ArrayList<String> lines = new ArrayList<>();
            while (l != null) {
                lines.add(l);
                l = reader.readLine();
            }
//            System.out.println(lines.size());

            int i = 0;
            for (String line: lines) {
                if (i == 0) {
                    String[] split = line.split(" ");
                    width = Integer.parseInt(split[0]);
                    height = Integer.parseInt(split[1]);
                } else {
                    String[] split = line.split(":");
                    int id = Integer.parseInt(split[0]);
                    graph.put(id, new GraphNode(id));
                }
                i++;
            }

            i = 0;
            for (String line: lines) {
                if (i == 0) {
                    i++;
                    continue;
                }

                String[] split = line.split(":");
                int id = Integer.parseInt(split[0]);

                String[] edges = split[1].split(",");
                for (int j = 0; j < edges.length; j++) {
                    int direction = j + 1;
                    if (!edges[j].trim().equals("")) {
                        int connectedId = Integer.parseInt(edges[j].trim());
                        graph.get(id).addConnection(direction, connectedId);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Container container = new Container();
        container.graph = graph;
        container.width = width;
        container.height = height;

        return container;
    }

//    public static ArrayList<GraphNode> readFromFile() {
//        String path = "graph.txt";
//        HashMap<Integer, GraphNode> graph = new HashMap<>();
//
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(path));
//            String l = reader.readLine();
//            ArrayList<String> lines = new ArrayList<>();
//            while (l != null) {
//                lines.add(l);
//                l = reader.readLine();
//            }
////            System.out.println(lines.size());
//
//            for (String line: lines) {
//                String[] split = line.split(":");
//                int id = Integer.parseInt(split[0]);
//                graph.put(id, new GraphNode(id));
//            }
//
//            for (String line: lines) {
//                String[] split = line.split(":");
//                int id = Integer.parseInt(split[0]);
//
//                if (split.length > 1) {
//                    String[] edges = split[1].split(",");
//                    for (String edge : edges) {
//                        String[] edgeSplit = edge.split("-");
//                        int connectedID = Integer.parseInt(edgeSplit[0].trim());
//                        int edgeCost = Integer.parseInt(edgeSplit[1].trim());
//
//                        graph.get(id).addConnection(graph.get(connectedID), edgeCost);
//                    }
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return new ArrayList<>(graph.values());
//    }

    public static void main(String[] args) {
        GraphNode g1 = new GraphNode(1);
        GraphNode g2 = new GraphNode(2);
        GraphNode g3 = new GraphNode(3);
        GraphNode g4 = new GraphNode(4);
        GraphNode g5 = new GraphNode(5);
        GraphNode g6 = new GraphNode(6);

//        g1.addConnection(g2, 12);
//        g1.addConnection(g4, 15);
//        g2.addConnection(g4, 3);
//        g2.addConnection(g3, 7);
//        g3.addConnection(g4, 2);
//        g4.addConnection(g5, 21);

        ArrayList<GraphNode> graph = new ArrayList<>();
        graph.add(g1);
        graph.add(g2);
        graph.add(g3);
        graph.add(g4);
        graph.add(g5);
        graph.add(g6);

        for (GraphNode node: graph) {
            System.out.println(node);
        }
        System.out.println("----------------------------");

//        HashMap<Integer, GraphNode> graph2 = readMazeFromFile();

//        for (GraphNode node: graph2.values()) {
//            System.out.println(node);
//        }
    }
}
