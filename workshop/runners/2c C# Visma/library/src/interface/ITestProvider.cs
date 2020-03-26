using System;
using System.Threading.Tasks;

namespace Testify.EcFeed
{
    public interface ITestProvider
    {
        ITestProvider Copy();

        string KeyStorePath { get; set; }
        string KeyStorePassword { get; set; }
        string CertificateHash { get; set; }
        string GeneratorAddress { get; set; }

        void ValidateConnectionSettings();

        Task<string> Generate(ITestProviderContext testProviderContext, 
            string template = EcFeedConstants.DefaultContextTemplate);
        Task<string> GenerateCartesian(ITestProviderContext testProviderContext, 
            string template = EcFeedConstants.DefaultContextTemplate);
        Task<string> GenerateNWise(ITestProviderContext testProviderContext, 
            int n = EcFeedConstants.DefaultContextN, 
            int coverage = EcFeedConstants.DefaultContextCoverage, 
            string template = EcFeedConstants.DefaultContextTemplate);
        Task<string> GenerateRandom(ITestProviderContext testProviderContext, 
            int length = EcFeedConstants.DefaultContextLength, 
            bool duplicates = EcFeedConstants.DefaultContextDuplicates,
            string template = EcFeedConstants.DefaultContextTemplate);
        Task<string> GenerateStatic(ITestProviderContext testProviderContext, 
            string[] testSuites = null, 
            string template = EcFeedConstants.DefaultContextTemplate);

        void AddTestEventHandler(EventHandler<ITestEventArgs> testEventHandler);
        void RemoveTestEventHandler(EventHandler<ITestEventArgs> testEventHandler);
        void AddStatusEventHandler(EventHandler<IStatusEventArgs> testEventHandler);
        void RemoveStatusEventHandler(EventHandler<IStatusEventArgs> testEventHandler);
    }
}