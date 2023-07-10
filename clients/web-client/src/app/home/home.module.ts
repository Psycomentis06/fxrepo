import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import {HomeRoutingModule} from "./home-routing.module";
import {HomePageComponent} from "./home-page/home-page.component";



@NgModule({
  declarations: [
    HomeComponent,
    HomePageComponent
  ],
    imports: [
        CommonModule,
        HomeRoutingModule
    ]
})
export class HomeModule { }
