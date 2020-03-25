using System;
using System.Threading.Tasks;

namespace Testify.EcFeed.Runner.Export
{
    public interface ITestProvider
    {
        ITestProvider Copy();

        string KeyStorePath { get; set; }
        string KeyStorePassword { get; set; }
        string CertificateHash { get; set; }
        string GeneratorAddress { get; set; }

        void ValidateConnectionSettings();

        Task<string> Generate(ITestProviderContext testProviderContext, string template = "JSON");
        Task<string> GenerateCartesian(ITestProviderContext testProviderContext, string template = "JSON");
        Task<string> GenerateNWise(ITestProviderContext testProviderContext, int n = 2, int coverage = 100, string template = "JSON");
        Task<string> GenerateRandom(ITestProviderContext testProviderContext, int length = 10, bool duplicates = true, string template = "JSON");
        Task<string> GenerateStatic(ITestProviderContext testProviderContext, string[] testSuites, string template = "JSON");

        void AddTestEventHandler(EventHandler<ITestEventArgs> testEventHandler);
        void RemoveTestEventHandler(EventHandler<ITestEventArgs> testEventHandler);
        void AddStatusEventHandler(EventHandler<IStatusEventArgs> testEventHandler);
        void RemoveStatusEventHandler(EventHandler<IStatusEventArgs> testEventHandler);
    }
}