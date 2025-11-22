import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {

  fullName = '';
  email = '';
  password = '';
  confirmPassword = '';
  telefono = '';
  rol = 'VISITANTE';

  successMsg = '';
  errorMsg = '';
  isLoading = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  register() {
    this.errorMsg = '';
    this.successMsg = '';

    // Validaciones
    if (!this.fullName || !this.email || !this.password || !this.confirmPassword) {
      this.errorMsg = 'Completa todos los campos';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMsg = 'Las contraseñas no coinciden';
      return;
    }

    // Preparar datos para enviar al backend
      const usuarioData = {
        nombre: this.fullName,
        email: this.email,
        'contraseña': this.password,
        telefono: this.telefono || '',
        rol: this.rol || 'VISITANTE'
      };

    this.isLoading = true;

    // Llamar al servicio para registrar el usuario
    this.authService.register(usuarioData).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMsg = 'Usuario registrado correctamente';
        this.clearForm();
        
        // Redirigir al login después de 2 segundos
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error al registrar usuario:', error);

        // Backend puede devolver mensaje como string o como objeto { message: '...' }
        const backendMessage = (() => {
          if (!error) return null;
          if (typeof error === 'string') return error;
          if (error.error) {
            if (typeof error.error === 'string') return error.error;
            if (error.error.message) return error.error.message;
          }
          if (error.message) return error.message;
          return null;
        })();

        if (error.status === 409) {
          this.errorMsg = backendMessage || 'El email ya está registrado';
        } else if (error.status === 400) {
          this.errorMsg = backendMessage || 'Datos inválidos. Por favor verifica la información';
        } else if (backendMessage) {
          this.errorMsg = backendMessage;
        } else {
          this.errorMsg = 'Error al registrar usuario. Por favor intenta nuevamente';
        }
      }
    });
  }

  clearForm() {
    this.fullName = '';
    this.email = '';
    this.password = '';
    this.confirmPassword = '';
  }

  backToLogin() {
    this.router.navigate(['/login']);
  }
}


