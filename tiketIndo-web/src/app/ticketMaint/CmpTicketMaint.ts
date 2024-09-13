import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {TableQuery} from '../shared/TabelQuery';
import {PrvTicketMaint} from './PrvTicketMaint';
import {TranslateService} from '@ngx-translate/core';
import {BaseListComponent} from '../shared/BaseListComponent';
import {SrvUtil} from 'app/shared/SrvUtil';
import {SrvMasterData} from '../shared/SrvMasterData';

@Component({
	templateUrl: './CmpTicketMaint.html',
})

export class CmpTicketMaint extends BaseListComponent implements OnInit {
	selected = [];
	tableHeight: string;
	canMaintain = false;

	constructor(router: Router,
		route: ActivatedRoute,
		translate: TranslateService,
		private srvMasterData: SrvMasterData,
		private srvUtil: SrvUtil,
		private prvTicket: PrvTicketMaint) {
		super('CmpTicketMaint', translate, route, router);
		this.canMaintain = this.srvMasterData.hasPermission('ticket:write') || this.srvMasterData.isAdmin();

		this.criteria = {
			namaEvent: {value: '', label: 'Nama Event'},
			type: {value: '', label: 'Tipe Tiket'},
			amount: {value: '', label: 'Jumlah'}
		};
	}
	ngOnInit() {
		this.tableHeight = (window.innerHeight - 225) + 'px';
		super.defaultOnInit();
	}

	getData(query: TableQuery) {
		return this.prvTicket.find(this.paginationObject.startIndex, this.paginationObject.maxRows, query);
	}

	edit() {
		if (this.selected && this.selected.length > 0) {
			this.router.navigate(['../' + this.selected[0].id], {relativeTo: this.route});
		}
	}
	deleteSelected() {
		if (this.selected && this.selected.length > 0) {
			this.srvUtil.showDialogConfirm(
				'<strong>' + this.translate.instant('delete.confirmation'),
				(answer) => {
					if (answer === 'yes') {
						this.PanictUtil.showRequestIndicator();
						this.prvTicket.deleteRecord(this.selected[0].id, this.selected[0].version).subscribe(
							data => this.reload(),
							error => {
								this.handleError(error, this.router);
							});
					}
				});
		}
	}
}