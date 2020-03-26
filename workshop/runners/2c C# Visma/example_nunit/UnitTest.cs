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
        private ITestProvider _testProvider = new TestProvider();

        public Feed()
        {
            _testProvider.Model = "7482-5194-2849-1943-2448";
            _testProvider.Method = "com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)";
        }

        public IEnumerator GetEnumerator()
        {
            return _testProvider.QueueNWise().GetEnumerator();
        }
    }
}