namespace Testify.EcFeed
{
    public sealed class StatusEventArgs
    {
        public StatusMessage Structure { get; set; }
        public string RawData { get; set; }
        public bool Completed { get; set; }

        public override string ToString()
        {
            return 
                $"StatusEventArgs:\n" + 
                $"\t[string: { !string.IsNullOrEmpty(RawData) }]\n" +
                $"\t[struct: { Structure.Status != null }]\n" +
                $"\t[completed: { Completed }]";
        }
    } 
}