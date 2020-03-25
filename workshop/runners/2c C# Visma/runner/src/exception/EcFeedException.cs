using System;
using System.Runtime.Serialization;

namespace Testify.EcFeed.Runner.Export
{
    [Serializable()]
    public class EcFeedException : Exception
    {
        public EcFeedException() : base() { }
        public EcFeedException(string message) : base(message) { }
        public EcFeedException(string message, System.Exception inner) : base(message, inner) { }
        protected EcFeedException(SerializationInfo info, StreamingContext context) : base(info, context) { }
    } 
}