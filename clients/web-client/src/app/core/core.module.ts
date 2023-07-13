import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LayoutComponent} from "./layout/layout.component";
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {RouterModule} from "@angular/router";
import {MobileComponent} from './components/header/components/mobile/mobile.component';
import {DesktopComponent} from './components/header/components/desktop/desktop.component';


@NgModule({
  declarations: [LayoutComponent, HeaderComponent, FooterComponent, MobileComponent, DesktopComponent],
  imports: [
    CommonModule,
    RouterModule
  ],
  exports: [
    LayoutComponent
  ]
})
export class CoreModule {
}
