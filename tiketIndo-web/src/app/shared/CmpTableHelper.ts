import { AfterViewInit, Component, EventEmitter, Input, Output, ViewChild, Inject } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { DatatableComponent, TableColumn } from '@swimlane/ngx-datatable';
import { SrvMasterData } from './SrvMasterData';
import { SrvUtil } from './SrvUtil';
import { TranslateService } from '@ngx-translate/core';

interface ColumnInfo {
	prop: any;
	width: number;
	visible: boolean;
	name: string;
}

@Component({
	template: `
<h2 mat-dialog-title>{{'save.template' | translate }}</h2>
<div mat-dialog-content>
	<div class="form-group row">
		<label class="col-sm-6  col-form-label">{{'template.name' | translate}}</label>
		<div class="col-sm-6">
			<input cdkFocusInitial [(ngModel)]="data.templateName" maxlength="10">
		</div>
	</div>
</div>
<div mat-dialog-actions>
  <button (click)="saveDialogAnswer('save')" class="btn btn-default mr-2">{{'save'| translate}}</button>
  <button (click)="saveDialogAnswer('cancel')" class="btn btn-default">{{'cancel' | translate }}</button>
</div>
	`,
})
export class CmpTableHelperSaveDialog {
	constructor(
		public dialogRef: MatDialogRef<CmpTableHelperSaveDialog>,
		@Inject(MAT_DIALOG_DATA) public data: any) { }

	saveDialogAnswer(answer: string) {
		if (answer === 'save') {
			this.dialogRef.close(this.data.templateName);
		} else {
			this.dialogRef.close();
		}
	}
}

@Component({
	moduleId: module.id,
	selector: 'p-table-helper',
	templateUrl: 'CmpTableHelper.html',
	styleUrls: ['CmpTableHelper.css'],
})
export class CmpTableHelper implements AfterViewInit {
	@ViewChild('divDropdown') divDropdown: any;
	oriColumns: TableColumn[];
	savedColumns: ColumnInfo[];
	private _table: DatatableComponent;
	private _templateKey: string;
	@Input() srvMasterData: SrvMasterData;
	@Output() sort: EventEmitter<any> = new EventEmitter();
	lstTemplate: any[];
	templateName = 'default';
	allChecked = false;
	completeRows: any[];
	displayedRows: any[];
	selected: any;
	sorts: any;

	public static getSavedSorts(prefix: string) {
		return JSON.parse(localStorage.getItem(prefix + '_tbl_sorts'));
	}

	constructor(
		private dialog: MatDialog,
		private srvUtil: SrvUtil,
		private translate: TranslateService
	) { }

	get templateKey() {
		return this._templateKey;
	}

	@Input()
	set templateKey(key: string) {
		this._templateKey = key + '_tbl';
	}

	get table() {
		return this._table;
	}

	@Input()
	set table(tbl: DatatableComponent) {
		this._table = tbl;
		this._table.resize.subscribe(e => this.onResize(e));
		this._table.reorder.subscribe(e => this.saveState(null));
		this._table.sort.subscribe(e => {
			this.sorts = e.sorts;
			localStorage.setItem(this._templateKey + '_sorts', JSON.stringify(this.sorts));
			this.sort.emit(this.sorts);
		});

	}

	toggleOpen() {
		if ((' ' + this.divDropdown.nativeElement.className + ' ').indexOf(' show ') > -1) {
			this.divDropdown.nativeElement.classList.remove('show');
		} else {
			this.divDropdown.nativeElement.classList.add('show');
		}
	}

	onResize(e) {
		for (let i = 0; i < this.savedColumns.length; i++) {
			let clm = this.savedColumns[i];
			if (clm.prop === e.column.prop) {
				clm.width = e.newValue;
				break;
			}
		}
		this.saveState(null);
	}

	@Input()
	set rows(rows: any[]) {
		this.completeRows = rows;
		this.displayedRows = [...rows];
		if (this.displayedRows != null) {
			this._table.rows = this.displayedRows;
		}
	}

	get rows() {
		return this.displayedRows;
	}

	ngAfterViewInit() {
		if (this._table) {
			setTimeout(() => {
				this.oriColumns = [...this._table._internalColumns];
				this.oriColumns.forEach(e => e['oriWidth'] = e.width);
				this.loadState();
			});
		}

		if (this.displayedRows != null) {
			setTimeout(() => {
				this._table.rows = this.displayedRows;
				this.sorts = JSON.parse(localStorage.getItem(this._templateKey + '_sorts'));
				this._table.sorts = this.sorts;
			}, 100);
		}
	}

	saveState(name: string) {
		let stringInfo = JSON.stringify(this.savedColumns);
		localStorage.setItem(this._templateKey, stringInfo);
		if (this.srvMasterData && name) {
			let authInfo = this.getAuthInfo();
			let lstUserSetting = authInfo.userSettings;

			for (let i = 0; i < lstUserSetting.length; i++) {
				let userSetting = lstUserSetting[i];
				if (userSetting.category == this._templateKey && userSetting.name == name) {
					lstUserSetting.splice(i, 1);
					break;
				}
			}

			lstUserSetting.push({ category: this._templateKey, name: name, value: stringInfo });
			this.srvMasterData.saveUserSetting(this._templateKey, name, stringInfo).subscribe();
			this.srvMasterData.saveAuthInfo(authInfo);
		}
	}

	toggle(rowNum: number) {
		this.savedColumns[rowNum]['visible'] = !this.savedColumns[rowNum]['visible'];
		this.applyColumnConfig();
	}

	up() {
		if (!this.selected) return;
		let idx = this.savedColumns.indexOf(this.selected);
		if (idx <= 0) return;
		let temp = this.savedColumns[idx - 1];
		this.savedColumns[idx - 1] = this.savedColumns[idx];
		this.savedColumns[idx] = temp;
		this.applyColumnConfig();
	}
	down() {
		if (!this.selected) return;
		let idx = this.savedColumns.indexOf(this.selected);
		if (idx > this.savedColumns.length - 2) return;
		let temp = this.savedColumns[idx + 1];
		this.savedColumns[idx + 1] = this.savedColumns[idx];
		this.savedColumns[idx] = temp;
		this.applyColumnConfig();
	}

	getAuthInfo() {
		let authInfo = this.srvMasterData.getAuthInfo();
		if (authInfo.userSettings == undefined) {
			authInfo.userSettings = [];
		}
		return authInfo;
	}

	private applyColumnConfig(save: boolean = true) {
		if (!this.savedColumns) {
			return;
		}
		let savedColumns2: any[] = [];
		this.savedColumns.forEach(element => {
			if (element.visible != false) {
				for (let i = 0; i < this.oriColumns.length; i++) {
					let ori = this.oriColumns[i];
					if (element.prop === ori.prop) {
						ori.width = element.width;
						savedColumns2.push(ori);
						break;
					}
				}
			}
		});
		this._table.columns = savedColumns2;
		if (save) {
			this.saveState(null);
		}
	}

	loadTemplate(templateName: string) {
		this.templateName = templateName;
		if (this.srvMasterData) {
			let authInfo = this.srvMasterData.getAuthInfo();
			let lstUserSetting = authInfo.userSettings;
			for (var i = 0; i < lstUserSetting.length; i++) {
				let userSetting = lstUserSetting[i];
				if (userSetting.category == this._templateKey && templateName == userSetting.name) {
					this.savedColumns = JSON.parse(userSetting.value);
					this.applyColumnConfig();
					return;
				}
			}
		}
	}

	loadState() {
		if (this.srvMasterData) {
			let authInfo = this.getAuthInfo();
			let alreadyLoaded = false;
			for (var i = 0; i < authInfo.userSettings.length; i++) {
				let userSetting = authInfo.userSettings[i];
				if (userSetting.category == this._templateKey) {
					alreadyLoaded = true;
					this.populateTemplate();
					break;
				}
			}
			if (!alreadyLoaded) {
				let lstUserSetting = authInfo.userSettings;
				this.srvMasterData.getUserSettings(this._templateKey).subscribe(
					data => {
						for (var i = lstUserSetting.length - 1; i >= 0; i--) {
							let userSetting = lstUserSetting[i];
							if (userSetting.category == this._templateKey) {
								lstUserSetting.splice(i, 1);
							}
						}
						for (var i = 0; i < data.length; i++) {
							let userSetting = data[i];
							lstUserSetting.push(userSetting);
						}
						this.srvMasterData.saveAuthInfo(authInfo);
						this.populateTemplate();
					}
				);
			}
		}
		this.savedColumns = JSON.parse(localStorage.getItem(this._templateKey));
		if (!this.savedColumns) {
			this.savedColumns = [];
		}
		this.initSavedColumns();
		this.applyColumnConfig(false);

	}

	private initSavedColumns() {
		this.oriColumns.forEach(ori => {
			let found = false;
			for (let i = 0; i < this.savedColumns.length; i++) {
				let saved = this.savedColumns[i];
				if (saved.prop == ori.prop) {
					saved.name = ori.name;
					found = true;
					break;
				}
			}
			if (!found) {
				this.savedColumns.push({ prop: ori.prop, width: ori.width, visible: true, name: ori.name });
			}
		});
	}

	populateTemplate() {
		if (this.srvMasterData) {
			let authInfo = this.getAuthInfo();
			this.lstTemplate = [];
			for (var i = 0; i < authInfo.userSettings.length; i++) {
				let userSetting = authInfo.userSettings[i];
				if (userSetting.category == this._templateKey) {
					this.lstTemplate.push(userSetting);
				}
			}
		}
	}

	reset() {
		this.oriColumns.forEach(e => {
			e.width = e['oriWidth'];
		});
		this._table.columns = [...this.oriColumns];
		localStorage.removeItem(this._templateKey);
		this.savedColumns = [];
		this.initSavedColumns();
	}

	deleteTemplate(templateName: string) {
		this.templateName = templateName;
		if (this.srvMasterData && templateName) {
			let authInfo = this.getAuthInfo();
			let lstUserSetting = authInfo.userSettings;

			for (var i = lstUserSetting.length - 1; i >= 0; i--) {
				let userSetting = lstUserSetting[i];
				if (userSetting.category == this._templateKey && userSetting.name == templateName) {
					lstUserSetting.splice(i, 1);
					break;
				}
			}
			this.srvMasterData.saveAuthInfo(authInfo);
			this.srvMasterData.deleteUserSetting(this._templateKey, templateName).subscribe();
		}
	}

	showSaveDialog() {
		const countryForm = this.dialog.open(CmpTableHelperSaveDialog,
			{
				data: { templateName: this.templateName },
				width: '400px',
				disableClose: true
			});
		countryForm.afterClosed().subscribe(result => {
			if (result !== '' && result !== undefined) {
				this.templateName = result;
				this.saveState(this.templateName);
				this.populateTemplate();
			}
		});
	}

	removeTemplate(templateName: string) {
		this.templateName = templateName;
		const delMessage = this.translate.instant('delete.template', { par1: templateName });
		this.srvUtil.showDialogConfirm(delMessage, (answer: string) => {
			if (answer === 'yes') {
				this.deleteTemplate(this.templateName);
				this.populateTemplate();
			}
		});
	}

	toggleAll() {
		this.allChecked = !this.allChecked;
		this.savedColumns.forEach(e => e['visible'] = this.allChecked);
		this.applyColumnConfig();
	}

}
