package br.com.uern.rotas_samu.dtos;

public record DijkstraResponse(int[] path, int distance, double executionTimeMillis) {
}
