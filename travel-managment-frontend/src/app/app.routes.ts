import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login.component';
import { RegisterComponent } from './auth/register.component';
import { AdminComponent } from './admin/admin.component';
import { DestinationsComponent } from './destinations/destinations.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'admin', component: AdminComponent },
    { path: 'destinations', component: DestinationsComponent },
    { path: '', redirectTo: 'login', pathMatch: 'full' }
];
