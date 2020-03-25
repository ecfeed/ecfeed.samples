using System;

namespace Testify.EcFeed.Runner.Export
{
    public class TestEventArgs : EventArgs, ITestEventArgs
    {
        public string TestString { get; set; }
        public MessageTest TestStructure { get; set; }
        public object[] TestObject { get; set; }

        public override string ToString()
        {
            return 
                $"TestEventArgs:\n" + 
                $"\t[string: { !string.IsNullOrEmpty(TestString) }]\n" +
                $"\t[struct: { TestStructure.TestArguments != null }]\n" +
                $"\t[object: { TestObject != null }]";
        }
    } 
}