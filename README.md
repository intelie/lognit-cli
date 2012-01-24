# Installation

``` sh
sudo sh < <(curl -s https://raw.github.com/intelie/lognit-cli/master/install)
```

# Usage

First, connect to a server:

``` sh
$ **nit -s myserver**
server: localhost:9006
HTTP/1.1 302 Found
login: **mylogin**
mylogin's password:
Welcome My Login!
```

Then start using it!

``` sh
nit '*'                     #searches everything
nit '*' -f                  #searches everything, follows new messages
nit -i                      #request current server message
nit 'abcd' -n 50 -f         #searches for 'abcd', last 50 messages, following new messages
nit 'abcd' -t 1             #searches for 'abcd', but waits only 1 second to all lognit nodes to respond.
```
