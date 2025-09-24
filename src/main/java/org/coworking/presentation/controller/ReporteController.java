package org.coworking.presentation.controller;

import java.util.List;
import org.coworking.domain.service.ReporteService;
import org.coworking.domain.dto.ReporteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "API para la gestión de reportes")
public class ReporteController {

    private final ReporteService reporteService;

    @Autowired
    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los reportes", description = "Devuelve una lista de todos los reportes registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reportes obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ReporteDTO>> getAllReportes() {
        return null;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reporte por ID", description = "Devuelve los detalles de un reporte específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReporteDTO> getReporteById(@PathVariable @Parameter (description = "ID del reporte a obtener") String idReporte) {
        return null;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo reporte", description = "Crea un nuevo reporte en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reporte creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReporteDTO> createReporte(@RequestBody @Parameter(description = "Datos del reporte a crear") ReporteDTO reporteDTO) {
        return null;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reporte por ID", description = "Actualiza los detalles de un reporte existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reporte actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"), 
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReporteDTO> updateReporte(@PathVariable String idReporte, @RequestBody ReporteDTO reporteDTO) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reporte por ID", description = "Elimina un reporte específico del sistema por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reporte eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteReporte(@PathVariable String idReporte) {
        return null;
    }
    
}
