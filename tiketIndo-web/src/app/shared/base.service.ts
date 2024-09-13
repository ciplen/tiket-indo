
import {throwError as observableThrowError,  Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Config } from '../shared/index';

export class BaseService {
	apiUrl = Config.API;
	
	protected handleError(error: Response | any) {
		return observableThrowError(error || 'Server error');
	}

	getApiUrl() {
		return this.apiUrl;
	}
}