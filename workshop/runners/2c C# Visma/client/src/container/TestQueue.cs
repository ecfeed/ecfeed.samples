using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.Concurrent;

namespace Testify.EcFeed
{
    public class TestQueue : IEnumerable<object[]>
    {
        private BlockingCollection<object[]> _fifo = new BlockingCollection<object[]>();

        public int Count
        {
            get => _fifo.Count;
        }

        public TestQueue(ITestProvider testProvider, ITestProviderContext testProviderContext)
        {
            ITestProvider fifoTestProvider = testProvider.Copy();
            fifoTestProvider.AddTestEventHandler(TestEventHandler);
            fifoTestProvider.AddStatusEventHandler(StatusEventHandler);
            
            fifoTestProvider.Generate(testProviderContext, "Stream");
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return this.GetEnumerator();
        }

        public IEnumerator<object[]> GetEnumerator()
        {
            while (!_fifo.IsCompleted)
            {
                object[] element = null;

                try
                {
                    element = _fifo.Take();
                }
                catch (InvalidOperationException) { }

                if (element != null)
                {
                    yield return element;
                }
            }
        }

        private void TestEventHandler(object sender, ITestEventArgs args)
        {
            _fifo.Add(args.TestObject);
        }

        private void StatusEventHandler(object sender, IStatusEventArgs args)
        {
            if (args.TerminateExecution)
            {
                _fifo.CompleteAdding();
            }
        }

        public override string ToString()
        {
            string progress = _fifo.IsCompleted ? "Completed" : "In progress";
            return $"Remaining cases: { _fifo.Count }, Generation status: { progress }.";
        }

    }
}