import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

export interface UsuarioDTO {
  id?: number;
  nombre: string;
  email: string;
  contraseña: string;
  telefono?: string;
  rol: string;
  estado?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/v1'; // Adjust if backend port differs
  private tokenKey = 'authToken';
  private userKey = 'currentUser';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<UsuarioDTO> {
    return this.http.post<UsuarioDTO>(`${this.apiUrl}/usuarios/login`, { email, password }, {
      params: { email, contraseña: password }
    }).pipe(
      tap(user => {
        this.setToken('dummy-token'); // In a real app, get token from response
        this.setCurrentUser(user);
      }),
      catchError(this.handleError)
    );
  }

  register(user: UsuarioDTO): Observable<UsuarioDTO> {
    return this.http.post<UsuarioDTO>(`${this.apiUrl}/usuarios`, user).pipe(
      catchError(this.handleError)
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  private setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  getCurrentUser(): UsuarioDTO | null {
    const user = localStorage.getItem(this.userKey);
    return user ? JSON.parse(user) : null;
  }

  private setCurrentUser(user: UsuarioDTO): void {
    localStorage.setItem(this.userKey, JSON.stringify(user));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (error.error instanceof ErrorEvent) {
      // Client-side or network error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Backend returned an error
      errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(errorMessage);
  }
}
