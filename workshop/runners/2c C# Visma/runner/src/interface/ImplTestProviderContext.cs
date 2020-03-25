using System.Collections.Generic;

namespace Testify.EcFeed.Runner.Export
{
    struct TestProviderContext : ITestProviderContext
    {
        public string Model { get; set; }
        public string Method { get; set; }
        public string Template { get; set; }
        public Dictionary<string, object> Settings { get; set; }       

        public ITestProviderContext Copy() => this;
    }
}