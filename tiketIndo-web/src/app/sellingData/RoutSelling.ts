import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CmpSelling} from './CmpSelling';

@NgModule({
	imports: [
		RouterModule.forChild([
			{path: '', component: CmpSelling},
		])
	],
})
export class RoutSelling {}
