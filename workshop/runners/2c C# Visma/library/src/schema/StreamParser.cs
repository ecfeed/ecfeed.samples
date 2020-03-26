using Newtonsoft.Json;
using NUnit.Framework;

namespace Testify.EcFeed
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

        public static object[] ParseTestToDataType(string data)
        {
            return ParseTestToDataType(ParseTest(data));
        }
        public static object[] ParseTestToDataType(MessageTest data)
        {
            object[] result = new object[data.TestArguments.Length];

            for (int i=0 ; i < data.TestArguments.Length ; i++)
            {
                result[i] = data.TestArguments[i].Value;
            }

            return result;
        }

        public static TestCaseData ParseTestToNUnit(string data)
        {
            return ParseTestToNUnit(ParseTest(data));
        }

        public static TestCaseData ParseTestToNUnit(MessageTest data)
        {
            return new TestCaseData(ParseTestToDataType(data));
        }

    }

}