package fr.univ_orleans.iut45.r207.tp2;


import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.nio.AttributeType;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.util.SupplierUtil;




public class Executable {
    public static void main(String[] args) throws IOException {
        Graph<String, DefaultEdge> graphe = Echauffement.convertir("datatest.txt");

        DOTExporter<String, DefaultEdge> exporter = new DOTExporter<>();
        exporter.setVertexAttributeProvider(nom -> Map.of("label", DefaultAttribute.createAttribute(nom)));
        exporter.exportGraph(graphe, new FileWriter("graphe_collaborations.dot"));

        System.out.println("Graphe exporté dans graphe_collaborations.dot");
    }

    public static Graph<String, DefaultEdge> graphTestBuilder() {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        List<String> noms = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace");
        noms.forEach(graph::addVertex);

        graph.addEdge("Alice", "Bob");
        graph.addEdge("Alice", "Charlie");
        graph.addEdge("Bob", "Charlie");
        graph.addEdge("Charlie", "David");
        graph.addEdge("David", "Eve");
        graph.addEdge("Eve", "Frank");
        graph.addEdge("Frank", "Grace");
        graph.addEdge("Bob", "Eve");
        graph.addEdge("Charlie", "Frank");
        graph.addEdge("Alice", "Grace");
        return graph;
    }

    
}