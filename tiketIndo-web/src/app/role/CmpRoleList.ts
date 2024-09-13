import {Component, OnInit} from '@angular/core';
import {BaseListComponent} from '../shared/BaseListComponent';
import {Router, ActivatedRoute} from '@angular/router';
import {TableQuery} from '../shared/TabelQuery';
import {PrvRole} from './PrvRole';
import {TranslateService} from '@ngx-translate/core';
import {SrvMasterData} from '../shared/SrvMasterData';
import { SrvUtil } from 'app/shared/SrvUtil';

@Component({
	templateUrl: './CmpRoleList.html',
})

export class CmpRoleList extends BaseListComponent implements OnInit {
	selected = [];
	tableHeight: string;
	canMaintain = false;
	constructor(router: Router,
		route: ActivatedRoute,
		translate: TranslateService,
		private srvUtil: SrvUtil,
		private prvRole: PrvRole,
		private srvMasterData: SrvMasterData) {
		super('CmpRoleList', translate, route, router);
		this.canMaintain = srvMasterData.hasPermission('role:write');
		this.criteria = {
			name: {value: '', label: 'access.right'},
			description: {value: '', label: 'description'}
		};
	}

	ngOnInit() {
		this.tableHeight = (window.innerHeight - 225) + 'px';
		this.defaultOnInit();
	}

	getData(query: TableQuery) {
		return this.prvRole.find(this.paginationObject.startIndex, this.paginationObject.maxRows, query);
	}

	exportData() {
		let query = this.getQueryObject();
		this.prvRole.exportData(this.paginationObject.startIndex, this.paginationObject.maxRows, query).subscribe((res) => {
			window['saveAs'](res, 'role.xlsx');
		}, error => {
			this.handleError(error, this.router);
		});
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
				(answer: string) => {
					if (answer === 'yes') {
						this.PanictUtil.showRequestIndicator();
						this.prvRole.deleteRecord(this.selected[0].id, this.selected[0].version).subscribe(
							data => this.reload(),
							error => {
								this.handleError(error, this.router);
							});
					}
				});

		}
	}
}