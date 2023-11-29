import {Component} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {CreateAccountFormData} from "../../models/create-account-response";
import {ToastService, ToastType} from "../../core/service/toast.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-register-page',
    templateUrl: './register-page.component.html',
    styleUrls: ['./register-page.component.scss']
})
export class RegisterPageComponent {
    constructor(private authService: AuthService, private toastService: ToastService, private router: Router) {
    }

    onRegister(data: any) {
        console.log(data)
        this.authService.createAccount(data as CreateAccountFormData)
            .subscribe({
                next: res => {
                    if (res.code === 201) {
                        this.toastService.add({
                            id: 1,
                            title: 'Registration successful',
                            content: res.message,
                        }, 5000)
                        this.router.navigate(['/login'])
                    } else {
                        this.toastService.add({
                            id: 3,
                            title: "Registration failed",
                            content: res.message,
                            type: ToastType.warning
                        })
                    }
                },
                error: err => this.toastService.add({
                    id: 2,
                    title: "Registration failed",
                    content: err,
                    type: ToastType.error
                }, 5000)
            })
    }
}
