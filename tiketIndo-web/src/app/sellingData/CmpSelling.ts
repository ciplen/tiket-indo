import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {TableQuery} from '../shared/TabelQuery';
import {PrvTicketMaint} from '../ticketMaint/PrvTicketMaint';
import {TranslateService} from '@ngx-translate/core';
import {BaseListComponent} from '../shared/BaseListComponent';
import {SrvUtil} from 'app/shared/SrvUtil';
import {SrvMasterData} from '../shared/SrvMasterData';


@Component({
	templateUrl: './CmpSelling.html',
})

export class CmpSelling extends BaseListComponent implements OnInit {
	selected = [];
	tableHeight: string;
	data: any;
	lstEvent: any;
	eventId: any;

	constructor(router: Router,
		route: ActivatedRoute,
		translate: TranslateService,
		private srvMasterData: SrvMasterData,
		private srvUtil: SrvUtil,
		private prvTicket: PrvTicketMaint) {
		super('CmpSelling', translate, route, router);
		this.srvMasterData.checkAuth().subscribe(
			data => {},
			error => this.handleError(error, this.router)
		);
	}
	ngOnInit() {
		this.tableHeight = (window.innerHeight - 225) + 'px';
		super.defaultOnInit();
		//		this.find();
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
		return this.prvTicket.getDataPenjualan(eventId).subscribe(
			data => {
				this.data = data;
			}
		);
	}
}