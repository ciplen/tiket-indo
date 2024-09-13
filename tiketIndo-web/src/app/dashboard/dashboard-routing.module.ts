import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CmpDashboard } from './CmpDashboard';

@NgModule({
	imports: [
		RouterModule.forChild([
			{ path: '', component: CmpDashboard }
		])
	],
})
export class DashboardRoutingModule { }
