import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {TableQuery} from '../shared/TabelQuery';
import {PrvTicketDtl} from '../ticketDtl/PrvTicketDtl';
import {TranslateService} from '@ngx-translate/core';
import {BaseListComponent} from '../shared/BaseListComponent';
import {SrvUtil} from 'app/shared/SrvUtil';
import {SrvMasterData} from '../shared/SrvMasterData';
import {PrvTicketMaint} from '../ticketMaint/PrvTicketMaint';

@Component({
	templateUrl: './CmpVisitor.html',
})

export class CmpVisitor extends BaseListComponent implements OnInit {
	selected = [];
	tableHeight: string;
	data: any;
	lstEvent: any;
	eventId: any;
	constructor(router: Router,
		route: ActivatedRoute,
		private prvTicket: PrvTicketMaint,
		translate: TranslateService,
		private srvMasterData: SrvMasterData,
		private prvTicketDtl: PrvTicketDtl) {
		super('CmpVisitor', translate, route, router);
		this.srvMasterData.checkAuth().subscribe(
			data => {},
			error => this.handleError(error, this.router)
		);
	}
	ngOnInit() {
		this.tableHeight = (window.innerHeight - 225) + 'px';
		super.defaultOnInit();
		this.getLstVenue();
	}
	getLstVenue() {
		return this.prvTicket.findLstVenue().subscribe(
			data => {
				this.lstEvent = data;
			}
		);
	}
	getData(query: TableQuery) {
		return null;
	}
	find(eventId) {
		return this.prvTicketDtl.countVisitorEntered(eventId).subscribe(
			data => {
				this.data = data;
			}
		);
	}
}