package fr.univ_orleans.iut45.r207.tp2;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jgrapht.Graphs;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.nio.csv.CSVImporter;

public class Echauffement{
    public static Graph<String, DefaultEdge> convertir(String chemin) throws IOException {
        Graph<String, DefaultEdge> graphe = new SimpleGraph<>(DefaultEdge.class); // creation d'un graphe simple avec des aretes Default Edge

        try (BufferedReader reader = new BufferedReader(new FileReader(chemin))) { // lecture de toutes les lignes du fichiers 
            String ligne;
            while ((ligne = reader.readLine()) != null) { // boucle While : tant que la lecture n'est pas finie 
                if (ligne.isBlank()) continue;  // la lecture ne prend pas en compte les lignes vides 
                JsonObject film = JsonParser.parseString(ligne).getAsJsonObject();  // sinon on converti la ligne en un objet JSON                                 
                if (film.has("cast")) { // Si un film contient une liste d'acteur 
                    JsonArray acteurs = film.getAsJsonArray("cast"); // on recupere l'ensemble des acteurs sous la forme d'un tableau JsonArray

                    for (int i = 0; i < acteurs.size(); i++) { // parcour de l'ensemble des acteurs par indice 
                        String acteur1 = acteurs.get(i).getAsString().replaceAll("\\[\\[|\\]\\]", "");  // on recupere le nom de chaque acteur en enlevant les crochets 
                        graphe.addVertex(acteur1); // on ajoute tous les acteurs en tant que sommets du graphe 

                        for (int j = i + 1; j < acteurs.size(); j++) { // 2eme parcours des acteurs par indice 
                            String acteur2 = acteurs.get(j).getAsString().replaceAll("\\[\\[|\\]\\]", ""); // meme traitement que pour le premier parcour 
                            graphe.addVertex(acteur2); // ajout des second acteurs sur le graphe 
                            graphe.addEdge(acteur1, acteur2); // creation d'arete entre les acteurs 
                        }
                    }
                }
            }
        }
        return graphe; // on retourne le graphe 
}

    public static Set<String> getCollaborateursCommuns(Graph<String, DefaultEdge> g, String u, String v) {
        Set<String> voisinsU = new HashSet<>(Graphs.neighborListOf(g, u));
        Set<String> voisinsV = new HashSet<>(Graphs.neighborListOf(g, v));
    
        Set<String> collaborateursCommuns = new HashSet<>();
        for (String voisin : voisinsU) {
            if (voisinsV.contains(voisin)) {
                collaborateursCommuns.add(voisin);
            }
        }
    
        return collaborateursCommuns;
    }

    public static Set<String> collaboProche(Graph<String, DefaultEdge> g, String u, int k){
        if (!g.containsVertex(u)) {
            System.out.println(u + " est un illustre inconnu");
            return null;
        }
        Set<String> collaborateurs = new HashSet<>();
        collaborateurs.add(u);

        for (int i = 1; i <= k; i++) {
            Set<String> collaborateursDirects = new HashSet<>();
            for (String c : collaborateurs) {
                for (String voisin : Graphs.neighborListOf(g, c)) {
                    if (!collaborateurs.contains(voisin)) {
                        collaborateursDirects.add(voisin);
                    }
                }
            }    
            collaborateurs.addAll(collaborateursDirects);
        }
    
        return collaborateurs;
    }


    

    public static int distanceEntreActeurs(Graph<String, DefaultEdge> g, String u, String v) {
    if (!g.containsVertex(u) || !g.containsVertex(v)) {
        System.out.println("Un des sommets est inconnu");
        return -1;
    }
    Set<String> visites = new HashSet<>();
    visites.add(u); 
    List<String> courant = new ArrayList<>();
    courant.add(u);
    int distance = 0;

    while (!courant.isEmpty()) {
        List<String> prochain = new ArrayList<>();

        for (String sommet : courant) {
            if (sommet.equals(v)) {
                return distance;
            }
            for (String voisin : Graphs.neighborListOf(g, sommet)) {
                if (!visites.contains(voisin)) {
                    visites.add(voisin);
                    prochain.add(voisin);
                }
            }
        }
        courant = prochain; 
        distance++; 
    }
    return -1;
    }
}








