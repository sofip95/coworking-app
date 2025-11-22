import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

export interface RecursoDTO {
  id?: number;
  nombre?: string;
  descripcion?: string;
  tipo?: string;
  ubicacion?: string;
  capacidad?: number;
  precioHora?: number;
  estado?: string;
}

@Injectable({ providedIn: 'root' })
export class RecursoService {
  // Backend API port 8081
  private apiUrl = 'http://localhost:8081/api/v1/recursos';

  constructor(private http: HttpClient, private auth: AuthService) {}

  getRecursos(filters?: { tipo?: string; ubicacion?: string; estado?: string }): Observable<RecursoDTO[]> {
    let params = new HttpParams();
    if (filters?.tipo) params = params.set('tipo', filters.tipo);
    if (filters?.ubicacion) params = params.set('ubicacion', filters.ubicacion);
    if (filters?.estado) params = params.set('estado', filters.estado);
    return this.http.get<RecursoDTO[]>(this.apiUrl, { params });
  }

  getRecursoById(id: number) {
    return this.http.get<RecursoDTO>(`${this.apiUrl}/${id}`);
  }

  getAllRecursos() {
    return this.http.get<RecursoDTO[]>(this.apiUrl);
  }

  createRecurso(recurso: RecursoDTO) {
    const user = this.auth.getCurrentUser();
    const role = user?.rol || '';
    const headers = new HttpHeaders().set('X-User-Role', String(role));
    return this.http.post<RecursoDTO>(this.apiUrl, recurso, { headers });
  }
}
