export const section_header = document.querySelector('header');
export const section_footer = document.querySelector('footer');
export const section_form = document.querySelector('.section-form');
export const section_additional = document.querySelector('.section-additional');
export const country = document.getElementById('country');
export const name = document.getElementById('name');
export const address = document.getElementById('address');
export const product = document.getElementById('product');
export const color = document.getElementById('color');
export const size = document.getElementById('size');
export const quantity = document.getElementById('quantity');
export const payment = document.getElementById('payment');
export const delivery = document.getElementById('delivery');
export const phone = document.getElementById('phone');
export const email = document.getElementById('email');
export const status = document.getElementById('status');
export const time = document.getElementById('time');
export const response = document.getElementById('response');
export const submit = document.getElementById('submit');
export const submenu_complete = document.getElementById('submenu-complete');
export const submenu_clear = document.getElementById('submenu-clear');
export const submenu_test = document.getElementById('submenu-test');
export const submenu_test_file = document.getElementById('submenu-test-file');
export const footer_message = document.getElementById('footer__progress');
export const effect_overlay = document.getElementById('effect-overlay');
export const modal_message = document.querySelector('#modal__message');
export const modal_status_success = document.querySelector('#modal__success');
export const modal_status_success_id = document.querySelector('#modal__success--number');
export const modal_results_failure_input = document.querySelector('#modal__failureInput');
export const modal_results_failure_input_id = document.querySelector('#modal__failureInput--number');
export const modal_results_failure_output = document.querySelector('#modal__failureOutput');
export const modal_results_failure_output_id = document.querySelector('#modal__failureOutput--number');
export const execution_details = document.getElementById('execution-details');

//------------------------------------------------

export const color_red = document.createElement('option');
color_red.setAttribute('value', 'red');
color_red.appendChild(document.createTextNode('red'));

export const color_green = document.createElement('option');
color_green.setAttribute('value', 'green');
color_green.appendChild(document.createTextNode('green'));

export const color_blue = document.createElement('option');
color_blue.setAttribute('value', 'blue');
color_blue.appendChild(document.createTextNode('blue'));

export const payment_cash_on_delivery = document.createElement('option');
payment_cash_on_delivery.setAttribute('value', 'cash_on_delivery');
payment_cash_on_delivery.appendChild(document.createTextNode('cash on delivery'));

export const delivery_express = document.createElement('option');
delivery_express.setAttribute('value', 'express');
delivery_express.appendChild(document.createTextNode('express'));