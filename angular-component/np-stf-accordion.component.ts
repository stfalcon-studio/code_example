export class NpStfAccordionComponent implements ng.IComponentOptions
{
    public template: string = require("./np-stf-accordion.html");
    public controller: any = NpStfAccordionController;
    public transclude: boolean = true; 
}

export class NpStfAccordionController{
    constructor(){

    }
}

