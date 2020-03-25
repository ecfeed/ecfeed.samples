using System;
using System.Threading;
using System.Collections;
using System.Collections.Generic;

namespace Testify.EcFeed.Runner.Export
{
    class TestList : IEnumerable<ITestEventArgs>
    {
        private List<ITestEventArgs> _list = new List<ITestEventArgs>();
        private volatile bool _addingCompleted = false;

        public int Count
        {
            get => _list.Count;
        }

        public TestList(ITestProvider testProvider, ITestProviderContext testProviderContext)
        {
            ITestProvider fifoTestProvider = testProvider.Copy();
            fifoTestProvider.AddTestEventHandler(TestEventHandler);
            fifoTestProvider.AddStatusEventHandler(StatusEventHandler);
            
            fifoTestProvider.Generate(testProviderContext, "Stream");
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return _list.GetEnumerator();
        }

        public IEnumerator<ITestEventArgs> GetEnumerator()
        {
            return _list.GetEnumerator();
        }

        public ITestEventArgs this[int index]
        {
            get
            {
                if (index < 0 &&  index >= _list.Count)
                    throw new IndexOutOfRangeException("Index out of range");

                    return _list[index];
            }

            set
            {
                throw new EcFeedException("The test list cannot be modified manually");
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

        private void TestEventHandler(object sender, ITestEventArgs args)
        {
            _list.Add(args);
        }

        private void StatusEventHandler(object sender, IStatusEventArgs args)
        {
            if (args.TerminateExecution)
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