import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService, UsuarioDTO } from '../services/auth.service';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './perfil.html',
  styleUrls: ['./perfil.css'],
})
export class Perfil implements OnInit {
  id: number | null = null;
  nombre = '';
  email = '';
  telefono = '';
  rol = '';

  isLoading = false;
  successMsg = '';
  errorMsg = '';

  constructor(private auth: AuthService) {}

  ngOnInit(): void {
    const user = this.auth.getCurrentUser();
    if (user) {
      this.id = user.id ?? null;
      this.nombre = user.nombre || '';
      this.email = user.email || '';
      this.telefono = user.telefono || '';
      this.rol = user.rol || '';
    }
  }

  save() {
    this.errorMsg = '';
    this.successMsg = '';

    if (!this.id) {
      this.errorMsg = 'Usuario no identificado';
      return;
    }

    this.isLoading = true;
    const payload: Partial<UsuarioDTO> = {
      nombre: this.nombre,
      telefono: this.telefono,
      rol: this.rol
    };

    this.auth.updateUsuario(this.id, payload).subscribe({
      next: (updated) => {
        this.isLoading = false;
        this.successMsg = 'Perfil actualizado correctamente';
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMsg = 'Error al actualizar perfil';
        console.error('Perfil.save error', err);
      }
    });
  }
}
