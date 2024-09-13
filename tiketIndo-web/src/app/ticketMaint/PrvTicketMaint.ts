import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {BaseService} from '../shared/base.service';
import {TableQuery} from '../shared/TabelQuery';

@Injectable()
export class PrvTicketMaint extends BaseService {
	constructor(private http: HttpClient) {
		super();
	}
	find(start: number, max: number, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'tm?start=' + start + '&max=' + max, JSON.stringify(tq));
	}

	findLstTicket(page: number, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'tm/lstTicket?page=' + page, JSON.stringify(tq));
	}

	findById(id: number) {
		return this.http.get(this.apiUrl + 'tm/' + id);
	}
	findLstVenue() {
		return this.http.get(this.apiUrl + 'vm/lst');
	}
	checkAvailability(ticketType: string, qty: number) {
		return this.http.get(this.apiUrl + 'tm/check/' + ticketType + '/' + qty);
	}
	checkAvailableTicket(id: number) {
		return this.http.get(this.apiUrl + 'tm/check/' + id);
	}
	save(id: number, data: any) {
		return this.http.put(this.apiUrl + 'tm/' + id, JSON.stringify(data));
	}
	saveBooking(id: number, data: any) {
		return this.http.put(this.apiUrl + 'bookTicket/' + id, JSON.stringify(data));
	}
	getCodeUniq() {
		return this.http.get(this.apiUrl + 'bookTicket/getUniqCode');
	}
	getDiscount(id: number, data: any) {
		return this.http.put(this.apiUrl + 'discount/getDiscount/' + id, JSON.stringify(data));
	}
	findVenue(start: number, max: number, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'vm?start=' + start + '&max=' + max, JSON.stringify(tq));
	}
	deleteRecord(id, version) {
		return this.http.delete(this.apiUrl + 'tm/' + id + '/' + version);
	}
	findTicketByVenueId(id: number) {
		return this.http.get(this.apiUrl + 'tm/venueId/' + id);
	}
	insertBooking(id: number, data: any) {
		return this.http.put(this.apiUrl + 'bookTicket/insert' + id, JSON.stringify(data));
	}
	generate(payload: any) {
		return this.http.post(this.apiUrl + 'td/generate', JSON.stringify(payload));
	}
	getDataPenjualan(venueId) {
		return this.http.get(this.apiUrl + 'tm/penjualan/venueId/' + venueId);
	}
	getListProvinsi() {
		return this.http.get(this.apiUrl + 'provinsi/lstProvinsi');
	}
	getListKota(provinsiId) {
		return this.http.get(this.apiUrl + 'kota/provinsi/' + provinsiId);
	}

}