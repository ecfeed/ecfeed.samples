namespace Testify.EcFeed
{
    public interface ITestProviderList
    {
        TestList List();
        
        TestList ListCartesian();
        
        TestList ListNWise(
            int n = Constants.DefaultContextN, 
            int coverage = Constants.DefaultContextCoverage);
        
        TestList ListRandom(
            int length = Constants.DefaultContextLength, 
            bool duplicates = Constants.DefaultContextDuplicates);
        
        TestList ListStatic(
            object testSuites = null);
    }
}