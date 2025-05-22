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
        voisinsU.remove(u);
        voisinsU.remove(v);
        voisinsV.remove(u);
        voisinsV.remove(v);
        voisinsU.retainAll(voisinsV);
        return voisinsU;
    }

    public static Set<String> collaboProche(Graph<String, DefaultEdge> g, String u, int k){
        if (!g.containsVertex(u)) { // si le sommet u n'est pas présent dans le graphe 
            System.out.println(u + " est un illustre inconnu"); // indique que le sommet est inexistant 
            return null;
        }
        Set<String> collaborateurs = new HashSet<>(); // creation d'un ensemble vide pour les collaborateurs
        collaborateurs.add(u); // on ajoute le sommet u dans l'ensemble des collaborateurs 

        for (int i = 1; i <= k; i++) { // boucle de profondeur k 
            Set<String> collaborateursDirects = new HashSet<>(); // creation d'un ensemble vide pour les collaborateurs directs 
            for (String c : collaborateurs) { // parcour pour chaque sommets contenu dans les collaborateurs 
                for (String voisin : Graphs.neighborListOf(g, c)) { //  pour chaque sommet de c , on regarde tous ses voisins 
                    if (!collaborateurs.contains(voisin)) { // si un voisin n'est pas dans l'ensemble collaborateur
                        collaborateursDirects.add(voisin); // on l'ajoute dans l'ensemble des collaborateur direct 
                    }
                }
            }    
            collaborateurs.addAll(collaborateursDirects); // On ajoute tous les nouveaux collaborateurs du niveau actuel à l’ensemble principal `collaborateurs
        }
    
        return collaborateurs; // on retourne tous les collaborateurs
    }


    

    public static int distanceEntreActeurs(Graph<String, DefaultEdge> g, String u, String v) {
    if (!g.containsVertex(u) || !g.containsVertex(v)) { // si le sommet u ou v n'existe pas dans le graphe 
        System.out.println("Un des sommets est inconnu"); // on indique qu'il n'existe pas 
        return -1;
    }
    Set<String> visites = new HashSet<>(); // creation d'un ensemble vide pour les sommets deja visités 
    visites.add(u);  // on y ajoute le sommet u 
    List<String> courant = new ArrayList<>(); // creation d'un ensemble vide pour les sommets du niveau courant 
    courant.add(u); // on y ajoute le sommet u 
    int distance = 0; // creation d'une variable distance initialisé à 0

    while (!courant.isEmpty()) { // tant que  l'ensemble des sommets courants n'est pas vide
        List<String> prochain = new ArrayList<>(); // creation d'une liste pour les prochains sommets 

        for (String sommet : courant) { // pour chaque sommets de l'ensemble des sommets courants 
            if (sommet.equals(v)) { // si un des sommet est le sommet v 
                return distance; // on retourne la distance 
            }
            for (String voisin : Graphs.neighborListOf(g, sommet)) { // pour chaque voisin du sommet actuel 
                if (!visites.contains(voisin)) { // si un voisin n'a pas été visitée 
                    visites.add(voisin); // on l'ajoute dans l'ensemble des sommets visitée
                    prochain.add(voisin); // on l'ajoute au prochain niveau 
                }
            }
        }
        courant = prochain; 
        distance++;  // on incremente la distance 
    }
    return -1; // si on sort de la boucle sans jamais avoir trouvé 'v' , ca veut dire qu'il n'existe aucun chemin entre u et v  
    }
}








