import { Component, Input, AfterViewInit, Output, EventEmitter, OnChanges, SimpleChange } from '@angular/core';
import { Observable } from 'rxjs';

export class PaginationObject {
	private _totalRows: number = 0;
	startIndex: number = 0;
	maxRows: number = 50;
//	private initialized = false;
	private totalRowsObserver: any;
	totalRowsObservable: Observable<number> = Observable.create(observer => {
		this.totalRowsObserver = observer;
		if (this._totalRows) {
			this.totalRowsObserver.next(this._totalRows);
		}
	});

	get totalRows(): number {
		return this._totalRows;
	}

	set totalRows(val: number) {
		if(this.totalRowsObserver) {
			this.totalRowsObserver.next(val);
		}
		this._totalRows = val;
	}
}

@Component({
	moduleId: module.id,
	selector: 'p-pagination',
	templateUrl: 'CmpPagination.html',
	styleUrls: ['CmpPagination.css']
})

/**
 * Bootstrap alert component. Possible value of type is success, info, warning and danger
 */
export class CmpPagination implements AfterViewInit, OnChanges {
	@Input() paginationObject: PaginationObject;
	maxRow: number;
	totalRow: number;
	page: number;
	lstPage: number[];
	disablePrev = true;
	disableNext = true;
	@Output() reload: EventEmitter<any> = new EventEmitter();
	ngAfterViewInit() {

	}

	first() {
		this.paginationObject.startIndex = 0;
		this.reload.emit();
	}

	prev() {
		let start = this.paginationObject.startIndex - this.paginationObject.maxRows;
		if (start < 0) {
			start = 0;
		}
		this.paginationObject.startIndex = start;
		this.reload.emit();
	}

	next() {
		let start = this.paginationObject.startIndex + this.paginationObject.maxRows;
		if (start < this.paginationObject.totalRows) {
			this.paginationObject.startIndex = start;
			this.reload.emit();
		}
	}

	last() {
		if (this.paginationObject.totalRows > 0) {
			let totalPage = Math.ceil(this.paginationObject.totalRows / this.paginationObject.maxRows);
			let start = (totalPage - 1) * this.paginationObject.maxRows;
			this.paginationObject.startIndex = start;
			this.reload.emit();
		}
	}

	resetPage() {
		this.paginationObject.startIndex = (this.page - 1) * this.paginationObject.maxRows;
		this.reload.emit();
	}

	subscriber: any;
	ngOnChanges(changes: { [propName: string]: SimpleChange }) {
		if (changes['paginationObject']) {
			let info: PaginationObject = changes['paginationObject'].currentValue;
			if (this.subscriber) {
				console.log('unsubscribe pagination');
				this.subscriber.unsubscribe();
			}
			this.subscriber = info.totalRowsObservable.subscribe(
				data => {
					this.lstPage = [];
					if (data > 0) {
						this.page = this.paginationObject.startIndex / this.paginationObject.maxRows + 1;
						let lastPage = Math.ceil(data / this.paginationObject.maxRows);
						let i = 0;
						while (this.lstPage.push(i++) < lastPage);
						this.disablePrev = this.page < 2;
						this.disableNext = this.page == lastPage;
					} else {
						this.page = 0;
						this.disablePrev = true;
						this.disableNext = true;
					}
				}
			);
		}
	}

}

