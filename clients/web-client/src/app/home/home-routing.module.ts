import {NgModule} from "@angular/core";
import {Route, RouterModule} from "@angular/router";
import {HomeComponent} from "./home.component";
import {HomePageComponent} from "./home-page/home-page.component";

const routes: Route[] = [
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: '',
        component: HomePageComponent,
        data: {
          'header-position': 'fixed',
          title: 'Fx Repo home page'
        }
      },
      {
        path: 'images',
        loadChildren: () => import('./image/image.module').then(m => m.ImageModule)
      }
    ]
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule {}
