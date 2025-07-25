import { Routes } from '@angular/router';

export const meetingRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./meeting-list/meeting-list.component').then(m => m.MeetingListComponent)
  }
];

