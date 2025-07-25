import { Routes } from '@angular/router';

export const documentRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./document-list/document-list.component').then(m => m.DocumentListComponent)
  }
];

