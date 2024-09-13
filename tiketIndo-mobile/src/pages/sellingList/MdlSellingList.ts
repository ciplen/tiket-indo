import {NgModule} from '@angular/core';
import {IonicPageModule} from 'ionic-angular';
import {CmpSellingList} from './CmpSellingList';

@NgModule({
	declarations: [
		CmpSellingList
	],
	imports: [
		IonicPageModule.forChild(CmpSellingList),
	],
	exports: [
		CmpSellingList
	]
})

export class MdlSellingList {}