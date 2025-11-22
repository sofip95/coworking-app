import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './layout.html',
  styleUrls: ['./layout.css'],
})
export class LayoutComponent implements OnInit, OnDestroy {

  username = 'Usuario';
  isAdmin = false;
  private userSub?: Subscription;

  constructor(private router: Router, private auth: AuthService) {}

  ngOnInit() {
    this.userSub = this.auth.currentUser$.subscribe(user => {
      if (user) {
        this.username = user.nombre || 'Usuario';
        this.isAdmin = String(user.rol).toUpperCase() === 'ADMIN';
      } else {
        this.username = 'Usuario';
        this.isAdmin = false;
      }
    });
  }

  ngOnDestroy() {
    if (this.userSub) this.userSub.unsubscribe();
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
