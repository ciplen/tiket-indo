import { Component, Input, AfterViewInit, ElementRef, Output, EventEmitter, ViewChild } from '@angular/core';

export type AlertType = 'success' | 'info' | 'warning' | 'danger';

declare var jQuery: any;

export class Alert {
	message: string;
	type: AlertType;

	constructor(message: string, mtype: AlertType) {
		this.message = message;
		this.type = mtype;
	}
}

@Component({
	selector: 'b-alert',
	template: `
<div #bAlert [class]="'alert alert-'+message.type" role="alert" style="position: fixed;
    top: 50px;
    right: 0px;">
  <button type="button" class="close" aria-label="Close" (click)="close()">&nbsp;&nbsp;&nbsp;<span aria-hidden="true">&times;</span></button>
  {{message.message}}
</div>`,
})

/**
 * Bootstrap alert component. Possible value of type is success, info, warning and danger
 */
export class AlertComponent {
	@ViewChild('bAlert') bAlert: any;
	@Input() message: Alert;
	@Output() closed: EventEmitter<any> = new EventEmitter();
	constructor() { }

	close() {
		this.closed.emit('closed');
	}
}