import sys
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.support.select import Select
from ecfeed import TestProvider
import pytest
import time

# ---------------------------------------------------------

endpoint = "http://www.workshop-2021-december.ecfeed.com?mode=error"

form = {
    "execute": ["submit"],
    "output": ["status", "response"],
    "input": [["name", "address", "quantity", "phone", "email"], ["country", "product", "color", "size", "payment", "delivery"]]
}

ecfeed = TestProvider(model='6EG2-YL4S-LMAK-Y5VW-VPV9')
method = 'com.example.test.Demo.typeString'

driver = webdriver.Firefox(executable_path=r'C:\\Users\\kskor\\selenium\\geckodriver.exe')

# ---------------------------------------------------------

def set_form(values):
    validate_values(form["input"], values)

    set_form_text(values[0])
    set_form_combo(values[1])

def validate_values(reference, values):

    if len(values) != 2:
        raise Exception("The dimension of the input array is incorrect.")
        
    if len(reference[0]) != len(values[0]): 
        raise Exception("The number of the input text fields is incorrect.")
        
    if len(reference[1]) != len(values[1]):
        raise Exception("The number of the input select fields is incorrect.")

def set_form_text(values):
    for i in range(len(form["input"][0])):
        element = driver.find_element_by_id(form["input"][0][i])
        element.clear()
        element.send_keys(values[i])
        
def set_form_combo(values):
    for i in range(len(form["input"][1])):
        Select(driver.find_element_by_id(form["input"][1][i])).select_by_visible_text(values[i])

def execute():
    for element in form["execute"]:
        driver.find_element_by_id(element).click()

def get_response():
    status = driver.find_element_by_id(form["output"][0]).get_attribute("value")

    return parse_to_dictionary(driver.find_element_by_id(form["output"][1]).get_attribute("value").split("\n")) if "rejected" in status else {}
    
def parse_to_dictionary(array):
    index = 1
    results = {}
    
    for x in array:
        results[str(index)] = x[2:]
        index += 1

    return results
# ---------------------------------------------------------

def set_selenium_form(Country, Name, Address, Product, Color, Size, Quantity, Payment, Delivery, Phone, Email, test_handle):
    timeStart = time.time()

    try:
        set_form([[Name, Address, Quantity, Phone, Email], [Country, Product, Color, Size, Payment, Delivery]])
        execute()
    except NoSuchElementException as err:
        assert False, test_handle.add_feedback(False, comment="Input", custom={'1': err.msg})
    
    timeEnd = time.time()

    return {
        "response": get_response(),
        "time": int(1000 * (timeEnd - timeStart))
    }

def process_selenium_results(response, test_handle):
    
    assert len(response["response"]) == 0, test_handle.add_feedback(
        False, duration=response["time"], comment="Output", custom=response["response"])
    
    test_handle.add_feedback(True, duration=response["time"])

# ---------------------------------------------------------

@pytest.fixture(scope='session', autouse=True)
def setup():
    driver.get(endpoint) 
    yield driver
    driver.close()

@pytest.mark.parametrize(ecfeed.test_header(method, feedback=True), ecfeed.nwise(method=method, feedback=True, constraints="ALL", label="Selenium"))
def test_method_random(Country, Name, Address, Product, Color, Size, Quantity, Payment, Delivery, Phone, Email, test_handle):

    response = set_selenium_form(Country, Name, Address, Product, Color, Size, Quantity, Payment, Delivery, Phone, Email, test_handle)
    time.sleep(1)
    process_selenium_results(response, test_handle)

    
    