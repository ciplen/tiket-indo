import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CmpTicketMaint} from './CmpTicketMaint';
import {CmpTicketMaintForm} from './CmpTicketMaintForm';

@NgModule({
	imports: [
		RouterModule.forChild([
			{path: ':id', component: CmpTicketMaintForm},
			{path: '', component: CmpTicketMaint},
		])
	],
})
export class RoutTicketMaint {}