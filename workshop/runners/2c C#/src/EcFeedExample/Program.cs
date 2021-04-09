using System;
using EcFeed;

namespace EcFeedExample
{
    class Example : Runner {

        public override void ProcessResponseHandler(String line) {
            Console.WriteLine(line);
        }

        static void Main(string[] args) {
            string body = "{" +
                "\"method\":\"void com.example.test.Demo.typeString(String,String,String,String,String,String,String,String,String,String,String)\"," +
                "\"model\":\"7482-5194-2849-1943-2448\",\"template\":\"JSON\",\"userData\":\"{'dataSource':'genNWise','constraints':'NONE'}\"" +
            "}";

            Runner program = new Example();
           
            program.WebService(body);
        }

    }

}
