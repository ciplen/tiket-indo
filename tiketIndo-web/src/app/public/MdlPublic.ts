import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CmpBuyTicket} from './buyTicket/CmpBuyTicket';
import {CmpQuantityForm} from './quantityForm/CmpQuantityForm';
import {CmpTicketForm} from './ticketForm/CmpTicketForm';
import {CmpConfirm} from './confirmPayment/CmpConfirm';
import {CmpLandingPage} from './CmpLandingPage';
import {CmpHeader} from './CmpHeader';
import {CmpFooter} from './CmpFooter';
import {RoutPublic} from './RoutPublic';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatTabsModule} from '@angular/material'
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {PrvTicketMaint} from '../ticketMaint/PrvTicketMaint';
import {PrvVenueMaint} from '../venueMaint/PrvVenueMaint';
import {PrvBookingTicket} from '../bookingTicket/PrvBookingTicket';
import {ScrollToModule} from '@nicky-lenaers/ngx-scroll-to';
import {MatDialogModule} from '@angular/material/dialog';
import {MdlShared} from '../shared/MdlShared';

@NgModule({
	imports: [CommonModule, RoutPublic,
		BrowserModule,
		BrowserAnimationsModule,
		MatTabsModule,
		ScrollToModule.forRoot(),
		FormsModule,
		ReactiveFormsModule,
		MdlShared],
	declarations: [CmpLandingPage, CmpBuyTicket, CmpQuantityForm, CmpTicketForm, CmpHeader, CmpFooter, CmpConfirm],
	exports: [CmpLandingPage, MatDialogModule],
	providers: [PrvTicketMaint, PrvVenueMaint, PrvBookingTicket]
})

export class MdlPublic {}
