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
        TestQueue fifo;

        public Feed()
        {
            ITestProvider testProvider = new TestProvider();
            testProvider.Model = "7482-5194-2849-1943-2448";
            testProvider.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";
            testProvider.Settings = new Dictionary<string, object> { { "dataSource", "genNWise" }, { "constraints", "NONE" } };

            fifo = new TestQueue(testProvider);
        }

        public IEnumerator GetEnumerator()
        {
            return fifo.GetEnumerator();
        }
    }
}