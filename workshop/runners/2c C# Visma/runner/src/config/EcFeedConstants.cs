using System;
using System.Collections.Generic;

namespace Testify.EcFeed.Runner.Export
{
    sealed class EcFeedConstants
    {
        internal static readonly string DefaultKeyStorePassword = "changeit";
        internal static readonly string DefaultServiceAddress = "https://develop-gen.ecfeed.com";
        // internal static readonly string DefaultServiceAddress = "https://gen.ecfeed.com";
        internal static readonly string DefaultCertificateHash = "2E2E935816C95225719F9D6DAD07F5699F7B947E";
        // internal static readonly string DefaultCertificateHash = "AAE72557A7DB47EA4CF4C649108E422528EFDA1B";
        internal static readonly string[] DefaultKeyStorePath =
        {
            Environment.GetEnvironmentVariable("HOME") + "/.ecfeed/security.p12",
            Environment.GetEnvironmentVariable("HOME") + "/ecfeed/security.p12",
            "./security.p12"
        };

        internal static string EndpointHealthCheck = "genServiceVersion";
        internal static string EndpointGenerator = "testCaseService";

        internal static readonly string[] DefaultTestSuite = 
        { 
            "default suite" 
        };

        internal static readonly string DefaultContextTemplate = "JSON";
        internal static Dictionary<string, object> DefaultContextSettings = new Dictionary<string, object> { { "dataSource", "genNWise" } };

    }    
}