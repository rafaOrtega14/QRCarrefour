var express = require('express');
var router = express.Router();
var db = require('../controllers/redis.js')
var pay = require('../controllers/stripe.js')
var async = require('async')

router.post('/createFirstCustomer', function(req, res, next) {
  var bodyReq = req.body
  var token = bodyReq.token
  var phone = bodyReq.phone
  var customerInfo
  async.waterfall([function(callback){
    pay.createCustomer(token,phone,callback)
  },function(arg1,callback){
    customerInfo = arg1
    db.addCustomerId(phone,arg1.id,arg1.default_source,callback)
  }],function(err){
    if(err) res.end("error")
    else res.end(customerInfo.default_source)
  })
});

router.post('/addCard',function(req,res,next){
  var bodyReq = req.body
  var token = bodyReq.token
  var phone = bodyReq.phone

async.waterfall([function(callback){
  db.getCustomerId(phone,callback)
},function(arg1,callback){
  pay.addNewcard(arg1,token,callback)
},function(arg1,callback){
  db.storeCard(phone,arg1,callback)
}],function(err){
  if(err) res.end(err)
  else res.end("correctly_added")
})
})

module.exports = router;
