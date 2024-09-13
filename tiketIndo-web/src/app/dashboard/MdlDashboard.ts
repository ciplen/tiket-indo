import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CmpDashboard} from './CmpDashboard';
import {DashboardRoutingModule} from './dashboard-routing.module';
import {MdlShared} from '../shared/MdlShared';
import { ChartsModule } from 'ng2-charts';

@NgModule({
	imports: [CommonModule, DashboardRoutingModule, MdlShared, ChartsModule],
	declarations: [CmpDashboard],
	exports: [CmpDashboard],
	providers: []
})

export class MdlDashboard {}
