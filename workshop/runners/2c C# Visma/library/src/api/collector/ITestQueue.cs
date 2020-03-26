using System.Collections.Generic;
using NUnit.Framework;

namespace Testify.EcFeed
{
    public interface ITestQueue : IEnumerable<TestCaseData>
    {
        int Size();
    }
}