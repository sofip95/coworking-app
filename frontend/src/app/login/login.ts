import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {

  email = '';
  password = '';
  errorMsg = '';
  isLoading = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  login() {
    this.errorMsg = '';

    if (!this.email || !this.password) {
      this.errorMsg = 'Por favor completa todos los campos';
      return;
    }

    this.isLoading = true;

    this.authService.login(this.email, this.password).subscribe({
      next: (response) => {
        this.isLoading = false;
        // AuthService.login already stores current user and token
        const current = this.authService.getCurrentUser();
        if (current) {
          // Redirect to dashboard (or catalogo if not admin)
          if (current.rol === 'ADMIN') {
            this.router.navigate(['/dashboard']);
          } else {
            this.router.navigate(['/catalogo']);
          }
        } else {
          this.router.navigate(['/catalogo']);
        }
      },
      error: (error) => {
        this.isLoading = false;
        console.error('Error al iniciar sesión:', error);
        
        if (error.status === 401) {
          this.errorMsg = 'Correo o contraseña incorrectos';
        } else {
          this.errorMsg = 'Error al iniciar sesión. Por favor intenta nuevamente';
        }
      }
    });
  }

  goToRegister() {
    this.router.navigate(['/register']);
  }
}

