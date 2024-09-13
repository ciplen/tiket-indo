export enum Type {INFO, CONFIRM, ERROR}

export interface MessageDialogObject {
	title: string;
	content: string;
	type: Type;
	yesLabel?: string;
	noLabel?: string;
	cancelLabel?: string;
	onClose?: any;
}