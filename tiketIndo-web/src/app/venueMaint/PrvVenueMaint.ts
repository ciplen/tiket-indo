import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {BaseService} from '../shared/base.service';
import {TableQuery} from '../shared/TabelQuery';

@Injectable()
export class PrvVenueMaint extends BaseService {
	constructor(private http: HttpClient) {
		super();
	}
	find(start: number, max: number, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'vm?start=' + start + '&max=' + max, JSON.stringify(tq));
	}

	findAll() {
		let start = 0;
		let max = -1;
		let tq = new TableQuery;
		tq.sortingInfos.push({attributeName: 'roleNama', direction: 'asc'});
		return this.find(start, max, tq);
	}
	findById(id: number) {
		return this.http.get(this.apiUrl + 'vm/' + id);
	}
	getDataVenue(data: any) {
		return this.http.get(this.apiUrl + 'vm/lstVanue', data);
	}
	getValidateVenue(data: any) {
		return this.http.get(this.apiUrl + 'vm/lstValidatingVenue', data);
	}
	findUpComingVenue(data: any) {
		return this.http.get(this.apiUrl + 'vm/lstUpComingVanue', data);
	}
	findEventName() {
		return this.http.get(this.apiUrl + 'vm/lst');
	}
	save(id: number, data: any) {
		//let form1 = new FormData();
		//let form2 = new FormData();
		//form1.append('attachment', small, confirm.name);
		//form2.append('attachment', big, confirm.name)

		return this.http.put(this.apiUrl + 'vm/' + id, JSON.stringify(data));
		//return this.http.put(this.apiUrl + 'vm/' + id, JSON.stringify(data));
	}

	saveSmallBanner(confirm: any, venueId: number) {
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
			xhr.open('PUT', this.apiUrl + 'vm/savesmallbanner/' + venueId, true);
			xhr.withCredentials = true;
			xhr.send(form);
		});
	}

	saveBigBanner(confirm: any, venueId: number) {
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
			xhr.open('PUT', this.apiUrl + 'vm/savebigbanner/' + venueId, true);
			xhr.withCredentials = true;
			xhr.send(form);
		});
	}

	saveTotalTic(model: any) {
		return this.http.put(this.apiUrl + 'bookTicket/saveTotalTic', JSON.stringify(model));
	}
	deleteRecord(id, version) {
		return this.http.delete(this.apiUrl + 'vm/' + id + '/' + version);
	}
}