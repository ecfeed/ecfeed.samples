using System.Linq;
using System.Collections.Generic;
using Newtonsoft.Json;

namespace Testify.EcFeed
{
    public static class ContextHelper
    {
        public static string SerializeContext(ITestProviderContext context)
        {
            ValidateContext(ref context);

            var parsedRequest = new
            {
                model = context.Model,
                method = context.Method,
                template = context.Template,
                userData = JsonConvert.SerializeObject(context.Settings, Formatting.None).Replace("\"", "\'")
            };

            return JsonConvert.SerializeObject(parsedRequest);
        }

        internal static void ValidateContext(ref ITestProviderContext context)
        {

            if (string.IsNullOrEmpty(context.Model))
            {
                throw new EcFeedException("The model ID is not defined and the default value cannot be used.");
            }

            if (string.IsNullOrEmpty(context.Method))
            {
                throw new EcFeedException("The method name is not defined and the default value cannot be used.");
            }

            if (string.IsNullOrEmpty(context.Template))
            {
                context.Template = EcFeedConstants.DefaultContextTemplate;
            }

            if (context.Settings == null)
            {
                context.Settings = EcFeedConstants.DefaultContextSettings;
            }

        }

        internal static Dictionary<string, object> MergeContextSettings(Dictionary<string, object> settingsFrom, Dictionary<string, object> settingsTo)
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