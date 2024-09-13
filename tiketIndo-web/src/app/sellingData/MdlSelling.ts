import {NgModule} from '@angular/core';
import {MdlShared} from '../shared/MdlShared';
import {CmpSelling} from './CmpSelling';
import {RoutSelling} from './RoutSelling';
import {PrvTicketMaint} from '../ticketMaint/PrvTicketMaint';

@NgModule({
	imports: [RoutSelling, MdlShared],
	declarations: [CmpSelling],
	providers: [PrvTicketMaint]
})
export class MdlSelling{}