using System;
using NUnit.Framework;

namespace Testify.EcFeed
{
    public class TestEventArgs : EventArgs, ITestEventArgs
    {
        public string DataRaw { get; set; }
        public MessageTest Schema { get; set; }
        public object[] DataType { get; set; }
        public TestCaseData TestNUnit { get; set; }

        public override string ToString()
        {
            return 
                $"TestEventArgs:\n" + 
                $"\t[string: { !string.IsNullOrEmpty(DataRaw) }]\n" +
                $"\t[struct: { Schema.TestArguments != null }]\n" +
                $"\t[object: { DataType != null }]";
        }
    } 
}