import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CmpVisitor} from './CmpVisitor';

@NgModule({
	imports: [
		RouterModule.forChild([
			{path: '', component: CmpVisitor},
		])
	],
})
export class RoutVisitor {}
