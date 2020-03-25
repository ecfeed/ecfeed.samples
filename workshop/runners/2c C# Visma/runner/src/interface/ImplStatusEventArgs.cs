namespace Testify.EcFeed.Runner.Export
{
    public struct StatusEventArgs : IStatusEventArgs
    {
        public string StatusString { get; set; }
        public bool TerminateExecution { get; set; }
        public MessageStatus StatusStructure { get; set; }

        public StatusEventArgs Copy() => this;
    } 
}