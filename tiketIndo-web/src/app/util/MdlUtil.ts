import {NgModule} from '@angular/core';
import {MdlShared} from '../shared/MdlShared';
import {RoutUtil} from './RoutUtil';
import {CmpUtility} from './CmpUtility';
import {SrvMasterData} from '../shared/SrvMasterData'


@NgModule({
	imports: [RoutUtil, MdlShared],
	declarations: [CmpUtility],
	providers: [SrvMasterData]
})
export class MdlUtil {}