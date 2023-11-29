import {Component, EventEmitter, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ControlSDoesNotMatch} from "../../../form-validators/control-s-does-not-match";

type RegisterForm = {
    username: FormControl
    password: FormControl
    email: FormControl
    rePass: FormControl
}

type RegisterEvent = {
    username: string
    email: string
    password: string
    rePass: string
}

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
    form: FormGroup<RegisterForm>
    @Output('register') registerEvent: EventEmitter<RegisterEvent>

    constructor() {
        this.registerEvent = new EventEmitter<RegisterEvent>()
        this.form = new FormGroup<RegisterForm>({
            username: new FormControl('', [
                Validators.required,
                Validators.minLength(3),
                Validators.maxLength(25)
            ]),
            password: new FormControl('', [
                Validators.required,
                Validators.minLength(6),
                Validators.maxLength(16)
            ]),
            email: new FormControl('', [
                Validators.required,
                Validators.email,
            ]),
            rePass: new FormControl('', [
                Validators.required
            ])
        }, {validators: [ControlSDoesNotMatch('password', 'rePass')]})
    }

    get username() {
        return this.form.controls.username
    }

    get email() {
        return this.form.controls.email
    }

    get password() {
        return this.form.controls.password
    }

    get rePass() {
        return this.form.controls.rePass
    }

    onSubmit(e: Event) {
        e.preventDefault()
        if (this.form.invalid) {
            alert("Can't create user. Form has invalid data.")
            return
        }
        this.registerEvent.emit(this.form.value as RegisterEvent)
    }
}
