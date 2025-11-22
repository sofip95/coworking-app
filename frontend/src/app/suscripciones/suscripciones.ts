import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SuscripcionService } from '../services/suscripcion.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-suscripciones',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './suscripciones.html'
})
export class Suscripciones implements OnInit {
  disponibles: any[] = [];
  mySubs: any[] = [];
  loading = false;

  constructor(private susService: SuscripcionService, private auth: AuthService) {}

  ngOnInit() {
    this.loadDisponibles();
    this.loadMySubs();
  }

  loadDisponibles() {
    this.susService.getAllSuscripciones().subscribe({ next: (s:any[]) => this.disponibles = s, error: (e)=> console.error(e) });
  }

  loadMySubs() {
    const user = this.auth.getCurrentUser();
    if (!user) return;
    this.susService.getAllUsuarioSuscripciones().subscribe({ next: (all:any[]) => this.mySubs = all.filter(x=> x.usuarioId === user.id), error: (e)=> console.error(e) });
  }

  subscribeTo(susId: number) {
    const user = this.auth.getCurrentUser();
    if (!user) { alert('Debes iniciar sesión'); return; }
    const payload = { usuarioId: user.id, suscripcionId: susId };
    this.susService.createUsuarioSuscripcion(payload).subscribe({ next: (r)=> { alert('Suscrito'); this.loadMySubs(); }, error: (e)=> { console.error(e); alert('Error'); } });
  }
}
