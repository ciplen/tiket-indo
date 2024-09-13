import { Component, Input, AfterViewInit, OnInit, forwardRef, ElementRef } from '@angular/core';
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from '@angular/forms';

export const TYPEAHEAD_VALUE_ACCESSOR: any = {
	provide: NG_VALUE_ACCESSOR,
	useExisting: forwardRef(() => CmpTypeahead),
	multi: true
};

declare var jQuery: any;

@Component({
	moduleId: module.id,
	selector: 'p-typeahead',
	template: `<ng-content></ng-content>`,
	providers: [TYPEAHEAD_VALUE_ACCESSOR],
	styleUrls: ['typeahead.scss']
})

export class CmpTypeahead implements OnInit, AfterViewInit, ControlValueAccessor {
	private objRef: any;
	value: string;
	onModelChange: Function = () => { };
	onModelTouched: Function = () => { };
	@Input() disabled: any;
	@Input() placeholder = '';
	@Input() allowClear = true;
	selectElm: any;
	initItemsCount = 0;

	constructor(private elRef: ElementRef) {
	}

	ngOnInit() {
		this.selectElm = this.elRef.nativeElement.getElementsByTagName('select')[0];
		this.initItemsCount = this.selectElm.options.length;
		this.objRef = jQuery(this.selectElm);
		this.objRef.select2({
			placeholder: this.placeholder,
			allowClear: this.allowClear
		});
	}

	ngAfterViewInit() {
		this.objRef.on("select2:select", (e) => {
			this.value = this.objRef.val();
			this.onModelChange(this.value);
			this.selectElm.focus();
		});

		this.objRef.on("select2:unselect", (e) => {
			this.value = null;
			this.onModelChange(this.value);
			this.selectElm.focus();
		});

	}

	writeValue(value: any): void {
		if (value === undefined) {
			value = null;
		}
		this.value = value;
		if (this.objRef[0].multiple && this.value) {
			let arr = value.split(",");
			this.objRef.val(arr).trigger("change");
		} else {
			this.objRef.val(value).trigger("change");
		}
	}

	ngAfterViewChecked() {
		if (this.selectElm && this.selectElm.options.length != this.initItemsCount) {
			/**
			 * trigger change in writeValue() doesn't work if the items (the option tags) in 
			 * <select> haven't been rendered. This trigger is to fix this problem
			 */
			this.initItemsCount = this.selectElm.options.length;
			this.value = this.value + '';
			if (this.value && this.value.indexOf(',')) {
				let arr = this.value.split(",");
				this.objRef.val(arr).trigger("change");
			} else {
				this.objRef.val(this.value).trigger("change");
			}
		}
	}

	registerOnChange(fn: Function): void {
		this.onModelChange = fn;
	}

	registerOnTouched(fn: Function): void {
		this.onModelTouched = fn;
	}

	setDisabledState(val: boolean): void {
		this.disabled = val;
		this.objRef.prop("disabled", val);
	}

}