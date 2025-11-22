import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReservaService } from '../services/reserva.service';
import { RecursoService } from '../services/recurso.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reportes.html'
})
export class Reportes {
  loading = false;
  stats: any = {};

  constructor(private reservaService: ReservaService, private recursoService: RecursoService, private auth: AuthService) {}

  generar() {
    this.loading = true;
    // Simple client-side report: total recursos, total reservas, reservas por estado
    this.recursoService.getAllRecursos().subscribe({
      next: (recursos: any[]) => {
        this.stats.totalRecursos = recursos.length;
        this.reservaService.getAllReservas().subscribe({
          next: (reservas: any[]) => {
            this.stats.totalReservas = reservas.length;
            this.stats.porEstado = reservas.reduce((acc: any, r: any) => { acc[r.estado] = (acc[r.estado] || 0) + 1; return acc; }, {});
            this.loading = false;
          },
          error: (err) => { console.error(err); this.loading = false; }
        });
      },
      error: (err) => { console.error(err); this.loading = false; }
    });
  }
}
