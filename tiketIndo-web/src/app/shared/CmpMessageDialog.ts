import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { MessageDialogObject } from './MessageDialogObject';

@Component({
	selector: 'cmp-message-dialog',
	templateUrl: './CmpMessageDialog.html',
	styleUrls: ['./CmpMessageDialog.scss']
})

export class CmpMessageDialog {

	constructor(public dialogRef: MatDialogRef<CmpMessageDialog>,
		@Inject(MAT_DIALOG_DATA) public data: MessageDialogObject) {

		if (data.noLabel || data.cancelLabel) {
			this.dialogRef.disableClose = true;
		}
	}
	close(answer) {
		this.dialogRef.close(answer);
	}
}