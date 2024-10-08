import { Injectable } from '@angular/core';
import {
	CanActivate, Router,
	ActivatedRouteSnapshot,
	RouterStateSnapshot,
	CanActivateChild,
	NavigationExtras,
	CanLoad, Route
} from '@angular/router';
import { UserService } from '../shared/user.service';
@Injectable()
export class AuthGuard implements CanActivate, CanActivateChild, CanLoad {
	constructor(private router: Router, private authService: UserService) {
	}

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
		let url: string = state.url;

		return this.checkLogin(url);
	}

	canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
		return this.canActivate(route, state);
	}

	canLoad(route: Route): boolean {
		let url = `/${route.path}`;

		return this.checkLogin(url);
	}



	checkLogin(url: string): boolean {
		if (this.authService.isLoggedIn()) { return true; }
		this.authService.redirectUrl = url;
		this.router.navigate(['/login', { origin: encodeURIComponent(url) }]);
		return false;
	}
}


/*
Copyright 2016 Google Inc. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at http://angular.io/license
*/