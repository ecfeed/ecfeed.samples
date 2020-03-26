using System.Text;
using Newtonsoft.Json;

namespace Testify.EcFeed
{
    public struct MessageTest
    {
        [JsonProperty("testCase")] public MessageTestArgument[] TestArguments { get; set; }

        public override string ToString() => MessageTestHelper.ParseToString(ref this);
    }

    static class MessageTestHelper
    {
        internal static string ParseToString(ref MessageTest schema)
        {
            if (schema.TestArguments == null)
            {
                return "NOT PARSABLE";
            }

            StringBuilder description = new StringBuilder("");

            description.AppendLine($"Number of arguments: { schema.TestArguments.Length }.");

            foreach (MessageTestArgument testArgument in schema.TestArguments)
            {
                description.AppendLine(testArgument.ToString());
            }

            return description.ToString();
        }
    }
}