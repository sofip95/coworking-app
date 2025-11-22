import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ReservaService } from '../services/reserva.service';
import { RecursoService } from '../services/recurso.service';
import { AuthService } from '../services/auth.service';
import { SuscripcionService } from '../services/suscripcion.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'],
})
export class DashboardComponent implements OnInit {

  // Estadísticas
  totalReservas = 0;
  reservasConfirmadas = 0;
  reservasCanceladas = 0;
  totalUsuarios = 0;
  totalRecursos = 0;
  recursosDisponibles = 0;
  ingresosMes = 0;

  recursosMap: Map<string, any> = new Map();
  usuariosMap: Map<string, any> = new Map();
  reservasRecientes: any[] = [];
  // Para vistas de usuario
  mySuscripciones: any[] = [];

  // Crear recurso (admin)
  showCreateForm = false;
  newRecurso: any = {
    nombre: '',
    descripcion: '',
    tipo: '',
    ubicacion: '',
    capacidad: undefined,
    precioPorHora: undefined,
    estado: 'DISPONIBLE'
  };

  // Crear suscripción (admin)
  showCreateSusForm = false;
  newSuscripcion: any = {
    nombre: '',
    tipo: '',
    duracion: '',
    precio: undefined
  };

  isLoading = false;
  private userSub?: Subscription;
  private lastRole: string | null = null;

  constructor(
    private recursoService: RecursoService,
    public authService: AuthService,
    private router: Router
    , private suscripcionService: SuscripcionService
  ) {}
  

  ngOnInit() {
    // Suscribirse a cambios de usuario para actualizar la vista cuando cambie la sesión/rol
    this.userSub = this.authService.currentUser$.subscribe(current => {
      if (!current) {
        this.router.navigate(['/login']);
        return;
      }

      const role = current.rol ? String(current.rol).toUpperCase() : null;
      if (role === this.lastRole) {
        // mismo rol que antes, no recargar innecesariamente
        return;
      }
      this.lastRole = role;

      if (role === 'ADMIN') {
        this.cargarDatos();
      } else {
        this.cargarRecursos();
        this.loadMySuscripciones();
      }
    });
  }

  ngOnDestroy() {
    if (this.userSub) this.userSub.unsubscribe();
  }

  cargarDatos() {
    this.isLoading = true;
    this.cargarRecursos();
    this.cargarUsuarios();
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.mySuscripciones = [];
      return;
    }
    this.suscripcionService.getAllUsuarioSuscripciones().subscribe({
      next: (res: any) => {
        // filter client-side by usuario id since backend endpoint returns all
        this.mySuscripciones = (res || []).filter((us: any) => us.usuarioId === user.id);
      },
      error: (err: any) => {
        console.error('Error loading suscripciones for user', err);
        this.mySuscripciones = [];
      }
    });
  }

  cargarRecursos() {
    this.recursoService.getAllRecursos().subscribe({
      next: (recursos) => {
        this.totalRecursos = recursos.length;
        this.recursosDisponibles = recursos.filter(r => r.estado === 'DISPONIBLE').length;

        // Mapear recursos para uso posterior (usar id como string para Map key)
        recursos.forEach(recurso => {
          const key = (recurso.id !== undefined && recurso.id !== null) ? String(recurso.id) : '';
          this.recursosMap.set(key, recurso);
        });
      },
      error: (error) => {
        console.error('Error al cargar recursos:', error);
      }
    });
  }

  toggleCreateForm() {
    this.showCreateForm = !this.showCreateForm;
  }

  submitCreateRecurso() {
    // Basic validation
    if (!this.newRecurso.nombre || this.newRecurso.nombre.trim() === '') {
      alert('El nombre del recurso es obligatorio');
      return;
    }

    this.recursoService.createRecurso(this.newRecurso).subscribe({
      next: (created: any) => {
        this.createSuccessMessage = 'Recurso creado con éxito';
        this.createErrorMessage = '';
        // reset form and reload recursos
        this.newRecurso = { nombre: '', descripcion: '', tipo: '', ubicacion: '', capacidad: undefined, precioPorHora: undefined, estado: 'DISPONIBLE' };
        this.showCreateForm = false;
        this.cargarRecursos();
      },
      error: (err: any) => {
        console.error('Error creando recurso', err);
        this.createErrorMessage = 'Error al crear recurso';
        this.createSuccessMessage = '';
      }
    });
  }

  toggleCreateSusForm() {
    this.showCreateSusForm = !this.showCreateSusForm;
  }

  submitCreateSuscripcion() {
    if (!this.newSuscripcion.nombre || this.newSuscripcion.nombre.trim() === '') {
      alert('El nombre de la suscripción es obligatorio');
      return;
    }

    const payload = {
      nombre: this.newSuscripcion.nombre,
      tipo: this.newSuscripcion.tipo,
      duracion: this.newSuscripcion.duracion,
      precio: this.newSuscripcion.precio
    };

    this.suscripcionService.createSuscripcion(payload).subscribe({
      next: (created: any) => {
        this.createSusSuccessMessage = 'Suscripción creada con éxito';
        this.createSusErrorMessage = '';
        this.newSuscripcion = { nombre: '', tipo: '', duracion: '', precio: undefined };
        this.showCreateSusForm = false;
      },
      error: (err: any) => {
        console.error('Error creando suscripción', err);
        this.createSusErrorMessage = 'Error al crear suscripción';
        this.createSusSuccessMessage = '';
      }
    });
  }

  createSuccessMessage = '';
  createErrorMessage = '';
  createSusSuccessMessage = '';
  createSusErrorMessage = '';

  isAdmin(): boolean {
    const rol = this.authService.getCurrentUser()?.rol;
    if (!rol) return false;
    return String(rol).toUpperCase() === 'ADMIN';
  }

  loadMySuscripciones() {
    const user = this.authService.getCurrentUser();
    if (!user) {
      this.mySuscripciones = [];
      return;
    }

    this.suscripcionService.getAllUsuarioSuscripciones().subscribe({
      next: (all: any[]) => {
        this.mySuscripciones = (all || []).filter(s => s.usuarioId === user.id);
      },
      error: (err: any) => {
        console.error('Error al cargar suscripciones de usuario:', err);
        this.mySuscripciones = [];
      }
    });
  }

  cargarUsuarios() {
    this.authService.getAllUsuarios().subscribe({
      next: (usuarios: any) => {
        this.totalUsuarios = usuarios.length;
        
        // Mapear usuarios para uso posterior
        usuarios.forEach((usuario: any) => {
          this.usuariosMap.set(usuario.id, usuario);
        });
      },
      error: (error) => {
        console.error('Error al cargar usuarios:', error);
      }
    });
  }

  getNombreRecurso(recursoId: string): string {
    const recurso = this.recursosMap.get(recursoId);
    return recurso ? recurso.nombre : 'Recurso no encontrado';
  }

  getNombreUsuario(usuarioId: string): string {
    const usuario = this.usuariosMap.get(usuarioId);
    return usuario ? `${usuario.nombre} ${usuario.apellido}` : 'Usuario no encontrado';
  }

  formatearFecha(fecha: string): string {
    if (!fecha) return '';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-ES', { 
      year: 'numeric', 
      month: '2-digit', 
      day: '2-digit' 
    });
  }

  formatearHora(fecha: string): string {
    if (!fecha) return '';
    const date = new Date(fecha);
    return date.toLocaleTimeString('es-ES', { 
      hour: '2-digit', 
      minute: '2-digit' 
    });
  }

  getEstadoBadgeClass(estado: string): string {
    switch(estado) {
      case 'CONFIRMADA':
        return 'success';
      case 'PENDIENTE':
        return 'pending';
      case 'CANCELADA':
        return 'cancelled';
      default:
        return 'pending';
    }
  }

  getEstadoTexto(estado: string): string {
    switch(estado) {
      case 'CONFIRMADA':
        return 'Confirmado';
      case 'PENDIENTE':
        return 'Pendiente';
      case 'CANCELADA':
        return 'Cancelado';
      default:
        return estado;
    }
  }
}
