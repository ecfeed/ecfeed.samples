using System.Collections.Generic;

namespace Testify.EcFeed
{
    public interface ITestProviderContext
    {
        ITestProviderContext Copy();
        
        string Model { get; set; }
        string Method { get; set; }
        string Template { get; set; }
        Dictionary<string, object> Settings { get; set; }

    }
}