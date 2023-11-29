import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from '../../environments/environment'
import {ResponseModel} from "../core/models/response";
import {CreateAccountFormData, CreateAccountResponse} from "../models/create-account-response";
import {LoginResponse} from "../models/loginResponse";
import {tap} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    readonly CREATE_ACCOUNT_ENDPOINT = environment.api.fx_main.v1 + '/auth/createAccount'
    readonly LOGIN_ENDPOINT = environment.api.fx_main.v1 + '/auth/login'
    readonly AUTH_LOCALSTORAGE_KEYS = {
        token: "fxrepo:auth:jwttoken",
        refreshToken: "fxrepo:auth:refreshToken",
        user: "fxrepo:auth:user"
    }

    constructor(private http: HttpClient) {
    }

    createAccount(data: CreateAccountFormData) {
        return this.http.post<ResponseModel<CreateAccountResponse>>(this.CREATE_ACCOUNT_ENDPOINT, data)
    }

    login(username: string, password: string) {
        return this.http.post<ResponseModel<LoginResponse>>(this.LOGIN_ENDPOINT, {username, password})
            .pipe(
                tap(res => {
                    if (res.code === 200) {
                        localStorage.setItem(this.AUTH_LOCALSTORAGE_KEYS.token, res.data.token)
                        localStorage.setItem(this.AUTH_LOCALSTORAGE_KEYS.refreshToken, res.data.refreshToken)
                    }
                })
            )
    }

    logout() {
        localStorage.removeItem(this.AUTH_LOCALSTORAGE_KEYS.token)
        localStorage.removeItem(this.AUTH_LOCALSTORAGE_KEYS.refreshToken)
    }

    get isLoggedIn() {
        return
    }

    get user() {
        return ""
    }
}
