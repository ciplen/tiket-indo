import {NgModule} from '@angular/core';
import {CmpBookingTicket} from './CmpBookingTicket';
import {CmpBuktiTrans} from './CmpBuktiTrans';
//import {CmpConfirm} from '../public/confirmPayment/CmpConfirm';
import {RoutBookingTicket} from './RoutBookingTicket';
import {PrvBookingTicket} from '../bookingTicket/PrvBookingTicket';
import {PrvTicketMaint} from '../ticketMaint/PrvTicketMaint';
import {MdlShared} from '../shared/MdlShared';

@NgModule({
	imports: [RoutBookingTicket, MdlShared],
	declarations: [CmpBookingTicket, CmpBuktiTrans],
	providers: [PrvBookingTicket, PrvTicketMaint]
})
export class MdlBookingTicket {}