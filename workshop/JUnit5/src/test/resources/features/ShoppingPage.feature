Feature: EcFeed - Shopping Page

  Background:
    Given I access the web page

  Scenario Outline: Validate a test stream
    When I enter the product name <product>
    When I enter the product color <color>
    When I enter the product size <size>
    When I enter the product quantity <quantity>
    When I enter the shipment country <country>
    When I enter the name <name>
    When I enter the address <address>
    When I enter the payment method <payment>
    When I enter the delivery option <delivery>
    When I enter the phone number <phone>
    When I enter my email <email>
    When I place the order
    Then I should be presented with a successful submission message

    Examples:
    | country   | name              | address                                         | product   | color   | size   | quantity   | payment             | delivery   | phone          | email            |
    | other     | "Ola Nordmann"    | "Tollbodgata 138. 0484 Langtvekkistan"          | t-shirt   | blue    | M      | 3          | "MASTERCARD"        | "standard" | "+47123456789" | ola@nordmann.no  |
    | Norway    | "Max Mustermann"  | "Timmersloher Landstrasse 300. D-12129 Kuhdorf" | hoodie    | black   | XS     | 1          | "bank transfer"     | "express"  | "+47123456789" | anna@kowalska.pl |
    | Poland    | "John Doe"        | "Aleja Pysiowa 129. 81-384 Warszawa"            | hoodie    | white   | L      | 2          | "MASTERCARD"        | "standard" | "+48123456789" | anna@kowalska.pl |
    | Poland    | "Anna Kowalska"   | "Timmersloher Landstrasse 300. D-12129 Kuhdorf" | t-shirt   | blue    | XL     | 2          | "VISA"              | "express"  | "+48123456789" | ola@nordmann.no  |
    | Norway    | "Anna Kowalska"   | "Tollbodgata 138. 0484 Langtvekkistan"          | t-shirt   | green   | S      | 1          | "cash on delivery"  | "standard" | "+47123456789" | anna@kowalska.pl |
    | other     | "Ola Nordmann"    | "Aleja Pysiowa 129. 81-384 Warszawa"            | t-shirt   | red     | XS     | 2          | "bank transfer"     | "standard" | "+48123456789" | anna@kowalska.pl |
    | Norway    | "Anna Kowalska"   | "Aleja Pysiowa 129. 81-384 Warszawa"            | hoodie    | white   | XS     | 3          | "cash on delivery"  | "express"  | "+47123456789" | ola@nordmann.no  |
    | Norway    | "John Doe"        | "Tollbodgata 138. 0484 Langtvekkistan"          | t-shirt   | red     | L      | 2          | "VISA"              | "express"  | "+47123456789" | ola@nordmann.no  |
    | other     | "Max Mustermann"  | "Tollbodgata 138. 0484 Langtvekkistan"          | hoodie    | black   | S      | 3          | "VISA"              | "standard" | "+48123456789" | ola@nordmann.no  |
    | Norway    | "Ola Nordmann"    | "Timmersloher Landstrasse 300. D-12129 Kuhdorf" | hoodie    | white   | XL     | 1          | "MASTERCARD"        | "standard" | "+47123456789" | anna@kowalska.pl |
    | Poland    | "Anna Kowalska"   | "Aleja Pysiowa 129. 81-384 Warszawa"            | hoodie    | black   | M      | 1          | "VISA"              | "express"  | "+48123456789" | anna@kowalska.pl |
    | Poland    | "Max Mustermann"  | "Aleja Pysiowa 129. 81-384 Warszawa"            | t-shirt   | green   | S      | 2          | "MASTERCARD"        | "express"  | "+48123456789" | ola@nordmann.no  |
    | other     | "John Doe"        | "Timmersloher Landstrasse 300. D-12129 Kuhdorf" | t-shirt   | green   | XL     | 3          | "bank transfer"     | "standard" | "+48123456789" | ola@nordmann.no  |
    | Poland    | "Ola Nordmann"    | "Timmersloher Landstrasse 300. D-12129 Kuhdorf" | t-shirt   | red     | L      | 3          | "cash on delivery"  | "standard" | "+48123456789" | anna@kowalska.pl |
    | Norway    | "John Doe"        | "Timmersloher Landstrasse 300. D-12129 Kuhdorf" | t-shirt   | blue    | S      | 1          | "bank transfer"     | "standard" | "+47123456789" | ola@nordmann.no  |
    | Poland    | "John Doe"        | "Tollbodgata 138. 0484 Langtvekkistan"          | t-shirt   | white   | M      | 2          | "bank transfer"     | "express"  | "+48123456789" | anna@kowalska.pl |
    | Norway    | "Max Mustermann"  | "Tollbodgata 138. 0484 Langtvekkistan"          | t-shirt   | black   | XL     | 2          | "cash on delivery"  | "standard" | "+47123456789" | anna@kowalska.pl |
    | other     | "Anna Kowalska"   | "Aleja Pysiowa 129. 81-384 Warszawa"            | t-shirt   | blue    | L      | 1          | "bank transfer"     | "standard" | "+48123456789" | anna@kowalska.pl |
    | Poland    | "Ola Nordmann"    | "Tollbodgata 138. 0484 Langtvekkistan"          | t-shirt   | green   | XS     | 1          | "VISA"              | "express"  | "+48123456789" | ola@nordmann.no  |
    | Norway    | "Max Mustermann"  | "Timmersloher Landstrasse 300. D-12129 Kuhdorf" | t-shirt   | red     | M      | 1          | "MASTERCARD"        | "express"  | "+47123456789" | anna@kowalska.pl |
    | other     | "Max Mustermann"  | "Timmersloher Landstrasse 300. D-12129 Kuhdorf" | t-shirt   | white   | S      | 1          | "VISA"              | "standard" | "+47123456789" | ola@nordmann.no  |
    | Norway    | "John Doe"        | "Aleja Pysiowa 129. 81-384 Warszawa"            | hoodie    | black   | XS     | 3          | "MASTERCARD"        | "express"  | "+47123456789" | ola@nordmann.no  |
    | Poland    | "Anna Kowalska"   | "Tollbodgata 138. 0484 Langtvekkistan"          | t-shirt   | red     | S      | 2          | "MASTERCARD"        | "express"  | "+48123456789" | ola@nordmann.no  |
    | Poland    | "John Doe"        | "Tollbodgata 138. 0484 Langtvekkistan"          | t-shirt   | blue    | M      | 1          | "cash on delivery"  | "standard" | "+48123456789" | ola@nordmann.no  |
    | Norway    | "Max Mustermann"  | "Aleja Pysiowa 129. 81-384 Warszawa"            | t-shirt   | green   | L      | 2          | "VISA"              | "express"  | "+47123456789" | ola@nordmann.no  |
    | Norway    | "Anna Kowalska"   | "Aleja Pysiowa 129. 81-384 Warszawa"            | t-shirt   | red     | XL     | 3          | "cash on delivery"  | "express"  | "+47123456789" | ola@nordmann.no  |
    | other     | "Ola Nordmann"    | "Tollbodgata 138. 0484 Langtvekkistan"          | t-shirt   | black   | S      | 3          | "VISA"              | "standard" | "+48123456789" | anna@kowalska.pl |
    | Norway    | "Max Mustermann"  | "Timmersloher Landstrasse 300. D-12129 Kuhdorf" | t-shirt   | blue    | XS     | 1          | "cash on delivery"  | "standard" | "+47123456789" | anna@kowalska.pl |
    | Poland    | "Ola Nordmann"    | "Tollbodgata 138. 0484 Langtvekkistan"          | t-shirt   | green   | M      | 1          | "bank transfer"     | "express"  | "+48123456789" | ola@nordmann.no  |
    | other     | "John Doe"        | "Timmersloher Landstrasse 300. D-12129 Kuhdorf" | t-shirt   | black   | L      | 3          | "bank transfer"     | "standard" | "+48123456789" | anna@kowalska.pl |