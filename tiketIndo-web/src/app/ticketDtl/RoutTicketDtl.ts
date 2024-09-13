import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CmpTicketDtl} from './CmpTicketDtl';

@NgModule({
	imports: [
		RouterModule.forChild([
			{path: '', component: CmpTicketDtl},
		])
	],
})
export class RoutTicketDtl {}