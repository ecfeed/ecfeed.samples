using System;
using System.Threading;
using System.Threading.Tasks;
using System.Collections.Generic;
using NUnit.Framework;

namespace Testify.EcFeed.Example
{
    class Runner
    {
        public static async Task<int> Main(string[] args)
        {
            // return await Synchronous();
            // return await Asynchronous();
            // return await Event();
            // ExampleTestQueue();
            ExampleTestList();

            return 0;
        }

        public static async Task<int> Synchronous()
        {
            ITestProvider testProvider = new TestProvider();
            testProvider.Model = "7482-5194-2849-1943-2448";
            testProvider.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";
            
            Console.WriteLine(testProvider);

            Console.WriteLine(await testProvider.GenerateNWise(template: "CSV"));

            return 0;
        }

        public static async Task<int> Asynchronous()
        {
            ITestProvider testProvider = new TestProvider();
            testProvider.Model = "7482-5194-2849-1943-2448";
            testProvider.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";
            testProvider.Settings = new Dictionary<string, object> { { "dataSource", "genNWise" }, { "constraints", "NONE" } };

            Console.WriteLine(testProvider);

            Task<string> response = testProvider.Generate(template: "CSV");

            Console.WriteLine("\nWaiting...\n");

            Console.WriteLine(await response);

            return 0;
        }

        public static async Task<int> Event()
        {
            ITestProvider testProvider = new TestProvider();
            testProvider.Model = "7482-5194-2849-1943-2448";
            testProvider.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";
            testProvider.Settings = new Dictionary<string, object> { { "dataSource", "genNWise" }, { "constraints", "NONE" } };
            
            testProvider.AddTestEventHandler(TestEventHandler);
            testProvider.AddStatusEventHandler(StatusEventHandler);

            await testProvider.Generate(template: "Stream");

            return 0;
        }

        public static void ExampleTestQueue()
        {
            ITestProvider testProvider = new TestProvider();
            testProvider.Model = "7482-5194-2849-1943-2448";
            testProvider.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";

            TestQueue queue = testProvider.QueueNWise();

            Console.WriteLine(queue);
            Thread.Sleep(2000);
            Console.WriteLine(queue);

            foreach(TestCaseData element in queue)
            {
                Console.WriteLine("HANDLER: [{0}]", string.Join(", ", element.Arguments));
            }

            Console.WriteLine(queue);
        }

        public static void ExampleTestList()
        {
            ITestProvider testProvider = new TestProvider();
            testProvider.Model = "7482-5194-2849-1943-2448";
            testProvider.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";

            TestList list = testProvider.ListNWise();

            Console.WriteLine(list);
            list.WaitUntilFinished();
            Console.WriteLine(list);

            Console.WriteLine(list[0]);

            foreach(TestEventArgs element in list)
            {
                Console.WriteLine("HANDLER: [{0}]", string.Join(", ", element));
            }

            Console.WriteLine(list);
        }

        static void TestEventHandler(object sender, TestEventArgs args)
        {
        //    Console.WriteLine("TEST HANDLER: " + args);
        //    Console.WriteLine("TEST HANDLER: " + args.DataRaw);
        //    Console.WriteLine("TEST HANDLER: " + args.DataType);
        //    Console.WriteLine("TEST HANDLER: " + args.Schema);
           Console.WriteLine("TEST HANDLER: [{0}]", string.Join(", ", args.TestData.Arguments));
        }

        static void StatusEventHandler(object sender, StatusEventArgs args)
        {
           Console.WriteLine("STATUS HANDLER: " + args.Structure);
        }
    }
}