import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-document-list',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  template: `
    <div class="space-y-6">
      <!-- Header -->
      <div class="flex justify-between items-center">
        <h1 class="text-2xl font-bold text-gray-900">
          {{ 'document.title' | translate }}
        </h1>
        <button class="btn-primary">
          {{ 'document.upload' | translate }}
        </button>
      </div>

      <!-- Content -->
      <div class="bg-white shadow rounded-lg p-6">
        <div class="text-center py-12">
          <svg class="mx-auto h-12 w-12 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
          </svg>
          <h3 class="mt-2 text-sm font-medium text-gray-900">Aucun document</h3>
          <p class="mt-1 text-sm text-gray-500">Téléchargez vos premiers documents et procédures.</p>
          <div class="mt-6">
            <button class="btn-primary">
              {{ 'document.upload' | translate }}
            </button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class DocumentListComponent {}

