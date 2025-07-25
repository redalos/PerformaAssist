import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-action-plan-list',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  template: `
    <div class="space-y-6">
      <!-- Header -->
      <div class="flex justify-between items-center">
        <h1 class="text-2xl font-bold text-gray-900">
          {{ 'actionPlan.title' | translate }}
        </h1>
        <button 
          routerLink="/action-plans/new"
          class="btn-primary">
          {{ 'actionPlan.create' | translate }}
        </button>
      </div>

      <!-- Content -->
      <div class="bg-white shadow rounded-lg p-6">
        <div class="text-center py-12">
          <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
          </svg>
          <h3 class="mt-2 text-sm font-medium text-gray-900">Aucun plan d'action</h3>
          <p class="mt-1 text-sm text-gray-500">Créez votre premier plan d'action pour commencer.</p>
          <div class="mt-6">
            <button 
              routerLink="/action-plans/new"
              class="btn-primary">
              {{ 'actionPlan.create' | translate }}
            </button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class ActionPlanListComponent {}

