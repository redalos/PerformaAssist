import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-action-plan-form',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  template: `
    <div class="space-y-6">
      <!-- Header -->
      <div class="flex items-center space-x-4">
        <button 
          routerLink="/action-plans"
          class="text-gray-400 hover:text-gray-600">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
          </svg>
        </button>
        <h1 class="text-2xl font-bold text-gray-900">
          {{ 'actionPlan.create' | translate }}
        </h1>
      </div>

      <!-- Form -->
      <div class="bg-white shadow rounded-lg p-6">
        <div class="text-center py-12">
          <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 100 4m0-4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 100 4m0-4v2m0-6V4"></path>
          </svg>
          <h3 class="mt-2 text-sm font-medium text-gray-900">Formulaire en cours de développement</h3>
          <p class="mt-1 text-sm text-gray-500">Le formulaire de création de plan d'action sera bientôt disponible.</p>
          <div class="mt-6">
            <button 
              routerLink="/action-plans"
              class="btn-secondary">
              {{ 'common.cancel' | translate }}
            </button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class ActionPlanFormComponent {}

