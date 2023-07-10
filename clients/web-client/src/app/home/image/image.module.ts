import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageComponent } from './image.component';
import {ImageRoutingModule} from "./image-routing.module";
import { ImagesComponent } from './images/images.component';
import { PostComponent } from './post/post.component';



@NgModule({
  declarations: [
    ImageComponent,
    ImagesComponent,
    PostComponent
  ],
  imports: [
    CommonModule,
    ImageRoutingModule
  ]
})
export class ImageModule { }
