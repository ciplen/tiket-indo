import {BaseComponent} from './base.component';
import {TranslateService} from '@ngx-translate/core';
import {TableQuery, TableCriteria, SortingInfo} from './TabelQuery';
import {Observable} from 'rxjs';
import {PaginationObject} from '../shared/CmpPagination';
import {Router, ActivatedRoute, Params} from '@angular/router';
import {catchError, switchMap} from 'rxjs/operators';

export abstract class BaseListComponent extends BaseComponent {
	protected sorts: any[];
	criteria: any;
	searchLabel = '';
	tableData: any;
	protected paginationObject = new PaginationObject();
	protected noRowSelected = true;
	protected forceSearch = false;

	constructor(
		public prefix: string,
		translate: TranslateService,
		protected route: ActivatedRoute,
		protected router: Router
	) {
		super(translate);
	}

	getQueryObject() {
		let query = new TableQuery;

		if (this.sorts && this.sorts.length > 0) {
			this.sorts.forEach(el => {
				query.sortingInfos.push(new SortingInfo(el.prop, el.dir));
			});
		}
		this.searchLabel = '';
		for (var k in this.criteria) {
			if (this.criteria[k].value) {
				query.tableCriteria.push(new TableCriteria(k, this.criteria[k].value));
				this.searchLabel = this.searchLabel + ' [' + this.translate.instant(this.criteria[k].label) + ' : ' + this.criteria[k].value + ']';
			}
		}
		return query;
	}

	onSort($event) {
		this.sorts = $event;
		this.getDataAbstract().subscribe(data => this.displayData(data));
	}

	getDataAbstract(): Observable<any> {
		let query = this.getQueryObject();
		this.PanictUtil.showRequestIndicator();
		return this.getData(query);

	}
	
	textChange(e) {
		if (e.keyCode == 13) {
			this.reloadFirstPage();
		}
	}
	
	abstract getData(query: TableQuery): Observable<any>;
	
	displayData(data: any) {
		this.tableData = data;
		this.paginationObject.totalRows = this.tableData.totalRows;
		this.PanictUtil.hideRequestIndicator();
		this.collapsePanel('#pnlBody');
	}

	defaultOnInit() {
		this.sorts = JSON.parse(localStorage.getItem(this.prefix + '_tbl_sorts'));;

		this.route.params.pipe(switchMap((params: Params) => {
			let load = false;
			this.noRowSelected = true;
			for (var k in this.criteria) {
				this.criteria[k].value = this.emptyStringIfUndefined(params[k]);
				load = true;
			}

			this.paginationObject.startIndex = this.zeroIfUndefined(params['start']);
			if (load || this.forceSearch) {
				this.PanictUtil.showRequestIndicator();
				return this.getDataAbstract().pipe(catchError(err => this.handleErrorReturnObservable(err, this.router)));
			} else {
				return Observable.create(observer => null);
			}
		})).subscribe(data => this.displayData(data));
	}

	reloadFirstPage() {
		this.paginationObject.startIndex = 0;
		this.reload();
	}

	reload() {
		this.forceSearch = true;
		let navObject: any = {start: this.paginationObject.startIndex, random: Math.random()};
		for (var k in this.criteria) {
			if (this.criteria[k].value) {
				navObject[k] = this.criteria[k].value;
			}
		}
		this.router.navigate(['../', navObject], {relativeTo: this.route});
	}

	onSelect({selected}) {
		this.noRowSelected = false;
	}

}