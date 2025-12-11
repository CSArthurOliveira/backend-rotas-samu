package br.com.uern.rotas_samu.graph;

import br.com.uern.rotas_samu.dtos.DijkstraResponse;
import lombok.Getter;

import java.io.*;
import java.util.*;

@Getter
public class Graph {
    private final int[][]  distanceMatrix;
    private final int samu = 0;
    private final int[] hospitals = {2,66,105};
    private int vertexQuantity;
    private final int INF = 9999999;

    public Graph(String filepath) throws IOException {
        this.distanceMatrix = loadDistanceMatrix(filepath);
    }

    private int[][] loadDistanceMatrix(String filepath) throws IOException {
        InputStream is = getClass().getResourceAsStream(filepath);

        if(is == null){
            throw new FileNotFoundException("File not found: " + filepath);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        this.vertexQuantity = Integer.parseInt(br.readLine().trim());

        int[][] matrix = new int[vertexQuantity][vertexQuantity];

        for (int i = 0; i < vertexQuantity; i++) {
            String line = br.readLine();

            if (line == null) {
                throw new IOException("Unexpected null line in file while reading row " + i);
            }

            String[] parts = line.trim().split("\\s+");

            int column = 0;
            for (String valueStr : parts) {
                int value = Integer.parseInt(valueStr);

                if (value == -1) {
                    break;
                }

                if (column < vertexQuantity) {
                    matrix[i][column] = value;
                    column++;
                }
            }
        }

        br.close();
        return matrix;
    }

    public int getRandomVertex() {
        int[] protectedVertices = {samu, hospitals[0], hospitals[1], hospitals[2]};
        int randomVertex;
        boolean isProtected;
        do{
            isProtected = true;
            randomVertex = (int) (Math.random() * vertexQuantity);
            for (int protectedVertex : protectedVertices) {
                if (randomVertex == protectedVertex) {
                    isProtected = false;
                    break;
                }
            }
        }while(!isProtected);
        return randomVertex;
    }

    private int[][] copyDistanceMatrix() {
        int[][] copy = new int[vertexQuantity][vertexQuantity];

        for (int i = 0; i < vertexQuantity; i++) {
            System.arraycopy(distanceMatrix[i], 0, copy[i], 0, vertexQuantity);
        }

        return copy;
    }

    public DijkstraResponse getShortestPath(int start, int end) {

        double init = System.nanoTime();

        int[][] matrix = copyDistanceMatrix(); // trabalha na cópia
        int[] dist = new int[vertexQuantity];
        boolean[] visited = new boolean[vertexQuantity];
        int[] parent = new int[vertexQuantity];

        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);

        dist[start] = 0;

        for (int count = 0; count < vertexQuantity - 1; count++) {

            // —— Escolhe o vértice não visitado com menor distância ——
            int u = -1;
            int min = INF;

            for (int i = 0; i < vertexQuantity; i++) {
                if (!visited[i] && dist[i] < min) {
                    min = dist[i];
                    u = i;
                }
            }

            if (u == -1) break; // sem mais caminhos possíveis

            visited[u] = true;

            // —— Relaxamento das arestas ——
            for (int v = 0; v < vertexQuantity; v++) {
                int weight = matrix[u][v];

                // Se não houver conexão (9999), ignora
                if (weight == INF || weight == 0) continue;

                // Se achar caminho melhor, atualiza
                if (!visited[v] && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    parent[v] = u;
                }
            }
        }

        // —— Se não existe caminho ——
        if (dist[end] == INF) {
            return new DijkstraResponse(new int[0], INF, (System.nanoTime() - init)/1000000);
        }

        // —— Reconstrução do caminho ——
        List<Integer> pathList = new ArrayList<>();
        int current = end;

        while (current != -1) {
            pathList.add(current);
            current = parent[current];
        }

        Collections.reverse(pathList);

        int[] pathArray = pathList.stream().mapToInt(Integer::intValue).toArray();

        return new DijkstraResponse(pathArray, dist[end],(System.nanoTime() - init)/1000000);
    }

    public DijkstraResponse getBetterHospitalPath(int ocurrenceVertex) {
        DijkstraResponse bestPath = null;
        for (int hospital : this.hospitals) {
            DijkstraResponse currentPath = getShortestPath(ocurrenceVertex, hospital);
            if (bestPath == null || currentPath.distance() < bestPath.distance()) {
                bestPath = currentPath;
            }
        }
        return bestPath;
    }
}
