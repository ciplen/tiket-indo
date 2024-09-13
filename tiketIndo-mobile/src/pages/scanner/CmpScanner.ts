import {Component, OnInit} from '@angular/core';
import {Platform, AlertController} from 'ionic-angular';
import {BarcodeScanner} from '@ionic-native/barcode-scanner';
import {JwtHelperService} from '@auth0/angular-jwt';
import {PrvTicket} from '../../providers/PrvTicket';

const helper = new JwtHelperService();


@Component({
	selector: 'cmp-scanner',
	templateUrl: 'CmpScanner.html'
})

export class CmpScanner implements OnInit {
	barcodeText = '-';
	decodedToken = '';
	gateKeeper: boolean = false;
	authInfo: any;
	data: any;
	isScanning = true;
	constructor(
		public platform: Platform,
		private barcodeScanner: BarcodeScanner,
		private alertCtrl: AlertController,
		private prvTicket: PrvTicket
	) {
		this.authInfo = JSON.parse(localStorage.getItem('auth_info'));
		this.gateKeeper = this.authInfo.role == 'gate_keeper' || this.authInfo.role=='supertester';
	}
	ngOnInit() {
            this.scan();
        }

	decode() {
		this.decodedToken = helper.decodeToken(this.barcodeText);
	}

	scan() {
		// alert('Scan');
		this.isScanning = false;
		if (!this.platform.is('cordova')) {
			alert('Not Cordova');
			return;
		}
		this.barcodeScanner.scan().then(barcodeData => {
			//console.log('Barcode data', barcodeData);
			console.log('Barcode Data: ' + JSON.stringify(barcodeData));
			this.barcodeText = barcodeData.text;
			this.checkIn(barcodeData.text);
			this.decode();
		}).catch(err => {
			let alert = this.alertCtrl.create({
				title: 'Gagal',
				message: JSON.stringify(err),
				buttons: [
					{
						text: 'Ok'
					}
				],
			});
			alert.present();
		});
	}

	checkIn(ticketToken) {
		let alert;
		this.prvTicket.checkIn(ticketToken).subscribe(
			(result: any) => {
				this.data = result;
			},
			err => {
				if (err['status'] == 0) {
					alert = this.alertCtrl.create({
						title: 'Gagal',
						message: 'Silahkan ke pusat informasi.',
						buttons: [
							{
								text: 'Ok'
							}
						],
					});
					alert.present();
				} else if (err['status'] == 400) {
					if (err.error) {
						alert = this.alertCtrl.create({
							title: 'Gagal',
							message: err.error.message,
							buttons: [
								{
									text: 'Ok'
								}
							],
						});
						alert.present();
						//						alert(err.error.message);
					} else {
						alert = this.alertCtrl.create({
							title: 'Gagal',
							message: JSON.stringify(err),
							buttons: [
								{
									text: 'Ok'
								}
							],
						});
						alert.present();
						//						alert(JSON.stringify(err));
					}
				} else {
					alert = this.alertCtrl.create({
						title: 'Gagal',
						message: JSON.stringify(err),
						buttons: [
							{
								text: 'Ok'
							}
						],
					});
					alert.present();
					//					alert(JSON.stringify(err));
				}
				this.data = null;
				this.isScanning = true;
			}
		);
	}
	verify(id, status) {
		let alert;
		this.prvTicket.verify(id, status).subscribe(
			(result: any) => {
				if (status == 'VERIFIED') {
					alert = this.alertCtrl.create({
						title: 'Verified',
						message: 'Silahkan masuk.',
						buttons: [
							{
								text: 'Ok'
							}
						],
					});
					alert.present();
				}
			},
			err => {
				alert = this.alertCtrl.create({
					title: 'Gagal',
					message: JSON.stringify(err) + '. Silahkan ke pusat informasi.',
					buttons: [
						{
							text: 'Ok'
						}
					],
				});
				alert.present();
			}
		)
		console.log(id);
		this.decodedToken = null;
		this.data = null;
		this.isScanning = true;
	}
}