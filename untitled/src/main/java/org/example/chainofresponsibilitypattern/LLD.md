Chain Of Responsibility Pattern

Usage:
If a sender sends request and he doesnt care which reciever fulfils the request then we use this pattern.


Client -(Withdraw 2000Rs)-> ATM System[2000Rs Handler, 500Rs Handler, 100Rs Handler]
It will go to each handler one by one and try to fulfil the request

Client -> Handler/Processor/Reciever(inherited by ConcreteHandler1, ConcreteHandler2)-> Handler(and loop continues untill one handler fulfills it)


