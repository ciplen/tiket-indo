import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {TableQuery} from '../shared/TabelQuery';
import {PrvVenueMaint} from './PrvVenueMaint';
import {TranslateService} from '@ngx-translate/core';
import {SrvUtil} from 'app/shared/SrvUtil';
import {BaseListComponent} from '../shared/BaseListComponent';
import {SrvMasterData} from '../shared/SrvMasterData';

@Component({
	templateUrl: './CmpVenueMaint.html',
})

export class CmpVenueMaint extends BaseListComponent implements OnInit {
	selected = [];
	tableHeight: string;
	canMaintain = false;

	constructor(router: Router,
		route: ActivatedRoute,
		private srvMasterData: SrvMasterData,
		private srvUtil: SrvUtil,
		translate: TranslateService,
		private prvVenueMaint: PrvVenueMaint) {
		super('CmpVenueMaint', translate, route, router);

		this.canMaintain = this.srvMasterData.hasPermission('venue:write') || this.srvMasterData.isAdmin();
		this.criteria = {
			nama: {value: '', label: 'Nama Event'},
			tempat: {value: '', label: 'Tempat'},
			tanggalAwal: {value: '', label: 'Tanggal Awal'},
			tanggalAkhir: {value: '', label: 'Tanggal Akhir'}
		};
	}
	ngOnInit() {
		this.tableHeight = (window.innerHeight - 225) + 'px';
		super.defaultOnInit();
	}

	getData(query: TableQuery) {
		return this.prvVenueMaint.find(this.paginationObject.startIndex, this.paginationObject.maxRows, query);
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
						this.prvVenueMaint.deleteRecord(this.selected[0].id, this.selected[0].version).subscribe(
							data => this.reload(),
							error => {
								this.handleError(error, this.router);
							});
					}
				});
		}
	}

}
