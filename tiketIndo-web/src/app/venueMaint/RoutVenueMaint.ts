import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CmpVenueMaint} from './CmpVenueMaint';
import {CmpVenueMaintForm} from './CmpVenueMaintForm';

@NgModule({
	imports: [
		RouterModule.forChild([
			{path: ':id', component: CmpVenueMaintForm},
			{path: '', component: CmpVenueMaint},
		])
	],
})
export class RoutVenueMaint {}