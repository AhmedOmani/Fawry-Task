import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable({
    providedIn: "root"
})
export class DestinationService {

    private adminUrl = "http://localhost:8080/api/admin/destinations";
    private userUrl = "http://localhost:8080/api/destinations";

    constructor(private http: HttpClient) {}

    addDestination(destination: any): Observable<any> {
        return this.http.post(this.adminUrl, destination);
    }

    deleteDestination(id: string): Observable<any> {
        return this.http.delete(`${this.adminUrl}/${id}`);
    }

    fetchFromExternalApi(countryName: string): Observable<any> {
        return this.http.get(`${this.adminUrl}?query=${countryName}`);
    }

    bulkSave(destinations: any[]): Observable<any> {
        return this.http.post(`${this.adminUrl}/bulk`, destinations);
    }

    getDestinations(page: number = 0, size: number = 10, search: string = ""): Observable<any> {
        let url = `${this.userUrl}?page=${page}&size=${size}`;
        if (search) {
            url += `&search=${search}`;
        }
        return this.http.get(url);
    }

    getDestinationById(id: string): Observable<any> {
        return this.http.get(`${this.userUrl}/${id}`);
    }

    addToWishlist(destinationId: string): Observable<any> {
        return this.http.post(`${this.userUrl}/${destinationId}/want-to-visit`, {}, { responseType: 'text' });
    }

    removeFromWishlist(destinationId: string): Observable<any> {
        return this.http.delete(`${this.userUrl}/${destinationId}/want-to-visit`, { responseType: 'text' });
    }

    getWishlist(): Observable<any> {
        return this.http.get(`${this.userUrl}/want-to-visit`);
    }
}
