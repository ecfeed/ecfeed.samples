using System;
using System.Threading;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Diagnostics;
using NUnit.Framework;
using OpenQA.Selenium;
using OpenQA.Selenium.Firefox;
using OpenQA.Selenium.Remote;
using OpenQA.Selenium.Support.UI;
using EcFeed;

namespace Workshop
{
    [TestFixture]
    public class SeleniumWorkshop
    {
        private static IEnumerable dataString = new TestProvider("6EG2-YL4S-LMAK-Y5VW-VPV9").GenerateNWise("com.example.test.Demo.typeString", feedback:true, constraints: "ALL", label:"Selenium");
        private static RemoteWebDriver seleniumDriver = new FirefoxDriver("/home/krzysztof") { Url = "http://www.workshop-2020-march.ecfeed.com?mode=error"};
        private static string[] pageFormControl = new string[] { "submit" };
        private static string[] pageFormOutput = new string[] { "status", "response" };
        private static string[][] pageFormInput = new string[2][] { new string[] {"name", "address", "quantity", "phone", "email"}, new string[] {"country", "product", "color", "size", "payment", "delivery"} };

        private static void SetForm(string[][] values) 
        {
            ValidateInput(values);

            SetFormText(values[0]);
            SetFormSelect(values[1]);
        }

        private static void ValidateInput(string[][] input) 
        {
            if (input.Length != 2) 
            {
                throw new ArgumentException("The dimension of the input array is incorrect.");
            }
            if (pageFormInput[0].Length != input[0].Length) 
            {
                throw new ArgumentException("The number of the input text fields is incorrect.");
            }
            if (pageFormInput[1].Length != input[1].Length) 
            {
                throw new ArgumentException("The number of the input select fields is incorrect.");
            }
        }

        private static void SetFormText(string[] values) 
        {
            for (int i = 0 ; i < pageFormInput[0].Length ; i++) 
            {
                IWebElement element = seleniumDriver.FindElementById(pageFormInput[0][i]);
                element.Clear();
                element.SendKeys(values[i]);
            }
        }

        private static void SetFormSelect(string[] values) 
        {
            for (int i = 0 ; i < pageFormInput[1].Length ; i++) 
            {
                (new SelectElement(seleniumDriver.FindElementById(pageFormInput[1][i]))).SelectByText(values[i]);
            }
        }

        private static void Execute() 
        {
            foreach (string element in pageFormControl) 
            {
                seleniumDriver.FindElementById(element).Click();
            }
        }

        private string[] GetResponse() 
        {
            string status = seleniumDriver.FindElementById(pageFormOutput[0]).GetAttribute("value");

            return status.Contains("rejected") ? seleniumDriver.FindElementById(pageFormOutput[1]).GetAttribute("value").Split("\n") : new string[0];
        }

        private void HandleInputError(string error, TestHandle ecfeed)
        {
            ecfeed.addFeedback(false, comment:"Input", custom:new Dictionary<string, string>{{"1", error}});
            Assert.Fail();
        }

        private void HandleOutputError(string[] error, int duration, TestHandle ecfeed)
        {
            if (error.Length > 0)
            {
                int index = 1;
                dynamic custom = error.ToDictionary(e => "" + index++, e => e.Substring(2));
                ecfeed.addFeedback(false, comment:"Output", duration:duration, custom:custom);
                Assert.Fail();
            }
        }

        private void Delay()
        {
            Thread.Sleep(1000);
        }

        private int GetTime(Stopwatch stopwatch)
        {
            stopwatch.Stop();
            return (int)stopwatch.ElapsedMilliseconds;
        }

        [OneTimeTearDown]
        public static void AfterAll()
        {
            seleniumDriver.Close();
        }

        [TestCaseSource("dataString")]
        public void Test(string country, string name, string address, string product, string color, string size, string quantity, string payment, string delivery, string phone, string email, TestHandle ecfeed)
        {
            string[][] input = new string[2][] { new string[] {name, address, quantity, phone, email}, new string[] {country, product, color, size, payment, delivery} };

            Stopwatch stopwatch = Stopwatch.StartNew();

            try 
            {
                SetForm(input);
                
            } 
            catch (Exception e) 
            {
                HandleInputError(e.Message, ecfeed);
            }

            Execute();
            int duration = GetTime(stopwatch);
            string[] response = GetResponse();
            Delay();

            HandleOutputError(response, duration, ecfeed);
            
            ecfeed.addFeedback(true, duration:duration);
        }
    }

}
