import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {LayoutComponent} from "./layout/layout.component";
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import {RouterModule} from "@angular/router";
import { PageComponent } from './component/page-component/page.component';
import { MobileComponent } from './header/components/mobile/mobile.component';
import { DesktopComponent } from './header/components/desktop/desktop.component';



@NgModule({
  declarations: [LayoutComponent, HeaderComponent, FooterComponent, PageComponent, MobileComponent, DesktopComponent],
  imports: [
    CommonModule,
    RouterModule
  ],
  exports: [
    LayoutComponent
  ]
})
export class CoreModule { }
