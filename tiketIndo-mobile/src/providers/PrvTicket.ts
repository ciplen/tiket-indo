import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Config} from './env.config';
import {TableQuery} from '../shared/TabelQuery';

@Injectable()
export class PrvTicket {
	constructor(private http: HttpClient) {}

	findVenue(start: number, max: number, tq: TableQuery) {
		return this.http.post(Config.API + 'vm?start=' + start + '&max=' + max, JSON.stringify(tq));
	}
	findTicketByVenueId(id: number) {
		return this.http.get(Config.API + 'tm/venueId/' + id);
	}
	//	generate(lstDtl: any) {
	generate(payload: any) {
		return this.http.post(Config.API + 'td/generate', JSON.stringify(payload));
	}

	checkIn(ticketToken: string) {
		let payload = {ticketToken: ticketToken};
		return this.http.post(Config.API + 'td/checkin', JSON.stringify(payload));
	}

	verify(id: number, status: string) {
		return this.http.post(Config.API + 'td/verify/' + id + '/' + status, JSON.stringify(""));
	}

	countSellingList(venueId: number, resellerId: number) {
		return this.http.get(Config.API + 'td/countSellingList/' + venueId + "/" + resellerId);
	}
	countSellingListByKoorId(venueId: number, koor: string) {
		return this.http.get(Config.API + 'td/countSellingListKoorId/' + venueId + "/" + koor);
	}
}