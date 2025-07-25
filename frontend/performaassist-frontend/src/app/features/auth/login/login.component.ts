import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TranslateModule],
  template: `
    <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
      <div class="max-w-md w-full space-y-8">
        <!-- Header -->
        <div class="text-center">
          <h1 class="text-4xl font-bold text-primary-600 mb-2">{{ 'app.title' | translate }}</h1>
          <p class="text-gray-600">{{ 'app.subtitle' | translate }}</p>
          <h2 class="mt-6 text-3xl font-extrabold text-gray-900">
            {{ 'auth.login' | translate }}
          </h2>
        </div>

        <!-- Language Selector -->
        <div class="flex justify-center space-x-4">
          <button 
            (click)="changeLanguage('fr')"
            [class]="currentLang === 'fr' ? 'bg-primary-600 text-white' : 'bg-gray-200 text-gray-700'"
            class="px-3 py-1 rounded-md text-sm font-medium transition-colors">
            Français
          </button>
          <button 
            (click)="changeLanguage('ar')"
            [class]="currentLang === 'ar' ? 'bg-primary-600 text-white' : 'bg-gray-200 text-gray-700'"
            class="px-3 py-1 rounded-md text-sm font-medium transition-colors">
            العربية
          </button>
        </div>

        <!-- Login Form -->
        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()" class="mt-8 space-y-6">
          <div class="space-y-4">
            <!-- Email -->
            <div>
              <label for="email" class="form-label">
                {{ 'auth.email' | translate }}
              </label>
              <input
                id="email"
                type="email"
                formControlName="email"
                class="form-input"
                [class.border-red-500]="loginForm.get('email')?.invalid && loginForm.get('email')?.touched"
                [placeholder]="'auth.email' | translate">
              <div *ngIf="loginForm.get('email')?.invalid && loginForm.get('email')?.touched" 
                   class="mt-1 text-sm text-red-600">
                <span *ngIf="loginForm.get('email')?.errors?.['required']">
                  {{ 'validation.required' | translate }}
                </span>
                <span *ngIf="loginForm.get('email')?.errors?.['email']">
                  {{ 'validation.email' | translate }}
                </span>
              </div>
            </div>

            <!-- Password -->
            <div>
              <label for="password" class="form-label">
                {{ 'auth.password' | translate }}
              </label>
              <input
                id="password"
                type="password"
                formControlName="password"
                class="form-input"
                [class.border-red-500]="loginForm.get('password')?.invalid && loginForm.get('password')?.touched"
                [placeholder]="'auth.password' | translate">
              <div *ngIf="loginForm.get('password')?.invalid && loginForm.get('password')?.touched" 
                   class="mt-1 text-sm text-red-600">
                <span *ngIf="loginForm.get('password')?.errors?.['required']">
                  {{ 'validation.required' | translate }}
                </span>
              </div>
            </div>

            <!-- Remember Me -->
            <div class="flex items-center">
              <input
                id="rememberMe"
                type="checkbox"
                formControlName="rememberMe"
                class="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded">
              <label for="rememberMe" class="ml-2 block text-sm text-gray-900">
                {{ 'auth.rememberMe' | translate }}
              </label>
            </div>
          </div>

          <!-- Error Message -->
          <div *ngIf="errorMessage" class="bg-red-50 border border-red-200 rounded-md p-4">
            <div class="flex">
              <div class="ml-3">
                <h3 class="text-sm font-medium text-red-800">
                  {{ errorMessage }}
                </h3>
              </div>
            </div>
          </div>

          <!-- Submit Button -->
          <div>
            <button
              type="submit"
              [disabled]="loginForm.invalid || isLoading"
              class="btn-primary w-full flex justify-center py-3 px-4 text-sm font-medium disabled:opacity-50 disabled:cursor-not-allowed">
              <svg *ngIf="isLoading" class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              {{ isLoading ? ('common.loading' | translate) : ('auth.signIn' | translate) }}
            </button>
          </div>
        </form>

        <!-- Footer -->
        <div class="text-center">
          <p class="text-sm text-gray-600">
            PerformaAssist v1.0.0 - {{ 'app.subtitle' | translate }}
          </p>
        </div>
      </div>
    </div>
  `
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  currentLang = 'fr';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private translate: TranslateService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      rememberMe: [false]
    });
  }

  ngOnInit(): void {
    // Rediriger si déjà connecté
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }

    // Initialiser la langue
    this.currentLang = this.translate.currentLang || 'fr';
    this.updateDirection();
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

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';

      const credentials = this.loginForm.value;

      this.authService.login(credentials).subscribe({
        next: (response) => {
          this.isLoading = false;
          
          // Récupérer l'URL de retour ou rediriger vers le dashboard
          const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
          this.router.navigate([returnUrl]);
        },
        error: (error) => {
          this.isLoading = false;
          
          if (error.status === 401) {
            this.errorMessage = this.translate.instant('auth.invalidCredentials');
          } else {
            this.errorMessage = error.error?.message || 'Une erreur inattendue s\'est produite';
          }
        }
      });
    }
  }
}

