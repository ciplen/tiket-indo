
import {Observable} from 'rxjs';
import {_throw as observableThrowError} from 'rxjs/observable/throw';

import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Config} from '../providers/env.config';

export class BaseService {
	apiUrl = Config.API;
	protected extractData(res: Response) {
		let body: any = res.json();
		return body.data || {};
	}

	protected handleError(error: Response | any) {
		return observableThrowError(error || 'Server error');
	}

	getApiUrl() {
		return this.apiUrl;
	}
}