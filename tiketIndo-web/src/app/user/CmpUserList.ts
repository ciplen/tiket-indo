import {Component, OnInit} from '@angular/core';
import {UserService} from '../shared/user.service';
import {Router, ActivatedRoute} from '@angular/router';
import {TableQuery} from '../shared/TabelQuery';
import {SrvMasterData} from '../shared/SrvMasterData';
import {BaseListComponent} from '../shared/BaseListComponent';
import {TranslateService} from '@ngx-translate/core';
import { SrvUtil } from 'app/shared/SrvUtil';



@Component({
	templateUrl: './CmpUserList.html',
})

export class CmpUserList extends BaseListComponent implements OnInit {
	selected = [];
	tableHeight: string;
	canMaintain = false;
	constructor(private prvUser: UserService, router: Router,
		route: ActivatedRoute,
		private srvMasterData: SrvMasterData,
		private srvUtil: SrvUtil,
		translate: TranslateService) {
		super('CmpUserList', translate, route, router);
		this.criteria = {
			username: {value: '', label: 'user.name'},
			role: {value: '', label: 'access.right'},
			email: {value: '', label: 'email'},
		};
		this.canMaintain = srvMasterData.hasPermission('user:write');
	}

	ngOnInit() {
		this.tableHeight = (window.innerHeight - 225) + 'px';
		this.defaultOnInit();
	}


	getData(query: TableQuery) {
		return this.prvUser.findAll(this.paginationObject.startIndex, this.paginationObject.maxRows, query);
	}

	exportData() {
		let query = this.getQueryObject();
		this.prvUser.exportData(this.paginationObject.startIndex, this.paginationObject.maxRows, query).subscribe((res) => {
			window['saveAs'](res, 'member.xlsx');
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
				(answer) => {
					if (answer === 'yes') {
						this.PanictUtil.showRequestIndicator();
						this.prvUser.deleteRecord(this.selected[0].id).subscribe(
							data => this.reload(),
							error => {
								this.handleError(error, this.router);
							});
					}
				});

		}
	}


}