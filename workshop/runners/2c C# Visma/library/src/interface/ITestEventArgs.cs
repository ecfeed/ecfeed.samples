using NUnit.Framework;

namespace Testify.EcFeed
{
    public interface ITestEventArgs
    {
        string DataRaw { get; set; }
        MessageTest Schema { get; set; }
        object[] DataType { get; set; }
        TestCaseData TestNUnit { get; set; }
    } 
}