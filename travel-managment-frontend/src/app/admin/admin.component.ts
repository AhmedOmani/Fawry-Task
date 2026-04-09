import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { DestinationService } from "../services/destination.service";
import { AuthService } from "../services/auth.service";

@Component({
    selector: "app-admin",
    imports: [FormsModule],
    templateUrl: "./admin.component.html",
    styleUrl: "./admin.component.css"
})
export class AdminComponent implements OnInit {
    searchCountry = "";
    searchResults: any[] = [];
    destinations: any[] = [];
    newDest = { country: "", capital: "", region: "", population: 0, currency: "", flagImageUrl: "" };

    message = "";
    isError = false;

    currentPage = 0;
    totalPages = 0;

    constructor(
        private destService: DestinationService,
        private authService: AuthService,
        private router: Router
    ) {}

    ngOnInit() {
        this.loadDestinations();
    }

    showMessage(text: string, error: boolean = false) {
        this.message = text;
        this.isError = error;
    }

    loadDestinations() {
        this.destService.getDestinations(this.currentPage, 10).subscribe({
            next: (response) => {
                this.destinations = response.content;
                this.totalPages = response.totalPages;
            },
            error: () => { this.showMessage("Failed to load destinations", true); }
        });
    }

    nextPage() {
        if (this.currentPage < this.totalPages - 1) {
            this.currentPage++;
            this.loadDestinations();
        }
    }

    prevPage() {
        if (this.currentPage > 0) {
            this.currentPage--;
            this.loadDestinations();
        }
    }

    searchExternal() {
        if (!this.searchCountry) return;
        this.destService.fetchFromExternalApi(this.searchCountry).subscribe({
            next: (results: any[]) => {
                this.searchResults = results.map((r: any) => {
                    const currencyKeys = Object.keys(r.currencies || {});
                    const currencyName = currencyKeys.length > 0 ? r.currencies[currencyKeys[0]].name : "";
                    return {
                        country: r.name?.common || "",
                        capital: Array.isArray(r.capital) ? r.capital[0] : (r.capital || ""),
                        region: r.region || "",
                        population: r.population || 0,
                        currency: currencyName,
                        flagImageUrl: r.flags?.png || ""
                    };
                });
            },
            error: () => { this.showMessage("No country found matching '" + this.searchCountry + "'. Try a real country name.", true); }
        });
    }

    saveFromExternal(dest: any) {
        this.destService.addDestination(dest).subscribe({
            next: () => {
                this.showMessage(dest.country + " added!");
                this.loadDestinations();
                this.searchResults = [];
                this.searchCountry = "";
            },
            error: () => { this.showMessage("Failed to add destination", true); }
        });
    }

    saveAllFromExternal() {
        this.destService.bulkSave(this.searchResults).subscribe({
            next: () => {
                this.showMessage(this.searchResults.length + " destinations added!");
                this.loadDestinations();
                this.searchResults = [];
                this.searchCountry = "";
            },
            error: () => { this.showMessage("Failed to bulk add destinations", true); }
        });
    }

    addManually() {
        this.destService.addDestination(this.newDest).subscribe({
            next: () => {
                this.showMessage(this.newDest.country + " added!");
                this.loadDestinations();
                this.newDest = { country: "", capital: "", region: "", population: 0, currency: "", flagImageUrl: "" };
            },
            error: () => { this.showMessage("Failed to add destination", true); }
        });
    }

    deleteDestination(id: string, name: string) {
        this.destService.deleteDestination(id).subscribe({
            next: () => {
                this.showMessage(name + " deleted!");
                this.loadDestinations();
            },
            error: () => { this.showMessage("Failed to delete", true); }
        });
    }

    logout() {
        this.authService.logout().subscribe({
            next: () => this.router.navigate(["/login"]),
            error: () => {
                this.authService.clearSession();
                this.router.navigate(["/login"]);
            }
        });
    }
}
