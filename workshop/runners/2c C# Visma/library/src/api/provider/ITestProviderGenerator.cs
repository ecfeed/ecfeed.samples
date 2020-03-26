using System.Threading.Tasks;

namespace EcFeed
{
    public interface ITestProviderGenerator
    {
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
    }
}