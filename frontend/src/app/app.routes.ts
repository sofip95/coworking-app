import { Routes } from '@angular/router';
import { provideRouter } from '@angular/router';

import { LoginComponent } from './login/login';
import { LayoutComponent } from './layout/layout';
import { DashboardComponent } from './dashboard/dashboard';
import { RegisterComponent } from './register/register';
import { Catalogo } from './catalogo/catalogo';
import { Reserva } from './reserva/reserva';
import { Perfil } from './perfil/perfil';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },

    {
        path: '',
        component: LayoutComponent,
        children: [
            { path: 'dashboard', component: DashboardComponent },
            { path: 'recurso', loadComponent: () => import('./recurso/recurso').then(m => m.Recurso) },
            { path: 'reportes', loadComponent: () => import('./reportes/reportes').then(m => m.Reportes) },
            { path: 'suscripciones', loadComponent: () => import('./suscripciones/suscripciones').then(m => m.Suscripciones) },
            { path: 'catalogo', component: Catalogo },
            { path: 'mis-reservas', component: Reserva },
            { path: 'perfil', component: Perfil },
        ]
    },

    {path: '**', redirectTo: 'login'}
];
