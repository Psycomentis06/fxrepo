import {NgModule} from "@angular/core";
import {Route, RouterModule} from "@angular/router";
import {ImageComponent} from "./image.component";
import {ImagesComponent} from "./images/images.component";
import {PostComponent} from "./post/post.component";

const routes: Route[] = [
  {
    path: '',
    component: ImageComponent,
    children: [
      {
        path: '',
        component: ImagesComponent
      },
      {
        path: 'post',
        component: PostComponent
      }
    ]
  }
]
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ImageRoutingModule {}
