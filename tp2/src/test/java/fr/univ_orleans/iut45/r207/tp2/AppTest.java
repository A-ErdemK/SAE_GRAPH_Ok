package fr.univ_orleans.iut45.r207.tp2;

import java.io.IOException;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

    @Test
    public void testCollaboProche1() throws IOException{
        Graph<String, DefaultEdge> g1 = AppTest.graphTestEchauffement();

        Graph<String, DefaultEdge> gtest1 = new SimpleGraph<>(DefaultEdge.class);
        gtest1.addVertex("Alice");
        gtest1.addVertex("Bob");
        gtest1.addVertex("Charlie");
        gtest1.addVertex("Grace");

        gtest1.addEdge("Alice", "Bob");
        gtest1.addEdge("Alice", "Charlie");
        gtest1.addEdge("Alice", "Grace");

        // Les collaborateurs proches à distance 1 de Alice sont : Alice, Bob, Charlie, Grace
        Set<String> attendu1 = Set.of("Alice", "Bob", "Charlie", "Grace");
        assertEquals(attendu1, Echauffement.collaboProche(gtest1, "Alice", 1));
    }

    @Test
    public void testDistanceEntreActeurs2() throws Exception {
        Graph<String, DefaultEdge> g2 = Echauffement.convertir("datatest.txt");
        assertEquals(1, Echauffement.distanceEntreActeurs(g2, "Alice", "Bob"));
        assertEquals(2, Echauffement.distanceEntreActeurs(g2, "Alice", "Eve"));
        assertEquals(1, Echauffement.distanceEntreActeurs(g2, "Charlie", "Frank"));
        assertEquals(2, Echauffement.distanceEntreActeurs(g2, "Alice", "David"));
        assertEquals(-1, Echauffement.distanceEntreActeurs(g2, "Alice", "Inconnu"));
    }

    @Test
    public void testCentralite3() throws Exception {
        Graph<String, DefaultEdge> g3 = Echauffement.convertir("datatest.txt");
        // Centralité = plus grande distance à un autre sommet
        assertEquals(2, Echauffement.centralite(g3, "Alice"));
        assertEquals(2, Echauffement.centralite(g3, "Eve"));
    }

    @Test
    public void testCentreGraphe4() throws Exception {
        Graph<String, DefaultEdge> g4 = Echauffement.convertir("datatest.txt");
        // Le centre est un sommet avec la plus petite centralité
        String centre4 = Echauffement.centreGraphe(g4);
        assertTrue(Set.of("Alice", "Charlie", "Eve", "Frank").contains(centre4));
    }

    @Test
    public void testDistanceMaximale5() throws Exception {
        Graph<String, DefaultEdge> g5 = Echauffement.convertir("datatest.txt");
        assertEquals(2, Echauffement.distanceMaximale(g5));
    }

    @Test
    public void testSousGrapheDesProches6() throws Exception {
        Graph<String, DefaultEdge> g6 = Echauffement.convertir("datatest.txt");
        Graph<String, DefaultEdge> sousGraphe6 = Echauffement.sousGrapheDesProches(g6, "Alice", 1);
        Set<String> attendu6 = Set.of("Alice", "Bob", "Charlie", "Frank");
        assertEquals(attendu6, sousGraphe6.vertexSet());
    }
}
