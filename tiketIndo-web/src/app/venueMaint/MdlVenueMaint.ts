import {NgModule} from '@angular/core';
import {MdlShared} from '../shared/MdlShared';
import {PrvVenueMaint} from './PrvVenueMaint';
import {RoutVenueMaint} from './RoutVenueMaint';
import {CmpVenueMaint} from './CmpVenueMaint';
import {CmpVenueMaintForm} from './CmpVenueMaintForm';

@NgModule({
	imports: [RoutVenueMaint, MdlShared],
	declarations: [CmpVenueMaint, CmpVenueMaintForm],
	providers: [PrvVenueMaint]
})
export class MdlVenueMaint {}