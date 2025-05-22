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
        exporter.setVertexAttributeProvider((nom) -> Map.of("label", new DefaultAttribute<>(nom, AttributeType.STRING)));
        exporter.exportGraph(graphe, new FileWriter("graphe_collaborations.dot"));

        System.out.println("Graphe exporté dans graphe_collaborations.dot");
    }
}