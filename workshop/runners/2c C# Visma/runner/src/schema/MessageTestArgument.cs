using Newtonsoft.Json;

namespace Testify.EcFeed.Runner.Export
{
    public struct MessageTestArgument
    {
        [JsonProperty("name")] public string Name { get; set; }
        [JsonProperty("value")] public object Value { get; set; }

        public override string ToString() => $"\t[ { Value.GetType() } : { Name } : { Value } ]";
    }

    static class MessageTestArgumentHelper { }
}