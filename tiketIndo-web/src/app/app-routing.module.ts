import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

export const routes: Routes = [
	{path: '', redirectTo: '/landingPage', pathMatch: 'full'},
];

@NgModule({
	imports: [RouterModule.forRoot(routes, {scrollPositionRestoration: 'enabled'})],
	exports: [RouterModule]
})
export class AppRoutingModule {}
