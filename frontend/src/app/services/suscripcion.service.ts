import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface SuscripcionDTO {
  id?: number;
  nombre?: string;
  tipo?: string;
  duracion?: string;
  precio?: number;
}

export interface UsuarioSuscripcionDTO {
  id?: number;
  usuarioId?: number;
  suscripcionId?: number;
  estado?: string;
  fechaInicio?: string;
  fechaFin?: string;
}

@Injectable({ providedIn: 'root' })
export class SuscripcionService {
  private apiUrl = 'http://localhost:8081/api/v1';

  constructor(private http: HttpClient) {}

  getAllSuscripciones(): Observable<SuscripcionDTO[]> {
    return this.http.get<SuscripcionDTO[]>(`${this.apiUrl}/suscripciones`);
  }

  getAllUsuarioSuscripciones(): Observable<UsuarioSuscripcionDTO[]> {
    return this.http.get<UsuarioSuscripcionDTO[]>(`${this.apiUrl}/usuario_suscripciones`);
  }

  createUsuarioSuscripcion(payload: Partial<UsuarioSuscripcionDTO>) {
    return this.http.post<UsuarioSuscripcionDTO>(`${this.apiUrl}/usuario_suscripciones`, payload);
  }

  createSuscripcion(payload: Partial<SuscripcionDTO>) {
    return this.http.post<SuscripcionDTO>(`${this.apiUrl}/suscripciones`, payload);
  }
}
