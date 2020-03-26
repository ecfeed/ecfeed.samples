using System;
using System.Threading;
using System.Threading.Tasks;
using System.Collections.Generic;
using Testify.EcFeed;

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

            Console.WriteLine(testProvider);

            ITestProviderContext testProviderContext = new TestProviderContext();
            testProviderContext.Model = "7482-5194-2849-1943-2448";
            testProviderContext.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";
            
            Console.WriteLine(await testProvider.GenerateNWise(testProviderContext, template: "XML"));

            return 0;
        }

        public static async Task<int> Asynchronous()
        {
            ITestProvider testProvider = new TestProvider();

            Console.WriteLine(testProvider);

            ITestProviderContext testProviderContext = new TestProviderContext();
            testProviderContext.Model = "7482-5194-2849-1943-2448";
            testProviderContext.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";
            testProviderContext.Settings = new Dictionary<string, object> { { "dataSource", "genNWise" }, { "constraints", "NONE" } };

            Task<string> response = testProvider.Generate(testProviderContext, template: "JSON");

            Console.WriteLine("Waiting\n");

            Console.WriteLine(await response);

            return 0;
        }

        public static async Task<int> Event()
        {
            ITestProvider testProvider = new TestProvider();

            testProvider.AddTestEventHandler(TestEventHandler);
            testProvider.AddStatusEventHandler(StatusEventHandler);

            ITestProviderContext testProviderContext = new TestProviderContext();
            testProviderContext.Model = "7482-5194-2849-1943-2448";
            testProviderContext.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";
            testProviderContext.Settings = new Dictionary<string, object> { { "dataSource", "genNWise" }, { "constraints", "NONE" } };

            await testProvider.Generate(testProviderContext, template: "Stream");

            return 0;
        }

        public static void ExampleTestQueue()
        {
            ITestProvider testProvider = new TestProvider();

            ITestProviderContext testProviderContext = new TestProviderContext();
            testProviderContext.Model = "7482-5194-2849-1943-2448";
            testProviderContext.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";
            testProviderContext.Settings = new Dictionary<string, object> { { "dataSource", "genNWise" }, { "constraints", "NONE" } };

            TestQueue queue = new TestQueue(testProvider,testProviderContext);

            Console.WriteLine(queue);
            Thread.Sleep(2000);
            Console.WriteLine(queue);

            foreach(object[] element in queue)
            {
                Console.WriteLine("HANDLER: [{0}]", string.Join(", ", element));
            }

            Console.WriteLine(queue);
        }

        public static void ExampleTestList()
        {
            ITestProvider testProvider = new TestProvider();

            ITestProviderContext testProviderContext = new TestProviderContext();
            testProviderContext.Model = "7482-5194-2849-1943-2448";
            testProviderContext.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";
            testProviderContext.Settings = new Dictionary<string, object> { { "dataSource", "genNWise" }, { "constraints", "NONE" } };

            TestList list = new TestList(testProvider,testProviderContext);

            Console.WriteLine(list);
            list.WaitUntilFinished();
            Console.WriteLine(list);

            Console.WriteLine(list[0]);

            foreach(ITestEventArgs element in list)
            {
                Console.WriteLine("HANDLER: [{0}]", string.Join(", ", element));
            }

            Console.WriteLine(list);
        }

        static void TestEventHandler(object sender, ITestEventArgs args)
        {
        //    Console.WriteLine("TEST HANDLER: " + args);
           Console.WriteLine("TEST HANDLER: " + args.TestString);
        //    Console.WriteLine("TEST HANDLER: " + args.TestStructure);
        //    Console.WriteLine("TEST HANDLER: [{0}]", string.Join(", ", args.TestObject));
        }

        static void StatusEventHandler(object sender, IStatusEventArgs args)
        {
           Console.WriteLine("STATUS HANDLER: " + args.StatusStructure);
        }
    }
}