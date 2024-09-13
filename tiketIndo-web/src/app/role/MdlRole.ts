import { NgModule } from '@angular/core';
import { MdlShared } from '../shared/MdlShared';
import {CmpRoleList} from './CmpRoleList';
import {CmpRoleForm} from './CmpRoleForm';
import {RoutRole} from './RoutRole';
import {PrvRole} from './PrvRole';
@NgModule({
	imports: [RoutRole, MdlShared],
	declarations: [CmpRoleList, CmpRoleForm],
	providers:[PrvRole]
})
export class MdlRole { }
