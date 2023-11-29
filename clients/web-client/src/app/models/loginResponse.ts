export interface LoginResponse {
    token: string
    refreshToken: string
}

export interface LoginFormData {
    username: string
    password: string
}
