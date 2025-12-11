package br.com.uern.rotas_samu.services;

import br.com.uern.rotas_samu.dtos.*;
import br.com.uern.rotas_samu.graph.Graph;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AppService {
    private final Graph graph;

    public AppService() throws IOException {
        this.graph = new Graph("/DistSAMU.txt");
    }

    public RandomVertexResponse getRandomVertex() {
        return new RandomVertexResponse(graph.getRandomVertex());
    }

    public DijkstraResponse getShortestPath(DijkstraRequest request) {
        return graph.getShortestPath(graph.getSamu(),request.ocurrence());
    }

    public DijkstraResponse getBetterHospitalPath(BetterHospitalRequest request) {
        return graph.getBetterHospitalPath(request.ocurrence());
    }

    public CompleteRouteResponse getCompleteRouteRandomly(){
       int ocurrenceVertex = graph.getRandomVertex();
       DijkstraResponse toOccurencePath = graph.getShortestPath(graph.getSamu(),ocurrenceVertex);
       DijkstraResponse toHospitalPath = graph.getBetterHospitalPath(ocurrenceVertex);
       return new CompleteRouteResponse(ocurrenceVertex,toOccurencePath.path(),toOccurencePath.distance(),toHospitalPath.path()[toHospitalPath.path().length-1],
               toHospitalPath.path(),toHospitalPath.distance(),
               (toOccurencePath.executionTimeMillis() + toHospitalPath.executionTimeMillis()));
    }

    public CompleteRouteResponse getCompleteRoute(DijkstraRequest request) {
        int ocurrenceVertex = request.ocurrence();
        DijkstraResponse toOccurencePath = graph.getShortestPath(graph.getSamu(),ocurrenceVertex);
        DijkstraResponse toHospitalPath = graph.getBetterHospitalPath(ocurrenceVertex);
        return new CompleteRouteResponse(ocurrenceVertex,toOccurencePath.path(),toOccurencePath.distance(),toHospitalPath.path()[toHospitalPath.path().length-1],
                toHospitalPath.path(),toHospitalPath.distance(),
                (toOccurencePath.executionTimeMillis() + toHospitalPath.executionTimeMillis()));
    }
}
