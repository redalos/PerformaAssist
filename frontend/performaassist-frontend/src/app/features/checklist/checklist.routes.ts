import { Routes } from '@angular/router';

export const checklistRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./checklist-list/checklist-list.component').then(m => m.ChecklistListComponent)
  },
  {
    path: 'templates',
    loadComponent: () => import('./template-list/template-list.component').then(m => m.TemplateListComponent)
  },
  {
    path: 'templates/new',
    loadComponent: () => import('./template-form/template-form.component').then(m => m.TemplateFormComponent)
  },
  {
    path: 'templates/:id',
    loadComponent: () => import('./template-detail/template-detail.component').then(m => m.TemplateDetailComponent)
  },
  {
    path: 'templates/:id/edit',
    loadComponent: () => import('./template-form/template-form.component').then(m => m.TemplateFormComponent)
  }
];

