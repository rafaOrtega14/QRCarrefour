var express = require('express');
var router = express.Router();
var db = require('../controllers/redis.js')
var async = require('async')


router.get('/:id',function(req,res,next){
  var params = req.params
  console.log(params)
  async.parallel([function(callback){
    db.getProduct(params.id,callback)
  }],function(err,args){
    console.log(args[0])
    res.end(JSON.stringify(args[0]))
  })
})

module.exports = router;
