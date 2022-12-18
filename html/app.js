//imports
const express = require('express')
const app=new express()
const port=3000
const bodyParser = require('body-parser');

/* require('dotenv').config() */
const path = require("path")
app.use(bodyParser.urlencoded({ extended: true })); 

//TODO demás
app.use(express.static('public'))
app.use(express.static('/css',express.static(__dirname+'/public/css')))
app.use(express.static('/js',express.static(__dirname+'/public/js')))
app.use(express.static('/img',express.static(__dirname+'/public/img')))



app.get('/',(req,res)=>{
   res.sendFile(path.join(__dirname,'/public/views/index.html')) 
   
})

app.post('/sendJson', (req, res) => {
   //res.sendFile(path.join(__dirname,'/public/views/index.html')) 

   //console.log(req.body)
   const fs = require('fs')

let user = {
  name: 'John Doe',
  email: 'john.doe@example.com',
  age: 27,
  gender: 'Male',
  profession: 'Software Developer'
}

// convert JSON object to a string
const data = JSON.stringify(user)

// write file to disk
fs.writeFile('envio.json', data, 'utf8', err => {
  if (err) {
    console.log(`Error writing file: ${err}`)
  } else {
    console.log(`File is written successfully!`)
  }
})
 });

/* app.get('/check-health',(req,res )=>{
   res.status(200).json({message:'Server is running'})
})
 */
//TODO listen
app.listen(port,()=>console.info(`Escuchando en el puerto ${port}`))
