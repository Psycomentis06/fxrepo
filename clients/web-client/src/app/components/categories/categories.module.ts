import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CategoriesComponent } from './categories.component';
import {NgIconsModule} from "@ng-icons/core"
import {heroXCircle} from "@ng-icons/heroicons/outline";



@NgModule({
    declarations: [
        CategoriesComponent
    ],
    exports: [
        CategoriesComponent
    ],
    imports: [
        CommonModule,
        NgIconsModule.withIcons({
            heroXCircle
        })
    ]
})
export class CategoriesModule { }
