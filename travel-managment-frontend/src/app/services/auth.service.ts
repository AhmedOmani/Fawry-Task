import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";

@Injectable({
    providedIn: "root"
})
export class AuthService {
    private apiUrl = "http://localhost:8080/api/auth";
    private accessToken: string | null = null;
    private userRole: string | null = null;

    constructor(private http: HttpClient) {}

    register(name: string, email: string, password: string): Observable<any> {
        return this.http.post(`${this.apiUrl}/register`, { name, email, password }, { withCredentials: true }).pipe(
            tap((response: any) => {
                this.accessToken = response.token;
                this.userRole = "USER";
            })
        );
    }

    login(email: string, password: string): Observable<any> {
        return this.http.post(`${this.apiUrl}/login`, { email, password }, { withCredentials: true }).pipe(
            tap((response: any) => {
                this.accessToken = response.token;
                this.userRole = response.role;
            })
        );
    }

    refreshAccessToken(): Observable<any> {
        return this.http.post(`${this.apiUrl}/refresh`, {}, { withCredentials: true }).pipe(
            tap((response: any) => {
                this.accessToken = response.token;
                this.userRole = response.role;
            })
        );
    }

    getToken(): string | null {
        return this.accessToken;
    }

    getRole(): string | null {
        return this.userRole;
    }

    isLoggedIn(): boolean {
        return this.accessToken !== null;
    }

    logout(): Observable<any> {
        return this.http.post(`${this.apiUrl}/logout`, {}, { withCredentials: true }).pipe(
            tap(() => {
                this.accessToken = null;
                this.userRole = null;
            })
        );
    }

    clearSession(): void {
        this.accessToken = null;
        this.userRole = null;
    }
}