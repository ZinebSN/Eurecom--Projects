import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SecurePagesGuard } from './secure-pages.guard';
import { SigninComponent } from './signin/signin.component';
import { WishlistComponent } from './wishlist/wishlist.component';

const routes: Routes = [
  {path: "signin" , component: SigninComponent, canActivate:[SecurePagesGuard]},
  {path: "dashboard" , component: DashboardComponent,
    canActivate: [AuthGuard] },
  {path:"wishlist" , component: WishlistComponent,canActivate:[AuthGuard]},
  {path: "" , pathMatch:"full",redirectTo:"signin"},
  {path:"**", redirectTo:"signin"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
