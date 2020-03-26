using System;
using System.Collections.Generic;

namespace EcFeed
{
    static class Constants
    {
    // Production

    // internal const string DefaultServiceAddress = "https://gen.ecfeed.com";
    // internal const string DefaultCertificateHash = "AAE72557A7DB47EA4CF4C649108E422528EFDA1B";

    // Development

        internal const string DefaultProviderKeyStorePassword = "changeit";
        internal const string DefaultProviderGeneratorAddress = "https://develop-gen.ecfeed.com";
        internal const string DefaultProviderCertificateHash = "2E2E935816C95225719F9D6DAD07F5699F7B947E";
        internal static readonly string[] DefaultProviderKeyStorePath =
        {
            Environment.GetEnvironmentVariable("HOME") + "/.ecfeed/security.p12",
            Environment.GetEnvironmentVariable("HOME") + "/ecfeed/security.p12",
            "./security.p12"
        };

        internal const string DefaultTemplate = "JSON";
        
        internal const string DefaultContextTestSuite = "ALL";
        internal const bool DefaultContextDuplicates = true;
        internal const int DefaultContextN = 2;
        internal const int DefaultContextCoverage = 100;
        internal const int DefaultContextLength = 10;

        internal const string EndpointHealthCheck = "genServiceVersion";
        internal const string EndpointGenerator = "testCaseService";

        internal static readonly Dictionary<string, object> DefaultContextSettings = new Dictionary<string, object> { { "dataSource", "genNWise" } };
    }    
}