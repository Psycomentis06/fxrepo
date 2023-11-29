import {NgModule} from "@angular/core";
import {Route, RouterModule} from "@angular/router";
import {HomeComponent} from "./home.component";
import {HomePageComponent} from "./home-page/home-page.component";
import {LoginComponent} from "../core/components/login/login.component";
import {RegisterComponent} from "../core/components/register/register.component";

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
          'header-bg': 'bg-transparent',
          title: 'Fx Repo home page'
        }
      },
      {
        path: 'images',
        loadChildren: () => import('./image/image.module').then(m => m.ImageModule)
      },
      {path: 'login', component: LoginComponent},
      {path: 'register', component: RegisterComponent}
    ]
  }
]

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule {
}
