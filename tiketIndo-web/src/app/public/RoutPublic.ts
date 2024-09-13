import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CmpLandingPage} from './CmpLandingPage';
import {CmpHeader} from './CmpHeader';
import {CmpBuyTicket} from './buyTicket/CmpBuyTicket';
import {CmpQuantityForm} from './quantityForm/CmpQuantityForm';
import {CmpTicketForm} from './ticketForm/CmpTicketForm';
import {CmpConfirm} from './confirmPayment/CmpConfirm';

@NgModule({
	imports: [RouterModule.forChild([
		{path: 'buyTic/:id', component: CmpBuyTicket},
		{path: 'quantityForm/:id', component: CmpQuantityForm},
		{path: 'ticketForm/:id/:qty', component: CmpTicketForm},
		{path: 'confirm/:id/:email', component: CmpConfirm},
		//		{path: 'confirm', component: CmpConfirm},
		{
			path: 'landingPage', component: CmpLandingPage,
			children: []
		},
	])],
	exports: [
		RouterModule
	],
})
export class RoutPublic {}
