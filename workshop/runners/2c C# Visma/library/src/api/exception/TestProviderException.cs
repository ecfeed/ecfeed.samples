using System;
using System.Runtime.Serialization;

namespace Testify.EcFeed
{
    [Serializable()]
    public sealed class TestProviderException : Exception
    {
        public TestProviderException() : base() { }
        public TestProviderException(string message) : base(message) { }
        public TestProviderException(string message, System.Exception inner) : base(message, inner) { }
    } 
}