import { NgModule } from '@angular/core';
import { CmpUserList } from './CmpUserList';
import { CmpUserForm } from './CmpUserForm';
import { RoutUser} from './RoutUser';
import { MdlShared } from '../shared/MdlShared';

@NgModule({
	imports: [RoutUser, MdlShared],
	declarations: [CmpUserList, CmpUserForm],
})
export class MdlUser { }
