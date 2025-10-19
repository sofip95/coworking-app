package org.coworking.presentation.controller;

import org.coworking.domain.dto.ReporteDTO;
import org.coworking.domain.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operaciones de reportes
 */
@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reportes", description = "Operaciones CRUD para la gestión de reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteService reporteService;

    // -------------------------- CREATE --------------------------
    @PostMapping
    @Operation(summary = "Crear nuevo reporte", description = "Genera un nuevo reporte en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReporteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o incompletos"),
            @ApiResponse(responseCode = "404", description = "Recurso no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> createReporte(@RequestBody ReporteDTO reporteDTO) {
        try {
            ReporteDTO created = reporteService.createReporte(reporteDTO);
            return ResponseEntity.status(HttpStatus.OK).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al crear reporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // -------------------------- SEARCH BY ID --------------------------
    @GetMapping("/{id}")
    @Operation(summary = "Buscar reporte por ID", description = "Obtiene los detalles de un reporte específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReporteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getReporteById(@PathVariable Long id) {
        try {
            ReporteDTO reporte = reporteService.getReporteById(id);
            return ResponseEntity.ok(reporte);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reporte no encontrado con ID: " + id);
        } catch (Exception e) {
            log.error("Error interno al obtener reporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // -------------------------- SEARCH ALL --------------------------
    @GetMapping
    @Operation(summary = "Listar todos los reportes", description = "Obtiene la lista completa de reportes registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "No se encontraron reportes"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getAllReportes() {
        try {
            List<ReporteDTO> reportes = reporteService.getAllReportes();
            if (reportes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron reportes registrados");
            }
            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            log.error("Error interno al obtener todos los reportes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // -------------------------- DELETE --------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reporte", description = "Elimina un reporte del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Reporte no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> deleteReporte(@PathVariable Long id) {
        try {
            reporteService.deleteReporte(id);
            return ResponseEntity.ok("Reporte eliminado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reporte no encontrado con ID: " + id);
        } catch (Exception e) {
            log.error("Error interno al eliminar reporte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // -------------------------- SEARCH BY TIPO --------------------------
    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar reportes por tipo", description = "Obtiene todos los reportes que coinciden con el tipo especificado (ej. 'INGRESOS', 'OCUPACION')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reportes encontrados"),
            @ApiResponse(responseCode = "400", description = "Tipo inválido"),
            @ApiResponse(responseCode = "404", description = "No se encontraron reportes del tipo especificado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getReportesByTipo(@PathVariable String tipo) {
        try {
            List<ReporteDTO> reportes = reporteService.getReportesByTipo(tipo);
            if (reportes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron reportes del tipo: " + tipo);
            }
            return ResponseEntity.ok(reportes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al obtener reportes por tipo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

}
