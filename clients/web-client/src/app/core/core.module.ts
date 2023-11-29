import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {LayoutComponent} from "./layout/layout.component";
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {RouterModule} from "@angular/router";
import {MobileComponent} from './components/header/components/mobile/mobile.component';
import {DesktopComponent} from './components/header/components/desktop/desktop.component';
import { NotificationsComponent } from './components/notifications/notifications.component';
import { ToastsComponent } from './components/toasts/toasts.component';
import { ModalComponent } from './components/modal/modal.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [LayoutComponent, HeaderComponent, FooterComponent, MobileComponent, DesktopComponent, NotificationsComponent, ToastsComponent, ModalComponent, LoginComponent, RegisterComponent],
    imports: [
        CommonModule,
        RouterModule,
        NgOptimizedImage,
        ReactiveFormsModule
    ],
    exports: [
        LayoutComponent,
        LoginComponent,
        RegisterComponent
    ]
})
export class CoreModule {
}
