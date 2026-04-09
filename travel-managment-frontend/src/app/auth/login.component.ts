import { Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Router, RouterLink } from "@angular/router";
import { AuthService } from "../services/auth.service";

@Component({
    selector: "app-login",
    imports: [FormsModule, RouterLink],
    templateUrl: "./login.component.html",
    styleUrl: "./login.component.css"
})

export class LoginComponent {
    email = "";
    password = "";
    errorMessage = "";

    constructor (private authService: AuthService, private router: Router) {}

    onLogin() {
        this.authService.login(this.email, this.password).subscribe({
            next: (response) => {
                if (response.role === "ADMIN") {
                    this.router.navigate(["/admin"]);
                } else {
                    this.router.navigate(["/destinations"]);
                }
            },
            error: (err) => {
                this.errorMessage = "Invalid email or password";
            }
        });
    }
}
