import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecursoService } from '../services/recurso.service';

@Component({
  selector: 'app-recurso',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './recurso.html',
  styleUrls: ['./recurso.css']
})
export class Recurso {
  newRecurso: any = {
    nombre: '',
    descripcion: '',
    tipo: '',
    ubicacion: '',
    capacidad: undefined,
    precioPorHora: undefined,
    estado: 'DISPONIBLE'
  };
  isLoading = false;
  successMessage = '';
  errorMessage = '';

  constructor(private recursoService: RecursoService) {}

  submit() {
    if (!this.newRecurso.nombre) {
      alert('El nombre es obligatorio');
      return;
    }
    this.isLoading = true;
    this.recursoService.createRecurso(this.newRecurso).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.successMessage = 'Recurso creado correctamente';
        this.errorMessage = '';
        this.newRecurso = { nombre: '', descripcion: '', tipo: '', ubicacion: '', capacidad: undefined, precioPorHora: undefined, estado: 'DISPONIBLE' };
      },
      error: (err) => {
        this.isLoading = false;
        console.error(err);
        this.errorMessage = 'Error creando recurso';
        this.successMessage = '';
      }
    });
  }
}
