var express = require('express');
var router = express.Router();
var async = require('async')
var db = require('../controllers/redis.js')
var pay = require('../controllers/stripe.js')
router.post('/payment',function(req,res,next){
  console.log(req.body)

  var precio = 0
  var basket
  var paymentInfo
  async.waterfall([function(callback){
    db.getBasketProducts(req.body.basket_id,callback)
  },function(arg1,callback){
    basket = arg1
    console.log(basket)
    for(var i=0;i<arg1.length;i++){
      precio += arg1[i].price*arg1[i].quantity
    }
    callback(null)
  },function(callback){
        db.getCustomerId(req.body.phone,callback)
      },function(arg1,callback){
        console.log(precio)
      pay.addPayment(req.body.basket_id,basket,precio,req.body.creditcard,arg1,callback)
  },function(arg1,callback){
    paymentInfo = arg1
      db.setAsPaid(req.body.basket_id,arg1,callback)
  }],function(err,ans){
    if(err) {console.log(err)
      res.end("error")}
    else res.end(paymentInfo.id)
  })
})

router.post('/paymentInfo',function(req,res,next){
  console.log(req.body)
  async.parallel([
    function(callback){
      db.getPaymentInfo(req.body.charge_id,callback)
    }
  ],function(err,ans){
    if(err){
      console.log(err)
      res.end("error")
    }
    else{
      var products = []
      for(var i=0;i<ans[0][0].length;i+=2) {
        var parse = JSON.parse(ans[0][0][i])
        products.push({name:parse.name,product_id:parse.product_id,price:parse.price,quantity:ans[0][0][i+1]})}
      res.end(JSON.stringify({status:ans[0][1].status,amount:ans[0][1].amount/100,products:products}))
    }

  })
})


router.get('/basket_id',function(req,res,next){
  async.parallel([function(callback){
    db.getBasket_id(callback)
  }],function(err,final){
    if(err) console.log(err)
    else res.end(JSON.stringify(final[0]))
  })
})

router.post('/storeProduct',function(req,res,next){
  console.log(req.body)
  async.parallel([function(callback){
    db.storeProduct(req.body.basket_id,req.body.product_id,req.body.quantity,callback)
  }],function(err,ans){
    if(err) console.log(err)
    else res.end("correctly_added")
  })
})

module.exports = router;
