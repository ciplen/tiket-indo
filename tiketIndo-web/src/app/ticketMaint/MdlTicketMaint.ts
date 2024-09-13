import {NgModule} from '@angular/core';
import {MdlShared} from '../shared/MdlShared';
import {PrvTicketMaint} from './PrvTicketMaint';
import {RoutTicketMaint} from './RoutTicketMaint';
import {CmpTicketMaint} from './CmpTicketMaint';
import {CmpTicketMaintForm} from './CmpTicketMaintForm';

@NgModule({
	imports: [RoutTicketMaint, MdlShared],
	declarations: [CmpTicketMaint, CmpTicketMaintForm
	],
	providers: [PrvTicketMaint]
})
export class MdlTicketMaint {}