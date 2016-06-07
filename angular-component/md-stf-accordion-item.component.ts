export class NpStfAccordionItemComponent
{
    public template: string = require("./np-stf-accordion-item.html");
    public controller: any = NpStfAccordionItemController;
    public transclude: any = {
        header: "mdStfAccordionItemHeader",
        body:   "mdStfAccordionItemBody",
    };

    public bindings: Object = {
        opened: '<',
    };
}

export class NpStfAccordionItemController{
    public opened: boolean = false;

    constructor(){

    }

    openClose(){
        this.opened = !this.opened;
    }
}