package br.com.uern.rotas_samu.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BetterHospitalRequest(@NotNull @Size(max = 116) int ocurrence) {
}
