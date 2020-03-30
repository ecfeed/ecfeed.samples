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
        internal const string DefaultProviderCertificateHash = "FD3D44720A70F2A22454AAA0B3F1E8AE6FC0D84E";
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