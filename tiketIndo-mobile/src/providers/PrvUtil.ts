//import {Injectable} from '@angular/core';
//import {TranslateService} from '@ngx-translate/core';
//import {AlertController, Platform} from 'ionic-angular';
//import {Tag} from '../models/Tag';
//import {Member} from '../models/Member';
//import {Contacts, ContactFindOptions} from '@ionic-native/contacts';
//import {UserTeam} from '../models/UserTeam';
//import {PhotoViewer} from '@ionic-native/photo-viewer';
//import * as dl from 'cordova-plugin-android-downloadmanager';
//export const LOC_STOR_CONTACTS = 'contacts';
//export const CUSTOMER_SERVICE = '6281226536747';//don't use + here
//import {File} from '@ionic-native/file';
//
//@Injectable()
//export class PrvUtil {
//	homeNeedRefresh = false;
//	appStoreInitialized = false;
//	silverPrice: string;
//	goldPrice: string;
//	goToSubscriptionCallback: Function = null;
//	desktopMode = false;
//	bannerAds:any;
//	constructor(private alertCtrl: AlertController,
//		private contacts: Contacts,
//		private platform: Platform,
//		private photoViewer: PhotoViewer,
//		private translate: TranslateService) {}
//
//	public getTodaysDateString(): string {
//		let today = new Date();
//		let dd = today.getDate();
//		let mm = today.getMonth() + 1; //January is 0!
//		let yyyy = today.getFullYear();
//		let stringDay = dd + '';
//		let stringMonth = mm + '';
//		if (dd < 10) {
//			stringDay = '0' + dd;
//		}
//		if (mm < 10) {
//			stringMonth = '0' + mm;
//		}
//		let todayString = yyyy + '-' + stringMonth + '-' + stringDay;
//		return todayString
//	}
//
//	public handleError(err) {
//		console.error(`Backend returned error ${JSON.stringify(err)}`);
//		let subTitle = '';
//		let title = err.status;
//		let body = null;
//		if (err.error) {
//			//this is error from http
//			body = err.error;
//			if (body instanceof Blob) {
//				let reader = new FileReader();
//				reader.addEventListener("loadend", (e) => {
//					err.error = JSON.parse(reader.result + '');
//					this.handleError(err);
//				});
//				reader.readAsText(body);
//				return;
//			}
//		} else if (err.body) {
//			//this is error from ionic filetransfer
//			body = JSON.parse(err.body);
//			title = err.http_status;
//		}
//
//
//		if (body && body.code) {
//			title = body.code;
//
//			let paramObject = {};
//			if (body.parameters) {
//				let params: any[] = body.parameters;
//				params.forEach((val, idx) => paramObject['par' + idx] = val);
//			}
//
//			subTitle = this.translate.instant(body.code, paramObject);
//			if (subTitle == body.code) {
//				subTitle = body.message;
//			}
//		} else if (err.status == 401) {
//			subTitle = this.translate.instant('not.authorized');
//		} else if (err.status == 0 || err.statusText == 'Unknown Error') {
//			subTitle = this.translate.instant('please.ensure.internet.connection');
//		} else {
//			if (isNaN(err)) {
//				subTitle = JSON.stringify(err);
//			} else {
//				title = JSON.stringify(err);
//				subTitle = this.translate.instant('native.plugin.error');
//			}
//		}
//
//		if (title === 'ER0033'
//			|| title === 'ER0038'
//			|| title === 'ER0039'
//			|| title === 'ER0047'
//			|| title === 'ER0048'
//		) {
//			this.showSubscriptionLimitationError(title, subTitle);
//		} else {
//			const alert = this.alertCtrl.create({
//				title: 'Error ' + title,
//				subTitle: subTitle,
//				buttons: [{text: this.translate.instant('ok'),
//					handler: () => {
//						if (err.status == 401) {
//							localStorage.removeItem('auth_token');
//							location.reload();
//						}
//						}}]
//			});
//			alert.present();
//		}
//
//	}
//
//	public showSubscriptionLimitationError(title: string, subTitle: string) {
//		const alert = this.alertCtrl.create({
//			title: 'Error ' + title,
//			subTitle: subTitle,
//			buttons: [
//				{
//					text: this.translate.instant('subscription'),
//					handler: () => {
//						this.goToSubscriptionPage();
//					}
//				},
//				{
//					text: this.translate.instant('cancel'),
//					role: 'cancel',
//				}]
//		});
//		alert.present();
//	}
//
//	public setGoToSubscriptionCallback(f:Function) {
//		this.goToSubscriptionCallback = f;
//	}
//
//	private goToSubscriptionPage() {
//		if (this.goToSubscriptionCallback != null) {
//			this.goToSubscriptionCallback();
//		}
//	}
//
//	public getLocal() {
//		let supportedLocal = ['en', 'id', 'fr'];
//		let local = this.translate.getBrowserLang();
//		if (supportedLocal.indexOf(local) < 0) {
//			local = 'en';
//		}
//		console.log(`browser language ${this.translate.getBrowserLang()}. using local ${local}`);
//		return local;
//	}
//
//	public defineTagObjects(userTeam, rows: {tags: any[], tagObjects: Tag[]}[]) {
//		rows.forEach(row => {
//			row.tagObjects = [];
//			if (row.tags) {
//				row.tags.forEach(aTag => {
//					let tagObject = userTeam.tags.find(o => o.text === aTag);
//					if (tagObject) {
//						row.tagObjects.push(tagObject);
//					} else {
//						let dummyTag = new Tag;
//						dummyTag.color = 0;
//						dummyTag.text = aTag;
//						row.tagObjects.push(dummyTag);
//					}
//				});
//			}
//		});
//	}
//
//	defineCoActorName(userTeam: UserTeam, rows: {coActorId: number, coActorName}[]) {
//		rows.forEach(row => {
//			if (row.coActorId) {
//				let found = false;
//				for (let i = 0; i < userTeam.members.length; i++) {
//					let m = userTeam.members[i];
//					if (row.coActorId == m.userid) {
//						row.coActorName = m.name;
//						found = true;
//						break;
//					}
//				}
//				if (!found) {
//					row.coActorName = `?[ID ${row.coActorId}]`;
//				}
//			}
//		});
//	}
//
//	defineUserNameFromContact(userTeam: UserTeam, rows: {userInfoId: number, username}[]) {
//		rows.forEach(row => {
//			if (row.userInfoId) {
//				let found = false;
//				for (let i = 0; i < userTeam.members.length; i++) {
//					let m = userTeam.members[i];
//					if (row.userInfoId == m.userid) {
//						row.username = m.name;
//						found = true;
//						break;
//					}
//				}
//				if (!found) {
//					row.username = `?[ID ${row.userInfoId}]`;
//				}
//			}
//		});
//	}
//
//	public showImage(pictPath: string, evt: Event) {
//		if (!pictPath) {
//			evt.stopPropagation();
//			return;
//		}
//		this.photoViewer.show(pictPath, '', {share: true});
//		evt.stopPropagation();
//	}
//	
//	public downloadImage(pictPath: string, file: File){
//		let fileName = pictPath.substr(pictPath.lastIndexOf('/') + 1,pictPath.length);
//		let uppercaseFileName = fileName.toUpperCase();
//		let mimeType = 'image/jpeg';
//		if (uppercaseFileName.endsWith('PNG')) {
//			mimeType = 'image/png';
//		} else if (uppercaseFileName.endsWith('GIF')) {
//			mimeType = 'image/gif';
//		} else if (uppercaseFileName.endsWith('BMP')) {
//			mimeType = 'image/bmp'; 
//		} else if (uppercaseFileName.endsWith('WEBP')) {
//			mimeType = 'image/webp';
//		}
//
//		let req = {
//			uri: pictPath,
//			title: fileName,
//			description: 'TeamMoney',
//			mimeType: mimeType,
//
//			visibleInDownloadsUi: true,
//			notificationVisibility: 1,
//
//			destinationUri: file.externalRootDirectory + '/Download/' + fileName,
//			headers: [{header: 'Authorization', value: `Bearer ${localStorage.getItem('auth_token')}`}]
//		};
//		dl.enqueue(req, console.info);
//	}
//
//	public getTrxImage(category: string) {
//		if (category === 'CAPITAL') {
//			return 'assets/imgs/capital.png';
//		} else if (category === 'EXPENSE') {
//			return 'assets/imgs/expense.png';
//		} else if (category === 'TRANSFER_IN') {
//			return 'assets/imgs/transfer_in.png';
//		}
//		return 'assets/imgs/transfer_out.png';
//	}
//
//	public getTrxImageSvg(category: string) {
//		if (category === 'CAPITAL') {
//			return 'assets/imgs/capital.svg';
//		} else if (category === 'EXPENSE') {
//			return 'assets/imgs/expense.svg';
//		} else if (category === 'TRANSFER_IN') {
//			return 'assets/imgs/transfer_in.svg';
//		}
//		return 'assets/imgs/transfer_out.svg';
//	}
//
//	resolveMemberNameInContact(members: Member[], countryCode: string) {
//		if (this.platform.is('cordova')) {
//			this.doResolve(members, countryCode);
//		}
//	}
//
//	trimNumberTo2FractionDecimal(v: string): string {
//		var array = v.split(".");
//		if (array.length < 2) return v;
//		array.push(array.pop().substring(0, 2));
//		var trimmedNumber = array.join(".");
//		return trimmedNumber;
//	}
//
//	private readContactsPhone(countryCode: string, onFinish: Function) {
//		console.log('reading contact');
//		var options = new ContactFindOptions();
//		options.filter = "";
//		options.multiple = true;
//		options.desiredFields = ['displayName', 'phoneNumbers'];
//		options.hasPhoneNumber = true;
//		this.contacts.find(['displayName', 'phoneNumbers'], options).then(
//			data => {
//				console.log('found contacts: ' + data.length);
//				let contactArray = [];
//				for (let item of data) {
//					let phonesOnly = item.phoneNumbers.map(val => val.value.replace(/\D/g, ''));
//
//					phonesOnly = phonesOnly.filter((value, index, self) => self.indexOf(value) === index);//unique
//					phonesOnly = phonesOnly.map(value => {
//
//						let strippedPhone = value.replace(/\D/g, '');
//						if (strippedPhone.startsWith('0')) {
//							strippedPhone = countryCode + strippedPhone.substring(1, strippedPhone.length);
//						} else {
//							strippedPhone = '+' + strippedPhone;
//						}
//						return strippedPhone;
//
//					});
//					phonesOnly.unshift(item.displayName);
//					contactArray.push(phonesOnly);
//				}
//				data = [];//free memory
//				localStorage.setItem(LOC_STOR_CONTACTS, JSON.stringify(contactArray));
//				if (onFinish) onFinish();
//			},
//			error => {
//
//				this.handleError(error);
//			}
//		);
//	}
//
//	translateContactName(members: Member[]) {
//		let contactArray: any[] = JSON.parse(localStorage.getItem(LOC_STOR_CONTACTS));
//		members.forEach(m => {
//			for (let i = 0; i < contactArray.length; i++) {
//				let aContact: any[] = contactArray[i];
//				if (aContact.indexOf(m.phone) > -1) {
//					m.name = aContact[0];
//				}
//			}
//		});
//	}
//
//	doResolve(members: Member[], countryCode: string) {
//		if (!localStorage.getItem(LOC_STOR_CONTACTS)) {
//			this.readContactsPhone(countryCode, () => this.translateContactName(members));
//		} else {
//			this.translateContactName(members);
//		}
//	}
//
//	getContactName(phoneNumber: string): string {
//		let contactArray: any[] = JSON.parse(localStorage.getItem(LOC_STOR_CONTACTS));
//		if (contactArray != null && contactArray.length > 0) {
//			for (let i = 0; i < contactArray.length; i++) {
//				let aContact: any[] = contactArray[i];
//				if (aContact.indexOf(phoneNumber) > -1) {
//					return aContact[0];
//				}
//			}
//		}
//		return null;
//	}
//
//	getPlatform() {
//		return this.platform.platforms();
//	}
//
//	isDesktopMode() {
//		return this.platform.is('core');
//	}
//
//	getMonthDiff(date: Date) {
//		let timeNow = new Date;
//		let diffYear = timeNow.getFullYear() - date.getFullYear();
//		let diffMonth = diffYear * 12 + timeNow.getMonth() - date.getMonth();
//		return diffMonth;
//	}
//
//	setBannerAds(banner) {
//		this.bannerAds = banner;
//	}
//
//	showAds() {
//		if (this.bannerAds) {
//			this.bannerAds['show']();
//		}
//	}
//
//	hideAds() {
//		if (this.bannerAds) {
//			this.bannerAds['hide']();
//		}
//	}
//}