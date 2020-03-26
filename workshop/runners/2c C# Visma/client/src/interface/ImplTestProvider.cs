using System;
using System.IO;
using System.Text;
using System.Net;
using System.Net.Security;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Security.Cryptography;
using System.Security.Cryptography.X509Certificates;
using Newtonsoft.Json;

namespace Testify.EcFeed
{
    public sealed class TestProvider : ITestProvider
    {
        static TestProvider()
        {
            System.Net.ServicePointManager.SecurityProtocol = System.Net.SecurityProtocolType.Tls12;
        }

        public string KeyStorePath { get; set; }
        public string KeyStorePassword { get; set; }
        public string CertificateHash { get; set; }
        public string GeneratorAddress { get; set; }

        public event EventHandler<ITestEventArgs> TestEventHandler;
        public event EventHandler<IStatusEventArgs> StatusEventHandler;

        public TestProvider(string keyStorePath = "", string keyStorePassword = "", string certificateHash = "", string generatorAddress = "", bool verify = true)
        {
            KeyStorePath = keyStorePath.Equals("") ? SetDefaultKeyStorePath() : keyStorePath;
            KeyStorePassword = keyStorePassword.Equals("") ? SetDefaultKeyStorePassword() : keyStorePassword;
            CertificateHash = certificateHash.Equals("") ? SetDefaultCertificateHash() : certificateHash;
            GeneratorAddress = generatorAddress.Equals("") ? SetDefaultServiceAddress() : generatorAddress;

            if (verify)
            {
                ValidateConnectionSettings();
            }
        }

        public TestProvider(ITestProvider testProvider) 
            : this(testProvider.KeyStorePath, testProvider.KeyStorePassword, testProvider.CertificateHash, testProvider.GeneratorAddress, false) { }

        public ITestProvider Copy()
        {
            return new TestProvider(this);
        }

        private string SetDefaultKeyStorePath()
        {
            foreach (string path in EcFeedConstants.DefaultProviderKeyStorePath)
            {
                try
                {
                    KeyStorePath = path;

                    ValidateKeyStorePathSyntax();
                    ValidateKeyStorePathCorectness();

                    return path;
                }
                catch (EcFeedException) { }
            }

            Console.WriteLine("The default keystore could not be loaded. In order to use the test generator, please provide a correct path.");
            return "";
        }

        private string SetDefaultKeyStorePassword()
        {
            return EcFeedConstants.DefaultProviderKeyStorePassword;
        }

        private string SetDefaultCertificateHash()
        {
            return EcFeedConstants.DefaultProviderCertificateHash;
        }

        private string SetDefaultServiceAddress()
        {
            return EcFeedConstants.DefaultProviderGeneratorAddress;
        }

        public async void ValidateConnectionSettings() 
        {

            ValidateKeyStorePathSyntax();
            ValidateKeyStorePathCorectness();

            ValidateKeyStorePasswordSyntax();
            ValidateKeyStorePasswordCorectness();

            ValidateCertificateHashSyntax();
            ValidateCertificateHashSyntax();

            ValidateServiceAddressSyntax();
            ValidateServiceAddressCorectness();

            await SendRequest(GenerateHealthCheckURL(GeneratorAddress), false);
        }

        private void ValidateKeyStorePathSyntax()
        {

            if (string.IsNullOrEmpty(KeyStorePath))
            {
                throw new EcFeedException("The keystore path is not defined.");
            }

        }

        private void ValidateKeyStorePathCorectness()
        {

            if (!File.Exists(KeyStorePath)) 
            {
                throw new EcFeedException($"The keystore path is incorrect. It does not point to a file. Keystore path: '{ Path.GetFullPath(KeyStorePath) }'");
            }

        }

        private void ValidateKeyStorePasswordSyntax()
        {

            if (string.IsNullOrEmpty(KeyStorePassword))
            {
                throw new EcFeedException("The certificate password is not defined.");
            }

        }

        private void ValidateKeyStorePasswordCorectness()
        {

            try 
            {
                new X509Certificate2(KeyStorePath, KeyStorePassword);
            }
            catch (CryptographicException)
            {
                throw new EcFeedException($"The certificate password is incorrect. Keystore path: '{ Path.GetFullPath(KeyStorePath) }'");
            }

        }

        private void ValidateCertificateHashSyntax()
        {

            if (string.IsNullOrEmpty(CertificateHash))
            {
                throw new EcFeedException("The certificate hash is not defined.");
            }

        }

        private void ValidateCertificateHashCorectness() { }

        private void ValidateServiceAddressSyntax()
        {

            if (string.IsNullOrEmpty(GeneratorAddress))
            {
                throw new EcFeedException("The service address is not defined.");
            }

        }

        private void ValidateServiceAddressCorectness() { }

        public async Task<string> Generate(ITestProviderContext testProviderContext, 
            string template = EcFeedConstants.DefaultContextTemplate)
        {
            testProviderContext.Template = template;

            string request = GenerateRequestURL(testProviderContext, GeneratorAddress, GetRequestType(template));
            Task<string> response = SendRequest(request, template.Equals("Stream"));

            return await response;
        }

        public async Task<string> GenerateCartesian(ITestProviderContext testProviderContext, 
            string template = EcFeedConstants.DefaultContextTemplate)
        {
            Dictionary<string, object> additionalData = new Dictionary<string, object> { { "dataSource", "genCartesian" } };

            testProviderContext.Settings = ContextHelper.MergeContextSettings(additionalData, testProviderContext.Settings);
            testProviderContext.Template = template;

            string request = GenerateRequestURL(testProviderContext, GeneratorAddress, GetRequestType(template));
            Task<string> response = SendRequest(request, template.Equals("Stream"));

            return await response;
        }

        public async Task<string> GenerateNWise(ITestProviderContext testProviderContext, 
            int n = EcFeedConstants.DefaultContextN, 
            int coverage = EcFeedConstants.DefaultContextCoverage, 
            string template = EcFeedConstants.DefaultContextTemplate)
        {
            Dictionary<string, object> additionalData = new Dictionary<string, object> { { "dataSource", "genNWise" }, { "n", n }, { "coverage", coverage } };

            testProviderContext.Settings = ContextHelper.MergeContextSettings(additionalData, testProviderContext.Settings);
            testProviderContext.Template = template;

            string request = GenerateRequestURL(testProviderContext, GeneratorAddress, GetRequestType(template));
            Task<string> response = SendRequest(request, template.Equals("Stream"));

            return await response;
        }

        public async Task<string> GenerateRandom(ITestProviderContext testProviderContext, 
            int length = EcFeedConstants.DefaultContextLength, 
            bool duplicates = EcFeedConstants.DefaultContextDuplicates, 
            string template = EcFeedConstants.DefaultContextTemplate)
        {
            Dictionary<string, object> additionalData = new Dictionary<string, object> { { "dataSource", "random" }, { "length", length }, { "duplicates", duplicates } };

            testProviderContext.Settings = ContextHelper.MergeContextSettings(additionalData, testProviderContext.Settings);
            testProviderContext.Template = template;

            string request = GenerateRequestURL(testProviderContext, GeneratorAddress, GetRequestType(template));
            Task<string> response = SendRequest(request, template.Equals("Stream"));

            return await response;
        }

        public async Task<string> GenerateStatic(ITestProviderContext testProviderContext, 
            string[] testSuites = null, 
            string template = EcFeedConstants.DefaultContextTemplate)
        {
            string[] updateTestSuites = testSuites == null ? EcFeedConstants.DefaultContextTestSuite : testSuites;

            Dictionary<string, object> additionalData = new Dictionary<string, object> { { "dataSource", "static" }, { "testSuites", updateTestSuites } };

            testProviderContext.Settings = ContextHelper.MergeContextSettings(additionalData, testProviderContext.Settings);
            testProviderContext.Template = template;

            string request = GenerateRequestURL(testProviderContext, GeneratorAddress, GetRequestType(template));
            Task<string> response = SendRequest(request, template.Equals("Stream"));

            return await response;
        }

        private string GetRequestType(string template)
        {
            return template.Equals("Stream") || template.Equals("StreamRaw") ? "requestData" : "requestExport";
        }

        public void AddTestEventHandler(EventHandler<ITestEventArgs> testEventHandler)
        {
            
            if (TestEventHandler == null)
            {
                TestEventHandler += testEventHandler;
            }
            else
            {
                if (Array.IndexOf(TestEventHandler.GetInvocationList(), testEventHandler) == -1)
                {
                    TestEventHandler += testEventHandler;
                } 
            }
            
        }

        public void RemoveTestEventHandler(EventHandler<ITestEventArgs> testEventHandler)
        {
            
            if (TestEventHandler != null)
            {
                if (Array.IndexOf(TestEventHandler.GetInvocationList(), testEventHandler) != -1)
                {
                    TestEventHandler -= testEventHandler;
                }
            }

        }

        public void AddStatusEventHandler(EventHandler<IStatusEventArgs> statusEventHandler)
        {
            
            if (StatusEventHandler == null)
            {
                StatusEventHandler += statusEventHandler;
            }
            else
            {
                if (Array.IndexOf(StatusEventHandler.GetInvocationList(), statusEventHandler) == -1)
                {
                    StatusEventHandler += statusEventHandler;
                } 
            }
            
        }

        public void RemoveStatusEventHandler(EventHandler<IStatusEventArgs> statusEventHandler)
        {
            
            if (StatusEventHandler != null)
            {
                if (Array.IndexOf(StatusEventHandler.GetInvocationList(), statusEventHandler) != -1)
                {
                    StatusEventHandler -= statusEventHandler;
                }
            }

        }

        private bool ValidateServerCertificate(object sender, X509Certificate cert, X509Chain chain, SslPolicyErrors sslPolicyErrors) {
            
            foreach(X509ChainElement certificate in chain.ChainElements) {

                if (certificate.Certificate.GetCertHashString() == CertificateHash) {
                    return true;
                }

            }
 
            return false;
        }

        private string GenerateHealthCheckURL(string address)
        {
            string request = $"{ address }/{ EcFeedConstants.EndpointHealthCheck }";

            return request;
        }

        private string GenerateRequestURL(ITestProviderContext context, string address, string type)
        {
            string requestData = ContextHelper.SerializeContext(context);
            string request = $"{ address }/{ EcFeedConstants.EndpointGenerator }?requestType={ type }&request={ requestData }";

            return request;
        }

        private async Task<string> SendRequest(string request, bool streamFilter)
        {

            try 
            {
                HttpWebRequest httpWebRequest = (HttpWebRequest) HttpWebRequest.Create(request);
                httpWebRequest.ServerCertificateValidationCallback = ValidateServerCertificate;
                httpWebRequest.ClientCertificates.Add(new X509Certificate2(KeyStorePath, KeyStorePassword));

                return await ProcessResponse((HttpWebResponse) httpWebRequest.GetResponse(), streamFilter);
            }
            catch (CryptographicException)
            {
                throw new EcFeedException($"The keystore password is incorrect. Keystore path: '{ Path.GetFullPath(KeyStorePath) }'");
            }
            catch (WebException e)
            {
                throw new EcFeedException(e.Message);
            }

        }

        private async Task<string> ProcessResponse(HttpWebResponse response, bool streamFilter) 
        {    
            StringBuilder responseBuilder = new StringBuilder("");

            using (StreamReader reader = new StreamReader(response.GetResponseStream(), Encoding.UTF8)) {
                string line;
                
                while ((line = await reader.ReadLineAsync()) != null) {
                    ProcessSingleResponse(line, streamFilter, responseBuilder);
                }

            }

            return responseBuilder.ToString();
        }

        private void ProcessSingleResponse(string line, bool streamFilter, StringBuilder responseBuilder)
        {
            ProcessSingleStatusResponse(line);
            ProcessSingleTestResponse(line, streamFilter, responseBuilder);
        }

        private void ProcessSingleStatusResponse(string line)
        {
            try
            {
                StatusEventArgs statusEventArgs = new StatusEventArgs() { StatusString = line };
                statusEventArgs.StatusStructure = JsonConvert.DeserializeObject<MessageStatus>(line);

                if (statusEventArgs.StatusStructure.Status != null)
                {

                    if (statusEventArgs.StatusStructure.Status.Equals("END_DATA"))
                    {
                        statusEventArgs.TerminateExecution = true;
                    }
                    else 
                    {
                        statusEventArgs.TerminateExecution = false;
                    }
                        
                    GenerateStatusEvent(statusEventArgs);
                }
            }
            catch (JsonReaderException) { }
            catch (JsonSerializationException) { }
        }

        private void ProcessSingleTestResponse(string line, bool streamFilter, StringBuilder responseBuilder)
        {
            TestEventArgs testEventArgs = new TestEventArgs() { TestString = line };

            try
            {
                testEventArgs.TestStructure = JsonConvert.DeserializeObject<MessageTest>(line);

                if (testEventArgs.TestStructure.TestArguments == null)
                {
                    throw new EcFeedException("The message cannot be parsed.");
                }

                testEventArgs.TestObject = StreamParser.ParseTestSchema(testEventArgs.TestStructure);

                responseBuilder.AppendLine(line);
                GenerateTestEvent(testEventArgs);
            }
            catch (Exception)
            {
                if (streamFilter)
                {
                    return;
                }

                responseBuilder.AppendLine(line);
                GenerateTestEvent(testEventArgs);
            }
        }

        private void GenerateStatusEvent(IStatusEventArgs args)
        {

            if (StatusEventHandler != null && StatusEventHandler.GetInvocationList().Length > 0)
            {
                StatusEventHandler(this, args);
            }

        }

        private void GenerateTestEvent(ITestEventArgs args)
        {

            if (TestEventHandler != null && TestEventHandler.GetInvocationList().Length > 0)
            {
                TestEventHandler(this, args);
            }

        }

        public override string ToString()
        {
            return
                $"TestProvider:\n" +
                $"\t[KeyStorePath: { Path.GetFullPath(KeyStorePath) }]\n" +
                $"\t[KeyStorePassword: { KeyStorePassword }]\n" +
                $"\t[CertificateHash: { CertificateHash }]\n" +
                $"\t[GeneratorAddress: { GeneratorAddress }]";
        }
    }
}