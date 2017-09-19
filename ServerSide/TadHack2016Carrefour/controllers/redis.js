var redis = require("redis"),
    client = redis.createClient({
      port: 3543,
      password: "itsoktobeafraid"
    });

exports.addCode = function(phone,code,callback){
  client.multi()
    .hset("sms_codes",phone,code)
    .hset(phone,"status","unactivated")
    .exec(function(){
      callback(null)
    })
}
exports.checkCode = function(phone,code,callback){
  client.hget("sms_codes",phone,function(err,res){
    if (res == code) {
      client.multi()
      .hset(phone,"status","activated")
      .hdel("sms_codes",phone)
      .exec(function(){
        callback(null)
      })
    }
    else callback("wrong_code")
  })
}

exports.addCustomerId = function(phone,customer_id,creditcard,callback){
  client.multi()
    .sadd(phone+":cards",creditcard)
    .hset(phone,"customer_id",customer_id)
    .exec(function(err){
      if(err) console.log(err)
      else callback(null)
  })
}

exports.getCustomerId = function(phone,callback){
  client.hget(phone,"customer_id",function(err,res){
    if(err) callback(err)
    else callback(null,res)
  })
}

exports.storeCard = function(phone,card_id,callback){
  client.sadd(phone + ":cards",card_id,function(err){
    if(err) callback(err)
    else callback(null)
  })
}

exports.getProduct = function(id,callback){
  client.hgetall("product:"+id,function(err,res){
    if(err) callback(err)
    else callback(null,res)
  })
}

exports.getCards = function(phone,callback){
  client.srandmember(phone+":cards",1,function(err,res){
    if(err) callback(err)
    else callback(null,res)
  })
}

exports.getBasket_id = function(callback){
  client.incr("basket_id",function(err,ans){
    if(err) callback(err)
    else callback(null,ans)
  })
}

exports.storeProduct = function(basket_id,product_id,quantity,callback){
  client.hgetall("product:"+product_id,function(err,ans1){
  client.multi()
  .zadd("basket:"+basket_id,quantity,JSON.stringify({product_id:product_id,price:ans1.price,name:ans1.name}))
  .hset("basket:"+basket_id+":status","status","nonpaid")
  .exec(function(err,ans){
    if(err) callback(err)
    else callback(null)
  })
})
}

exports.getBasketProducts = function(basket_id,callback){
  console.log(basket_id)
  var basket = []
  client.zrange("basket:"+basket_id,0,-1,"WITHSCORES",function(err,ans){
    for(var i=0;i<ans.length;i+=2) basket.push({product_id: JSON.parse(ans[i]).product_id, quantity: ans[i+1], price:JSON.parse(ans[i]).price})
    console.log(basket)
    callback(null,basket)
  })
}

exports.setAsPaid = function(basket_id,info,callback){
  console.log(info)
  client.multi()
  .hset("charges",info.id,basket_id)
  .hmset(["basket:"+basket_id+":status","status","paid","charge_id",info.id,"amount",info.amount])
  .exec(function(err,ans){
    if(err) callback(err)
    else callback(null)
  })
}

exports.getPaymentInfo = function(charge_id,callback){
  client.hget("charges",charge_id,function(err,ans){
    client.multi()
    .zrange("basket:"+ans,0,-1,"WITHSCORES")
    .hgetall("basket:"+ans+":status")
    .exec(function(err,ans){
      if(err) callback(err)
      else callback(null,ans)
    })
  })
}
