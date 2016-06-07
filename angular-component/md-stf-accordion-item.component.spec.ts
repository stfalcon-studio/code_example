import "angular";
import "angular-mocks";
import {ComponentTest} from "../../../../util/ComponentTest";
import 'phantomjs-polyfill';
import {NpStfAccordionItemController, NpStfAccordionItemComponent} from "./md-stf-accordion-item.component";

angular.module('app.application', [])
    .component("mdStfAccordionItem", new NpStfAccordionItemComponent())
;

describe('Component NpStfAccordionItemComponent', () => {
    var directiveTest: ComponentTest<NpStfAccordionItemController>;
    
    beforeEach(angular.mock.module('app.application'));
    
    beforeEach(() => {
        directiveTest = new ComponentTest<NpStfAccordionItemController>(
            `<md-stf-accordion-item class="md-stf-accordion__item">
                  <md-stf-accordion-item-header>
                      <div>Trunsclude</div>
                  </md-stf-accordion-item-header>
                  <md-stf-accordion-item-body>
                        <div>Trunsclude</div>
                  </md-stf-accordion-item-body>
              </md-stf-accordion-item>`, 
            "mdStfAccordionItem"
        );
    });
    
    it('should change class on click', () => {
        let vm: NpStfAccordionItemController = directiveTest.createComponent({});
        let accordItemInner = directiveTest.element.find('>div');
        let accordItemTrigger = directiveTest.element.find('.md-stf-accordion__header');
        
        expect(accordItemInner.hasClass('md-stf-accordion__item_active')).toBeFalsy();
        
        accordItemTrigger.triggerHandler('click');
        expect(accordItemInner.hasClass('md-stf-accordion__item_active')).toBeTruthy();
    });
});