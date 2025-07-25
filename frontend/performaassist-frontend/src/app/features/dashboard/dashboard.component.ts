import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { AuthService, UserInfo } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, TranslateModule],
  template: `
    <div class="space-y-6">
      <!-- Header -->
      <div class="bg-white shadow rounded-lg p-6">
        <div class="flex items-center justify-between">
          <div>
            <h1 class="text-2xl font-bold text-gray-900">
              {{ 'navigation.dashboard' | translate }}
            </h1>
            <p class="mt-1 text-sm text-gray-600">
              Bienvenue, {{ currentUser?.firstName }} {{ currentUser?.lastName }}
            </p>
          </div>
          <div class="text-right">
            <p class="text-sm text-gray-500">{{ getCurrentDate() }}</p>
            <p class="text-xs text-gray-400">{{ getRoleDisplayName() }}</p>
          </div>
        </div>
      </div>

      <!-- Statistiques rapides -->
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div class="bg-white overflow-hidden shadow rounded-lg">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0">
                <div class="w-8 h-8 bg-blue-500 rounded-md flex items-center justify-center">
                  <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
                  </svg>
                </div>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">
                    {{ 'checklist.title' | translate }}
                  </dt>
                  <dd class="text-lg font-medium text-gray-900">
                    12
                  </dd>
                </dl>
              </div>
            </div>
          </div>
          <div class="bg-gray-50 px-5 py-3">
            <div class="text-sm">
              <a routerLink="/checklists" class="font-medium text-primary-600 hover:text-primary-500">
                Voir tout
              </a>
            </div>
          </div>
        </div>

        <div class="bg-white overflow-hidden shadow rounded-lg">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0">
                <div class="w-8 h-8 bg-green-500 rounded-md flex items-center justify-center">
                  <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                  </svg>
                </div>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">
                    {{ 'actionPlan.title' | translate }}
                  </dt>
                  <dd class="text-lg font-medium text-gray-900">
                    8
                  </dd>
                </dl>
              </div>
            </div>
          </div>
          <div class="bg-gray-50 px-5 py-3">
            <div class="text-sm">
              <a routerLink="/action-plans" class="font-medium text-primary-600 hover:text-primary-500">
                Voir tout
              </a>
            </div>
          </div>
        </div>

        <div class="bg-white overflow-hidden shadow rounded-lg" *ngIf="isManager()">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0">
                <div class="w-8 h-8 bg-purple-500 rounded-md flex items-center justify-center">
                  <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"></path>
                  </svg>
                </div>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">
                    {{ 'meeting.title' | translate }}
                  </dt>
                  <dd class="text-lg font-medium text-gray-900">
                    3
                  </dd>
                </dl>
              </div>
            </div>
          </div>
          <div class="bg-gray-50 px-5 py-3">
            <div class="text-sm">
              <a routerLink="/meetings" class="font-medium text-primary-600 hover:text-primary-500">
                Voir tout
              </a>
            </div>
          </div>
        </div>

        <div class="bg-white overflow-hidden shadow rounded-lg">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0">
                <div class="w-8 h-8 bg-yellow-500 rounded-md flex items-center justify-center">
                  <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"></path>
                  </svg>
                </div>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">
                    {{ 'kpi.title' | translate }}
                  </dt>
                  <dd class="text-lg font-medium text-gray-900">
                    15
                  </dd>
                </dl>
              </div>
            </div>
          </div>
          <div class="bg-gray-50 px-5 py-3">
            <div class="text-sm">
              <a routerLink="/kpi" class="font-medium text-primary-600 hover:text-primary-500">
                Voir tout
              </a>
            </div>
          </div>
        </div>
      </div>

      <!-- Actions rapides -->
      <div class="bg-white shadow rounded-lg p-6">
        <h2 class="text-lg font-medium text-gray-900 mb-4">Actions rapides</h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <button 
            routerLink="/checklists/new"
            *ngIf="isManager()"
            class="flex items-center p-4 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors">
            <div class="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center mr-4">
              <svg class="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
            </div>
            <div class="text-left">
              <p class="font-medium text-gray-900">{{ 'checklist.create' | translate }}</p>
              <p class="text-sm text-gray-500">Créer un nouveau modèle</p>
            </div>
          </button>

          <button 
            routerLink="/action-plans/new"
            class="flex items-center p-4 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors">
            <div class="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center mr-4">
              <svg class="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path>
              </svg>
            </div>
            <div class="text-left">
              <p class="font-medium text-gray-900">{{ 'actionPlan.create' | translate }}</p>
              <p class="text-sm text-gray-500">Créer une nouvelle action</p>
            </div>
          </button>

          <button 
            routerLink="/documents"
            class="flex items-center p-4 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors">
            <div class="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center mr-4">
              <svg class="w-6 h-6 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path>
              </svg>
            </div>
            <div class="text-left">
              <p class="font-medium text-gray-900">{{ 'document.title' | translate }}</p>
              <p class="text-sm text-gray-500">Consulter les documents</p>
            </div>
          </button>
        </div>
      </div>

      <!-- Activité récente -->
      <div class="bg-white shadow rounded-lg p-6">
        <h2 class="text-lg font-medium text-gray-900 mb-4">Activité récente</h2>
        <div class="space-y-4">
          <div class="flex items-center space-x-3">
            <div class="w-2 h-2 bg-blue-500 rounded-full"></div>
            <p class="text-sm text-gray-600">
              <span class="font-medium">Checklist Sécurité</span> exécutée avec succès
            </p>
            <span class="text-xs text-gray-400">Il y a 2 heures</span>
          </div>
          <div class="flex items-center space-x-3">
            <div class="w-2 h-2 bg-green-500 rounded-full"></div>
            <p class="text-sm text-gray-600">
              <span class="font-medium">Action corrective #123</span> terminée
            </p>
            <span class="text-xs text-gray-400">Il y a 4 heures</span>
          </div>
          <div class="flex items-center space-x-3">
            <div class="w-2 h-2 bg-yellow-500 rounded-full"></div>
            <p class="text-sm text-gray-600">
              <span class="font-medium">Nouveau document</span> ajouté à la base
            </p>
            <span class="text-xs text-gray-400">Hier</span>
          </div>
        </div>
      </div>
    </div>
  `
})
export class DashboardComponent implements OnInit {
  currentUser: UserInfo | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }

  getCurrentDate(): string {
    return new Date().toLocaleDateString('fr-FR', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  getRoleDisplayName(): string {
    if (!this.currentUser) return '';
    
    const roles = {
      'ADMIN': 'Administrateur',
      'MANAGER': 'Manager',
      'EMPLOYEE': 'Employé'
    };
    
    return roles[this.currentUser.role] || this.currentUser.role;
  }

  isManager(): boolean {
    return this.authService.isManager();
  }
}

