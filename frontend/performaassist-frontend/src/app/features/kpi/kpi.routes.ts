import { Routes } from '@angular/router';

export const kpiRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./kpi-list/kpi-list.component').then(m => m.KpiListComponent)
  }
];

