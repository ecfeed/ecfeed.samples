/// <reference types="Cypress" />

import source from "../fixtures/source.csv";

describe('EcFeed', () => {

  source.forEach((element, index) => {

    it(`should process an order ${index}`,  () => {
      cy.visit('/');

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

      cy.get('#response').contains('Order processed without errors!')
    });
  });
});