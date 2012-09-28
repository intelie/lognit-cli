#Installation

```sh
curl -s nit.lognit.com | sudo sh
```

Installing another version:

```sh
curl -s nit.lognit.com | sudo sh -s 1.2
```

Alternativelly, using wget:

```sh
wget -qO- nit.lognit.com | sudo sh
```

# Usage

First, connect to a server:

```sh
$ nit -s myserver
```

Then start using it!

```sh
$ nit '*'                 #searches everything
$ nit '*' -f              #searches everything, follows new messages
$ nit -i                  #requests current server message
$ nit 'abcd' -n 50 -f     #searches for 'abcd', last 50 messages, following new messages
$ nit 'abcd' -t 1         #searches for 'abcd', but waits only 1 second to all lognit nodes to respond.
```

More usage options:

```sh
$ nit --help
```
