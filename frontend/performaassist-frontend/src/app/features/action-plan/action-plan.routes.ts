import { Routes } from '@angular/router';

export const actionPlanRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./action-plan-list/action-plan-list.component').then(m => m.ActionPlanListComponent)
  },
  {
    path: 'new',
    loadComponent: () => import('./action-plan-form/action-plan-form.component').then(m => m.ActionPlanFormComponent)
  },
  {
    path: ':id',
    loadComponent: () => import('./action-plan-detail/action-plan-detail.component').then(m => m.ActionPlanDetailComponent)
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./action-plan-form/action-plan-form.component').then(m => m.ActionPlanFormComponent)
  }
];

