import {NgModule} from '@angular/core';
import {MdlShared} from '../shared/MdlShared';
import {PrvTicketDtl} from './PrvTicketDtl';
import {RoutTicketDtl} from './RoutTicketDtl';
import {CmpTicketDtl} from './CmpTicketDtl';

@NgModule({
	imports: [RoutTicketDtl, MdlShared],
	declarations: [CmpTicketDtl
	],
	providers: [PrvTicketDtl]
})
export class MdlTicketDtl {}