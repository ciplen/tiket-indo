import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CmpHome} from './CmpHome';
import {CmpDashboard} from 'app/dashboard/CmpDashboard';
import {CmpHelp} from 'app/home/CmpHelp';
import {PageNotFoundComponent} from '../not-found.component';
import {AuthGuard} from '../login/auth-guard.service';

@NgModule({
	imports: [RouterModule.forChild([
		{
			path: 'home', component: CmpHome,
			canActivate: [AuthGuard],
			children: [
				{path: 'user', loadChildren: 'app/user/MdlUser#MdlUser'},
				{path: 'role', loadChildren: 'app/role/MdlRole#MdlRole'},
				{path: 'vm', loadChildren: 'app/venueMaint/MdlVenueMaint#MdlVenueMaint'},
				{path: 'util', loadChildren: 'app/util/MdlUtil#MdlUtil'},
				{path: 'tm', loadChildren: 'app/ticketMaint/MdlTicketMaint#MdlTicketMaint'},
				{path: 'td', loadChildren: 'app/ticketDtl/MdlTicketDtl#MdlTicketDtl'},
				{path: 'penjualan', loadChildren: 'app/sellingData/MdlSelling#MdlSelling'},
				{path: 'visitor', loadChildren: 'app/visitor/MdlVisitor#MdlVisitor'},
				{path: 'booking', loadChildren: 'app/bookingTicket/MdlBookingTicket#MdlBookingTicket'},
				{path: 'help', component: CmpHelp},
				{path: '', component: CmpDashboard},
			]
		},
		{path: '**', component: PageNotFoundComponent},
	])],
	exports: [RouterModule]
})
export class RoutHome {}
