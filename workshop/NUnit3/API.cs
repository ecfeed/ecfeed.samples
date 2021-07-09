using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Diagnostics;
using System.Net;
using System.IO;
using System.Text;
using NUnit.Framework;
using Newtonsoft.Json;
using EcFeed;

namespace Workshop
{
    [TestFixture]
    public class SeleniumApi
    {
        private static IEnumerable dataString = new TestProvider("6EG2-YL4S-LMAK-Y5VW-VPV9").GenerateNWise("com.example.test.Demo.typeString", constraints: "ALL", feedback:true, label:"API");
       
        private void HandleError(dynamic error, int duration, TestHandle ecfeed)
        {
            HandleErrorType(error.errorInput.ToObject<string[]>(), duration, "Input", ecfeed);
            HandleErrorType(error.errorOutput.ToObject<string[]>(), duration, "Output", ecfeed);
        }

        private void HandleErrorType(string[] error, int duration, string type, TestHandle ecfeed)
        {
            if (error.Length > 0)
            {
                int index = 1;
                dynamic custom = error.ToDictionary(e => "" + index++, e => e);
                ecfeed.addFeedback(false, comment:type, duration:duration, custom:custom);
                Assert.Fail();
            }
        }

        private int GetTime(Stopwatch stopwatch)
        {
            stopwatch.Stop();
            return (int)stopwatch.ElapsedMilliseconds;
        }

        [TestCaseSource("dataString")]
        public void Test(string country, string name, string address, string product, string color, string size, string quantity, string payment, string delivery, string phone, string email, TestHandle ecfeed)
        {
            try 
            {
                Stopwatch stopwatch = Stopwatch.StartNew();

                HttpWebRequest httpWebRequest = (HttpWebRequest) HttpWebRequest.Create("https://api.ecfeed.com/" + 
                    "?mode=error" +
                    "&country=" + country +
                    "&name=" + name +
                    "&address=" + address +
                    "&product=" + product +
                    "&color=" + color +
                    "&size=" + size +
                    "&quantity=" + quantity +
                    "&payment=" + payment +
                    "&delivery=" + delivery +
                    "&phone=" + phone.Replace("+", "%2B") +
                    "&email=" + email);
                httpWebRequest.Method = "Post";

                HttpWebResponse httpWebResponse = (HttpWebResponse) httpWebRequest.GetResponse();

                using (StreamReader reader = new StreamReader(httpWebResponse.GetResponseStream(), Encoding.ASCII)) {
                    HandleError(JsonConvert.DeserializeObject(reader.ReadLine()), GetTime(stopwatch), ecfeed);
                }

                ecfeed.addFeedback(true, duration:GetTime(stopwatch));
            }
            catch (WebException e)
            {
                string message = $"The connection could not be established.";
                ecfeed.addFeedback(false, comment:message);
                throw new TestProviderException(message, e);
            }    
        }
    }

}
