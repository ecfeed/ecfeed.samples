using System.Linq;
using System.Collections.Generic;
using Newtonsoft.Json;

namespace Testify.EcFeed
{
    public static class TestProviderContextHelper
    {
        internal static string SerializeTestProviderContext(TestProviderContext context)
        {
            ValidateTestProviderContext(ref context);

            var parsedRequest = new
            {
                model = context.Model,
                method = context.Method,
                template = context.Template,
                userData = JsonConvert.SerializeObject(context.Settings, Formatting.None).Replace("\"", "\'")
            };

            return JsonConvert.SerializeObject(parsedRequest);
        }

        static void ValidateTestProviderContext(ref TestProviderContext context)
        {

            if (string.IsNullOrEmpty(context.Model))
            {
                throw new TestProviderException("The model ID is not defined and the default value cannot be used.");
            }

            if (string.IsNullOrEmpty(context.Method))
            {
                throw new TestProviderException("The method name is not defined and the default value cannot be used.");
            }

            if (string.IsNullOrEmpty(context.Template))
            {
                context.Template = Constants.DefaultTemplate;
            }

            if (context.Settings == null)
            {
                context.Settings = Constants.DefaultContextSettings;
            }

        }

        internal static Dictionary<string, object> MergeTestProviderContextSettings(Dictionary<string, object> settingsFrom, Dictionary<string, object> settingsTo)
        {

            if (settingsFrom == null)
            {
                settingsFrom = new Dictionary<string, object> { };
            }

            if (settingsTo == null)
            {
                settingsTo = new Dictionary<string, object> { };
            }

            settingsFrom.ToList().ForEach(x => settingsTo[x.Key] = x.Value);

            return settingsTo;
        }

    }
}