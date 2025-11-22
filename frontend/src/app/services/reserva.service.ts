import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReservaDTO {
  id?: number;
  recursoId?: number;
  usuarioId?: number;
  fechaInicio?: string;
  fechaFin?: string;
  estado?: string;
}

@Injectable({ providedIn: 'root' })
export class ReservaService {
  // Backend API port 8081
  private apiUrl = 'http://localhost:8081/api/reservas';

  constructor(private http: HttpClient) {}

  getAllReservas(): Observable<ReservaDTO[]> {
    return this.http.get<ReservaDTO[]>(this.apiUrl);
  }

  getReservasByUsuario(usuarioId: number) {
    return this.http.get<ReservaDTO[]>(`${this.apiUrl}/usuario/${usuarioId}`);
  }

  createReserva(data: ReservaDTO) {
    return this.http.post<ReservaDTO>(this.apiUrl, data);
  }
}
