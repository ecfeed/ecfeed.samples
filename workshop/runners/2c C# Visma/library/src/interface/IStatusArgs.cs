namespace Testify.EcFeed
{
    public interface IStatusEventArgs
    {
        string DataRaw { get; set; }
        MessageStatus Schema { get; set; }
        bool Completed { get; set; }
    } 
}