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

        private TestProviderContext _testProviderContext;

        public string KeyStorePath { get; set; }
        public string KeyStorePassword { get; set; }
        public string CertificateHash { get; set; }
        public string GeneratorAddress { get; set; }

        public string Model { 
            get => _testProviderContext.Model; 
            set => _testProviderContext.Model = value; 
        }
        public string Method { 
            get => _testProviderContext.Method; 
            set => _testProviderContext.Method = value; 
        }
        public Dictionary<string, object> Settings { 
            get => _testProviderContext.Settings; 
            set => _testProviderContext.Settings = value; 
        }       

        public event EventHandler<TestEventArgs> TestEventHandler;
        public event EventHandler<StatusEventArgs> StatusEventHandler;

        public TestProvider(
            string keyStorePath = null, string keyStorePassword = null, string certificateHash = null, string generatorAddress = null, 
            string model = null, string method = null, Dictionary<string, object> settings = null,
            bool verify = true)
        {
            _testProviderContext = new TestProviderContext();

            KeyStorePath = string.IsNullOrEmpty(keyStorePath) ? SetDefaultKeyStorePath() : keyStorePath;
            KeyStorePassword = string.IsNullOrEmpty(keyStorePassword) ? SetDefaultKeyStorePassword() : keyStorePassword;
            CertificateHash = string.IsNullOrEmpty(certificateHash) ? SetDefaultCertificateHash() : certificateHash;
            GeneratorAddress = string.IsNullOrEmpty(generatorAddress) ? SetDefaultServiceAddress() : generatorAddress;

            if (verify)
            {
                ValidateConnectionSettings();
            }

            Model = model;
            Method = method;
            Settings = settings;
        }

        public TestProvider(ITestProvider testProvider) 
            : this(
                testProvider.KeyStorePath, testProvider.KeyStorePassword, testProvider.CertificateHash, testProvider.GeneratorAddress, 
                testProvider.Model, testProvider.Method, testProvider.Settings,
                false) { }

        public ITestProvider Copy()
        {
            return new TestProvider(this);
        }

        private string SetDefaultKeyStorePath()
        {
            foreach (string path in Constants.DefaultProviderKeyStorePath)
            {
                try
                {
                    KeyStorePath = path;

                    ValidateKeyStorePathSyntax();
                    ValidateKeyStorePathCorectness();

                    return path;
                }
                catch (TestProviderException) { }
            }

            Console.WriteLine("The default keystore could not be loaded. In order to use the test generator, please provide a correct path.");
            return "";
        }

        private string SetDefaultKeyStorePassword()
        {
            return Constants.DefaultProviderKeyStorePassword;
        }

        private string SetDefaultCertificateHash()
        {
            return Constants.DefaultProviderCertificateHash;
        }

        private string SetDefaultServiceAddress()
        {
            return Constants.DefaultProviderGeneratorAddress;
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
                throw new TestProviderException("The keystore path is not defined.");
            }

        }

        private void ValidateKeyStorePathCorectness()
        {

            if (!File.Exists(KeyStorePath)) 
            {
                throw new TestProviderException($"The keystore path is incorrect. It does not point to a file. Keystore path: '{ Path.GetFullPath(KeyStorePath) }'");
            }

        }

        private void ValidateKeyStorePasswordSyntax()
        {

            if (string.IsNullOrEmpty(KeyStorePassword))
            {
                throw new TestProviderException("The certificate password is not defined.");
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
                throw new TestProviderException($"The certificate password is incorrect. Keystore path: '{ Path.GetFullPath(KeyStorePath) }'");
            }

        }

        private void ValidateCertificateHashSyntax()
        {

            if (string.IsNullOrEmpty(CertificateHash))
            {
                throw new TestProviderException("The certificate hash is not defined.");
            }

        }

        private void ValidateCertificateHashCorectness() { }

        private void ValidateServiceAddressSyntax()
        {

            if (string.IsNullOrEmpty(GeneratorAddress))
            {
                throw new TestProviderException("The service address is not defined.");
            }

        }

        private void ValidateServiceAddressCorectness() { }

        public async Task<string> Generate(
            string template = Constants.DefaultTemplate)
        {
            _testProviderContext.Template = template;

            string request = GenerateRequestURL(_testProviderContext, GeneratorAddress, GetRequestType(template));
            Task<string> response = SendRequest(request, template.Equals("Stream"));

            return await response;
        }

        public async Task<string> GenerateCartesian(
            string template = Constants.DefaultTemplate)
        {
            _testProviderContext.Template = template;

            Dictionary<string, object> additionalData = new Dictionary<string, object> { { "dataSource", "genCartesian" } };

            TestProviderContext testProviderContext = _testProviderContext.Copy();
            testProviderContext.Settings = TestProviderContextHelper.MergeTestProviderContextSettings(additionalData, _testProviderContext.Settings);

            string request = GenerateRequestURL(testProviderContext, GeneratorAddress, GetRequestType(template));
            Task<string> response = SendRequest(request, template.Equals("Stream"));

            return await response;
        }

        public async Task<string> GenerateNWise(
            string template = Constants.DefaultTemplate,
            int n = Constants.DefaultContextN, 
            int coverage = Constants.DefaultContextCoverage)
        {
            _testProviderContext.Template = template;

            Dictionary<string, object> additionalData = new Dictionary<string, object> { { "dataSource", "genNWise" }, { "n", n }, { "coverage", coverage } };

            TestProviderContext testProviderContext = _testProviderContext;
            testProviderContext.Settings = TestProviderContextHelper.MergeTestProviderContextSettings(additionalData, _testProviderContext.Settings);

            string request = GenerateRequestURL(_testProviderContext, GeneratorAddress, GetRequestType(template));
            Task<string> response = SendRequest(request, template.Equals("Stream"));

            return await response;
        }

        public async Task<string> GenerateRandom(
            string template = Constants.DefaultTemplate,
            int length = Constants.DefaultContextLength, 
            bool duplicates = Constants.DefaultContextDuplicates)
        {
            _testProviderContext.Template = template;

            Dictionary<string, object> additionalData = new Dictionary<string, object> { { "dataSource", "random" }, { "length", length }, { "duplicates", duplicates } };

            TestProviderContext testProviderContext = _testProviderContext;
            testProviderContext.Settings = TestProviderContextHelper.MergeTestProviderContextSettings(additionalData, _testProviderContext.Settings);

            string request = GenerateRequestURL(_testProviderContext, GeneratorAddress, GetRequestType(template));
            Task<string> response = SendRequest(request, template.Equals("Stream"));

            return await response;
        }

        public async Task<string> GenerateStatic(
            string template = Constants.DefaultTemplate,
            object testSuites = null)
        {
            _testProviderContext.Template = template;
            
            object updateTestSuites = testSuites == null ? Constants.DefaultContextTestSuite : testSuites;

            Dictionary<string, object> additionalData = new Dictionary<string, object> { { "dataSource", "static" }, { "testSuites", updateTestSuites } };

            TestProviderContext testProviderContext = _testProviderContext;
            testProviderContext.Settings = TestProviderContextHelper.MergeTestProviderContextSettings(additionalData, _testProviderContext.Settings);

            string request = GenerateRequestURL(_testProviderContext, GeneratorAddress, GetRequestType(template));
            Task<string> response = SendRequest(request, template.Equals("Stream"));

            return await response;
        }

        private string GetRequestType(string template)
        {
            return template.Equals("Stream") || template.Equals("StreamRaw") ? "requestData" : "requestExport";
        }

        public void AddTestEventHandler(EventHandler<TestEventArgs> testEventHandler)
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

        public void RemoveTestEventHandler(EventHandler<TestEventArgs> testEventHandler)
        {
            
            if (TestEventHandler != null)
            {
                if (Array.IndexOf(TestEventHandler.GetInvocationList(), testEventHandler) != -1)
                {
                    TestEventHandler -= testEventHandler;
                }
            }

        }

        public void AddStatusEventHandler(EventHandler<StatusEventArgs> statusEventHandler)
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

        public void RemoveStatusEventHandler(EventHandler<StatusEventArgs> statusEventHandler)
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
            string request = $"{ address }/{ Constants.EndpointHealthCheck }";

            return request;
        }

        private string GenerateRequestURL(TestProviderContext context, string address, string type)
        {
            string requestData = TestProviderContextHelper.SerializeTestProviderContext(context);
            string request = $"{ address }/{ Constants.EndpointGenerator }?requestType={ type }&request={ requestData }";

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
                throw new TestProviderException($"The keystore password is incorrect. Keystore path: '{ Path.GetFullPath(KeyStorePath) }'");
            }
            catch (WebException e)
            {
                throw new TestProviderException(e.Message);
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
                StatusEventArgs statusEventArgs = new StatusEventArgs() { RawData = line };
                statusEventArgs.Structure = JsonConvert.DeserializeObject<StatusMessage>(line);

                if (statusEventArgs.Structure.Status != null)
                {

                    if (statusEventArgs.Structure.Status.Equals("END_DATA"))
                    {
                        statusEventArgs.Completed = true;
                    }
                    else 
                    {
                        statusEventArgs.Completed = false;
                    }
                        
                    GenerateStatusEvent(statusEventArgs);
                }
            }
            catch (JsonReaderException) { }
            catch (JsonSerializationException) { }
        }

        private void ProcessSingleTestResponse(string line, bool streamFilter, StringBuilder responseBuilder)
        {
            TestEventArgs testEventArgs = new TestEventArgs() { RawData = line };

            try
            {
                testEventArgs.Structure = JsonConvert.DeserializeObject<TestCase>(line);

                if (testEventArgs.Structure.TestCaseArguments == null)
                {
                    throw new TestProviderException("The message cannot be parsed.");
                }

                testEventArgs.ObjectData = StreamParser.ParseTestCaseToDataType(testEventArgs.Structure);
                testEventArgs.TestData = StreamParser.ParseTestToNUnit(testEventArgs.Structure);

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

        private void GenerateStatusEvent(StatusEventArgs args)
        {

            if (StatusEventHandler != null && StatusEventHandler.GetInvocationList().Length > 0)
            {
                StatusEventHandler(this, args);
            }

        }

        private void GenerateTestEvent(TestEventArgs args)
        {

            if (TestEventHandler != null && TestEventHandler.GetInvocationList().Length > 0)
            {
                TestEventHandler(this, args);
            }

        }

        public override string ToString()
        { 
            List<string> settings = new List<string>();
            foreach (var (key, value) in Settings)
            {
                settings.Add("{" + $"{ key }: { value }" + "}");
            }

            return
                $"TestProvider:\n" +
                $"\t[KeyStorePath: { Path.GetFullPath(KeyStorePath) }]\n" +
                $"\t[KeyStorePassword: { KeyStorePassword }]\n" +
                $"\t[CertificateHash: { CertificateHash }]\n" +
                $"\t[GeneratorAddress: { GeneratorAddress }]\n" +
                $"\t[Model: { Model }]\n" +
                $"\t[Method: { Method }]\n" +
                $"\t[Settings: { string.Join(", ", settings) }]";
        }
    }
}