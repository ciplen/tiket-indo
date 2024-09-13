import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CmpUtility} from './CmpUtility';

@NgModule({
	imports: [
		RouterModule.forChild([
			{path: '', component: CmpUtility},
		])
	],
})
export class RoutUtil {}