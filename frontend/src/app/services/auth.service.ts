import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
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
  providedIn: 'root'
})
export class AuthService {
  // Backend runs on port 8081
  private apiUrl = 'http://localhost:8081/api/v1';
  private tokenKey = 'authToken';
  private userKey = 'currentUser';
  private currentUserSubject: BehaviorSubject<UsuarioDTO | null> = new BehaviorSubject<UsuarioDTO | null>(localStorage.getItem('currentUser') ? JSON.parse(localStorage.getItem('currentUser')!) : null);

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<UsuarioDTO> {
    const params = new HttpParams()
      .set('email', email)
      .set('contraseña', password);

    return this.http.post<UsuarioDTO>(`${this.apiUrl}/usuarios/login`, null, { params }).pipe(
      tap(user => {
        // backend currently returns a UsuarioDTO; token handling omitted for simplicity
        this.setToken('dummy-token');
        this.setCurrentUser(user);
      }),
      catchError(this.handleError)
    );
  }

  register(user: UsuarioDTO): Observable<UsuarioDTO> {
    console.debug('AuthService.register: payload =', user);
    return this.http.post<UsuarioDTO>(`${this.apiUrl}/usuarios`, user).pipe(
      tap(resp => console.debug('AuthService.register: response =', resp)),
      catchError((err) => {
        console.error('AuthService.register error', err);
        return this.handleError(err);
      })
    );
  }

  getAllUsuarios() {
    return this.http.get<UsuarioDTO[]>(`${this.apiUrl}/usuarios`);
  }

  updateUsuario(id: number, user: Partial<UsuarioDTO>) {
    return this.http.put<UsuarioDTO>(`${this.apiUrl}/usuarios/${id}`, user).pipe(
      tap(updated => {
        // If the updated user is the current user, refresh stored copy
        const current = this.getCurrentUser();
        if (current && current.id === updated.id) {
          this.setCurrentUser(updated);
        }
      }),
      catchError(this.handleError)
    );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
    if (this.currentUserSubject) {
      this.currentUserSubject.next(null);
    }
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
    return user ? JSON.parse(user) : (this.currentUserSubject ? this.currentUserSubject.value : null);
  }

  private setCurrentUser(user: UsuarioDTO): void {
    localStorage.setItem(this.userKey, JSON.stringify(user));
    if (!this.currentUserSubject) {
      const stored = localStorage.getItem(this.userKey);
      this.currentUserSubject = new BehaviorSubject<UsuarioDTO | null>(stored ? JSON.parse(stored) : null);
    } else {
      this.currentUserSubject.next(user);
    }
  }

  /** Observable stream of current user that components can subscribe to */
  get currentUser$(): Observable<UsuarioDTO | null> {
    if (!this.currentUserSubject) {
      const stored = localStorage.getItem(this.userKey);
      this.currentUserSubject = new BehaviorSubject<UsuarioDTO | null>(stored ? JSON.parse(stored) : null);
    }
    return this.currentUserSubject.asObservable();
  }

  private handleError(error: HttpErrorResponse) {
    // Propagate the full HttpErrorResponse so components can inspect status/message
    return throwError(() => error);
  }
}
