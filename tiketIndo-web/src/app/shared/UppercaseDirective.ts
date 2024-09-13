import {Directive, Input, Output, EventEmitter} from '@angular/core';

@Directive({
	selector: '[ngModel][uppercase]',
	host: {
		"(input)": 'onInputChange($event)'
	}
})
export class UppercaseDirective {
	@Output() ngModelChange: EventEmitter<any> = new EventEmitter();
	@Input('uppercase') enable = true;
	value: any;

	onInputChange($event) {
		if (this.enable) {
			this.value = $event.target.value.toUpperCase();
			this.ngModelChange.emit(this.value);
		}
	}
}