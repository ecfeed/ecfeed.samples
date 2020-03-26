namespace Testify.EcFeed
{
    public interface ITestEventArgs
    {
        string TestString { get; set; }
        object[] TestObject { get; set; }
        MessageTest TestStructure { get; set; }
    } 
}