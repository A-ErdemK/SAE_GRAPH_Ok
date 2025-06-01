package fr.univ_orleans.iut45.r207.tp2;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;



//3.1
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

//3.2
    public static Set<String> getCollaborateursCommuns(Graph<String, DefaultEdge> g, String u, String v) {
        Set<String> voisinsU = new HashSet<>(Graphs.neighborListOf(g, u)); // on cree un ensemble en y mettant tous les voisins de u 
        Set<String> voisinsV = new HashSet<>(Graphs.neighborListOf(g, v)); // idem pour tous les voisins de v
        voisinsU.remove(u); // on retire u pour le premier ensemble 
        voisinsU.remove(v); // idem pour v 
        voisinsV.remove(u); // meme chose pour le second ensemble 
        voisinsV.remove(v); // idem
        voisinsU.retainAll(voisinsV); //  on retire tous les sommets du premier ensemble qui ne sont pas contenus dans le second ensemble 
        return voisinsU; // on retourne le premier ensemble 
    }

//3.3
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


    
//3.4
    public static int distanceEntreActeurs(Graph<String, DefaultEdge> g, String u, String v) {
    if (!g.containsVertex(u) || !g.containsVertex(v)) { // si le sommet u ou v n'existe pas dans le graphe 
        System.out.println("Un des sommets est inconnu"); // on indique qu'il n'existe pas 
        return -1;
    }
    Set<String> visites = new HashSet<>(); // creation d'un ensemble vide pour les sommets deja visités 
    visites.add(u);  // on y ajoute le sommet u 
    List<String> courant = new ArrayList<>(); // creation d'une liste vide pour les sommets du niveau courant 
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
    public static int centralite(Graph<String, DefaultEdge> g, String u) {
    Set<String> visites = new HashSet<>();
    visites.add(u);
    List<String> courant = new ArrayList<>();
    courant.add(u);
    int distance = 0;
    int maxDistance = 0;

    while (!courant.isEmpty()) {
        List<String> prochain = new ArrayList<>();
        for (String sommet : courant) {
            for (String voisin : Graphs.neighborListOf(g, sommet)) {
                if (!visites.contains(voisin)) {
                    visites.add(voisin);
                    prochain.add(voisin);
                }
            }
        }
        if (!prochain.isEmpty()) {
            maxDistance++;
        }
        courant = prochain;
    }
    if (visites.size() != g.vertexSet().size()) {
        return -1; // sommet isolé ou graphe non connexe
    }
    return maxDistance;
}

// Trouve le centre du graphe (sommet avec la plus petite excentricité)
public static String centreGraphe(Graph<String, DefaultEdge> g) {
    String centre = null;
    int minCentralite = Integer.MAX_VALUE;
    for (String v : g.vertexSet()) {
        int c = centralite(g, v);
        if (c != -1 && c < minCentralite) {
            minCentralite = c;
            centre = v;
        }
    }
    return centre;
}

//3.5
    public static int distanceMaximale(Graph<String, DefaultEdge> g) {
    int distanceMax = 0;
    List<String> sommets = new ArrayList<>(g.vertexSet());

    for (int i = 0; i < sommets.size(); i++) {
        for (int j = i + 1; j < sommets.size(); j++) {
            int d = distanceEntreActeurs(g, sommets.get(i), sommets.get(j));
            if (d > distanceMax) {
                distanceMax = d;
            }
        }
    }

    return distanceMax;
    }
    public static double distanceMoyenne(Graph<String, DefaultEdge> g, String s) {
    if (!g.containsVertex(s)) {
        System.out.println(s + " n'apparaît pas dans le graphe");
        return -1;
    }

    double sommeDistance = 0;
    int nbrDistances = 0;

    for (String sommet : g.vertexSet()) {
        if (!sommet.equals(s)) {
            int d = distanceEntreActeurs(g, s, sommet);
            if (d != -1) {
                sommeDistance += d;
                nbrDistances++;
            }
        }
    }

    if (nbrDistances > 0) {
        return sommeDistance / nbrDistances;
    } else {
        return -1;
    }
    }

    public static String acteurPlusProcheEnMoyenne(Graph<String, DefaultEdge> g) {
    String meilleur = null;
    double minMoyenne = Double.MAX_VALUE;

    for (String v : g.vertexSet()) {
        double moyenne = distanceMoyenne(g, v);
        if (moyenne != -1 && moyenne < minMoyenne) {
            minMoyenne = moyenne;
            meilleur = v;
        }
    }

    return meilleur;
    }

    //Bonus 
    public static String centreDuGroupe(Graph<String, DefaultEdge> g, Set<String> groupe) {
    String meilleur = null;
    int minMaxDistance = Integer.MAX_VALUE;

    for (String candidat : g.vertexSet()) {
        int maxDistance = 0;
        boolean accessible = true;

        for (String membre : groupe) {
            int d = Echauffement.distanceEntreActeurs(g, candidat, membre);
            if (d == -1) {
                accessible = false;
                break;
            }
            if (d > maxDistance) {
                maxDistance = d;
            }
        }

        if (accessible && maxDistance < minMaxDistance) {
            minMaxDistance = maxDistance;
            meilleur = candidat;
        }
    }

    return meilleur;
    }

    public static Graph<String, DefaultEdge> sousGrapheDesProches(Graph<String, DefaultEdge> g, String acteur, int k) {
        Set<String> proches = collaboProche(g, acteur, k); // réutilise ma méthode 
        return new org.jgrapht.graph.AsSubgraph<>(g, proches);
    }


}








