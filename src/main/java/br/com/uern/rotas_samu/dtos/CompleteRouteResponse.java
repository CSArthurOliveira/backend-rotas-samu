package br.com.uern.rotas_samu.dtos;

public record CompleteRouteResponse(int ocurrenceVertex, int[] toOcurrencePath, int toOcurrenceDistance,int hospitalVertex, int[] toHospitalPath, int toHospitalDistance, double executionTimeMillis) {
}
