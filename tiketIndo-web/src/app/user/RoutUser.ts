import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import {CmpUserList} from './CmpUserList';
import { CmpUserForm } from './CmpUserForm';

@NgModule({
	imports: [
		RouterModule.forChild([
			{ path: ':id', component: CmpUserForm },
			{ path: '', component: CmpUserList },
		])
	],
})
export class RoutUser { }
