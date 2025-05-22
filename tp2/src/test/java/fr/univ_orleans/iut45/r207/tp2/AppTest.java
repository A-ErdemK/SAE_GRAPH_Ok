package fr.univ_orleans.iut45.r207.tp2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.jgrapht.util.SupplierUtil;
import org.junit.Test;
import java.util.Set;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    public static Graph<String, DefaultEdge> graphTestEchauffement() {
    Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
    graph.addVertex("Alice");
    graph.addVertex("Bob");
    graph.addVertex("Charlie");
    graph.addVertex("David");
    graph.addVertex("Eve");
    graph.addVertex("Frank");
    graph.addVertex("Grace");

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
    @Test
    public void testGetCollaboCommuns(){
        Graph<String, DefaultEdge> g = graphTestEchauffement();
        // Alice et Bob ont Charlie comme collaborateur commun
        assertEquals(1, Echauffement.getCollaborateursCommuns(g, "Alice", "Bob").size());
        assertTrue(Echauffement.getCollaborateursCommuns(g, "Alice", "Bob").contains("Charlie"));

        // Alice et Charlie ont Bob comme collaborateur commun
        assertEquals(1, Echauffement.getCollaborateursCommuns(g, "Alice", "Charlie").size());
        assertTrue(Echauffement.getCollaborateursCommuns(g, "Alice", "Charlie").contains("Bob"));

        // Bob et Charlie ont Alice comme collaborateur commun
        assertEquals(1, Echauffement.getCollaborateursCommuns(g, "Bob", "Charlie").size());
        assertTrue(Echauffement.getCollaborateursCommuns(g, "Bob", "Charlie").contains("Alice"));

        // Alice et David ont Charlie comme collaborateur commun
        assertEquals(1, Echauffement.getCollaborateursCommuns(g, "Alice", "David").size());
        assertTrue(Echauffement.getCollaborateursCommuns(g, "Alice", "David").contains("Charlie"));

    }
}
