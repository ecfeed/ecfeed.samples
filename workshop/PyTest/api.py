import sys
sys.path.append('/home/krzysztof/Desktop/git/ecfeed.python')
from ecfeedX import TestProvider
import pytest
import time
import requests

# ---------------------------------------------------------

configuration = {
    "mode": "error",
    "endpoint": "https://api.ecfeed.com/"
}

ecfeed = TestProvider(model='6EG2-YL4S-LMAK-Y5VW-VPV9')
method = 'com.example.test.Demo.typeString'

# ---------------------------------------------------------

def send_request(Country, Name, Address, Product, Color, Size, Quantity, Payment, Delivery, Phone, Email):

    parameters = {
        "mode": configuration["mode"],
        "country": Country,
        "name": Name,
        "address": Address,
        "product": Product,
        "color": Color,
        "size": Size,
        "quantity": Quantity,
        "payment": Payment,
        "delivery": Delivery,
        "phone": Phone,
        "email": Email
    }

    return requests.post(configuration["endpoint"], params=parameters)

def parse_to_dictionary(array):
    index = 1
    results = {}
    
    for x in array:
        results[str(index)] = x
        index += 1

    return results

def process_request(Country, Name, Address, Product, Color, Size, Quantity, Payment, Delivery, Phone, Email):
    
    timeStart = time.time()
    response = send_request(Country, Name, Address, Product, Color, Size, Quantity, Payment, Delivery, Phone, Email)
    timeEnd = time.time()
    
    return {
        "response": response,
        "time": int(1000 * (timeEnd - timeStart)),
        "error": {
            "input": parse_to_dictionary(response.json()['errorInput']),
            "output": parse_to_dictionary(response.json()['errorOutput'])
        }
    }

def process_response(response, test_handle):

    assert len(response["error"]["input"]) == 0, test_handle.add_feedback(
        False, duration=response["time"], comment="Input", custom=response["error"]["input"])
    
    assert len(response["error"]["output"]) == 0, test_handle.add_feedback(
        False, duration=response["time"], comment="Output", custom=response["error"]["output"])
    
    test_handle.add_feedback(True, duration=response["time"])

# ---------------------------------------------------------

@pytest.mark.parametrize(ecfeed.test_header(method, feedback=True), ecfeed.nwise(method=method, feedback=True, constraints="ALL", label="API"))
def test_method_nwise(Country, Name, Address, Product, Color, Size, Quantity, Payment, Delivery, Phone, Email, test_handle):

    response = process_request(Country, Name, Address, Product, Color, Size, Quantity, Payment, Delivery, Phone, Email)
    process_response(response, test_handle)

    