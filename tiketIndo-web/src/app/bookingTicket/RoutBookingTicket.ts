import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CmpBookingTicket} from './CmpBookingTicket';
import {CmpBuktiTrans} from './CmpBuktiTrans';

@NgModule({
	imports: [
		RouterModule.forChild([
			{path: ':id', component: CmpBookingTicket},
			{path: '', component: CmpBookingTicket},
			{path: 'bktiTrans', component: CmpBuktiTrans},
		])
	],
})
export class RoutBookingTicket {}
