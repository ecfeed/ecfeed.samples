using System;
using NUnit.Framework;

namespace Testify.EcFeed
{
    public sealed class TestEventArgs : EventArgs
    {
        public TestCase Structure { get; set; }
        public string RawData { get; set; }
        public object[] ObjectData { get; set; }
        public TestCaseData TestData { get; set; }

        public override string ToString()
        {
            return 
                $"TestEventArgs:\n" + 
                $"\t[string: { !string.IsNullOrEmpty(RawData) }]\n" +
                $"\t[struct: { Structure.TestCaseArguments != null }]\n" +
                $"\t[object: { ObjectData != null }]";
        }
    } 
}