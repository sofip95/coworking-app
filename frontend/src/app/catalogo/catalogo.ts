import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RecursoService, RecursoDTO } from '../services/recurso.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-catalogo',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './catalogo.html',
  styleUrls: ['./catalogo.css'],
})
export class Catalogo implements OnInit {
  recursos: RecursoDTO[] = [];
  isLoading = false;
  error = '';

  constructor(private recursoService: RecursoService, private auth: AuthService) {}

  ngOnInit(): void {
    this.loadRecursos();
  }

  loadRecursos() {
    this.recursoService.getAllRecursos().subscribe({ next: (r:any[]) => this.recursos = r, error: (err) => console.error(err) });
  }
}
