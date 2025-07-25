import { Routes } from '@angular/router';
import { authGuard, roleGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: '/dashboard',
    pathMatch: 'full'
  },
  {
    path: 'auth',
    loadChildren: () => import('./features/auth/auth.routes').then(m => m.authRoutes)
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent),
    canActivate: [authGuard]
  },
  {
    path: 'checklists',
    loadChildren: () => import('./features/checklist/checklist.routes').then(m => m.checklistRoutes),
    canActivate: [authGuard]
  },
  {
    path: 'action-plans',
    loadChildren: () => import('./features/action-plan/action-plan.routes').then(m => m.actionPlanRoutes),
    canActivate: [authGuard]
  },
  {
    path: 'meetings',
    loadChildren: () => import('./features/meeting/meeting.routes').then(m => m.meetingRoutes),
    canActivate: [authGuard, roleGuard(['ADMIN', 'MANAGER'])]
  },
  {
    path: 'kpi',
    loadChildren: () => import('./features/kpi/kpi.routes').then(m => m.kpiRoutes),
    canActivate: [authGuard]
  },
  {
    path: 'documents',
    loadChildren: () => import('./features/document/document.routes').then(m => m.documentRoutes),
    canActivate: [authGuard]
  },
  {
    path: '**',
    redirectTo: '/dashboard'
  }
];

