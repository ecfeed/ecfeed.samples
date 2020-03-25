using Newtonsoft.Json;

namespace Testify.EcFeed.Runner.Export
{
    public struct MessageStatus
    {
        [JsonProperty("status")] public string Status { get; set; }

        public override string ToString() => $"Status: { Status }";
    }

    static class MessageStatusHelper { }
}