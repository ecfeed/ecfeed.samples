using Newtonsoft.Json;

namespace Testify.EcFeed.Runner.Export
{
    public static class StreamParser
    {
        public static MessageTest ParseTest(string data)
        {
            return JsonConvert.DeserializeObject<MessageTest>(data);
        }

        public static MessageStatus ParseStatus(string data)
        {
            return JsonConvert.DeserializeObject<MessageStatus>(data);
        }

        public static object[] ParseTestSchema(string data)
        {
            return ParseTestSchema(ParseTest(data));
        }
        public static object[] ParseTestSchema(MessageTest data)
        {
            object[] result = new object[data.TestArguments.Length];

            for (int i=0 ; i < data.TestArguments.Length ; i++)
            {
                result[i] = data.TestArguments[i].Value;
            }

            return result;
        }

    }

}