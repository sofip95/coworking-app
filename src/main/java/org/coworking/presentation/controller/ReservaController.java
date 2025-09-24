package org.coworking.presentation.controller;

import java.util.List;
import org.coworking.domain.service.ReservaService;
import org.coworking.domain.dto.ReservaDTO;
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
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "API para la gestión de reservas")
public class ReservaController {

    private final ReservaService reservaService;

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las reservas", description = "Devuelve una lista de todas las reservas registradas en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ReservaDTO>> getAllReservas() {
        return null;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID", description = "Devuelve los detalles de una reserva específica por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReservaDTO> getReservaById(@PathVariable @Parameter (description = "ID de la reserva a obtener") String idReserva) {
        return null;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reserva", description = "Crea una nueva reserva en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reserva creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReservaDTO> createReserva(@RequestBody @Parameter(description = "Datos de la reserva a crear") ReservaDTO reservaDTO) {
        return null;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reserva", description = "Actualiza los detalles de una reserva existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReservaDTO> updateReserva(@PathVariable String idReserva, @RequestBody ReservaDTO reservaDTO) {
        return null;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reserva", description = "Elimina una reserva existente del sistema por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteReserva(@PathVariable String idReserva) {
        return null;
    }
    
}
