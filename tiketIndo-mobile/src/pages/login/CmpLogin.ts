import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AlertController, LoadingController, NavController, NavParams } from 'ionic-angular';
import { PrvAuth } from '../../providers/PrvAuth';

@Component({
	selector: 'page-login',
	templateUrl: 'CmpLogin.html',
})
export class CmpLogin {
	mode: string;
	loginForm: FormGroup;
	signupForm: FormGroup;
	confirmPass: any;
//	userData = {'username': '', 'password': ''}
	dataUser = {'namaLengkap': '', 'username': '', 'email': '', 'password': ''}
	user: any;
	
	username:string='';
	password:string='';

	public backgroundImage = 'assets/imgs/logo_black.png';
	constructor(public navCtrl: NavController, public navParams: NavParams,
		private formBuilder: FormBuilder,
		private prvAuth: PrvAuth,
		private alertCtrl: AlertController,
		public loadingCtrl: LoadingController) {
		this.mode = 'login';
		this.initForm();
	}

	initForm() {
		let EMAILPATTERN = /^[a-z0-9!#$%&'*+\/=?^_`{|}~.-]+@[a-z0-9]([a-z0-9-]*[a-z0-9])?(\.[a-z0-9]([a-z0-9-]*[a-z0-9])?)*$/i;
		//		let PASSPATTERN = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/;
		this.loginForm = this.formBuilder.group({

			username: [
				'', Validators.compose([
					Validators.required
				])
			],
			password: [
				'', Validators.compose([
					Validators.minLength(4),
					//					Validators.pattern(PASSPATTERN),
					Validators.required
				])
			]
		});
	}
	ionViewDidLoad() {
		console.log('ionViewDidLoad LoginPage');
	}

	login() {
		this.prvAuth.login(this.username, this.password).subscribe(
			(data: any) => {
				this.user = data;
				if (this.user) {
					localStorage.setItem('auth_info', JSON.stringify(this.user));
					localStorage.setItem('auth_token', this.user.jwt);
					location.reload();
				}
			},
			error => {
				console.log('error: ' + JSON.stringify(error));
				let alert = this.alertCtrl.create({
					title: 'Login Gagal',
					message: 'Username atau password salah',
					buttons: [
						{
							text: 'Dismiss',
							role: 'cancel',
						},
						{
							text: 'Ok'
						}
					],
					cssClass: 'alertCustomCss'
				});
				alert.present();
			}
		);
	}

	lupaPassword() {
		//		this.navCtrl.push(CmpLupaPassword);
	}
}
