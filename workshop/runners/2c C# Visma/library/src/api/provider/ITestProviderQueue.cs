namespace EcFeed
{
    public interface ITestProviderQueue
    {
        TestQueue Queue();
        
        TestQueue QueueCartesian();
        
        TestQueue QueueNWise(
            int n = Constants.DefaultContextN, 
            int coverage = Constants.DefaultContextCoverage);
        
        TestQueue QueueRandom(
            int length = Constants.DefaultContextLength, 
            bool duplicates = Constants.DefaultContextDuplicates);
        
        TestQueue QueueStatic(
            object testSuites = null);
    }
}