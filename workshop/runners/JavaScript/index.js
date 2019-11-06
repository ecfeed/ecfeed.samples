const axios = require('axios')
const https = require('https')
const fs = require('fs')

const configurationRequestAdditional = {
    dataSource: 'genNWise',
    constraints: 'NONE'
}

const configurationRequest= {
    sessionId: 0,
    model: '9835-3029-2264-1682-5114',    
    method: 'void com.ecfeed.Model.simple(String,String,String,String,String,String,String,String,String,String,String)',
    userData: JSON.stringify(configurationRequestAdditional).replace(/\"/g, "'")
}

const configuration = {
    requestType: 'requestData',
    request: JSON.stringify(configurationRequest)
}

axios.get('https://gen.ecfeed.com/testCaseService', {
        params: configuration,
        httpsAgent: new https.Agent({
            cert: fs.readFileSync(`client.pem`),
            key: fs.readFileSync(`key.pem`),
            passphrase: 'changeit',
            rejectUnauthorized: false
        })
    })
    .then( 
        event => { console.log(event.data); }, 
        event => { console.log(event.response.data )} 
    )