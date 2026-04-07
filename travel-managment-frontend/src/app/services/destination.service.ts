import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { AuthService } from "./auth.service";

@Injectable({
    providedIn: "root"
})
export class DestinationService {

    private adminUrl = "http://localhost:8080/api/admin/destinations";
    private userUrl = "http://localhost:8080/api/destinations";

    constructor(private http: HttpClient, private authService: AuthService) {}

    private getHeaders(): HttpHeaders {
        const token = this.authService.getToken();
        return new HttpHeaders({ Authorization: `Bearer ${token}` });
    }

    addDestination(destination: any): Observable<any> {
        return this.http.post(this.adminUrl, destination, { headers: this.getHeaders() });
    }

    deleteDestination(id: string): Observable<any> {
        return this.http.delete(`${this.adminUrl}/${id}`, { headers: this.getHeaders() });
    }

    fetchFromExternalApi(countryName: string): Observable<any> {
        return this.http.get(`${this.adminUrl}?query=${countryName}`, { headers: this.getHeaders() });
    }

    bulkSave(destinations: any[]): Observable<any> {
        return this.http.post(`${this.adminUrl}/bulk`, destinations, { headers: this.getHeaders() });
    }

    getDestinations(page: number = 0, size: number = 10, search: string = ""): Observable<any> {
        let url = `${this.userUrl}?page=${page}&size=${size}`;
        if (search) {
            url += `&search=${search}`;
        }
        return this.http.get(url, { headers: this.getHeaders() });
    }

    getDestinationById(id: string): Observable<any> {
        return this.http.get(`${this.userUrl}/${id}`, { headers: this.getHeaders() });
    }

    addToWishlist(destinationId: string): Observable<any> {
        return this.http.post(`${this.userUrl}/${destinationId}/want-to-visit`, {}, { headers: this.getHeaders(), responseType: 'text' });
    }

    removeFromWishlist(destinationId: string): Observable<any> {
        return this.http.delete(`${this.userUrl}/${destinationId}/want-to-visit`, { headers: this.getHeaders(), responseType: 'text' });
    }

    getWishlist(): Observable<any> {
        return this.http.get(`${this.userUrl}/want-to-visit`, { headers: this.getHeaders() });
    }
}
