var express = require('express');
var router = express.Router();
var request = require('request')
var db = require('../controllers/redis.js')
var pay = require('../controllers/stripe.js')
var async = require('async')
/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});
router.post('/register', function(req, res, next) {
  var bodyReq = req.body
  var code = Math.round(Math.random() * 999999)
  request('https://api.tropo.com/1.0/sessions?action=create&token=6d587249574646586d6e79496479784376444f44786f4b6f4b4b415077685566524742676644536459486649&numberToDial='+bodyReq.phone+'&code='+code,function(err,httpResponse,body){
    console.log(body)
    async.parallel([function(callback){
      db.addCode(bodyReq.phone,code,callback)
    }],function(){
      res.end(code.toString())
    })
  })
});

router.post('/register/confirmation',function(req,res,next){
  var bodyReq = req.body
  var code = bodyReq.code
  var phone = bodyReq.phone
  async.waterfall([function(callback){
    db.checkCode(phone,code,callback)
  }],function(err){
    if(err) res.end("wrong_code")
    else res.end("ok")
  })
})

router.post('/alreadyCreated',function(req,res,next){
  var bodyReq = req.body
  var phone = bodyReq.phone
  async.parallel([function(callback){
    db.getCards(phone,callback)
  }],function(err,all){
    if(err) res.end(err)
    else res.end(all[0][0])
  })
})

router.get('/mostSellingProducts',function(req,res,next){

})


module.exports = router;
