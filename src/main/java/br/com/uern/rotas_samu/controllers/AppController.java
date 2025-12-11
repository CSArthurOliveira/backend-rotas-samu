package br.com.uern.rotas_samu.controllers;

import br.com.uern.rotas_samu.dtos.BetterHospitalRequest;
import br.com.uern.rotas_samu.dtos.DijkstraRequest;
import br.com.uern.rotas_samu.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AppController {
    private final AppService appService;

    @Autowired
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/sortear-vertice-ocorrencia")
    public ResponseEntity<?> getRandomVertex() {
        return ResponseEntity.ok(appService.getRandomVertex());
    }

    @PostMapping("/calcular-rota-ocorrencia")
    public ResponseEntity<?> getShortestPath(@RequestBody DijkstraRequest request) {
        return ResponseEntity.ok(appService.getShortestPath(request));
    }

    @PostMapping("/hospital-mais-proximo")
    public ResponseEntity<?> getBetterHospitalPath(@RequestBody BetterHospitalRequest request) {
        return ResponseEntity.ok(appService.getBetterHospitalPath(request));
    }

    @PostMapping("/rota-completa-selecionada")
    public ResponseEntity<?> getCompleteRoute(@RequestBody DijkstraRequest request) {
        return ResponseEntity.ok(appService.getCompleteRoute(request));
    }

    @GetMapping("/rota-completa-aleatoria")
    public ResponseEntity<?> getBetterHospitalPath() {
        return ResponseEntity.ok(appService.getCompleteRoute());
    }

}
