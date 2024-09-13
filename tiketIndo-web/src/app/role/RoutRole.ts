import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CmpRoleList} from './CmpRoleList';
import {CmpRoleForm} from './CmpRoleForm';

@NgModule({
	imports: [
		RouterModule.forChild([
			{path: ':id', component: CmpRoleForm},
			{path: '', component: CmpRoleList},
		])
	],
})
export class RoutRole {}
