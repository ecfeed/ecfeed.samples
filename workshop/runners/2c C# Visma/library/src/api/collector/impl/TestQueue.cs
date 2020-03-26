using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.Concurrent;
using NUnit.Framework;

namespace Testify.EcFeed
{
    public sealed class TestQueue : ITestQueue
    {
        private BlockingCollection<TestCaseData> _fifo = new BlockingCollection<TestCaseData>();

        public int Count
        {
            get => _fifo.Count;
        }

        public int Size()
        {
            return Count;
        }

        public TestQueue(ITestProvider testProvider)
        {
            ITestProvider fifoTestProvider = testProvider.Copy();
            fifoTestProvider.AddTestEventHandler(TestEventHandler);
            fifoTestProvider.AddStatusEventHandler(StatusEventHandler);
            
            fifoTestProvider.Generate("Stream");
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return this.GetEnumerator();
        }

        public IEnumerator<TestCaseData> GetEnumerator()
        {
            while (!_fifo.IsCompleted)
            {
                TestCaseData element = null;

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

        private void TestEventHandler(object sender, TestEventArgs args)
        {
            _fifo.Add(args.TestData);
        }

        private void StatusEventHandler(object sender, StatusEventArgs args)
        {
            if (args.Completed)
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