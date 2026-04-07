import { Component, OnInit } from "@angular/core";
import { DecimalPipe } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { Router } from "@angular/router";
import { DestinationService } from "../services/destination.service";
import { AuthService } from "../services/auth.service";

@Component({
    selector: "app-destinations",
    imports: [FormsModule, DecimalPipe],
    templateUrl: "./destinations.component.html",
    styleUrl: "./destinations.component.css"
})
export class DestinationsComponent implements OnInit {
    destinations: any[] = [];
    wishlist: any[] = [];
    searchQuery = "";
    currentPage = 0;
    totalPages = 0;
    message = "";
    activeTab = "browse";

    constructor(
        private destService: DestinationService,
        private authService: AuthService,
        private router: Router
    ) {}

    ngOnInit() {
        this.loadDestinations();
        this.loadWishlist();
    }

    loadDestinations() {
        this.destService.getDestinations(this.currentPage, 10, this.searchQuery).subscribe({
            next: (response) => {
                this.destinations = response.content;
                this.totalPages = response.totalPages;
            },
            error: () => { this.message = "Failed to load destinations"; }
        });
    }

    loadWishlist() {
        this.destService.getWishlist().subscribe({
            next: (data) => { this.wishlist = data; },
            error: () => {}
        });
    }

    search() {
        this.currentPage = 0;
        this.loadDestinations();
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

    isInWishlist(id: string): boolean {
        return this.wishlist.some((d) => d.id === id);
    }

    toggleWishlist(dest: any) {
        if (this.isInWishlist(dest.id)) {
            this.destService.removeFromWishlist(dest.id).subscribe({
                next: () => {
                    this.message = dest.country + " removed from wishlist";
                    this.loadWishlist();
                }
            });
        } else {
            this.destService.addToWishlist(dest.id).subscribe({
                next: () => {
                    this.message = dest.country + " added to wishlist!";
                    this.loadWishlist();
                }
            });
        }
    }

    logout() {
        this.authService.logout();
        this.router.navigate(["/login"]);
    }
}
