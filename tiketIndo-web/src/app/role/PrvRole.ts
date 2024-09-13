import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {BaseService} from '../shared/base.service';
import {TableQuery} from '../shared/TabelQuery';

@Injectable()
export class PrvRole extends BaseService {
	constructor(private http: HttpClient) {
		super();
	}

	find(start: number, max: number, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'role?start=' + start + '&max=' + max, JSON.stringify(tq));
	}

	findAll() {
		let start = 0;
		let max = -1;
		let tq = new TableQuery;
		tq.sortingInfos.push({attributeName: 'roleNama', direction: 'asc'});
		return this.find(start, max, tq);
	}

	exportData(start: number, max: number, tq: TableQuery) {
		return this.http.post(this.apiUrl + 'role/xls?start=' + start + '&max=' + max, JSON.stringify(tq), {
			responseType: 'blob'
		});
	}

	save(id: number, data: any) {
		return this.http.put(this.apiUrl + 'role/' + id, JSON.stringify(data));
	}

	deleteRecord(id, version) {
		return this.http.delete(this.apiUrl + 'role/' + id + '/' + version);
	}

	findById(id: number) {
		return this.http.get(this.apiUrl + 'role/' + id);
	}

}