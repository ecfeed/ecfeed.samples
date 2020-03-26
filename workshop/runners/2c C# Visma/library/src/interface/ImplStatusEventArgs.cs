namespace Testify.EcFeed
{
    public struct StatusEventArgs : IStatusEventArgs
    {
        public string DataRaw { get; set; }
        public MessageStatus Schema { get; set; }
        public bool Completed { get; set; }
        
        public StatusEventArgs Copy() => this;
    } 
}