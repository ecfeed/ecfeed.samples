using System.Collections.Generic;

namespace Testify.EcFeed
{
    struct TestProviderContext
    {
        public string Model { get; set; }
        public string Method { get; set; }
        public string Template { get; set; }
        public Dictionary<string, object> Settings { get; set; }       

        public TestProviderContext Copy() => this;
    }
}