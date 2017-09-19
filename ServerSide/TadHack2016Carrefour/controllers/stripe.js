var stripe = require("stripe")(
  "sk_test_L8P2sC4QOXzzHOSNouSgdWxF"
);

exports.createCustomer = function(token,phone,callback){
  stripe.customers.create({
  description: phone,
  source: token // obtained with Stripe.js
}, function(err, customer) {
  if(err) {
    callback(err)
  }
  else {
    callback(null,customer)}
});
}

exports.addNewCard = function(customer_id,token,callback){
  stripe.customers.createSource(
    customer_id,
    {source: token},
    function(err, card) {
      if(err) callback(err)
      else callback(null,card)
    }
  );
}

exports.addPayment = function(basket_id,basket,amount,creditcard,customer_id,callback){
  var string = ""
  console.log(customer_id)
  for(var i=0;i<basket.length;i++) string += `${basket[i].quantity}x${basket[i].product_id} \n`
  stripe.charges.create({
  amount: Math.round(amount*100),
  currency: "eur",
  source: creditcard,
  customer: customer_id,
  description: string
}, function(err, charge) {
  if(err){
    console.log(err)
    callback(err)
  }
  else callback(null,charge)
});
}
