import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {BaseService} from '../shared/base.service';
import {TableQuery} from '../shared/TabelQuery';

@Injectable()
export class PrvBookingTicket extends BaseService {
	constructor(private http: HttpClient) {
		super();
	}
	find(start: number, max: number, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'bookTicket?start=' + start + '&max=' + max, JSON.stringify(tq));
	}

	findLstBooking(page: number, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'bookTicket/lstTicket?page=' + page, JSON.stringify(tq));
	}

	findById(id: number) {
		return this.http.get(this.apiUrl + 'bookTicket/' + id);
	}
	getIdBooking(id: number) {
		return this.http.get(this.apiUrl + 'bookTicket/bookTiket/' + id);
	}
	save(id: number, data: any) {
		return this.http.put(this.apiUrl + 'bookTicket/' + id, JSON.stringify(data));
	}

	getEmailAndId(id: number, email: string) {
		return this.http.get(this.apiUrl + 'bookTicket/' + id + '/' + email);
	}

	deleteRecord(id, version) {
		return this.http.delete(this.apiUrl + 'bookTicket/' + id + '/' + version);
	}
	approve(id: number) {
		return this.http.put(this.apiUrl + 'bookTicket', JSON.stringify(id));
	}
	confirm(id: number) {
		return this.http.put(this.apiUrl + 'bookTicket/code/', JSON.stringify(id));
	}

	generate(payload: any) {
		return this.http.post(this.apiUrl + 'td/generate', JSON.stringify(payload));
	}


	uploadBktTrnfr(confirm: any, bookingId: number) {
		return Observable.create(observer => {
			let xhr: XMLHttpRequest = new XMLHttpRequest();

			let form = new FormData();
			form.append('attachment', confirm, confirm.name);

			xhr.onreadystatechange = () => {
				if (xhr.readyState === 4) {
					if (xhr.status === 200) {
						observer.next(JSON.parse(xhr.response).data);
						observer.complete();
					} else {
						observer.error(xhr.response);
					}
				}
			};
			xhr.upload.onabort = (e) => {
				console.log('abort');
			}
			xhr.upload.onerror = (e) => {
				console.log('error');
			}
			xhr.open('POST', this.apiUrl + 'confirm/uploadProfilePicture/' + bookingId, true);
			xhr.withCredentials = true;
			xhr.send(form);
		});
	}

	exportData(start: number, max: number, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'bookTicket/xls?start=' + start + '&max=' + max, JSON.stringify(tq), {
			responseType: 'blob'
		});
	}
	exportAllData(tq: TableQuery) {
		return this.http.post(this.apiUrl + 'bookTicket/xlsAll', JSON.stringify(tq), {
			responseType: 'blob'
		});
	}
	
	countBookingStatusAll(status : string) {
		return this.http.get(this.apiUrl + 'bookTicket/countBookingStatusAll/' + status);
	}

	countBookingStatus(venueId : number, status : string) {
		return this.http.get(this.apiUrl + 'bookTicket/countBookingStatus/' + venueId + '/' + status);
	}

	countBookingByAge(venueId : number) {
		return this.http.get(this.apiUrl + 'bookTicket/countAgeBooking/' + venueId);
	}

	countBookingByKota(venueId : number) {
		return this.http.get(this.apiUrl + 'bookTicket/countKotaBooking/' + venueId);
	}
}