using Newtonsoft.Json;

namespace Testify.EcFeed
{
    public struct MessageStatus
    {
        [JsonProperty("status")] public string Status { get; set; }

        public override string ToString() => $"Status: { Status }";
    }

    static class MessageStatusHelper { }
}