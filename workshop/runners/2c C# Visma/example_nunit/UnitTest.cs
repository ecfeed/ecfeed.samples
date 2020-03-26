using System.Collections;
using System.Collections.Generic;
using NUnit.Framework;
using Testify.EcFeed;

namespace example_nunit
{
    [TestFixture]
    public class UnitTest
    {
        [TestCaseSource(typeof(Feed))]
        public void Test(string a0, string a1, string a2, string a3, string a4, string a5, string a6, string a7, string a8, string a9, string a10)
        {
            Assert.That(false, Is.True);
        }
    }

    class Feed : IEnumerable
    {
        TestQueue queue;

        public Feed()
        {
            ITestProvider testProvider = new TestProvider();

            ITestProviderContext testProviderContext = new TestProviderContext();
            testProviderContext.Model = "7482-5194-2849-1943-2448";
            testProviderContext.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";
            testProviderContext.Settings = new Dictionary<string, object> { { "dataSource", "genNWise" }, { "constraints", "NONE" } };

            queue = new TestQueue(testProvider,testProviderContext);
        }

        public IEnumerator GetEnumerator()
        {
            return queue.GetEnumerator();
        }
    }
}