import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet, NavigationEnd } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { filter } from 'rxjs';
import { LayoutComponent } from './shared/components/layout/layout.component';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, LayoutComponent],
  template: `
    <div class="app-container">
      <app-layout *ngIf="showLayout">
        <router-outlet></router-outlet>
      </app-layout>
      <router-outlet *ngIf="!showLayout"></router-outlet>
    </div>
  `,
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'PerformaAssist';
  showLayout = false;

  constructor(
    private router: Router,
    private authService: AuthService,
    private translate: TranslateService
  ) {
    // Configuration de la langue par défaut
    this.translate.setDefaultLang('fr');
    this.translate.use('fr');
  }

  ngOnInit(): void {
    // Écouter les changements de route pour afficher/masquer le layout
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event) => {
        if (event instanceof NavigationEnd) {
          this.showLayout = !event.url.includes('/auth');
        }
      });

    // Vérifier l'état initial
    this.showLayout = !this.router.url.includes('/auth');
  }
}