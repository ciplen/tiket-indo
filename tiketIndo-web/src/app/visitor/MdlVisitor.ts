import {NgModule} from '@angular/core';
import {MdlShared} from '../shared/MdlShared';
import {CmpVisitor} from './CmpVisitor';
import {RoutVisitor} from './RoutVisitor';
import {PrvTicketDtl} from '../ticketDtl/PrvTicketDtl';

@NgModule({
	imports: [RoutVisitor, MdlShared],
	declarations: [CmpVisitor],
	providers: [PrvTicketDtl]
})
export class MdlVisitor {}