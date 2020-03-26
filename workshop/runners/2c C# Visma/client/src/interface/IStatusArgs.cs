namespace Testify.EcFeed
{
    public interface IStatusEventArgs
    {
        string StatusString { get; set; }
        bool TerminateExecution { get; set; }
        MessageStatus StatusStructure { get; set; }
    } 
}