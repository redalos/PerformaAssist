import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { AuthService, UserInfo } from '../../../core/services/auth.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, TranslateModule],
  template: `
    <div class="min-h-screen bg-gray-50">
      <!-- Navigation -->
      <nav class="bg-white shadow-sm border-b border-gray-200">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex justify-between h-16">
            <!-- Logo et navigation principale -->
            <div class="flex">
              <div class="flex-shrink-0 flex items-center">
                <h1 class="text-xl font-bold text-primary-600">{{ 'app.title' | translate }}</h1>
              </div>
              
              <!-- Menu principal -->
              <div class="hidden sm:ml-6 sm:flex sm:space-x-8">
                <a 
                  routerLink="/dashboard"
                  routerLinkActive="border-primary-500 text-gray-900"
                  class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium transition-colors">
                  {{ 'navigation.dashboard' | translate }}
                </a>
                
                <a 
                  routerLink="/checklists"
                  routerLinkActive="border-primary-500 text-gray-900"
                  class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium transition-colors">
                  {{ 'navigation.checklists' | translate }}
                </a>
                
                <a 
                  routerLink="/action-plans"
                  routerLinkActive="border-primary-500 text-gray-900"
                  class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium transition-colors">
                  {{ 'navigation.actionPlans' | translate }}
                </a>
                
                <a 
                  *ngIf="isManager()"
                  routerLink="/meetings"
                  routerLinkActive="border-primary-500 text-gray-900"
                  class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium transition-colors">
                  {{ 'navigation.meetings' | translate }}
                </a>
                
                <a 
                  routerLink="/kpi"
                  routerLinkActive="border-primary-500 text-gray-900"
                  class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium transition-colors">
                  {{ 'navigation.kpi' | translate }}
                </a>
                
                <a 
                  routerLink="/documents"
                  routerLinkActive="border-primary-500 text-gray-900"
                  class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium transition-colors">
                  {{ 'navigation.documents' | translate }}
                </a>
              </div>
            </div>

            <!-- Menu utilisateur -->
            <div class="flex items-center space-x-4">
              <!-- Sélecteur de langue -->
              <div class="flex space-x-2">
                <button 
                  (click)="changeLanguage('fr')"
                  [class]="currentLang === 'fr' ? 'bg-primary-100 text-primary-700' : 'text-gray-500 hover:text-gray-700'"
                  class="px-2 py-1 rounded text-sm font-medium transition-colors">
                  FR
                </button>
                <button 
                  (click)="changeLanguage('ar')"
                  [class]="currentLang === 'ar' ? 'bg-primary-100 text-primary-700' : 'text-gray-500 hover:text-gray-700'"
                  class="px-2 py-1 rounded text-sm font-medium transition-colors">
                  AR
                </button>
              </div>

              <!-- Informations utilisateur -->
              <div class="flex items-center space-x-3">
                <div class="text-sm">
                  <p class="font-medium text-gray-900">{{ currentUser?.firstName }} {{ currentUser?.lastName }}</p>
                  <p class="text-gray-500">{{ getRoleDisplayName() }}</p>
                </div>
                
                <!-- Menu dropdown -->
                <div class="relative">
                  <button 
                    (click)="toggleUserMenu()"
                    class="bg-white rounded-full flex text-sm focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500">
                    <span class="sr-only">Open user menu</span>
                    <div class="h-8 w-8 rounded-full bg-primary-600 flex items-center justify-center">
                      <span class="text-sm font-medium text-white">
                        {{ currentUser?.firstName?.charAt(0) }}{{ currentUser?.lastName?.charAt(0) }}
                      </span>
                    </div>
                  </button>

                  <!-- Dropdown menu -->
                  <div 
                    *ngIf="showUserMenu"
                    class="origin-top-right absolute right-0 mt-2 w-48 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-50">
                    <div class="py-1">
                      <a 
                        href="#" 
                        class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                        {{ 'navigation.profile' | translate }}
                      </a>
                      <button 
                        (click)="logout()"
                        class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">
                        {{ 'navigation.logout' | translate }}
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Menu mobile -->
        <div class="sm:hidden" *ngIf="showMobileMenu">
          <div class="pt-2 pb-3 space-y-1">
            <a 
              routerLink="/dashboard"
              class="border-transparent text-gray-600 hover:bg-gray-50 hover:border-gray-300 hover:text-gray-800 block pl-3 pr-4 py-2 border-l-4 text-base font-medium">
              {{ 'navigation.dashboard' | translate }}
            </a>
            <!-- Autres liens mobiles... -->
          </div>
        </div>
      </nav>

      <!-- Contenu principal -->
      <main class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <router-outlet></router-outlet>
      </main>
    </div>
  `
})
export class LayoutComponent implements OnInit {
  currentUser: UserInfo | null = null;
  currentLang = 'fr';
  showUserMenu = false;
  showMobileMenu = false;

  constructor(
    private authService: AuthService,
    private translate: TranslateService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });

    this.currentLang = this.translate.currentLang || 'fr';
    this.updateDirection();

    // Fermer les menus quand on clique ailleurs
    document.addEventListener('click', (event) => {
      const target = event.target as HTMLElement;
      if (!target.closest('.relative')) {
        this.showUserMenu = false;
      }
    });
  }

  changeLanguage(lang: string): void {
    this.currentLang = lang;
    this.translate.use(lang);
    this.updateDirection();
  }

  private updateDirection(): void {
    const direction = this.currentLang === 'ar' ? 'rtl' : 'ltr';
    document.documentElement.setAttribute('dir', direction);
    document.documentElement.setAttribute('lang', this.currentLang);
  }

  toggleUserMenu(): void {
    this.showUserMenu = !this.showUserMenu;
  }

  toggleMobileMenu(): void {
    this.showMobileMenu = !this.showMobileMenu;
  }

  isManager(): boolean {
    return this.authService.isManager();
  }

  getRoleDisplayName(): string {
    if (!this.currentUser) return '';
    
    const roleKey = `role.${this.currentUser.role.toLowerCase()}`;
    return this.translate.instant(roleKey);
  }

  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/auth/login']);
      },
      error: () => {
        // Forcer la déconnexion même en cas d'erreur
        this.authService.forceLogout();
      }
    });
  }
}

