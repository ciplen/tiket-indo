import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BaseService} from '../shared/base.service';
import {TableQuery} from '../shared/TabelQuery';

@Injectable()
export class PrvTicketDtl extends BaseService {
	constructor(private http: HttpClient) {
		super();
	}
	find(start: number, max: number, penjualan: string, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'td?start=' + start + '&max=' + max + '&penjualan=' + penjualan, JSON.stringify(tq));
	}

	verify(id: number, status: string) {
		return this.http.post(this.apiUrl + 'td/verify/' + id + '/' + status, JSON.stringify(""));
	}
	countVisitorEntered(venueId) {
		return this.http.get(this.apiUrl + 'td/countVisitorEntered/' + venueId);
	}

	exportData(start: number, max: number, penjualan: string, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'td/xls?start=' + start + '&max=' + max + '&penjualan=' + penjualan, JSON.stringify(tq), {
			responseType: 'blob'
		});
	}
	exportAllData(penjualan: string, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'td/xlsAll?penjualan=' + penjualan, JSON.stringify(tq), {
			responseType: 'blob'
		});
	}
	findLstVenue() {
		return this.http.get(this.apiUrl + 'vm/lst');
	}
	findLstSeller() {
		return this.http.get(this.apiUrl + 'user/lstSeller');
	}
}