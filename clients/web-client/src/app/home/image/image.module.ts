import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ImageComponent} from './image.component';
import {ImageRoutingModule} from "./image-routing.module";
import {ImagesComponent} from './images/images.component';
import {PostComponent} from './post/post.component';
import {HeaderComponent} from './components/header/header.component';
import {UploadImageFileComponent} from './post/components/upload-image-file/upload-image-file.component';
import {CreateImageFormComponent} from './post/components/create-image-form/create-image-form.component';
import {NgIconsModule} from '@ng-icons/core'
import {heroArrowLeft, heroArrowUpOnSquare, heroPhoto, heroXCircle, heroXMark} from '@ng-icons/heroicons/outline'
import {heroEyeSolid, heroHandThumbUpSolid} from '@ng-icons/heroicons/solid'
import {ReactiveFormsModule} from "@angular/forms";
import {ImageCardComponent} from './components/image-card/image-card.component';
import { ImageDetailsComponent } from './image-details/image-details.component';
import { PaginationComponent } from './components/pagination/pagination.component';
import {CategoriesModule} from "../../components/categories/categories.module";


@NgModule({
  declarations: [
    ImageComponent,
    ImagesComponent,
    PostComponent,
    HeaderComponent,
    UploadImageFileComponent,
    CreateImageFormComponent,
    ImageCardComponent,
    ImageDetailsComponent,
    PaginationComponent
  ],
  imports: [
    CommonModule,
    ImageRoutingModule,
    ReactiveFormsModule,
    NgIconsModule.withIcons({
      heroPhoto,
      heroArrowLeft,
      heroArrowUpOnSquare,
      heroXCircle,
      heroEyeSolid,
      heroHandThumbUpSolid,
      heroXMark
    }),
    CategoriesModule
  ]
})
export class ImageModule {
}
