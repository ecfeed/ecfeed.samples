using System;
using System.Threading.Tasks;
using System.Collections.Generic;

namespace Testify.EcFeed
{
    public interface ITestProvider
    {
        ITestProvider Copy();

        string KeyStorePath { get; set; }
        string KeyStorePassword { get; set; }
        string CertificateHash { get; set; }
        string GeneratorAddress { get; set; }
        string Model { get; set; }
        string Method { get; set; }
        Dictionary<string, object> Settings { get; set; }

        void ValidateConnectionSettings();

        Task<string> Generate(
            string template = Constants.DefaultTemplate);
        Task<string> GenerateCartesian(
            string template = Constants.DefaultTemplate);
        Task<string> GenerateNWise(
            string template = Constants.DefaultTemplate,
            int n = Constants.DefaultContextN, 
            int coverage = Constants.DefaultContextCoverage);
        Task<string> GenerateRandom(
            string template = Constants.DefaultTemplate,
            int length = Constants.DefaultContextLength, 
            bool duplicates = Constants.DefaultContextDuplicates);
        Task<string> GenerateStatic(
            string template = Constants.DefaultTemplate,
            object testSuites = null);

        void AddTestEventHandler(EventHandler<TestEventArgs> testEventHandler);
        void RemoveTestEventHandler(EventHandler<TestEventArgs> testEventHandler);
        void AddStatusEventHandler(EventHandler<StatusEventArgs> testEventHandler);
        void RemoveStatusEventHandler(EventHandler<StatusEventArgs> testEventHandler);
    }
}