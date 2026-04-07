import { Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Router, RouterLink } from "@angular/router";
import { AuthService } from "../services/auth.service";

@Component({
    selector: "app-register",
    imports: [FormsModule, RouterLink],
    templateUrl: "./register.component.html",
    styleUrl: "./register.component.css"
})
export class RegisterComponent {
    name = "";
    email = "";
    password = "";
    errorMessage = "";

    constructor(private authService: AuthService, private router: Router) {}

    onRegister() {
        this.authService.register(this.name, this.email, this.password).subscribe({
            next: (response) => {
                this.authService.saveToken(response.token);
                this.authService.saveRole("USER");
                this.router.navigate(["/destinations"]);
            },
            error: (err) => {
                this.errorMessage = "Registration failed. Email may already exist.";
            }
        });
    }
}
