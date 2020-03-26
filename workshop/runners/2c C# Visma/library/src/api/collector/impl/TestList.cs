using System;
using System.Threading;
using System.Collections;
using System.Collections.Generic;

namespace Testify.EcFeed
{
    public sealed class TestList : ITestList
    {
        private List<TestEventArgs> _list = new List<TestEventArgs>();
        private volatile bool _addingCompleted = false;

        public int Count
        {
            get => _list.Count;
        }

        public int Size()
        {
            return Count;
        }

        internal TestList(ITestProvider testProvider)
        {
            ITestProvider fifoTestProvider = testProvider.Copy();
            fifoTestProvider.AddTestEventHandler(TestEventHandler);
            fifoTestProvider.AddStatusEventHandler(StatusEventHandler);
            
            fifoTestProvider.Generate("Stream");
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return _list.GetEnumerator();
        }

        public IEnumerator<TestEventArgs> GetEnumerator()
        {
            return _list.GetEnumerator();
        }

        public TestEventArgs this[int index]
        {
            get
            {
                if (index < 0 &&  index >= _list.Count)
                    throw new IndexOutOfRangeException("Index out of range");

                    return _list[index];
            }

            set
            {
                throw new TestProviderException("The test list cannot be modified manually");
            }
        }

        public void WaitUntilFinished()
        {
            if (_addingCompleted)
            {
                return;
            }

            while (!_addingCompleted)
            {
                Thread.Sleep(100);
            }
        }

        private void TestEventHandler(object sender, TestEventArgs args)
        {
            _list.Add(args);
        }

        private void StatusEventHandler(object sender, StatusEventArgs args)
        {
            if (args.Completed)
            {
                _addingCompleted = true;
            }
        }

        public override string ToString()
        {
            string progress = _addingCompleted ? "Completed" : "In progress";
            return $"Test cases: { _list.Count }, Generation status: { progress }.";
        }

    }
}