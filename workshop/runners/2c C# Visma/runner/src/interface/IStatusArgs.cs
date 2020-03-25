namespace Testify.EcFeed.Runner.Export
{
    public interface IStatusEventArgs
    {
        string StatusString { get; set; }
        bool TerminateExecution { get; set; }
        MessageStatus StatusStructure { get; set; }
    } 
}