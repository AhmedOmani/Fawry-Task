import { HttpInterceptorFn, HttpErrorResponse } from "@angular/common/http";
import { inject } from "@angular/core";
import { Router } from "@angular/router";
import { AuthService } from "./auth.service";
import { catchError, switchMap, throwError } from "rxjs";

let isRefreshing = false;

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    // skip auth header for auth endpoints (except logout which needs the cookie only)
    if (req.url.includes("/api/auth/")) {
        return next(req);
    }

    const token = authService.getToken();
    let authReq = req;

    if (token) {
        authReq = req.clone({
            setHeaders: { Authorization: `Bearer ${token}` },
            withCredentials: true
        });
    }

    return next(authReq).pipe(
        catchError((error: HttpErrorResponse) => {
            if (error.status === 401 && !isRefreshing) {
                isRefreshing = true;
                return authService.refreshAccessToken().pipe(
                    switchMap(() => {
                        isRefreshing = false;
                        const newToken = authService.getToken();
                        const retryReq = req.clone({
                            setHeaders: { Authorization: `Bearer ${newToken}` },
                            withCredentials: true
                        });
                        return next(retryReq);
                    }),
                    catchError((refreshError) => {
                        isRefreshing = false;
                        authService.clearSession();
                        router.navigate(["/login"]);
                        return throwError(() => refreshError);
                    })
                );
            }
            return throwError(() => error);
        })
    );
};
