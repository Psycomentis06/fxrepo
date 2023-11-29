import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import {HomeRoutingModule} from "./home-routing.module";
import {HomePageComponent} from "./home-page/home-page.component";
import { LoginPageComponent } from './login-page/login-page.component';
import { RegisterPageComponent } from './register-page/register-page.component';
import {CoreModule} from "../core/core.module";



@NgModule({
  declarations: [
    HomeComponent,
    HomePageComponent,
    LoginPageComponent,
    RegisterPageComponent
  ],
    imports: [
        CommonModule,
        HomeRoutingModule,
        CoreModule
    ]
})
export class HomeModule { }
