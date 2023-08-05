import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageComponent } from './image.component';
import {ImageRoutingModule} from "./image-routing.module";
import { ImagesComponent } from './images/images.component';
import { PostComponent } from './post/post.component';
import { HeaderComponent } from './components/header/header.component';
import { UploadImageFileComponent } from './post/components/upload-image-file/upload-image-file.component';
import { CreateImageFormComponent } from './post/components/create-image-form/create-image-form.component';
import { NgIconsModule } from '@ng-icons/core'
import {
  heroPhoto,
  heroArrowLeft,
  heroArrowUpOnSquare, heroXCircle
} from '@ng-icons/heroicons/outline'
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    ImageComponent,
    ImagesComponent,
    PostComponent,
    HeaderComponent,
    UploadImageFileComponent,
    CreateImageFormComponent
  ],
  imports: [
    CommonModule,
    ImageRoutingModule,
    ReactiveFormsModule,
    NgIconsModule.withIcons({
      heroPhoto,
      heroArrowLeft,
      heroArrowUpOnSquare,
      heroXCircle
    })
  ]
})
export class ImageModule { }
