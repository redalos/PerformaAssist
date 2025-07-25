import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-template-list',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  template: `
    <div class="space-y-6">
      <!-- Header -->
      <div class="flex justify-between items-center">
        <h1 class="text-2xl font-bold text-gray-900">
          {{ 'checklist.templates' | translate }}
        </h1>
        <button 
          routerLink="/checklists/templates/new"
          class="btn-primary">
          {{ 'checklist.create' | translate }}
        </button>
      </div>

      <!-- Navigation tabs -->
      <div class="border-b border-gray-200">
        <nav class="-mb-px flex space-x-8">
          <a 
            routerLink="/checklists"
            routerLinkActive="border-blue-500 text-blue-600"
            class="border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm">
            {{ 'checklist.executions' | translate }}
          </a>
          <a 
            routerLink="/checklists/templates"
            routerLinkActive="border-blue-500 text-blue-600"
            class="border-blue-500 text-blue-600 whitespace-nowrap py-2 px-1 border-b-2 font-medium text-sm">
            {{ 'checklist.templates' | translate }}
          </a>
        </nav>
      </div>

      <!-- Content -->
      <div class="bg-white shadow rounded-lg p-6">
        <div class="text-center py-12">
          <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"></path>
          </svg>
          <h3 class="mt-2 text-sm font-medium text-gray-900">Aucun modèle de checklist</h3>
          <p class="mt-1 text-sm text-gray-500">Créez votre premier modèle de checklist pour commencer.</p>
          <div class="mt-6">
            <button 
              routerLink="/checklists/templates/new"
              class="btn-primary">
              {{ 'checklist.create' | translate }}
            </button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class TemplateListComponent {}

