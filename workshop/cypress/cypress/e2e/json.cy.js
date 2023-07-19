const source = require("../source/source.json");

describe('ecFeed demo', () => {

  source.testCases.forEach((element, index) => {

    context(`Test case: ${index}.`, () => {

      it('tutorial',  () => {
        cy.visit('http://www.workshop-2021-december.ecfeed.com?mode=error');

        cy.get('#quantity').type(element.Quantity);
        cy.get('#name').type(element.Name);
        cy.get('#address').type(element.Address);
        cy.get('#phone').type(element.Phone);
        cy.get('#email').type(element.Email);

        cy.get('#product').select(element.Product);
        cy.get('#color').select(element.Color);
        cy.get('#size').select(element.Size);
        cy.get('#country').select(element.Country);
        cy.get('#payment').select(element.Payment);
        cy.get('#delivery').select(element.Delivery);

        cy.get('#submit').click();

        cy.get('#response').should('have.value', 'Order processed without errors! Please wait for the delivery...')
      })
    })
  })
})